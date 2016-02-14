package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Courses.CoursePages;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Courses.CourseUtil.DateSelector;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Courses.CourseUtil.TimeSelector;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.calendar_database.CalendarContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.calendar_database.CalendarInfo;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.course_database.CourseCalendarContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.course_database.CourseCalendarInfo;

public class AddClassActivity extends AppCompatActivity {
    public static final String UPDATE = "update";
    public static final String CREATE = "create";

    private String mode;
    private String currName;

    private EditText[] editTexts;
    private CheckBox[] checkBoxes;
    private Boolean[] isChecked;
    private String[] editTextsInfo;
    private TextInputLayout[] errors;

    /**-------------------------------------------------------------------------------------------
     * private enum classes for array access
     *-------------------------------------------------------------------------------------------*/
    private enum Edits{
        COURSE, LOCATION, STARTTIME, ENDTIME, ENDDATE, NOTES, INSTR, EMAIL, WEB
    }

    private enum Boxes{
        SUN, MON, TUE, WED, THUR, FRI, SAT
    }

    private enum Error{
        COURSE, EMAIL, WEB, STARTTIME, ENDTIME, ENDDATE
    }

    /**-------------------------------------------------------------------------------------------
     * onCreate
     *-------------------------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addclass);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        initializeViews();
        initializeErrors();

        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");
        if(mode.contentEquals(UPDATE)){
            currName = intent.getStringExtra("class");
            displayCurrentInfo(currName);
        }

        Button add = (Button) findViewById(R.id.add);
        Button cancel = (Button) findViewById(R.id.cancel);

        clickHandler click_handler = new clickHandler();

        add.setOnClickListener(click_handler);
        cancel.setOnClickListener(click_handler);
        editTexts[Edits.STARTTIME.ordinal()].setOnClickListener(click_handler);
        editTexts[Edits.ENDTIME.ordinal()].setOnClickListener(click_handler);
        editTexts[Edits.ENDDATE.ordinal()].setOnClickListener(click_handler);
    }

    /**-------------------------------------------------------------------------------------------
     * Code in the following segment deals with displaying information to the user when the
     * user enters the activity on update mode.
     *-------------------------------------------------------------------------------------------*/
    private void displayCurrentInfo(String currName){
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(CourseCalendarContentProvider.CONTENT_URI,
                CourseCalendarInfo.FeedEntry.ALL_COLUMNS,
                CourseCalendarInfo.FeedEntry.COLUMN_COURSE_NAME + "=?",
                new String[] { currName + "" }, null);

        if(cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            updateDisplayInfo(
                    cursor.getString(cursor.getColumnIndex
                            (CourseCalendarInfo.FeedEntry.COLUMN_COURSE_NAME)),
                    cursor.getString(cursor.getColumnIndex
                            (CourseCalendarInfo.FeedEntry.COLUMN_COURSE_LOC)),
                    cursor.getString(cursor.getColumnIndex
                            (CourseCalendarInfo.FeedEntry.COLUMN_START_TIME)),
                    cursor.getString(cursor.getColumnIndex
                            (CourseCalendarInfo.FeedEntry.COLUMN_END_TIME)),
                    cursor.getString(cursor.getColumnIndex
                            (CourseCalendarInfo.FeedEntry.COLUMN_END_DATE)),
                    cursor.getInt(cursor.getColumnIndex
                            (CourseCalendarInfo.FeedEntry.COLUMN_SUN)),
                    cursor.getInt(cursor.getColumnIndex
                            (CourseCalendarInfo.FeedEntry.COLUMN_MON)),
                    cursor.getInt(cursor.getColumnIndex
                            (CourseCalendarInfo.FeedEntry.COLUMN_TUE)),
                    cursor.getInt(cursor.getColumnIndex
                            (CourseCalendarInfo.FeedEntry.COLUMN_WED)),
                    cursor.getInt(cursor.getColumnIndex
                            (CourseCalendarInfo.FeedEntry.COLUMN_THUR)),
                    cursor.getInt(cursor.getColumnIndex
                            (CourseCalendarInfo.FeedEntry.COLUMN_FRI)),
                    cursor.getInt(cursor.getColumnIndex
                            (CourseCalendarInfo.FeedEntry.COLUMN_SAT)),
                    cursor.getString(cursor.getColumnIndex
                            (CourseCalendarInfo.FeedEntry.COLUMN_NOTES)),
                    cursor.getString(cursor.getColumnIndex
                            (CourseCalendarInfo.FeedEntry.COLUMN_INSTR_NAME)),
                    cursor.getString(cursor.getColumnIndex
                            (CourseCalendarInfo.FeedEntry.COLUMN_INSTR_EMAIL)),
                    cursor.getString(cursor.getColumnIndex
                            (CourseCalendarInfo.FeedEntry.COLUMN_WEBSITE))
            );
        }
    }

