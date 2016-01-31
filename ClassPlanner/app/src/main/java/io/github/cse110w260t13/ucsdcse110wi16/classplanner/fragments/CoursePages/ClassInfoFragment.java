package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.CoursePages;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.CourseCalendarContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.CourseCalendarDbHelper;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.CourseCalendarInfo;

public class ClassInfoFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_classinfo, container, false);
        return rootView;
    }

    public void test(String class_name){
        ContentResolver cr = getActivity().getContentResolver();
        Cursor cursor = cr.query(CourseCalendarContentProvider.CONTENT_URI,
                new String[] {
                        CourseCalendarInfo.FeedEntry._ID,
                        CourseCalendarInfo.FeedEntry.COLUMN_COURSE_NAME,
                        CourseCalendarInfo.FeedEntry.COLUMN_COURSE_LOC,
                        CourseCalendarInfo.FeedEntry.COLUMN_START_TIME,
                        CourseCalendarInfo.FeedEntry.COLUMN_END_TIME
                },
                CourseCalendarInfo.FeedEntry.COLUMN_COURSE_NAME + "=?",
                new String[] { class_name + "" },
                null);

            if(cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                int nameIndex = cursor.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_COURSE_NAME);
                int locIndex = cursor.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_COURSE_LOC);
                int startHrIndex = cursor.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_START_TIME);
                int endHrIndex = cursor.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_END_TIME);

                String name = cursor.getString(nameIndex);
                String loc = cursor.getString(locIndex);
                String start = cursor.getString(startHrIndex);
                String end = cursor.getString(endHrIndex);

                update_info(name,loc, start, end);
            }
    }

    public void update_info(String name, String loc, String startTime,
                            String endTime){
        TextView nameTv = (TextView) rootView.findViewById(R.id.class_info_text);
        TextView locTv = (TextView) rootView.findViewById(R.id.location);
        TextView startTimeTv = (TextView) rootView.findViewById(R.id.start_time);
        TextView endTimeTv = (TextView) rootView.findViewById(R.id.end_time);

        nameTv.setText(name);
        locTv.setText(loc);
        startTimeTv.setText(startTime);
        endTimeTv.setText(endTime);
    }

    // creates a new loader after the initLoader () call
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                CourseCalendarInfo.FeedEntry._ID,
                CourseCalendarInfo.FeedEntry.COLUMN_COURSE_NAME,
                CourseCalendarInfo.FeedEntry.COLUMN_COURSE_LOC,
                CourseCalendarInfo.FeedEntry.COLUMN_START_TIME,
                CourseCalendarInfo.FeedEntry.COLUMN_END_TIME
        };
        CursorLoader cursor_ld = new CursorLoader(getContext(),
                CourseCalendarContentProvider.CONTENT_URI, projection, null, null, null);
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
            int nameIndex = data.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_COURSE_NAME);
            int locIndex = data.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_COURSE_LOC);
            int startHrIndex = data.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_START_TIME);
            int endHrIndex = data.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_END_TIME);

            int sunIndex = data.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_SUN);
            int monIndex = data.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_MON);
            int tueIndex = data.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_TUE);
            int wedIndex = data.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_WED);
            int thurIndex = data.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_THUR);
            int friIndex = data.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_FRI);
            int satIndex = data.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_SAT);

            String name = data.getString(nameIndex);
            String loc = data.getString(locIndex);
            String start = data.getString(startHrIndex);
            String end = data.getString(endHrIndex);
            //int startHr = data.getInt(startHrIndex);
            //int startMin = data.getInt(startMinIndex);
            //int endHr = data.getInt(endHrIndex);
            //int endMin = data.getInt(endMinIndex);

            update_info(name,loc, start, end);
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
