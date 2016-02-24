package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Home;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.view.View.OnClickListener;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Calendar.CalendarFragment;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Calendar.EventUtil.CalendarEvent;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Calendar.EventUtil.CalendarRecyclerAdapter;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Home.HomeUtil.HomeEvent;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Home.HomeUtil.HomeRecyclerAdapter;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.calendar_database.CalendarContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.calendar_database.CalendarInfo;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

public class HomeFragment extends Fragment implements HomeRecyclerAdapter.RecyclerAdapterCallback{

    private static final String LOG_TAG = "HomeFragment";
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private static final int REQUEST_CODE = 0;
    private RecyclerView list;
    private HomeRecyclerAdapter adapter;
    private Date daySelected;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // check Stuff to do, dependent on requestCode and resultCode
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Log.i("HomeResult", "Submit pressed on dialog");
            UpdateEventsTask eventUpdater = new UpdateEventsTask(
                    getActivity().getBaseContext(),
                    list,
                    getActivity().getContentResolver());
            eventUpdater.execute(daySelected, null, null);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        //Set up Home Frag Toolbar
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        if (toolbar != null){
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //Set up Home Frag Event Viewer (From database)
        list = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new HomeRecyclerAdapter(this, null, getActivity());
        list.setAdapter(adapter);

        Date today = new Date();
        daySelected = today;

        UpdateEventsTask eventUpdater = new UpdateEventsTask(
                getActivity().getBaseContext(),
                list,
                getActivity().getContentResolver());
        eventUpdater.execute(today, null, null);

        return rootView;
    }

    @Override
    public void onCreateEditDialog(String id) {

    }

    @Override
    public void onDeleteOK() {

    }

    /**-------------------------------------------------------------------------------------------
     * Private AsyncTask that updates and loads the events of each DAY onto the
     * HomeFragment recyclerView
     *-------------------------------------------------------------------------------------------*/

    private class UpdateEventsTask extends AsyncTask<Date, Void, ArrayList<HomeEvent>> {
        private SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        private ContentResolver cr;
        private Context context;
        private RecyclerView rvList;

        //Need context in order to create a new adapter. Pass it in from parent
        public UpdateEventsTask(Context context, RecyclerView rvList, ContentResolver cr) {
            super();
            this.cr = cr;
            this.context = context;
            this.rvList = rvList;
        }

        @Override
        protected ArrayList<HomeEvent> doInBackground(Date... date) {
            ArrayList<HomeEvent> returnList = new ArrayList<HomeEvent>();

            Calendar cal = Calendar.getInstance();
            cal.setTime(date[0]);
            Log.d("UpdateEventsTask: ", "doInBg  time" + date[0]);

            cal.add(Calendar.DATE, 1);

            String startDate = dayFormat.format(date[0]);
            String endDate = dayFormat.format(cal.getTime());
            Log.d("UpdateEventsTask: ", "doInBg  end date" + endDate);

            String daySelection = CalendarInfo.FeedEntry.DATE + " >= '" + startDate + "' AND "
                    + CalendarInfo.FeedEntry.DATE + " < '" + endDate + "'";

            //Query for all entries within date. Should be sorted by start time descending
            Cursor cursor = cr.query(CalendarContentProvider.CONTENT_URI,
                    CalendarInfo.FeedEntry.ALL_COLUMNS,
                    daySelection,
                    null,
                    CalendarInfo.FeedEntry.START_TIME + " ASC");

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    int eventTitle = cursor.getColumnIndex(CalendarInfo.FeedEntry.EVENT_TITLE);
                    int eventDesc = cursor.getColumnIndex(CalendarInfo.FeedEntry.EVENT_DESCR);
                    int startTime = cursor.getColumnIndex(CalendarInfo.FeedEntry.START_TIME);
                    int endTime = cursor.getColumnIndex(CalendarInfo.FeedEntry.END_TIME);
                    int eventID = cursor.getColumnIndex(CalendarInfo.FeedEntry._ID);
                    int eventType = cursor.getColumnIndex(CalendarInfo.FeedEntry.EVENT_TYPE);
                    Log.d("UpdateEventsTask: ", "doInBg " + cursor.getString(eventTitle));

                    //Add every event to the ArrayList
                    returnList.add(new HomeEvent(
                            cursor.getString(eventID),
                            cursor.getString(eventType),
                            cursor.getString(eventTitle),
                            cursor.getString(eventDesc),
                            cursor.getString(startTime),
                            cursor.getString(endTime)));
                    cursor.moveToNext();
                }
                cursor.close();
            }
            return returnList;
        }

        //@Override

        protected void onPostExecute(ArrayList<HomeEvent> HomeEventList) {
            Log.d("UpdateEventsTask: ", " onPostExecute");

            //Create a new adapter if there is no prior instance
            if(adapter == null){
                Log.d("UpdateEventsTask: ", " test context");
                adapter = new HomeRecyclerAdapter(HomeFragment.this, HomeEventList, context);
                list.setAdapter(adapter);
            }
            //otherwise clear the adapter and re-add new events
            else {
                adapter.swap(HomeEventList);
            }
            adapter.notifyDataSetChanged();
        }
    }

}
