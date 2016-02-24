package io.github.cse110w260t13.ucsdcse110wi16.classplanner.not_used;
/**
 * Currently not being used.
 * ISSUE: Inability to pass results to properly update colors and adapter list.
 * Currently using a work-around.
 */

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.calendar_database.CalendarContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.calendar_database.CalendarInfo;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.course_database.CourseCalendarContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.course_database.CourseCalendarInfo;

public class DeleteDialogFragment extends android.support.v4.app.DialogFragment{
    private String hold_id;
    DeleteDialogCallback mCallback;

    public DeleteDialogFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        hold_id = bundle.getString("id");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to delete this?");

        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ContentResolver cr = getActivity().getContentResolver();
                cr.delete(CalendarContentProvider.CONTENT_URI,
                        CalendarInfo.FeedEntry._ID + "=?",
                        new String[]{hold_id});
                mCallback.finish(true);
                dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mCallback.finish(false);
                dismiss();
            }
        });

        return builder.create();
    }

    public interface DeleteDialogCallback{
        void finish(boolean result);
    }
}