    public void updateDisplayInfo(String name, String loc, String startTime,
                            String endTime, String endDate, int sun, int mon, int tue,
                            int wed, int thur, int fri, int sat,
                            String notes, String instrName, String email,
                            String web){

        editTexts[Edits.COURSE.ordinal()].setText(name);
        editTexts[Edits.LOCATION.ordinal()].setText(loc);
        editTexts[Edits.STARTTIME.ordinal()].setText(startTime);
        editTexts[Edits.ENDTIME.ordinal()].setText(endTime);
        editTexts[Edits.ENDDATE.ordinal()].setText(endDate);

        checkBoxes[Boxes.SUN.ordinal()].setChecked(sun != 0);
        checkBoxes[Boxes.MON.ordinal()].setChecked(mon != 0);
        checkBoxes[Boxes.TUE.ordinal()].setChecked(tue != 0);
        checkBoxes[Boxes.WED.ordinal()].setChecked(wed!=0);
        checkBoxes[Boxes.THUR.ordinal()].setChecked(thur!=0);
        checkBoxes[Boxes.FRI.ordinal()].setChecked(fri!= 0);
        checkBoxes[Boxes.SAT.ordinal()].setChecked(sat != 0);

        editTexts[Edits.NOTES.ordinal()].setText(notes);
        editTexts[Edits.INSTR.ordinal()].setText(instrName);
        editTexts[Edits.EMAIL.ordinal()].setText(email);
        editTexts[Edits.WEB.ordinal()].setText(web);
    }

    /**--------------------------------------------------------------------------------------------
     * This segment initializes values of EditTexts, CheckBoxes, Strings, and Errors  to class
     * member arrays so that other class members have easy access
     *-------------------------------------------------------------------------------------------*/

    private void initializeViews(){
        EditText course = (EditText) findViewById(R.id.text_coursename);
        EditText loc = (EditText) findViewById(R.id.edit_loc);
        EditText startTime = (EditText) findViewById(R.id.start_time_picker);
        EditText endTime = (EditText) findViewById(R.id.end_time_picker);
        EditText endDate = (EditText) findViewById(R.id.end_date_picker);
        EditText notes = (EditText) findViewById(R.id.notes);
        EditText instr = (EditText) findViewById(R.id.edit_instr);
        EditText email = (EditText) findViewById(R.id.edit_instr_email);
        EditText web = (EditText) findViewById(R.id.website);

        CheckBox sun = (CheckBox) findViewById(R.id.sun);
        CheckBox mon = (CheckBox) findViewById(R.id.mon);
        CheckBox tue = (CheckBox) findViewById(R.id.tue);
        CheckBox wed = (CheckBox) findViewById(R.id.wed);
        CheckBox thur = (CheckBox) findViewById(R.id.thur);
        CheckBox fri = (CheckBox) findViewById(R.id.fri);
        CheckBox sat = (CheckBox) findViewById(R.id.sat);

        editTexts = new EditText[]{course, loc, startTime, endTime, endDate,
        notes, instr, email, web};

        checkBoxes = new CheckBox[]{ sun, mon, tue, wed, thur, fri, sat };
    }

    private void initializeBoxes(){
        isChecked = new Boolean[checkBoxes.length];
        for(int i=0; i< isChecked.length; i++){
            isChecked[i] = checkBoxes[i].isChecked();
        }
    }

    private void initializeStrings(){
        editTextsInfo = new String[editTexts.length];
        for(int i=0; i < editTextsInfo.length; i++){
            editTextsInfo[i] = editTexts[i].getText().toString();
        }
    }

    private void initializeErrors(){
        TextInputLayout TICourseName = (TextInputLayout) findViewById(R.id.ti_course);
        TextInputLayout TIEmail = (TextInputLayout) findViewById(R.id.ti_email);
        TextInputLayout TIWebsite = (TextInputLayout) findViewById(R.id.ti_website);
        TextInputLayout TIstart = (TextInputLayout) findViewById(R.id.ti_start);
        TextInputLayout TIend = (TextInputLayout) findViewById(R.id.ti_end);
        TextInputLayout TIendDate = (TextInputLayout) findViewById(R.id.ti_end_date);

        errors = new TextInputLayout[]{TICourseName, TIEmail, TIWebsite, TIstart, TIend, TIendDate};
    }

