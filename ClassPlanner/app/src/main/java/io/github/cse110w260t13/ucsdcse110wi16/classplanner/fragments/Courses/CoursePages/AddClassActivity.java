package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Courses.CoursePages;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.calendar_database.CalendarContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.calendar_database.CalendarInfo;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.course_database.CourseCalendarContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.course_database.CourseCalendarInfo;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Courses.CourseUtil.DateSelector;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Courses.CourseUtil.TimeSelector;

public class AddClassActivity extends AppCompatActivity {
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private final DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MMM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addclass);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        EditText startTime = (EditText) findViewById(R.id.start_time_picker);
        EditText endTime = (EditText) findViewById(R.id.end_time_picker);
        EditText endDate = (EditText) findViewById(R.id.end_date_picker);
        Button add = (Button) findViewById(R.id.add);
        Button cancel = (Button) findViewById(R.id.cancel);

        clickHandler click_handler = new clickHandler();

        add.setOnClickListener(click_handler);
        cancel.setOnClickListener(click_handler);
        startTime.setOnClickListener(click_handler);
        endTime.setOnClickListener(click_handler);
        endDate.setOnClickListener(click_handler);
    }

    private class clickHandler implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add:
                    if (insertAllData()) {
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

    private boolean checkValidity(EditText course, EditText email, EditText website,
                                  EditText strt, EditText end) {
        String courseStr = course.getText().toString();
        String emailStr = email.getText().toString();
        String webStr = website.getText().toString();
        String startStr = strt.getText().toString();
        String endStr = end.getText().toString();

        TextInputLayout TICourseName = (TextInputLayout) findViewById(R.id.ti_course);
        TextInputLayout TIEmail = (TextInputLayout) findViewById(R.id.ti_email);
        TextInputLayout TIWebsite = (TextInputLayout) findViewById(R.id.ti_website);
        TextInputLayout TIstart = (TextInputLayout) findViewById(R.id.ti_start);
        TextInputLayout TIend = (TextInputLayout) findViewById(R.id.ti_end);

        boolean valid = true;

        if (TextUtils.isEmpty(courseStr)) {
            TICourseName.setError("Course name is required.");
            valid = false;
        }
        //E-mail can be empty but not invalid.
        if (!TextUtils.isEmpty(emailStr) && !isEmailValid(emailStr)) {
            TIEmail.setError("E-mail is not valid.");
            valid = false;
        }
        //Website can be empty but not invalid.
        if (!TextUtils.isEmpty(webStr) && !URLUtil.isValidUrl(webStr)) {
            TIWebsite.setError("Website is not valid.");
            valid = false;
        }

        String[] startTime = startStr.split(":");
        String[] endTime = endStr.split(":");
        int startHr = 0;
        int startMin = 0;
        int endHr = 0;
        int endMin = 0;
        boolean timeValid = true;

        //Start time cannot be later than end time.
        //Time must be filled.
        if (!TextUtils.isEmpty(startStr) && !TextUtils.isEmpty(endStr)) {
            startHr = Integer.parseInt(startTime[0]);
            startMin = Integer.parseInt(startTime[1]);
            endHr = Integer.parseInt(endTime[0]);
            endMin = Integer.parseInt(endTime[1]);
        } else if (TextUtils.isEmpty(startStr)) {
            TIstart.setError("Time is required.");
            timeValid = false;
        }
        if (TextUtils.isEmpty(endStr)) {
            TIend.setError("End time required.");
            timeValid = false;
        }

        if (timeValid) {
            if (startHr > endHr) {
                TIstart.setError("Start time cannot be later than end time.");
                TIend.setError("Start time cannot be later than end time.");
                valid = false;
            } else if (startHr == endHr && startMin > endMin) {
                TIstart.setError("Start time cannot be later than end time.");
                TIend.setError("Start time cannot be later than end time.");
                valid = false;
            }
        }

        return valid;
    }

    private boolean isEmailValid(String email) {
        Pattern pattern = Pattern.compile("^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean insertAllData() {
        EditText course = (EditText) findViewById(R.id.text_coursename);
        EditText loc = (EditText) findViewById(R.id.edit_loc);
        EditText startTime = (EditText) findViewById(R.id.start_time_picker);
        EditText endTime = (EditText) findViewById(R.id.end_time_picker);
        EditText endDate = (EditText) findViewById(R.id.end_date_picker);
        CheckBox sun = (CheckBox) findViewById(R.id.sun);
        CheckBox mon = (CheckBox) findViewById(R.id.mon);
        CheckBox tue = (CheckBox) findViewById(R.id.tue);
        CheckBox wed = (CheckBox) findViewById(R.id.wed);
        CheckBox thur = (CheckBox) findViewById(R.id.thur);
        CheckBox fri = (CheckBox) findViewById(R.id.fri);
        CheckBox sat = (CheckBox) findViewById(R.id.sat);
        EditText notes = (EditText) findViewById(R.id.notes);
        EditText instr = (EditText) findViewById(R.id.edit_instr);
        EditText email = (EditText) findViewById(R.id.edit_instr_email);
        EditText web = (EditText) findViewById(R.id.website);

        if (!checkValidity(course, email, web, startTime, endTime)) {
            return false;
        }

        insertCourseData(course, loc, startTime, endTime, sun, mon, tue, wed, thur,
                fri, sat, notes, instr, email, web);

        insertCalendarData(course, loc, startTime, endTime, endDate, sun, mon, tue, wed, thur,
                fri, sat);
        return true;
    }

    private void insertCourseData(EditText course, EditText loc, EditText startTime, EditText endTime,
                                  CheckBox sun, CheckBox mon, CheckBox tue, CheckBox wed,
                                  CheckBox thur, CheckBox fri, CheckBox sat,
                                  EditText notes, EditText instr, EditText email, EditText web) {

        ContentValues values = new ContentValues();

        String val = course.getText().toString();
        values.put(CourseCalendarInfo.FeedEntry.COLUMN_COURSE_NAME, val);
        val = loc.getText().toString();
        values.put(CourseCalendarInfo.FeedEntry.COLUMN_COURSE_LOC, val);
        val = startTime.getText().toString();
        values.put(CourseCalendarInfo.FeedEntry.COLUMN_START_TIME, val);
        val = endTime.getText().toString();
        values.put(CourseCalendarInfo.FeedEntry.COLUMN_END_TIME, val);

        boolean checked = sun.isChecked();
        values.put(CourseCalendarInfo.FeedEntry.COLUMN_SUN, checked);
        checked = mon.isChecked();
        values.put(CourseCalendarInfo.FeedEntry.COLUMN_MON, checked);
        checked = tue.isChecked();
        values.put(CourseCalendarInfo.FeedEntry.COLUMN_TUE, checked);
        checked = wed.isChecked();
        values.put(CourseCalendarInfo.FeedEntry.COLUMN_WED, checked);
        checked = thur.isChecked();
        values.put(CourseCalendarInfo.FeedEntry.COLUMN_THUR, checked);
        checked = fri.isChecked();
        values.put(CourseCalendarInfo.FeedEntry.COLUMN_FRI, checked);
        checked = sat.isChecked();
        values.put(CourseCalendarInfo.FeedEntry.COLUMN_SAT, checked);

        val = notes.getText().toString();
        values.put(CourseCalendarInfo.FeedEntry.COLUMN_NOTES, val);
        val = instr.getText().toString();
        values.put(CourseCalendarInfo.FeedEntry.COLUMN_INSTR_NAME, val);
        val = email.getText().toString();
        values.put(CourseCalendarInfo.FeedEntry.COLUMN_INSTR_EMAIL, val);
        val = web.getText().toString();
        values.put(CourseCalendarInfo.FeedEntry.COLUMN_WEBSITE, val);

        ContentResolver cr = getContentResolver();
        cr.insert(CourseCalendarContentProvider.CONTENT_URI, values);
    }

    //This code sucks really bad. Please forgive me.
    private void insertCalendarData(EditText course, EditText loc, EditText startTime,
                                    EditText endTime, EditText endDate,
                                    CheckBox sun, CheckBox mon, CheckBox tue, CheckBox wed,
                                    CheckBox thur, CheckBox fri, CheckBox sat) {
        Log.d("insertCalendarData", "inserting");
        ContentValues values = new ContentValues();
        String val = null;
        ContentResolver cr = getContentResolver();

        ArrayList<Integer> daysOfWeek = new ArrayList<Integer>();
        if (sun.isChecked()) daysOfWeek.add(DateTimeConstants.SUNDAY);
        if (mon.isChecked()) daysOfWeek.add(DateTimeConstants.MONDAY);
        if (tue.isChecked()) daysOfWeek.add(DateTimeConstants.TUESDAY);
        if (wed.isChecked()) daysOfWeek.add(DateTimeConstants.WEDNESDAY);
        if (thur.isChecked()) daysOfWeek.add(DateTimeConstants.THURSDAY);
        if (fri.isChecked()) daysOfWeek.add(DateTimeConstants.FRIDAY);
        if (sat.isChecked()) daysOfWeek.add(DateTimeConstants.SATURDAY);

        Log.d("insertCalendarData", "arraylist size is: "+daysOfWeek.size());

        LocalDate startdate = new LocalDate();
        String end = endDate.getText().toString();
        Log.d("enddate string is ",end);
        LocalDate enddate = new LocalDate(end);

        for (int i = 0; i < 7; i++) {
            Log.d("insertCalendarData", "day is: "+i);
            for (int j = 0; j < daysOfWeek.size(); j++) {
                Log.d("Day of Week", startdate.getDayOfWeek() + " & " + daysOfWeek.get(j));
                if (startdate.getDayOfWeek() == daysOfWeek.get(j)) {
                    Log.d("insertCalendarData", "data insertion");
                    LocalDate copyStartDate = new LocalDate(startdate);
                    while (copyStartDate.isBefore(enddate)) {
                        val = copyStartDate.toString();
                        values.put(CalendarInfo.FeedEntry.DATE,val);

                        val = startTime.getText().toString();
                        values.put(CalendarInfo.FeedEntry.START_TIME, val);

                        val = endTime.getText().toString();
                        values.put(CalendarInfo.FeedEntry.END_TIME, val);

                        val = course.getText().toString();
                        values.put(CalendarInfo.FeedEntry.EVENT_TITLE, val);

                        val = loc.getText().toString();
                        values.put(CalendarInfo.FeedEntry.EVENT_DESCR, val);

                        cr.insert(CalendarContentProvider.CONTENT_URI, values);

                        values.clear();
                        copyStartDate = copyStartDate.plusWeeks(1);
                    }
                }
            }
            startdate = startdate.plusDays(1);
        }
    }


    @Override
    protected void onPause() {
        TextInputLayout TICourseName = (TextInputLayout) findViewById(R.id.ti_course);
        TextInputLayout TIEmail = (TextInputLayout) findViewById(R.id.ti_email);
        TextInputLayout TIWebsite = (TextInputLayout) findViewById(R.id.ti_website);
        TextInputLayout TIstart = (TextInputLayout) findViewById(R.id.ti_start);
        TextInputLayout TIend = (TextInputLayout) findViewById(R.id.ti_end);

        TICourseName.setError(null);
        TIEmail.setError(null);
        TIWebsite.setError(null);
        TIstart.setError(null);
        TIend.setError(null);

        super.onPause();
    }
}
