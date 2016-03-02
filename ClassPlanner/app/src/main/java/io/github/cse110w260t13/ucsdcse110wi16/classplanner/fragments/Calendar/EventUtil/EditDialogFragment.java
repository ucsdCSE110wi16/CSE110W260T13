package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Calendar.EventUtil;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.DateTimeUtil;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.calendar_database.CalendarContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.calendar_database.CalendarInfo;

public class EditDialogFragment extends android.support.v4.app.DialogFragment {

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
                        String dateString = DateTimeUtil.getDateString(datePicker.getYear(),
                                datePicker.getMonth(),
                                datePicker.getDayOfMonth());
                        String startString = DateTimeUtil.getTimeString(startPicker.getCurrentHour(),
                                startPicker.getCurrentMinute());
                        String endString = DateTimeUtil.getTimeString(endPicker.getCurrentHour(),
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
                    DateTimeUtil.getDateFromString(date, DateTimeUtil.YEAR),
                    DateTimeUtil.getDateFromString(date, DateTimeUtil.MONTH),
                    DateTimeUtil.getDateFromString(date, DateTimeUtil.DAY));
            startPicker.setCurrentHour(DateTimeUtil.getTime(start, DateTimeUtil.HOUR));
            startPicker.setCurrentMinute(DateTimeUtil.getTime(start, DateTimeUtil.MINUTE));
            endPicker.setCurrentHour(DateTimeUtil.getTime(end, DateTimeUtil.HOUR));
            endPicker.setCurrentMinute(DateTimeUtil.getTime(end, DateTimeUtil.MINUTE));

            eventTitle.setText(title);
            eventDescr.setText(descr);

            cursor.close();
        }

        return dialog;
    }

}
