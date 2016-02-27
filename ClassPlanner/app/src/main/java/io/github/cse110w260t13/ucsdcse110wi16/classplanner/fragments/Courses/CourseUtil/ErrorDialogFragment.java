package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Courses.CourseUtil;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

public class ErrorDialogFragment extends android.support.v4.app.DialogFragment{
    private String errorMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        errorMessage = getArguments().getString("error");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(errorMessage);

        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        builder.setCancelable(false);
        return builder.create();
    }
}
