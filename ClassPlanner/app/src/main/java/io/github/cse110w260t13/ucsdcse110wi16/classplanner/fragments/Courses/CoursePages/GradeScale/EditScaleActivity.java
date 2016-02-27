package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Courses.CoursePages.GradeScale;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.course_database.CourseCalendarContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.course_database.CourseCalendarInfo;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.course_database.GradeScaleContentProvider;

public class EditScaleActivity extends AppCompatActivity {

    private ArrayList<Category> mList = new ArrayList<>();
    private ArrayList<Category> original = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ScaleRecyclerAdapter adapter;
    private String currName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editscale);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        Button add = (Button) findViewById(R.id.add);
        Button cancel = (Button) findViewById(R.id.cancel);

        ClickHandler click_handler = new ClickHandler();

        add.setOnClickListener(click_handler);
        cancel.setOnClickListener(click_handler);

        Intent intent = getIntent();
        currName = intent.getStringExtra("class");
        Log.i("onCreate ", "currName is " + currName);
        displayInfo(currName);
    }

    /**--------------------------------------------------------------------------------------------
     * ClickHandler class which handles all clicks for the views found within the fragment.
     *-------------------------------------------------------------------------------------------*/
    private class ClickHandler implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add:
                    update(original);
                    break;
                case R.id.cancel:
                    finish();
                    break;
            }
        }
    }

    public void update( final ArrayList<Category> original){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure?");
        builder.setMessage("This will permanently change the categories for your" +
                "Assignments according to the order on this page.");
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                int index = 0;
                for (index = 0; index < original.size(); index++) {
                    if (index >= mList.size()) {
                        delete(original.get(index));
                        continue;
                    }
                    if (!original.get(index).equals(mList.get(index)))
                        update(original.get(index), mList.get(index));
                }
                if ((mList.size() - original.size()) > 0) {
                    for (int i = index; i < mList.size(); i++) {
                        add(mList.get(i));
                    }
                }
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void add(Category item){
        ContentValues values = new ContentValues();
        values.put(CourseCalendarInfo.GradeScale.COURSE_NAME,
                item.courseName);
        values.put(CourseCalendarInfo.GradeScale.CATEGORY,
                item.category);
        values.put(CourseCalendarInfo.GradeScale.WEIGHT,
                item.weight);
        ContentResolver cr = this.getContentResolver();
        cr.insert(GradeScaleContentProvider.CONTENT_URI,
                values);
    }

    private void delete(Category item){
        ContentResolver cr = this.getContentResolver();
        cr.delete(GradeScaleContentProvider.CONTENT_URI,
                CourseCalendarInfo.GradeScale._ID + "=?",
                new String[]{item.catID});
    }

    private void update(Category oldItem, Category newItem){
        ContentValues values = new ContentValues();
        values.put(CourseCalendarInfo.GradeScale.CATEGORY,
                newItem.category);
        values.put(CourseCalendarInfo.GradeScale.WEIGHT,
                newItem.weight);
        Log.i("updating ", ""+newItem.weight);
        ContentResolver cr = this.getContentResolver();
        cr.update(GradeScaleContentProvider.CONTENT_URI,
                values,
                CourseCalendarInfo.GradeScale._ID + "=?",
                new String[]{oldItem.catID});
        //update assignments with the same gradescale ID
    }


    private void displayInfo(String name) {
        Log.i("displayInfo ", "entered with name = " + name);
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(
                GradeScaleContentProvider.CONTENT_URI,
                CourseCalendarInfo.GradeScale.ALL_COLUMNS,
                CourseCalendarInfo.GradeScale.COURSE_NAME + "=?",
                new String[]{name +""}, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Log.i("displayInfo ", "Category " +
                        cursor.getString(cursor.getColumnIndex(CourseCalendarInfo.GradeScale._ID)) + " " +
                        cursor.getString(cursor.getColumnIndex(CourseCalendarInfo.GradeScale.COURSE_NAME)) + " " +
                        cursor.getString(cursor.getColumnIndex(CourseCalendarInfo.GradeScale.CATEGORY)) + " " +
                        cursor.getString(cursor.getColumnIndex(CourseCalendarInfo.GradeScale.WEIGHT)));
                mList.add(new Category(
                        cursor.getString(cursor.getColumnIndex(CourseCalendarInfo.GradeScale._ID)),
                        cursor.getString(cursor.getColumnIndex(CourseCalendarInfo.GradeScale.COURSE_NAME)),
                        cursor.getString(cursor.getColumnIndex(CourseCalendarInfo.GradeScale.CATEGORY)),
                        cursor.getInt(cursor.getColumnIndex(CourseCalendarInfo.GradeScale.WEIGHT))
                ));
                original.add(new Category(
                        cursor.getString(cursor.getColumnIndex(CourseCalendarInfo.GradeScale._ID)),
                        cursor.getString(cursor.getColumnIndex(CourseCalendarInfo.GradeScale.COURSE_NAME)),
                        cursor.getString(cursor.getColumnIndex(CourseCalendarInfo.GradeScale.CATEGORY)),
                        cursor.getInt(cursor.getColumnIndex(CourseCalendarInfo.GradeScale.WEIGHT))
                ));
                cursor.moveToNext();
            }
            cursor.close();
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ScaleRecyclerAdapter(mList, this);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
