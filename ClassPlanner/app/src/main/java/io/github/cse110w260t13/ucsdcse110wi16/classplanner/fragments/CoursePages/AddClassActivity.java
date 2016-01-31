package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.CoursePages;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.CourseCalendarContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.CourseCalendarDbHelper;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.CourseCalendarInfo;

public class AddClassActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addclass);

        final Bundle startArguments = new Bundle();
        final Bundle endArguments = new Bundle();
        startArguments.putInt("time",R.id.start_time_picker);
        endArguments.putInt("time", R.id.end_time_picker);

        EditText startTime = (EditText) findViewById(R.id.start_time_picker);
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newTimePicker = new TimeSelector();
                newTimePicker.setArguments(startArguments);
                newTimePicker.show(getSupportFragmentManager(), "startTimePicker");
            }
        });

        EditText endTime = (EditText) findViewById(R.id.end_time_picker);
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newTimePicker = new TimeSelector();
                newTimePicker.setArguments(endArguments);
                newTimePicker.show(getSupportFragmentManager(), "EndTimePicker");
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        Button add = (Button)findViewById(R.id.add);
        Button cancel = (Button) findViewById(R.id.cancel);
        clickHandler click_handler = new clickHandler();
        add.setOnClickListener(click_handler);
        cancel.setOnClickListener(click_handler);
    }
    private class clickHandler implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add:
                    insertAllData();
                    finish();
                    break;
                case R.id.cancel:
                    finish();
                    break;
            }
        }
    }

    private void insertAllData(){
        EditText course = (EditText) findViewById(R.id.text_coursename);
        EditText loc = (EditText) findViewById(R.id.edit_loc);
        EditText startTime = (EditText) findViewById(R.id.start_time_picker);
        EditText endTime = (EditText) findViewById(R.id.end_time_picker);
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


        /* This will be the most efficient way to insert all the data
        for (int i =0; i < allInfo.length; i++){
            String val = allInfo[i].getText().toString();
            values.put(CourseCalendarInfo.FeedEntry.ALL_COLUMNS[i], val);
        }

        db.insert(CourseCalendarInfo.FeedEntry.TABLE_NAME, null, values);
        values.clear();*/

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

        val=notes.getText().toString();
        values.put(CourseCalendarInfo.FeedEntry.COLUMN_NOTES, val);
        val=instr.getText().toString();
        values.put(CourseCalendarInfo.FeedEntry.COLUMN_INSTR_NAME, val);
        val=email.getText().toString();
        values.put(CourseCalendarInfo.FeedEntry.COLUMN_INSTR_EMAIL, val);
        val=web.getText().toString();
        values.put(CourseCalendarInfo.FeedEntry.COLUMN_WEBSITE, val);

        ContentResolver cr = getContentResolver();
        cr.insert(CourseCalendarContentProvider.CONTENT_URI,values);
    }
}
