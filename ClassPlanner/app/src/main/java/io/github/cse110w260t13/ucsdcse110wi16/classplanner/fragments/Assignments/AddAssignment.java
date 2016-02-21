package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Assignments;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
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

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.assignment_database.AssignmentInfo;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.calendar_database.CalendarContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.calendar_database.CalendarInfo;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.assignment_database.AssignmentContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.assignment_database.AssignmentInfo;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Courses.CourseUtil.DateSelector;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Courses.CourseUtil.TimeSelector;

public class AddAssignment extends AppCompatActivity {
    public static final String UPDATE = "update";
    public static final String CREATE = "create";

    private String mode;
    private String currName;

    private EditText[] editTexts;
    private Boolean[] isChecked;
    private String[] editTextsInfo;
    private TextInputLayout[] errors;

    private enum Edits{
        COURSE, NAME, TYPE, POINTSPOS, POINTSEARNED
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_assignment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        initializeViews();

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
    }

    private void displayCurrentInfo(String currName){
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(AssignmentContentProvider.CONTENT_URI,
                AssignmentInfo.FeedEntry.ALL_COLUMNS,
                AssignmentInfo.FeedEntry.ASSIGNMENT_NAME + "=?",
                new String[] { currName + "" }, null);

        if(cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            updateDisplayInfo(
                    cursor.getString(cursor.getColumnIndex(AssignmentInfo.FeedEntry.COURSE_NAME)),
                    cursor.getString(cursor.getColumnIndex(AssignmentInfo.FeedEntry.ASSIGNMENT_NAME)),
                    cursor.getString(cursor.getColumnIndex(AssignmentInfo.FeedEntry.TYPE)),
                    cursor.getString(cursor.getColumnIndex(AssignmentInfo.FeedEntry.POINTS_POSSIBLE)),
                    cursor.getString(cursor.getColumnIndex(AssignmentInfo.FeedEntry.POINTS_EARNED))

            );
        }
    }

    public void updateDisplayInfo(String course, String name, String type, String points,
                                  String earned/*, String notes*/){

        editTexts[Edits.COURSE.ordinal()].setText(course);
        editTexts[Edits.NAME.ordinal()].setText(name);
        editTexts[Edits.TYPE.ordinal()].setText(type);
        editTexts[Edits.POINTSPOS.ordinal()].setText(points);
        editTexts[Edits.POINTSEARNED.ordinal()].setText(earned);
    }

    /**--------------------------------------------------------------------------------------------
     * This segment initializes values of EditTexts, CheckBoxes, Strings, and Errors  to class
     * member arrays so that other class members have easy access
     *-------------------------------------------------------------------------------------------*/

    private void initializeViews(){
        EditText course = (EditText) findViewById(R.id.text_coursename);
        EditText name = (EditText) findViewById(R.id.edit_name);
        EditText type = (EditText) findViewById(R.id.edit_type);
        EditText pointspos = (EditText) findViewById(R.id.edit_possible);
        EditText earned = (EditText) findViewById(R.id.edit_earned);
        EditText notes = (EditText) findViewById(R.id.notes);
        editTexts = new EditText[]{course, name, type, pointspos, earned ,notes};

    }


    private void initializeStrings(){
        editTextsInfo = new String[editTexts.length];
        for(int i=0; i < editTextsInfo.length; i++){
            editTextsInfo[i] = editTexts[i].getText().toString();
        }
    }


    /**--------------------------------------------------------------------------------------------
     * clickHandler class which handles all clicks for the views found within the fragment.
     *-------------------------------------------------------------------------------------------*/
    private class clickHandler implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add:
                    initializeStrings();
                    if (insertAllData(mode)) {
                        finish();
                    }
                    break;
                case R.id.cancel:
                    finish();
                    break;

            }
        }
    }


    /**--------------------------------------------------------------------------------------------
     * The methods in this section deal with inserting/updating data into the Calendar db
     * Courses db.
     * insertAllData handles the calls for data insertion and completion then finishes the activ.
     * insertCourseData and insertCalendarData insert to their respective databases.
     *--------------------------------------------------------------------------------------------*/
    private boolean insertAllData(String mode) {
        insertAssignmentData(mode);
        return true;
    }


    private void insertAssignmentData(String mode) {

        ContentValues values = new ContentValues();

        values.put(AssignmentInfo.FeedEntry.COURSE_NAME,
                editTextsInfo[Edits.COURSE.ordinal()]);
        values.put(AssignmentInfo.FeedEntry.ASSIGNMENT_NAME,
                editTextsInfo[Edits.NAME.ordinal()]);
        values.put(AssignmentInfo.FeedEntry.TYPE,
                editTextsInfo[Edits.TYPE.ordinal()]);
        values.put(AssignmentInfo.FeedEntry.POINTS_POSSIBLE,
                editTextsInfo[Edits.POINTSPOS.ordinal()]);
        values.put(AssignmentInfo.FeedEntry.POINTS_EARNED,
                editTextsInfo[Edits.POINTSEARNED.ordinal()]);
        ContentResolver cr = getContentResolver();
        if (mode.contentEquals(CREATE)) {
            cr.insert(AssignmentContentProvider.CONTENT_URI, values);
        }
        else{
            cr.update(AssignmentContentProvider.CONTENT_URI, values,
                    AssignmentInfo.FeedEntry.ASSIGNMENT_NAME+"=?",
                    new String[]{currName});
        }
    }


}

