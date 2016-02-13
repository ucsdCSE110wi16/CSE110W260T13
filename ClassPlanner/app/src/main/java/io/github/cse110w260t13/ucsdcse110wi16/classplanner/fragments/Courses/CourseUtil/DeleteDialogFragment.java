package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Courses.CourseUtil;
/**
 * Currently not being used.
 */

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Courses.CoursesFragment;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.calendar_database.CalendarContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.calendar_database.CalendarInfo;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.course_database.CourseCalendarContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.course_database.CourseCalendarInfo;

public class DeleteDialogFragment extends android.support.v4.app.DialogFragment{
    private String currentClass;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentClass = getArguments().getString("currClass");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to delete this course?");

        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ContentResolver cr = getActivity().getContentResolver();
                cr.delete(CourseCalendarContentProvider.CONTENT_URI,
                        CourseCalendarInfo.FeedEntry.COLUMN_COURSE_NAME + "=?",
                        new String[]{currentClass});

                cr.delete(CalendarContentProvider.CONTENT_URI,
                        CalendarInfo.FeedEntry.EVENT_TITLE + "=?",
                        new String[]{currentClass});
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        return builder.create();
    }
}
