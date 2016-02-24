package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Courses.CoursePages;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Courses.CourseUtil.ErrorDialogFragment;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.course_database.CourseCalendarContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.course_database.CourseCalendarInfo;

public class ClassInfoFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    //identifiers for intents, loaders, and bundles
    public static final String MODE = "mode";
    public static final String CLASS = "class";
    public static final String ERROR = "error";
    public static final int REQUEST_CODE = 1;
    private static final int URL_LOADER = 0;

    //Argument values
    public static final String UPDATE = "update";

    //Private class information
    private String currName = null;
    private View rootView;
    private TextView[] textInfo;

    private enum Texts{
        COURSE, LOCATION, STARTTIME, ENDTIME, SUN, MON, TUE, WED, THUR, FRI, SAT,
        NOTES, INSTR, EMAIL, WEB
    }

    /**-------------------------------------------------------------------------------------------
     * onCreateView
     *-------------------------------------------------------------------------------------------*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_classinfo, container, false);

        initializeViews();

        getLoaderManager().initLoader(URL_LOADER, null, this);

        Button editButton = (Button) rootView.findViewById(R.id.edit_info);
        clickHandler handler = new clickHandler();
        editButton.setOnClickListener(handler);

        return rootView;
    }

    /**-------------------------------------------------------------------------------------------
     * Fills a private class array with the textviews of the fragment for later use.
     *-------------------------------------------------------------------------------------------*/
    private void initializeViews(){
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

        textInfo = new TextView[]{ nameTv, locTv, startTimeTv, endTimeTv, sunTv,
        monTv, tueTv, wedTv, thurTv, friTv, satTv, noteTv, instrTv, emailTv, webTv };

    }
    /**-------------------------------------------------------------------------------------------
     * clickHandle handles all click operations for the ClassInfoFragment
     *-------------------------------------------------------------------------------------------*/
    private class clickHandler implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.edit_info:
                    if(currName!=null) {
                        Intent intent = new Intent(getParentFragment().getContext(),
                                AddClassActivity.class);
                        intent.putExtra(MODE, UPDATE);
                        intent.putExtra(CLASS, currName);
                        getParentFragment().startActivityForResult(intent, REQUEST_CODE);
                    }
                    else{
                        ErrorDialogFragment error = new ErrorDialogFragment();
                        Bundle args = new Bundle();
                        args.putString(ERROR, "You currently have no classes.");
                        error.setArguments(args);
                        error.show(getFragmentManager(), "No Classes Error Popup");
                    }
                    break;
            }
        }
    }

    /**-------------------------------------------------------------------------------------------
     * selects the class to display information on on the basis of its parameters and queries
     * for the information
     *-------------------------------------------------------------------------------------------*/
    public void selectClass(String class_name){
        currName=class_name;

        if(class_name==null){
            resetInfo();
            return;
        }

        ContentResolver cr = getActivity().getContentResolver();
        Cursor cursor = cr.query(CourseCalendarContentProvider.CONTENT_URI,
                CourseCalendarInfo.GeneralInfo.ALL_COLUMNS,
                CourseCalendarInfo.GeneralInfo.COLUMN_COURSE_NAME + "=?",
                new String[]{class_name + ""}, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            update_info(
                    cursor.getString(cursor.getColumnIndex
                            (CourseCalendarInfo.GeneralInfo.COLUMN_COURSE_NAME)),
                    cursor.getString(cursor.getColumnIndex
                            (CourseCalendarInfo.GeneralInfo.COLUMN_COURSE_LOC)),
                    cursor.getString(cursor.getColumnIndex
                            (CourseCalendarInfo.GeneralInfo.COLUMN_START_TIME)),
                    cursor.getString(cursor.getColumnIndex
                            (CourseCalendarInfo.GeneralInfo.COLUMN_END_TIME)),
                    cursor.getInt(cursor.getColumnIndex
                            (CourseCalendarInfo.GeneralInfo.COLUMN_SUN)),
                    cursor.getInt(cursor.getColumnIndex
                            (CourseCalendarInfo.GeneralInfo.COLUMN_MON)),
                    cursor.getInt(cursor.getColumnIndex
                            (CourseCalendarInfo.GeneralInfo.COLUMN_TUE)),
                    cursor.getInt(cursor.getColumnIndex
                            (CourseCalendarInfo.GeneralInfo.COLUMN_WED)),
                    cursor.getInt(cursor.getColumnIndex
                            (CourseCalendarInfo.GeneralInfo.COLUMN_THUR)),
                    cursor.getInt(cursor.getColumnIndex
                            (CourseCalendarInfo.GeneralInfo.COLUMN_FRI)),
                    cursor.getInt(cursor.getColumnIndex
                            (CourseCalendarInfo.GeneralInfo.COLUMN_SAT)),
                    cursor.getString(cursor.getColumnIndex
                            (CourseCalendarInfo.GeneralInfo.COLUMN_NOTES)),
                    cursor.getString(cursor.getColumnIndex
                            (CourseCalendarInfo.GeneralInfo.COLUMN_INSTR_NAME)),
                    cursor.getString(cursor.getColumnIndex
                            (CourseCalendarInfo.GeneralInfo.COLUMN_INSTR_EMAIL)),
                    cursor.getString(cursor.getColumnIndex
                            (CourseCalendarInfo.GeneralInfo.COLUMN_WEBSITE))
            );
            cursor.close();
        }
    }

    /**-------------------------------------------------------------------------------------------
     * Displays/updates the information on the screen using its parameter values
     *-------------------------------------------------------------------------------------------*/
    public void update_info(String name, String loc, String startTime,
                            String endTime, int sun, int mon, int tue,
                            int wed, int thur, int fri, int sat,
                            String notes, String instrName, String email,
                            String web){
        if(textInfo != null) {
            textInfo[Texts.COURSE.ordinal()].setText(name);
            textInfo[Texts.LOCATION.ordinal()].setText(loc);
            textInfo[Texts.STARTTIME.ordinal()].setText(startTime);
            textInfo[Texts.ENDTIME.ordinal()].setText(endTime);

            setColor(textInfo[Texts.SUN.ordinal()], sun);
            setColor(textInfo[Texts.MON.ordinal()], mon);
            setColor(textInfo[Texts.TUE.ordinal()], tue);
            setColor(textInfo[Texts.WED.ordinal()], wed);
            setColor(textInfo[Texts.THUR.ordinal()], thur);
            setColor(textInfo[Texts.FRI.ordinal()], fri);
            setColor(textInfo[Texts.SAT.ordinal()], sat);

            textInfo[Texts.NOTES.ordinal()].setText(notes);
            textInfo[Texts.INSTR.ordinal()].setText(instrName);
            textInfo[Texts.EMAIL.ordinal()].setText(email);
            textInfo[Texts.WEB.ordinal()].setText(web);
        }
    }

    /**-------------------------------------------------------------------------------------------
     * Resets the information on the screen to default values
     *-------------------------------------------------------------------------------------------*/
    private void resetInfo(){
        textInfo[Texts.COURSE.ordinal()].setText(R.string.course_here);
        textInfo[Texts.LOCATION.ordinal()].setText(R.string.loc_here);
        textInfo[Texts.STARTTIME.ordinal()].setText(R.string.time_here);
        textInfo[Texts.ENDTIME.ordinal()].setText(R.string.time_here);

        setColor(textInfo[Texts.SUN.ordinal()], 0);
        setColor(textInfo[Texts.MON.ordinal()], 0);
        setColor(textInfo[Texts.TUE.ordinal()], 0);
        setColor(textInfo[Texts.WED.ordinal()], 0);
        setColor(textInfo[Texts.THUR.ordinal()], 0);
        setColor(textInfo[Texts.FRI.ordinal()], 0);
        setColor(textInfo[Texts.SAT.ordinal()], 0);

        textInfo[Texts.NOTES.ordinal()].setText(R.string.note_here);
        textInfo[Texts.INSTR.ordinal()].setText(R.string.instr_name_here);
        textInfo[Texts.EMAIL.ordinal()].setText(R.string.instr_email_here);
        textInfo[Texts.WEB.ordinal()].setText(R.string.web_here);

        currName = null;
    }

    /**-------------------------------------------------------------------------------------------
     * Sets the color of the given textview based on the value of the second parameter
     *-------------------------------------------------------------------------------------------*/
    private void setColor(TextView v, int checked){
        if (checked == 1) v.setTextColor(ContextCompat.getColor(getContext(),R.color.colorAccent));
        else v.setTextColor(ContextCompat.getColor(getContext(),android.R.color.primary_text_light));
    }

    /**-------------------------------------------------------------------------------------------
     * LOADER INTERFACE IMPLEMENTATION
     *-------------------------------------------------------------------------------------------*/
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = CourseCalendarInfo.GeneralInfo.ALL_COLUMNS;

        CursorLoader cursor_ld;
        if(currName == null) {
            cursor_ld = new CursorLoader(getContext(),
                    CourseCalendarContentProvider.CONTENT_URI, projection,
                    null, null, null);
        }
        else{
            cursor_ld = new CursorLoader(getContext(),
                    CourseCalendarContentProvider.CONTENT_URI, projection,
                    CourseCalendarInfo.GeneralInfo.COLUMN_COURSE_NAME + "=?",
                    new String[]{currName}, null);

        }
        return cursor_ld;
    }

    //Update the UI with results from query
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            update_info(
                    cursor.getString(cursor.getColumnIndex
                            (CourseCalendarInfo.GeneralInfo.COLUMN_COURSE_NAME)),
                    cursor.getString(cursor.getColumnIndex
                            (CourseCalendarInfo.GeneralInfo.COLUMN_COURSE_LOC)),
                    cursor.getString(cursor.getColumnIndex
                            (CourseCalendarInfo.GeneralInfo.COLUMN_START_TIME)),
                    cursor.getString(cursor.getColumnIndex
                            (CourseCalendarInfo.GeneralInfo.COLUMN_END_TIME)),
                    cursor.getInt(cursor.getColumnIndex
                            (CourseCalendarInfo.GeneralInfo.COLUMN_SUN)),
                    cursor.getInt(cursor.getColumnIndex
                            (CourseCalendarInfo.GeneralInfo.COLUMN_MON)),
                    cursor.getInt(cursor.getColumnIndex
                            (CourseCalendarInfo.GeneralInfo.COLUMN_TUE)),
                    cursor.getInt(cursor.getColumnIndex
                            (CourseCalendarInfo.GeneralInfo.COLUMN_WED)),
                    cursor.getInt(cursor.getColumnIndex
                            (CourseCalendarInfo.GeneralInfo.COLUMN_THUR)),
                    cursor.getInt(cursor.getColumnIndex
                            (CourseCalendarInfo.GeneralInfo.COLUMN_FRI)),
                    cursor.getInt(cursor.getColumnIndex
                            (CourseCalendarInfo.GeneralInfo.COLUMN_SAT)),
                    cursor.getString(cursor.getColumnIndex
                            (CourseCalendarInfo.GeneralInfo.COLUMN_NOTES)),
                    cursor.getString(cursor.getColumnIndex
                            (CourseCalendarInfo.GeneralInfo.COLUMN_INSTR_NAME)),
                    cursor.getString(cursor.getColumnIndex
                            (CourseCalendarInfo.GeneralInfo.COLUMN_INSTR_EMAIL)),
                    cursor.getString(cursor.getColumnIndex
                            (CourseCalendarInfo.GeneralInfo.COLUMN_WEBSITE))
            );
        }
        else{
            resetInfo();
        }
    }

    //Release any references/resources that are not needed anymore
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    /**-------------------------------------------------------------------------------------------
     * Tells the fragment what to do when it is resumed.
     *-------------------------------------------------------------------------------------------*/
    @Override
    public void onResume(){
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);
    }

}
