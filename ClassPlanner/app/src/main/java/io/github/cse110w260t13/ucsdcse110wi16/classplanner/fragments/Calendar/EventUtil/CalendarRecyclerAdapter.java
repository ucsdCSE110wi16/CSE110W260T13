package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Calendar.EventUtil;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Calendar.AddCalendarDialogFragment;

public class CalendarRecyclerAdapter
        extends RecyclerView.Adapter<CalendarRecyclerAdapter.ViewHolder>{

    public static final int UPDATE = 0;

    private ArrayList<CalendarEvent> calendarEvents = null;
    private String id;
    private Context context;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        TextView start;
        TextView end;
        ImageButton add;
        ImageButton del;
        public ViewHolder(View v){
            super(v);
            title = (TextView) v.findViewById(R.id.event_title);
            description = (TextView) v.findViewById(R.id.description);
            start = (TextView) v.findViewById(R.id.start_time);
            end  = (TextView) v.findViewById(R.id.end_time);
            add = (ImageButton) v.findViewById(R.id.add_button);
            del = (ImageButton) v.findViewById(R.id.delete_button);
        }
    }

    public CalendarRecyclerAdapter( ArrayList<CalendarEvent> calendarEvents, Context context ){
        this.calendarEvents = calendarEvents;
        this.context = context;
    }
    public CalendarRecyclerAdapter(){}

    // Create new views (invoked by the layout manager)
    @Override
    public CalendarRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.layout_event, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(calendarEvents != null) {
            CalendarEvent event = calendarEvents.get(position);
            holder.title.setText(event.eventTitle);
            holder.description.setText(event.eventDescr);
            holder.start.setText(event.eventSTime);
            holder.end.setText(event.eventETime);

            clickHandler clickhandler = new clickHandler();
            holder.add.setOnClickListener(clickhandler);
            holder.del.setOnClickListener(clickhandler);

            id = event.eventID;
        }
    }

    private class clickHandler implements View.OnClickListener {
        private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add_button:
                    v.startAnimation(buttonClick);
                    /*
                    DialogFragment dialog = new AddCalendarDialogFragment();
                    Bundle args = new Bundle();
                    args.putInt("mode", UPDATE);
                    args.putString("id", id);
                    dialog.setArguments(args);
                    FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                    dialog.show(manager, "test");*/


                    break;
                case R.id.delete_button:
                    v.startAnimation(buttonClick);
                    break;
            }
        }
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(calendarEvents!=null) {
            return calendarEvents.size();
        }
        return 0;
    }

    public void swap(ArrayList<CalendarEvent> events) {
        if(calendarEvents!=null) {
            calendarEvents.clear();
            calendarEvents.addAll(events);
        }
        else{
            calendarEvents = events;
        }
        notifyDataSetChanged();
    }
}
