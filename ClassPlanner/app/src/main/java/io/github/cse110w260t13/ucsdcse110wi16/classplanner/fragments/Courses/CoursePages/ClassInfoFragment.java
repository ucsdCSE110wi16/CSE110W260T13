package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Courses.CoursePages;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.course_database.CourseCalendarContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.course_database.CourseCalendarInfo;

public class ClassInfoFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private static final int URL_LOADER = 0;
    private String currName = null;
    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_classinfo, container, false);

        getLoaderManager().initLoader(URL_LOADER, null, this);

        return rootView;
    }

    public void selectClass(String class_name){
        currName=class_name;
        Log.d("teststringclass_name ", "class name is " + class_name);
        ContentResolver cr = getActivity().getContentResolver();
        Cursor cursor = cr.query(CourseCalendarContentProvider.CONTENT_URI,
                CourseCalendarInfo.FeedEntry.ALL_COLUMNS,
                CourseCalendarInfo.FeedEntry.COLUMN_COURSE_NAME + "=?",
                new String[] { class_name + "" }, null);

        if(cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            int[] columnIndices = new int[CourseCalendarInfo.FeedEntry.ALL_COLUMNS.length];
            for(int i=0; i < CourseCalendarInfo.FeedEntry.ALL_COLUMNS.length; i++){
                columnIndices[i] = cursor.getColumnIndex(CourseCalendarInfo.FeedEntry.ALL_COLUMNS[i]);
            }

            update_info(
                    /*Schedule info*/
                    cursor.getString(columnIndices[1]),
                    cursor.getString(columnIndices[2]),
                    cursor.getString(columnIndices[3]),
                    cursor.getString(columnIndices[4]),
                    /*Days of the week*/
                    cursor.getInt(columnIndices[5]),
                    cursor.getInt(columnIndices[6]),
                    cursor.getInt(columnIndices[7]),
                    cursor.getInt(columnIndices[8]),
                    cursor.getInt(columnIndices[9]),
                    cursor.getInt(columnIndices[10]),
                    cursor.getInt(columnIndices[11]),
                    /*General Info*/
                    cursor.getString(columnIndices[12]),
                    cursor.getString(columnIndices[13]),
                    cursor.getString(columnIndices[14]),
                    cursor.getString(columnIndices[15])
            );
        }
    }

    public void update_info(String name, String loc, String startTime,
                            String endTime, int sun, int mon, int tue,
                            int wed, int thur, int fri, int sat,
                            String notes, String instrName, String email,
                            String web){

        TextView nameTv = (TextView) rootView.findViewById(R.id.class_info_text);
        TextView locTv = (TextView) rootView.findViewById(R.id.location);
        TextView startTimeTv = (TextView) rootView.findViewById(R.id.start_time);
        TextView endTimeTv = (TextView) rootView.findViewById(R.id.end_time);

        TextView sunTv = (TextView) rootView.findViewById(R.id.sunday);
        TextView monTv = (TextView) rootView.findViewById(R.id.monday);
        TextView tueTv = (TextView) rootView.findViewById(R.id.tuesday);
        TextView wedTv = (TextView) rootView.findViewById(R.id.wed);
        TextView thurTv = (TextView) rootView.findViewById(R.id.thur);
        TextView friTv = (TextView) rootView.findViewById(R.id.fri);
        TextView satTv = (TextView) rootView.findViewById(R.id.sat);

        TextView noteTv = (TextView) rootView.findViewById(R.id.notes);
        TextView instrTv = (TextView) rootView.findViewById(R.id.instr_name);
        TextView emailTv = (TextView) rootView.findViewById(R.id.instr_email);
        TextView webTv = (TextView) rootView.findViewById(R.id.website);

        nameTv.setText(name);
        locTv.setText(loc);
        startTimeTv.setText(startTime);
        endTimeTv.setText(endTime);

        setColor(sunTv, sun);
        setColor(monTv, mon);
        setColor(tueTv, tue);
        setColor(wedTv, wed);
        setColor(thurTv, thur);
        setColor(friTv, fri);
        setColor(satTv, sat);

        noteTv.setText(notes);
        instrTv.setText(instrName);
        emailTv.setText(email);
        webTv.setText(web);
    }

    private void setColor(TextView v, int checked){
        if (checked == 1) v.setTextColor(ContextCompat.getColor(getContext(),R.color.colorAccent));
        else v.setTextColor(ContextCompat.getColor(getContext(),android.R.color.primary_text_light));
    }

    // creates a new loader after the initLoader () call
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = CourseCalendarInfo.FeedEntry.ALL_COLUMNS;

        CursorLoader cursor_ld;
        if(currName == null) {
            cursor_ld = new CursorLoader(getContext(),
                    CourseCalendarContentProvider.CONTENT_URI, projection,
                    null, null, null);
        }
        else{
            cursor_ld = new CursorLoader(getContext(),
                    CourseCalendarContentProvider.CONTENT_URI, projection,
                    CourseCalendarInfo.FeedEntry.COLUMN_COURSE_NAME + "=?",
                    new String[]{currName}, null);

        }
        return cursor_ld;
    }

    //Update the UI with results from query

    /**
     * ToDo: It should be possible to be more compact, but let me get it working first.
     * @param loader
     * @param data
     */
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null && data.getCount() > 0){
            data.moveToFirst();
            int[] columnIndices = new int[CourseCalendarInfo.FeedEntry.ALL_COLUMNS.length];
            for(int i=0; i < CourseCalendarInfo.FeedEntry.ALL_COLUMNS.length; i++){
                columnIndices[i] = data.getColumnIndex(CourseCalendarInfo.FeedEntry.ALL_COLUMNS[i]);
            }
            update_info(
                    /*Schedule info*/
                    data.getString(columnIndices[1]),
                    data.getString(columnIndices[2]),
                    data.getString(columnIndices[3]),
                    data.getString(columnIndices[4]),
                    /*Days of the week*/
                    data.getInt(columnIndices[5]),
                    data.getInt(columnIndices[6]),
                    data.getInt(columnIndices[7]),
                    data.getInt(columnIndices[8]),
                    data.getInt(columnIndices[9]),
                    data.getInt(columnIndices[10]),
                    data.getInt(columnIndices[11]),
                    /*General Info*/
                    data.getString(columnIndices[12]),
                    data.getString(columnIndices[13]),
                    data.getString(columnIndices[14]),
                    data.getString(columnIndices[15])
            );
        }
    }

    //Release any references/resources that are not needed anymore
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void onResume(){
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);
    }

}
