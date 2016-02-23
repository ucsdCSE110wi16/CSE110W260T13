package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Courses.CoursePages;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;

public class GradescaleFragment extends Fragment{

    private View rootView;
    private PieChart mChart;

    private float[] yData = {10, 20, 20, 45, 5};
    private String[] xData = {"Homework", "Midterms", "Projects", "Final Exam", "Participation"};

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



        setData(yData, xData);

        return rootView;
    }

    private void setData(float[] yData, String[] xData) {

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        for (int i = 0; i < yData.length; i++) {
            yVals1.add(new Entry(yData[i], i));
        }

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < xData.length; i++)
            xVals.add(xData[i]);

        PieDataSet dataSet = new PieDataSet(yVals1, "Grade Distribution");
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

        for (int c : justBlueBrewer )
            colors.add(c);

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

}
