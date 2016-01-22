package io.github.cse110w260t13.ucsdcse110wi16.classplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String menuSpinnerOptionSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                Snackbar.make(view, "Open activity associated with menu item", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                        */

                // Open the relevant activity
                if(menuSpinnerOptionSelected.equals(getResources().getString(R.string.menu_spinner_calendar))) {

                    // Inflate calendar activity
                    Intent calendarActivityIntent = new Intent(MainActivity.this, CalendarActivity.class);
                    //calendarActivityIntent.putExtra("key", value); //Optional parameters
                    MainActivity.this.startActivity(calendarActivityIntent);

                } else if(menuSpinnerOptionSelected.equals(getResources().getString(R.string.menu_spinner_courses))) {

                    // Inflate courses activity
                    Intent coursesActivityIntent = new Intent(MainActivity.this, CoursesActivity.class);
                    //coursesActivityIntent.putExtra("key", value); //Optional parameters
                    MainActivity.this.startActivity(coursesActivityIntent);

                }

            }
        });

        // Populate the menuSpinner with options
        Spinner menuSpinner = (Spinner) findViewById(R.id.menuSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.menu_spinner, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        menuSpinner.setAdapter(adapter);
        // Set the listener to be the activity
        menuSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        menuSpinnerOptionSelected = parent.getItemAtPosition(pos).toString();

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
