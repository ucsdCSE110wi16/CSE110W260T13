package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Calendar.EventUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;

public class CalendarEventsAdapter extends ArrayAdapter<CalendarEvent> {
    private final Context context;
    private final ArrayList<CalendarEvent> calendarEvents;

    private static class ViewHolder {
        TextView title;
        TextView description;
        TextView start;
        TextView end;
    }

    public CalendarEventsAdapter(Context context, ArrayList<CalendarEvent> calendarEvents){
        super(context, 0, calendarEvents);
        this.context = context;
        this.calendarEvents = calendarEvents;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        CalendarEvent event = getItem(position);

        ViewHolder viewHolder;
        //Is an existing view being reused? If not, inflate a new view.
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.layout_event, parent, false);

            viewHolder.title = (TextView) convertView.findViewById(R.id.event_title);
            viewHolder.description = (TextView) convertView.findViewById(R.id.description);
            viewHolder.start = (TextView) convertView.findViewById(R.id.start_time);
            viewHolder.end  = (TextView) convertView.findViewById(R.id.end_time);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(event.eventTitle);
        viewHolder.description.setText(event.eventDescr);
        viewHolder.start.setText(event.eventSTime);
        viewHolder.end.setText(event.eventETime);

        return convertView;
    }
}
