package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Home.HomeUtil;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

/**
 * Created by Bryan Yang on 2/10/2016.
 */
public class CourseListCursorAdapter extends CursorAdapter {
    public CourseListCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        //Not Done
        return LayoutInflater.from(context).inflate(null,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }
}
