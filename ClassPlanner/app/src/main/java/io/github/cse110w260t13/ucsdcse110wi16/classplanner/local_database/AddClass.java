package io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;

public class AddClass extends AppCompatActivity{
    private SQLiteDatabase db;
    private ContentValues values;
    private CourseCalendarDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_database);

        mDbHelper = new CourseCalendarDbHelper(this.getBaseContext());
        db = mDbHelper.getWritableDatabase();
        values = new ContentValues();

    }

    public void onClick(View view){
        switch(view.getId()){
            case R.id.add:
                EditText editText = (EditText) findViewById(R.id.text_coursename);
                String val = editText.getText().toString();
                values.put(CourseCalendarInfo.FeedEntry.COLUMN_COURSE_NAME, val);
                db.insert(
                        CourseCalendarInfo.FeedEntry.TABLE_NAME,
                        null,
                        values);
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        mDbHelper.close();
        super.onPause();
    }

}
