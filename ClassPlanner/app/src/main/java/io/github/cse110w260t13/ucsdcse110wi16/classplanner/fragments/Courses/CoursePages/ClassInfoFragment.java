package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Courses.CoursePages;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.TextView;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Courses.CourseUtil.DeleteDialogFragment;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Courses.CourseUtil.ErrorDialogFragment;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.course_database.CourseCalendarContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.course_database.CourseCalendarInfo;

public class ClassInfoFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    public static final int REQUEST_CODE = 1;

    private static final int URL_LOADER = 0;
    private String currName = null;
    private View rootView;
    private boolean disabled;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_classinfo, container, false);

        getLoaderManager().initLoader(URL_LOADER, null, this);

        Button editButton = (Button) rootView.findViewById(R.id.edit_info);
        clickHandler handler = new clickHandler();
        editButton.setOnClickListener(handler);

        return rootView;
    }

    /**
     * click handler for all buttons in the Courses Page
     */
    private class clickHandler implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.edit_info:
                    if(currName!=null) {
                        Intent intent = new Intent(getParentFragment().getContext(), AddClassActivity.class);
                        intent.putExtra("mode", "update");
                        intent.putExtra("class", currName);
                        getParentFragment().startActivityForResult(intent, REQUEST_CODE);
                    }
                    else{
                        ErrorDialogFragment error = new ErrorDialogFragment();
                        Bundle args = new Bundle();
                        args.putString("errorMsg", "You currently have no classes.");
                        error.setArguments(args);
                        error.show(getFragmentManager(), "No Classes Error Popup");
                    }
                    break;
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("onActivityResult", "requestCode: " + requestCode + " resultCode" + resultCode + " act: " + Activity.RESULT_OK);
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("onActivityResult", "finished AddClass2");
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            currName = data.getStringExtra("newName");
            selectClass(currName);
            Log.i("onActivityResult", "requery" + currName);
        }
    }
    public void selectClass(String class_name){
        currName=class_name;

        if(class_name==null){
            resetInfo();
            return;
        }

        Log.d("teststringclass_name ", "class name is " + class_name);
        ContentResolver cr = getActivity().getContentResolver();
        Cursor cursor = cr.query(CourseCalendarContentProvider.CONTENT_URI,
                CourseCalendarInfo.FeedEntry.ALL_COLUMNS,
                CourseCalendarInfo.FeedEntry.COLUMN_COURSE_NAME + "=?",
                new String[] { class_name + "" }, null);

        if(cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            update_info(
                    cursor.getString(cursor.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_COURSE_NAME)),
                    cursor.getString(cursor.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_COURSE_LOC)),
                    cursor.getString(cursor.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_START_TIME)),
                    cursor.getString(cursor.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_END_TIME)),
                    cursor.getInt(cursor.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_SUN)),
                    cursor.getInt(cursor.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_MON)),
                    cursor.getInt(cursor.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_TUE)),
                    cursor.getInt(cursor.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_WED)),
                    cursor.getInt(cursor.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_THUR)),
                    cursor.getInt(cursor.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_FRI)),
                    cursor.getInt(cursor.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_SAT)),
                    cursor.getString(cursor.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_NOTES)),
                    cursor.getString(cursor.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_INSTR_NAME)),
                    cursor.getString(cursor.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_INSTR_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_WEBSITE))
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

    private void resetInfo(){
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

        nameTv.setText(R.string.course_here);
        locTv.setText(R.string.loc_here);
        startTimeTv.setText(R.string.time_here);
        endTimeTv.setText(R.string.time_here);

        setColor(sunTv, 0);
        setColor(monTv, 0);
        setColor(tueTv, 0);
        setColor(wedTv, 0);
        setColor(thurTv, 0);
        setColor(friTv, 0);
        setColor(satTv, 0);

        noteTv.setText(R.string.note_here);
        instrTv.setText(R.string.instr_name_here);
        emailTv.setText(R.string.instr_email_here);
        webTv.setText(R.string.web_here);

        currName = null;
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
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            update_info(
                    cursor.getString(cursor.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_COURSE_NAME)),
                    cursor.getString(cursor.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_COURSE_LOC)),
                    cursor.getString(cursor.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_START_TIME)),
                    cursor.getString(cursor.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_END_TIME)),
                    cursor.getInt(cursor.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_SUN)),
                    cursor.getInt(cursor.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_MON)),
                    cursor.getInt(cursor.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_TUE)),
                    cursor.getInt(cursor.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_WED)),
                    cursor.getInt(cursor.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_THUR)),
                    cursor.getInt(cursor.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_FRI)),
                    cursor.getInt(cursor.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_SAT)),
                    cursor.getString(cursor.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_NOTES)),
                    cursor.getString(cursor.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_INSTR_NAME)),
                    cursor.getString(cursor.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_INSTR_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(CourseCalendarInfo.FeedEntry.COLUMN_WEBSITE))
            );
            cursor.close();
        }
        else{
            resetInfo();
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