    /**--------------------------------------------------------------------------------------------
     * clickHandler class which handles all clicks for the views found within the fragment.
     *-------------------------------------------------------------------------------------------*/
    private class clickHandler implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add:
                    //Now add a new method to update and choose based on mode
                    //which method to enter.
                    initializeBoxes();
                    initializeStrings();
                    if (insertAllData(mode)) {
                        Intent intent = getIntent();
                        intent.putExtra("newName", editTextsInfo[Edits.COURSE.ordinal()]);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    break;
                case R.id.cancel:
                    finish();
                    break;
                case R.id.start_time_picker:
                    final Bundle startArguments = new Bundle();
                    startArguments.putInt("time", R.id.start_time_picker);
                    DialogFragment startTimePicker = new TimeSelector();
                    startTimePicker.setArguments(startArguments);
                    startTimePicker.show(getSupportFragmentManager(), "startTimePicker");
                    break;
                case R.id.end_time_picker:
                    final Bundle endArguments = new Bundle();
                    endArguments.putInt("time", R.id.end_time_picker);
                    DialogFragment endTimePicker = new TimeSelector();
                    endTimePicker.setArguments(endArguments);
                    endTimePicker.show(getSupportFragmentManager(), "EndTimePicker");
                    break;
                case R.id.end_date_picker:
                    final Bundle endDateArguments = new Bundle();
                    endDateArguments.putInt("endDate", R.id.end_date_picker);
                    DialogFragment endDatePicker = new DateSelector();
                    endDatePicker.setArguments(endDateArguments);
                    endDatePicker.show(getSupportFragmentManager(), "endDatePicker");
                    break;
            }
        }
    }

    /**--------------------------------------------------------------------------------------------
     * checkValidity checks the validity of everything the user has put into the form.
     * Section contains methods to check the validity of user input fields
     * @return boolean which indicates whether or not input was valid.
     *-------------------------------------------------------------------------------------------*/
    private boolean checkValidity() {
        boolean valid = true;

        if(!haveCourseErrors()) valid = false;
        if(!haveEmailErrors()) valid = false;
        if(!haveWebErrors()) valid = false;
        if(!haveTimeErrors()) valid = false;
        if(!haveInvalidDays()) valid = false;
        if(!haveInvalidEndDate()) valid = false;
        return valid;
    }

    //Course Errors
    private boolean haveCourseErrors(){
        if (TextUtils.isEmpty(editTextsInfo[Edits.COURSE.ordinal()])) {
            errors[Error.COURSE.ordinal()].setError("Course name is required.");
            return false;
        }
        errors[Error.COURSE.ordinal()].setError(null);
        return true;
    }

    //Email Errors
    private boolean haveEmailErrors(){
        //E-mail can be empty but not invalid.
        if (!TextUtils.isEmpty(editTextsInfo[Edits.EMAIL.ordinal()])
                && !isEmailValid(editTextsInfo[Edits.EMAIL.ordinal()])) {
            errors[Error.EMAIL.ordinal()].setError("E-mail is not valid.");
            return false;
        }
        errors[Error.EMAIL.ordinal()].setError(null);
        return true;
    }

    //Web Errors
    private boolean haveWebErrors(){
        //Website can be empty but not invalid.
        if (!TextUtils.isEmpty(editTextsInfo[Edits.WEB.ordinal()])
                && !URLUtil.isValidUrl(editTextsInfo[Edits.WEB.ordinal()])) {
            errors[Error.WEB.ordinal()].setError("Website is not valid.");
            return false;
        }
        errors[Error.WEB.ordinal()].setError(null);
        return true;
    }

    //Time Eerrors
    private boolean haveTimeErrors(){
        String[] startTime = editTextsInfo[Edits.STARTTIME.ordinal()].split(":");
        String[] endTime = editTextsInfo[Edits.ENDTIME.ordinal()].split(":");
        int startHr = 0;
        int startMin = 0;
        int endHr = 0;
        int endMin = 0;
        boolean timeValid = true;
        boolean valid = true;

        //Start time cannot be later than end time and Time must be filled.
        if (!TextUtils.isEmpty(editTextsInfo[Edits.STARTTIME.ordinal()])
                && !TextUtils.isEmpty(editTextsInfo[Edits.ENDTIME.ordinal()])) {
            startHr = Integer.parseInt(startTime[0]);
            startMin = Integer.parseInt(startTime[1]);
            endHr = Integer.parseInt(endTime[0]);
            endMin = Integer.parseInt(endTime[1]);
        }
        else if (TextUtils.isEmpty(editTextsInfo[Edits.STARTTIME.ordinal()])) {
            errors[Error.STARTTIME.ordinal()].setError("Time is required.");
            timeValid = false;
        }
        if (TextUtils.isEmpty(editTextsInfo[Edits.ENDTIME.ordinal()])) {
            errors[Error.ENDTIME.ordinal()].setError("End time required.");
            timeValid = false;
        }

        if (timeValid) {
            if (startHr > endHr) {
                errors[Error.STARTTIME.ordinal()]
                        .setError("Start time cannot be later than end time.");
                errors[Error.ENDTIME.ordinal()]
                        .setError("Start time cannot be later than end time.");
                valid = false;
            } else if (startHr == endHr && startMin > endMin) {
                errors[Error.STARTTIME.ordinal()]
                        .setError("Start time cannot be later than end time.");
                errors[Error.ENDTIME.ordinal()]
                        .setError("Start time cannot be later than end time.");
                valid = false;
            }
            else{
                errors[Error.STARTTIME.ordinal()].setError(null);
                errors[Error.ENDTIME.ordinal()].setError(null);
            }
        }
        return valid;
    }

    //No Days Selected Error
    private boolean haveInvalidDays(){
        boolean valid = false;
        for(boolean checked: isChecked){
            if (checked){
                valid = true;
                break;
            }
        }
        TextView selectDays = (TextView) findViewById(R.id.selectDays);
        if(!valid) {
            selectDays.setTextColor(ContextCompat.getColor(this, R.color.red));
        }
        else {
            selectDays.setTextColor(ContextCompat.getColor(this, R.color.colorSecondaryText));
        }
        return true;
    }

    //Invalid End Date Error
    private boolean haveInvalidEndDate(){
        if(!isEndDateValid()){
            errors[Error.ENDDATE.ordinal()].setError("End date is not valid.");
            return false;
        }
        errors[Error.ENDDATE.ordinal()].setError(null);
        return true;
    }

    //Check if the email is invalid
    private boolean isEmailValid(String email) {
        Pattern pattern = Pattern.compile("^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //Check if the end date is invalid
    private boolean isEndDateValid(){
        LocalDate startDate = new LocalDate();
        String end = editTextsInfo[Edits.ENDDATE.ordinal()];
        if(end.isEmpty()) return false;
        LocalDate endDate = new LocalDate(end);
        return(startDate.isBefore(endDate));
    }

    /**--------------------------------------------------------------------------------------------
     * The methods in this section deal with inserting/updating data into the Calendar db
     * Courses db.
     * insertAllData handles the calls for data insertion and completion then finishes the activ.
     * insertCourseData and insertCalendarData insert to their respective databases.
     *--------------------------------------------------------------------------------------------*/
    private boolean insertAllData(String mode) {
        if (!checkValidity()) {
            return false;
        }
        insertCourseData(mode);
        insertCalendarData(mode);
        return true;
    }

    private void insertCourseData(String mode) {

        ContentValues values = new ContentValues();

        values.put(CourseCalendarInfo.FeedEntry.COLUMN_COURSE_NAME,
                editTextsInfo[Edits.COURSE.ordinal()]);
        values.put(CourseCalendarInfo.FeedEntry.COLUMN_COURSE_LOC,
                editTextsInfo[Edits.LOCATION.ordinal()]);
        values.put(CourseCalendarInfo.FeedEntry.COLUMN_START_TIME,
                editTextsInfo[Edits.STARTTIME.ordinal()]);
        values.put(CourseCalendarInfo.FeedEntry.COLUMN_END_TIME,
                editTextsInfo[Edits.ENDTIME.ordinal()]);
        values.put(CourseCalendarInfo.FeedEntry.COLUMN_END_DATE,
                editTextsInfo[Edits.ENDDATE.ordinal()]);
        values.put(CourseCalendarInfo.FeedEntry.COLUMN_NOTES,
                editTextsInfo[Edits.NOTES.ordinal()]);
        values.put(CourseCalendarInfo.FeedEntry.COLUMN_INSTR_NAME,
                editTextsInfo[Edits.INSTR.ordinal()]);
        values.put(CourseCalendarInfo.FeedEntry.COLUMN_INSTR_EMAIL,
                editTextsInfo[Edits.EMAIL.ordinal()]);
        values.put(CourseCalendarInfo.FeedEntry.COLUMN_WEBSITE,
                editTextsInfo[Edits.WEB.ordinal()]);

        values.put(CourseCalendarInfo.FeedEntry.COLUMN_SUN,
                isChecked[Boxes.SUN.ordinal()]);
        values.put(CourseCalendarInfo.FeedEntry.COLUMN_MON,
                isChecked[Boxes.MON.ordinal()]);
        values.put(CourseCalendarInfo.FeedEntry.COLUMN_TUE,
                isChecked[Boxes.TUE.ordinal()]);
        values.put(CourseCalendarInfo.FeedEntry.COLUMN_WED,
                isChecked[Boxes.WED.ordinal()]);
        values.put(CourseCalendarInfo.FeedEntry.COLUMN_THUR,
                isChecked[Boxes.THUR.ordinal()]);
        values.put(CourseCalendarInfo.FeedEntry.COLUMN_FRI,
                isChecked[Boxes.FRI.ordinal()]);
        values.put(CourseCalendarInfo.FeedEntry.COLUMN_SAT,
                isChecked[Boxes.SAT.ordinal()]);

        ContentResolver cr = getContentResolver();
        if (mode.contentEquals(CREATE)) {
            cr.insert(CourseCalendarContentProvider.CONTENT_URI, values);
        }
        else{
            cr.update(CourseCalendarContentProvider.CONTENT_URI, values,
                    CourseCalendarInfo.FeedEntry.COLUMN_COURSE_NAME + "=?",
                    new String[]{currName});
        }
    }

    private void insertCalendarData(String mode) {
        ContentValues values = new ContentValues();
        ContentResolver cr = getContentResolver();

        ArrayList<Integer> daysOfWeek = new ArrayList<Integer>();
        if (isChecked[Boxes.SUN.ordinal()]) daysOfWeek.add(DateTimeConstants.SUNDAY);
        if (isChecked[Boxes.MON.ordinal()]) daysOfWeek.add(DateTimeConstants.MONDAY);
        if (isChecked[Boxes.TUE.ordinal()]) daysOfWeek.add(DateTimeConstants.TUESDAY);
        if (isChecked[Boxes.WED.ordinal()]) daysOfWeek.add(DateTimeConstants.WEDNESDAY);
        if (isChecked[Boxes.THUR.ordinal()]) daysOfWeek.add(DateTimeConstants.THURSDAY);
        if (isChecked[Boxes.FRI.ordinal()]) daysOfWeek.add(DateTimeConstants.FRIDAY);
        if (isChecked[Boxes.SAT.ordinal()]) daysOfWeek.add(DateTimeConstants.SATURDAY);

        LocalDate startdate = new LocalDate();
        String end = editTextsInfo[Edits.ENDDATE.ordinal()];
        LocalDate enddate = new LocalDate(end);

        if(mode.contentEquals(UPDATE)){
            cr.delete(CalendarContentProvider.CONTENT_URI,
                    CalendarInfo.FeedEntry.EVENT_TITLE + "=?",
                    new String[]{currName});
        }

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < daysOfWeek.size(); j++) {
                if (startdate.getDayOfWeek() == daysOfWeek.get(j)) {
                    LocalDate copyStartDate = new LocalDate(startdate);
                    while (copyStartDate.isBefore(enddate)) {

                        values.put(CalendarInfo.FeedEntry.DATE,
                                copyStartDate.toString());
                        values.put(CalendarInfo.FeedEntry.START_TIME,
                                editTextsInfo[Edits.STARTTIME.ordinal()]);
                        values.put(CalendarInfo.FeedEntry.END_TIME,
                                editTextsInfo[Edits.ENDTIME.ordinal()]);
                        values.put(CalendarInfo.FeedEntry.EVENT_TITLE,
                                editTextsInfo[Edits.COURSE.ordinal()]);
                        values.put(CalendarInfo.FeedEntry.EVENT_DESCR,
                                editTextsInfo[Edits.LOCATION.ordinal()]);
                        cr.insert(CalendarContentProvider.CONTENT_URI, values);
                        copyStartDate = copyStartDate.plusWeeks(1);
                    }
                }
            }
            startdate = startdate.plusDays(1);
        }
    }

    /**-------------------------------------------------------------------------------------------
     * OnPause resets the error messages that were triggered back to default status.
     *-------------------------------------------------------------------------------------------*/
    @Override
    protected void onPause() {
        for(TextInputLayout err: errors){
            err.setError(null);
        }

        super.onPause();
    }
}
