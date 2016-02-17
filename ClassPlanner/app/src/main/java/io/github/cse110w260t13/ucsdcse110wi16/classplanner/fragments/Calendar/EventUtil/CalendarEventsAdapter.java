package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Calendar.EventUtil;
/**
 * NO LONGER USING THIS ADAPTER.
 * Switched to using CalendarRecyclerAdapter for the purpose of displaying
 * ArrayList events to the user. This class will be left in the files in the
 * case that it is necessary to swap back to ListView in the future.
 */

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;

public class CalendarEventsAdapter extends ArrayAdapter<CalendarEvent> {
    private final Context context;
    private final ArrayList<CalendarEvent> calendarEvents;

    private static class ViewHolder {
        /*String id;
        * String type;
        * TextView typeBG; */
        TextView title;
        TextView description;
        TextView start;
        TextView end;
    }

    public CalendarEventsAdapter(Context context, ArrayList<CalendarEvent> calendarEvents) {
        super(context, 0, calendarEvents);
        this.context = context;
        this.calendarEvents = calendarEvents;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CalendarEvent event = getItem(position);

        ViewHolder viewHolder;
        //Is an existing view being reused? If not, inflate a new view.
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.layout_event, parent, false);

            viewHolder.title = (TextView) convertView.findViewById(R.id.event_title);
            viewHolder.description = (TextView) convertView.findViewById(R.id.description);
            viewHolder.start = (TextView) convertView.findViewById(R.id.start_time);
            viewHolder.end = (TextView) convertView.findViewById(R.id.end_time);
            /* viewHolder.typeBG = (TextView) convertView.findViewById(R.id.event_type);*/
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        /* This is how I will switch the background between different textviews -----------------
        viewHolder.id = event.eventID;      //needed to update/delete an event.
        viewHolder.type = event.eventType;  //needed for BG color & text
        switch(viewHolder.type) {
            case CLASS:
                typeBG.setText(CLASS);
                typeBG.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.classEvent));
                break;
            case EVENT:
                typeBG.setText(EVENT);
                typeBG.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.regEvent));
                break;
            case HWK:
                typeBG.setText(HWK);
                typeBG.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.hwkEvent));
                break;
            case TODO:
                typeBG.setText(TODO);
                typeBG.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.todoEvent));
                break;
        }*/

        viewHolder.title.setText(event.eventTitle);
        viewHolder.title.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.black));
        viewHolder.description.setText(event.eventDescr);
        viewHolder.start.setText(event.eventSTime);
        viewHolder.end.setText(event.eventETime);

        Button deleteButton = (Button) convertView.findViewById(R.id.delete_button);
        Button addButton = (Button) convertView.findViewById(R.id.add_button);
        clickHandler clickhandler = new clickHandler();
        deleteButton.setOnClickListener(clickhandler);
        addButton.setOnClickListener(clickhandler);

        return convertView;
    }

    private class clickHandler implements View.OnClickListener {
        private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add_button:
                    v.startAnimation(buttonClick);
                    Log.i("addClick", "add button pressed");
                    break;
                case R.id.delete_button:
                    v.startAnimation(buttonClick);
                    Log.i("delClick", "del button pressed");
                    break;
            }
        }
    }
}
