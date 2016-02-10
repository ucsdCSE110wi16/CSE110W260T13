package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Courses.CourseUtil;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Taiko on 2/9/2016.
 */
public class DateSelector extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    private TextView dateTv;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        int argument = bundle.getInt("endDate");
        dateTv = (TextView) getActivity().findViewById(argument);

        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(),android.R.style.Theme_Holo_Dialog,
                this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        String endDay = year + "-" + String.format("%02d", month)
                + "-" + String.format("%02d", day);
        dateTv.setText(endDay);
    }
}
