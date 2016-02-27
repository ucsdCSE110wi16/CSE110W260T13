package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Courses.CoursePages.GradeScale;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Courses.CoursePages.GradeScale.EditScaleActivity;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Courses.CourseUtil.ErrorDialogFragment;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Courses.CourseUtil.Scale;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.course_database.CourseCalendarContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.course_database.CourseCalendarInfo;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.course_database.GradeScaleContentProvider;

public class GradescaleFragment extends Fragment{

    public static final int REQUEST_CODE = 0;

    private View rootView;
    private PieChart mChart;
    private String currName;
    private Scale gradeScale;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_gradescale, container, false);

        mChart = (PieChart) rootView.findViewById(R.id.chart);
        mChart.setUsePercentValues(true);
        mChart.setHoleRadius(30f);
        mChart.setTransparentCircleRadius(35f);
        mChart.setHoleColorTransparent(true);
        mChart.setDescription(null);

        Legend legend = mChart.getLegend();
        legend.setWordWrapEnabled(true);
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);

        //query to get yData and xData st yData = WEIGHT and xData = CATEGORY
        if(currName!=null) {
            updateChart(currName);
        }
        else {
            //query for the first item in course calendar. updatechart on that one.
            ContentResolver cr = getActivity().getContentResolver();
            Cursor cursor = cr.query(CourseCalendarContentProvider.CONTENT_URI,
                    CourseCalendarInfo.GeneralInfo.ALL_COLUMNS, null, null, null);
            String name = null;
            if(cursor!=null && cursor.getCount() > 0){
                cursor.moveToFirst();
                name = cursor.getString(cursor.getColumnIndex(CourseCalendarInfo
                        .GeneralInfo.COLUMN_COURSE_NAME));
            }
            if (name!= null){
                currName = name;
                updateChart(name);
            }
        }

        Button editButton = (Button) rootView.findViewById(R.id.edit_info);
        ClickHandler clickHandler = new ClickHandler();
        editButton.setOnClickListener(clickHandler);

        return rootView;
    }

    private class ClickHandler implements View.OnClickListener {
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.edit_info:
                    if(currName!=null) {
                        Intent intent = new Intent(getParentFragment().getContext(),
                                EditScaleActivity.class);
                        intent.putExtra("class", currName);
                        getParentFragment().startActivityForResult(intent, REQUEST_CODE);
                    }
                    else{
                        ErrorDialogFragment error = new ErrorDialogFragment();
                        Bundle args = new Bundle();
                        args.putString("error", "You currently have no classes.");
                        error.setArguments(args);
                        error.show(getFragmentManager(), "No Classes Error Popup");
                    }
                    break;
            }
        }
    }

    public void updateChart(String courseName){
        if(courseName == null){
            mChart.setData(null);
            mChart.invalidate();
            currName = null;
            return;
        }
        currName = courseName;
        gradeScale = createScaleData(courseName);
        setData(gradeScale);
    }

    private Scale createScaleData(String courseName){
        Scale scale = new Scale(Scale.CUSTOM);
        ContentResolver cr = getActivity().getContentResolver();
        Log.i("createScaleData ", "entered with name = " + courseName);

        Cursor cursor = cr.query(
                GradeScaleContentProvider.CONTENT_URI,
                CourseCalendarInfo.GradeScale.ALL_COLUMNS,
                CourseCalendarInfo.GradeScale.COURSE_NAME + "=?",
                new String[] {courseName}, null );

        if(cursor!=null){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                String category = cursor.getString(cursor
                        .getColumnIndex(CourseCalendarInfo.GradeScale.CATEGORY));
                int weight = cursor.getInt(cursor
                        .getColumnIndex(CourseCalendarInfo.GradeScale.WEIGHT));
                scale.add(category, weight);
                Log.i("createScaleData ", "adding " + category + ", " + weight);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return scale;
    }

    private void setData(Scale gradeScale) {

        ArrayList<Entry> yVals = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        for (int i = 0; i < gradeScale.size(); i++) {
            yVals.add(new Entry(gradeScale.getWeight(i), i));
            xVals.add(gradeScale.getCategory(i));
        }

        PieDataSet dataSet = new PieDataSet(yVals, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        int[] blueGreenColorBrewer = {
                Color.rgb(199,233,180),
                Color.rgb(127, 205, 187),
                Color.rgb(65, 182, 196),
                Color.rgb(29, 145, 192),
                Color.rgb(34, 94, 168),
                Color.rgb(37, 52, 148),
                Color.rgb(8, 29, 88)
        };

        int[] justBlueBrewer = {
                Color.rgb(198,219,239),
                Color.rgb(158, 202, 225),
                Color.rgb(107, 174, 214),
                Color.rgb(66, 146, 198),
                Color.rgb(33, 113, 181),
                Color.rgb(8, 81, 156),
                Color.rgb(8, 48, 107)
        };

        for (int c : justBlueBrewer ) {
            colors.add(c);
        }
        for (int c : blueGreenColorBrewer ) {
            colors.add(c);
        }
        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    @Override
    public void onResume(){
        super.onResume();
        if (currName!=null){
            updateChart(currName);
        }
        else{
            mChart.setData(null);
            mChart.invalidate();
            currName = null;
        }
    }

}
