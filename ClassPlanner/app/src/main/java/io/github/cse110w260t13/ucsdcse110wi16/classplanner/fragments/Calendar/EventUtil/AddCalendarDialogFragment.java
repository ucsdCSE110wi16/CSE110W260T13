package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Calendar.EventUtil;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.DateTimeUtil;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.calendar_database.CalendarContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.calendar_database.CalendarInfo;

/**
 * Created by nick on 2/10/16.
 */
public class AddCalendarDialogFragment extends android.support.v4.app.DialogFragment {

    private LinearLayout calendarDialogLayout;
    private CheckBox repeatCheckBox;
    private View noRepeatView;
    private View repeatView;
    private boolean checked = false;


    @SuppressWarnings( "deprecation" )
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                R.style.AlertDialogCustom);

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_calendar_item, null);

        // Get the linear layout
        calendarDialogLayout = (LinearLayout) view.findViewById(R.id.calendar_dialog_linear_layout);
        // Get the checkbox
        //repeatCheckBox = (CheckBox) view.findViewById(R.id.calendar_repeat_checkbox);

        // get the repeating layout view
        LayoutInflater layoutInflater = (LayoutInflater)
                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        repeatView = layoutInflater.inflate(
                R.layout.dialog_add_calendar_item_repeat,
                (ViewGroup) calendarDialogLayout.getParent(),
                false);
        noRepeatView = layoutInflater.inflate(
                R.layout.dialog_add_calendar_item_norepeat,
                (ViewGroup) calendarDialogLayout.getParent(),
                false);

        // Default is for norepeatview to be visible...
        calendarDialogLayout.addView(noRepeatView);

        final DatePicker noRepeatStartDate = (DatePicker) noRepeatView.findViewById(R.id.calendarDialogDatePicker);
        /*final DatePicker repeatStartDate = (DatePicker) repeatView.findViewById(R.id.calendarDialogStartDatePicker);
        final DatePicker repeatEndDate = (DatePicker) repeatView.findViewById(R.id.calendarDialogEndDatePicker);
        Bundle args = getArguments();
        final String dateSelected = args.getString("day");
        noRepeatStartDate.updateDate(
                DateTimeUtil.getDate(dateSelected, DateTimeUtil.YEAR),
                DateTimeUtil.getDate(dateSelected, DateTimeUtil.MONTH),
                DateTimeUtil.getDate(dateSelected, DateTimeUtil.DAY));
        // Set the checkbox's on change listener
        repeatCheckBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        // show or hide the included view and the date
                        if (isChecked) {
                            checked = true;
                            calendarDialogLayout.addView(repeatView);
                            repeatStartDate.updateDate(
                                    DateTimeUtil.getDate(dateSelected, DateTimeUtil.YEAR),
                                    DateTimeUtil.getDate(dateSelected, DateTimeUtil.MONTH),
                                    DateTimeUtil.getDate(dateSelected, DateTimeUtil.DAY));
                            ((ViewGroup) repeatView.getParent()).removeView(noRepeatView);
                        } else {
                            checked = false;
                            calendarDialogLayout.addView(noRepeatView);
                            noRepeatStartDate.updateDate(
                                    DateTimeUtil.getDate(dateSelected, DateTimeUtil.YEAR),
                                    DateTimeUtil.getDate(dateSelected, DateTimeUtil.MONTH),
                                    DateTimeUtil.getDate(dateSelected, DateTimeUtil.DAY));
                            ((ViewGroup) repeatView.getParent()).removeView(repeatView);
                        }
                    }
                }
        );*/

        final EditText eventTitle = (EditText) view.findViewById(R.id.calendar_event_title);
        final EditText eventDescr = (EditText) view.findViewById(R.id.calendar_event_description);
        final TimePicker startPicker = (TimePicker) view.findViewById(R.id.calendar_start_picker);
        final TimePicker endPicker = (TimePicker) view.findViewById(R.id.calendar_end_picker);


        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        ContentResolver cr = getActivity().getContentResolver();
                        ContentValues values = new ContentValues();

                        /*if (!checked){*/
                            String oneDayString = DateTimeUtil.getDateString(noRepeatStartDate.getYear(),
                                    noRepeatStartDate.getMonth(),
                                    noRepeatStartDate.getDayOfMonth());
                            values.put(CalendarInfo.FeedEntry.DATE, oneDayString);
                            String startString = DateTimeUtil.getTimeString(startPicker.getCurrentHour(),
                                    startPicker.getCurrentMinute());
                            String endString = DateTimeUtil.getTimeString(endPicker.getCurrentHour(),
                                    endPicker.getCurrentMinute());
                            String title = eventTitle.getText().toString();
                            String descr = eventDescr.getText().toString();

                            values.put(CalendarInfo.FeedEntry.START_TIME,startString);
                            values.put(CalendarInfo.FeedEntry.END_TIME, endString);
                            values.put(CalendarInfo.FeedEntry.EVENT_TITLE, title);
                            values.put(CalendarInfo.FeedEntry.EVENT_DESCR, descr);
                            values.put(CalendarInfo.FeedEntry.EVENT_TYPE, "event");

                            cr.insert(CalendarContentProvider.CONTENT_URI, values);
                        //}
                        /*else{
                            LocalDate startdate = new LocalDate(DateTimeUtil.getDateString(
                                    repeatStartDate.getYear(),
                                    repeatStartDate.getMonth(),
                                    repeatStartDate.getDayOfMonth()));
                            LocalDate enddate = new LocalDate(DateTimeUtil.getDateString(
                                    noRepeatStartDate.getYear(),
                                    noRepeatStartDate.getMonth(),
                                    noRepeatStartDate.getDayOfMonth()));
                            while (startdate.isBefore(enddate)) {
                                values.put(CalendarInfo.FeedEntry.DATE,
                                        repeatStartDate.toString());
                                values.put(CalendarInfo.FeedEntry.START_TIME,
                                        DateTimeUtil.getTimeString(startPicker.getCurrentHour(),
                                                startPicker.getCurrentMinute()));
                                values.put(CalendarInfo.FeedEntry.END_TIME,
                                        DateTimeUtil.getTimeString(endPicker.getCurrentHour(),
                                                endPicker.getCurrentMinute()));
                                values.put(CalendarInfo.FeedEntry.EVENT_TITLE,
                                        eventTitle.getText().toString());
                                values.put(CalendarInfo.FeedEntry.EVENT_DESCR,
                                        eventDescr.getText().toString());
                                values.put(CalendarInfo.FeedEntry.EVENT_TYPE,
                                        "event");
                                cr.insert(CalendarContentProvider.CONTENT_URI, values);
                                values.clear();
                                startdate = startdate.plusWeeks(1);
                            }
                        }*/
                        getTargetFragment().onActivityResult(getTargetRequestCode(),
                                Activity.RESULT_OK, getActivity().getIntent());

                        dismiss();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddCalendarDialogFragment.this.getDialog().cancel();
                    }
                })
                .setTitle("Add event");

        Dialog dialog = builder.create();

        return dialog;
    }

}
