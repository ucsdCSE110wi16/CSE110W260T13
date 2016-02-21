package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Home;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.view.View.OnClickListener;


import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

public class HomeFragment extends Fragment {

    public HomeFragment(){}
        Button b1, b2;
       // EditText t1;
        ListView listView1;
        SQLiteDatabase db;
        Cursor c;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
/*
        b1 = (Button)findViewById(R.id.button1);
        b2 = (Button)findViewById(R.id.button2);
        //t1 = (EditText)findViewById(R.id.editText1);
        listView1 = (ListView)findViewById(R.id.listView1);

        db = openOrCreateDatabase("MyDB", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Names(Name VARCHAR)");
        //Need an onclick listener to get dates
        b1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                if (t1.getText().toString().equals("")) {
                    t1.setText("Enter Name");
                } else {
                    String input = t1.getText().toString();
                    db.execSQL("INSERT INTO Names VALUES(" + "'" + input + "'"
                            + ")");
                }

            }
        });

        //this draws the data from the query and display with list view
        b2.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                c = db.rawQuery("SELECT * FROM Names",null);
                int count = c.getCount();
                String values[] = new String[count+1];
                int i = 0;

                while(c.moveToNext())

                {
                    values[i]= c.getString(c.getColumnIndex("Name"));
                    i++;
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),
                        android.R.layout.activity_list_item, android.R.id.text1,values);

                listview1.setAdapter(adapter);
            }
        });
*/
        return rootView;

    }
}
