package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;

/**
 * Created by nick on 2/10/16.
 */
public class AddCalendarDialogFragment extends android.support.v4.app.DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_add_calendar_item, null))
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
                .setTitle("Title");
        return builder.create();
    }

}
