package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import java.util.Date;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;

/**
 * Created by nick on 2/10/16.
 */
public class AddCalendarDialogFragment extends android.support.v4.app.DialogFragment {

    private LinearLayout calendarDialogLayout;
    private CheckBox repeatCheckBox;
    private View noRepeatView;
    private View repeatView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom);
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_add_calendar_item, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        /*Intent intent = getActivity().getIntent();
                        intent.putExtra("Date", date);
                        getActivity().setResult(Activity.RESULT_OK, intent);
                        getTargetFragment().onActivityResult(getTargetRequestCode(),
                                Activity.RESULT_OK, getActivity().getIntent());
                        dismiss();*/
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddCalendarDialogFragment.this.getDialog().cancel();
                    }
                })
                .setTitle("Add event");

        Dialog dialog = builder.create();

        // Get the linear layout
        calendarDialogLayout = (LinearLayout) view.findViewById(R.id.calendar_dialog_linear_layout);

        // Get the checkbox
        repeatCheckBox = (CheckBox) view.findViewById(R.id.calendar_repeat_checkbox);

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

        // Set the checkbox's on change listener
        repeatCheckBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        // show or hide the included view and the date
                        if (isChecked) {
                            calendarDialogLayout.addView(repeatView);
                            ((ViewGroup) repeatView.getParent()).removeView(noRepeatView);
                        } else {
                            calendarDialogLayout.addView(noRepeatView);
                            ((ViewGroup) repeatView.getParent()).removeView(repeatView);
                        }

                    }
                }
        );

        return dialog;
    }

}
