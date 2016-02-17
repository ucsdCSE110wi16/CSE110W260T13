package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Courses.CourseUtil;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.widget.TextView;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;

public class TimeSelector extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
    private TextView timeTv;
    private static final int DEFAULT_HR = 12;
    private static final int DEFAULT_MIN = 0;


    public TimeSelector(){}

    @Override
    public Dialog onCreateDialog(Bundle savedInstancedState) {
        Bundle bundle = getArguments();
        int argument = bundle.getInt("time");
        timeTv = (TextView) getActivity().findViewById(argument);
        String timeText = timeTv.getText().toString();
        String[] timeSplit = timeText.split(":");

        int arg1, arg2;

        if(TextUtils.isEmpty(timeText)){
            arg1 = DEFAULT_HR;
            arg2 = DEFAULT_MIN;
        }
        else{
            arg1 = Integer.parseInt(timeSplit[0]);
            arg2 = Integer.parseInt(timeSplit[1]);
        }

        return new TimePickerDialog(getActivity(),
                R.style.MyPickerDialogTheme, this, arg1,
                arg2,false);
    }

    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        timeTv.setText(String.valueOf(hourOfDay)+":"+String.format("%02d", minute));
    }
}
