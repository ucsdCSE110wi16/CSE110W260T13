package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.CoursePages;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;

public class StartTimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    public StartTimePicker(){}

    @Override
    public Dialog onCreateDialog(Bundle savedInstancedState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(),
                android.R.style.Theme_Holo_Dialog, this,hour,minute,false);
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView startTimeTv = (TextView) getActivity().findViewById(R.id.start_time_picker);
        startTimeTv.setText(String.valueOf(hourOfDay)+":"+String.format("%02d", minute));
    }
}
