package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Calendar.EventUtil;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import org.joda.time.LocalDate;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.calendar_database.CalendarContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.calendar_database.CalendarInfo;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.course_database.CourseCalendarContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.course_database.CourseCalendarInfo;

public class EditDialogFragment extends android.support.v4.app.DialogFragment {
    public static final int HOUR = 0;
    public static final int MINUTE = 1;

    public static final int YEAR = 0;
    public static final int MONTH = 1;
    public static final int DAY = 2;

    @SuppressWarnings( "deprecation" )
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_calendar_item, null);

        final EditText eventTitle = (EditText) view.findViewById(R.id.calendar_event_title);
        final EditText eventDescr = (EditText) view.findViewById(R.id.calendar_event_description);
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.calendar_date_picker);
        final TimePicker startPicker = (TimePicker) view.findViewById(R.id.calendar_start_picker);
        final TimePicker endPicker = (TimePicker) view.findViewById(R.id.calendar_end_picker);

        Bundle args = getArguments();
        final String hold_id = args.getString("id");
        ContentResolver cr = getActivity().getContentResolver();

        builder.setView(view)
                .setPositiveButton("submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String dateString = getDateString(datePicker.getYear(),
                                datePicker.getMonth(),
                                datePicker.getDayOfMonth());
                        String startString = getTimeString(startPicker.getCurrentHour(),
                                startPicker.getCurrentMinute());
                        String endString = getTimeString(endPicker.getCurrentHour(),
                                endPicker.getCurrentMinute());
                        Log.i("onUpdate"," id is " + hold_id);
                        Log.i("onUpdate", " date is " + dateString);
                        Log.i("onUpdate", " start time is " + startString);
                        Log.i("onUpdate", " end time is " + endString);

                        ContentResolver cr = getActivity().getContentResolver();
                        ContentValues values = new ContentValues();
                        values.put(CalendarInfo.FeedEntry.EVENT_TITLE,
                                eventTitle.getText().toString());
                        values.put(CalendarInfo.FeedEntry.EVENT_DESCR,
                                eventDescr.getText().toString());
                        values.put(CalendarInfo.FeedEntry.DATE,
                                dateString
                                );
                        values.put(CalendarInfo.FeedEntry.START_TIME,
                                startString
                                );
                        values.put(CalendarInfo.FeedEntry.END_TIME,
                                endString
                                );
                        cr.update(CalendarContentProvider.CONTENT_URI,
                                values,
                                CalendarInfo.FeedEntry._ID + "=?",
                                new String[]{hold_id});

                        getTargetFragment().onActivityResult(getTargetRequestCode(),
                                Activity.RESULT_OK, getActivity().getIntent());

                        dismiss();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditDialogFragment.this.getDialog().cancel();
                    }
                })
                .setTitle("Add event");
        Dialog dialog = builder.create();

        Cursor cursor = cr.query(CalendarContentProvider.CONTENT_URI,
                CalendarInfo.FeedEntry.ALL_COLUMNS,
                CalendarInfo.FeedEntry._ID + "=?",
                new String[]{hold_id}, null);

        if(cursor!=null) {
            cursor.moveToFirst();

            String title = cursor.getString(cursor.getColumnIndex(CalendarInfo.FeedEntry.EVENT_TITLE));
            String descr = cursor.getString(cursor.getColumnIndex(CalendarInfo.FeedEntry.EVENT_DESCR));
            String date = cursor.getString(cursor.getColumnIndex(CalendarInfo.FeedEntry.DATE));
            String start = cursor.getString(cursor.getColumnIndex(CalendarInfo.FeedEntry.START_TIME));
            String end = cursor.getString(cursor.getColumnIndex(CalendarInfo.FeedEntry.END_TIME));

            datePicker.updateDate(
                    getFromCalendar(date, YEAR),
                    getFromCalendar(date, MONTH),
                    getFromCalendar(date, DAY));
            startPicker.setCurrentHour(getTime(start, HOUR));
            startPicker.setCurrentMinute(getTime(start, MINUTE));
            endPicker.setCurrentHour(getTime(end, HOUR));
            endPicker.setCurrentMinute(getTime(end, MINUTE));

            eventTitle.setText(title);
            eventDescr.setText(descr);

            cursor.close();
        }

        return dialog;
    }

    private int getFromCalendar(String strDate,int field)
    {
        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Date convertedDate = new Date();
        try {
            convertedDate = dayFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(convertedDate);

        switch(field){
            case YEAR:
                return cal.get(Calendar.YEAR);
            case MONTH:
                return cal.get(Calendar.MONTH);
            case DAY:
                return cal.get(Calendar.DAY_OF_MONTH);
        }
        return -1;
    }

    private int getTime(String time, int field)
    {
        String[] splitTime = time.split(":");
        if (!splitTime[0].isEmpty() && !splitTime[1].isEmpty()){
            if (field == HOUR){
                return Integer.parseInt(splitTime[0]);
            }
            else if (field == MINUTE) {
                return Integer.parseInt(splitTime[1]);
            }
        }
        return -1;
    }

    private String getDateString(int year, int month, int day){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, day);
        Date date = cal.getTime();

        return sdf.format(date);
    }

    private String getTimeString(int hour, int minutes){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minutes);
        Date date = cal.getTime();

        return sdf.format(date);
    }

}
