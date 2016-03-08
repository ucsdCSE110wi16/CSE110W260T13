package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.ToDoList;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.support.design.widget.FloatingActionButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.ToDo_database.ToDoTaskContract;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.ToDo_database.ToDoTaskDbHelper;


public class ToDoFragment extends Fragment {

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
                null, null, null, null, null);

        listAdapter = new CustomToDoListAdapter(
                getContext(),
                R.layout.todo_item,
                cursor,
                new String[]{ToDoTaskContract.Columns.TASK},
                new int[]{R.id.taskTextView}
        );

        toDoList.setAdapter(listAdapter);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_to_do, container, false);

        //Set up Home Frag Toolbar
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        if (toolbar != null){
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toDoList = (ListView) rootView.findViewById(R.id.toDolist);
        updateUI();

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.AddToDoItem);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add a task");
                //builder.setMessage("?");
                final EditText inputField = new EditText(getContext());
                builder.setView(inputField);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = inputField.getText().toString();

                        if(task.contains("'")){
                            task = task.replaceAll("'","''");
                        }


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

    /*===================Need to change the Adapter to delete======================================*/

    public class CustomToDoListAdapter extends SimpleCursorAdapter {
        private Context myContext;
        private int layout;
        private ToDoTaskDbHelper helper;
        private Button deleteButton;
        private Button doneButton;
        private boolean toggleState;

        public CustomToDoListAdapter(Context context, int layout, Cursor c,String[] from, int[] to){
            super(context,layout,c,from,to);
            this.myContext=context;
            this.layout=layout;
            toggleState = false;
        }

        @Override
        public void bindView(View v, Context context, Cursor c) {
            super.bindView(v, context, c);

            deleteButton = (Button) v.findViewById(R.id.deleteButton);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    View v = (View) view.getParent();
                    TextView taskTextView = (TextView) v.findViewById(R.id.taskTextView);
                    String task = taskTextView.getText().toString();

                    if(task.contains("'")){
                        task = task.replaceAll("'","''");
                    }

                    String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                            ToDoTaskContract.TABLE,
                            ToDoTaskContract.Columns.TASK,
                            task);


                    helper = new ToDoTaskDbHelper(myContext);
                    SQLiteDatabase sqlDB = helper.getWritableDatabase();
                    sqlDB.execSQL(sql);
                    updateUI();
                }

            });

            doneButton = (Button) v.findViewById(R.id.doneButton);
            doneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    View v = (View) view.getParent();
                    TextView tv = (TextView) v.findViewById(R.id.taskTextView);
                    //String task = taskTextView.getText().toString();
                    if(toggleState==false) {
                        tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        toggleState=true;
                    }else {
                        tv.setPaintFlags(0);
                        toggleState = false;
                    }
                }
            });

        }
    }


}



