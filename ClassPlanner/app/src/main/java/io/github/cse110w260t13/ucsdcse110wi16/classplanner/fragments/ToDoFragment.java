package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.ToDoTaskContract;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.ToDoTaskDbHelper;


public class ToDoFragment extends Fragment{

    private ListView toDoList;
    private ListAdapter listAdapter;
    private ToDoTaskDbHelper helper;

    public ToDoFragment() {
        // Required empty public constructor
    }

    private void updateUI(){
        helper = new ToDoTaskDbHelper(getContext());
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor cursor = sqlDB.query(ToDoTaskContract.TABLE,
                new String[]{ToDoTaskContract.Columns._ID, ToDoTaskContract.Columns.TASK},
                null,null,null,null,null);

        listAdapter = new SimpleCursorAdapter(
                getContext(),
                R.layout.todo_item,
                cursor,
                new String[]{ToDoTaskContract.Columns.TASK},
                new int[]{R.id.taskTextView},
                0);

        toDoList.setAdapter(listAdapter);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_to_do, container, false);
        toDoList = (ListView) rootView.findViewById(R.id.toDolist);
        updateUI();

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.AddToDoItem);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add a task");
                builder.setMessage("What do you wanna do?");
                final EditText inputField = new EditText(getContext());
                builder.setView(inputField);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = inputField.getText().toString();

                        helper = new ToDoTaskDbHelper(getContext());
                        SQLiteDatabase db = helper.getWritableDatabase();
                        ContentValues values = new ContentValues();

                        values.clear();
                        values.put(ToDoTaskContract.Columns.TASK, task);

                        db.insertWithOnConflict(ToDoTaskContract.TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
                        updateUI();
                    }

                });
                builder.setNegativeButton("Cancel", null);
                builder.create().show();

            }

        });





        return rootView;
    }


}
