package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Calendar.EventUtil;


import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.calendar_database.CalendarContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.calendar_database.CalendarInfo;

/**
 * CalendarRecyclerAdapter implements adapter functions for the recycler view for the calendar fragment.
 */
public class CalendarRecyclerAdapter
        extends RecyclerView.Adapter<CalendarRecyclerAdapter.ViewHolder>{

    public static final int UPDATE = 0;
    public static final String CLASS = "class";
    public static final String EVENT = "event";
    public static final String TODO = "todo";
    public static final String HOMEWORK  = "homework";

    private ArrayList<CalendarEvent> calendarEvents = null;
    private Context context;
    private ViewHolder holder;

    RecyclerAdapterCallback mCallback;

    /**
     * Container Activity must implement this interface
     */
    public interface RecyclerAdapterCallback {
        void onCreateEditDialog(String id);

        void onDeleteOK();
    }
    
    /**
     * Viewholder that extends a RecyclerView ViewHolder.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //member variables for the events
        TextView title;
        TextView description;
        TextView start;
        TextView end;

        TextView typeTitle;
        LinearLayout typeBG;

        ImageButton add;
        ImageButton del;

        String id;
        String type;
        
        /**
         * Constructor for the ViewHolder
         * @param v view that the ViewHolder parent class takes as an arg
         */
        public ViewHolder(View v){
            //calling the super constructor, then setting member vars
            super(v);
            title = (TextView) v.findViewById(R.id.event_title);
            description = (TextView) v.findViewById(R.id.description);
            start = (TextView) v.findViewById(R.id.start_time);
            end  = (TextView) v.findViewById(R.id.end_time);
            typeTitle = (TextView) v.findViewById(R.id.event_type);
            typeBG = (LinearLayout) v.findViewById(R.id.type_layout);
            add = (ImageButton) v.findViewById(R.id.add_button);
            del = (ImageButton) v.findViewById(R.id.delete_button);
        }
    }
    
    /**
     * CalendarRecyclerAdapter constructor
     * @param callback 
     * @param calendarEvents list of events to be shown
     * @param context
     */
    public CalendarRecyclerAdapter( RecyclerAdapterCallback callback,
                                    ArrayList<CalendarEvent> calendarEvents,
                                    Context context ){
        this.calendarEvents = calendarEvents;
        this.context=context;
        mCallback = callback;
    }

    /** 
     * Create new views (invoked by the layout manager)
     * @param parent to get layout from
     * @param viewType 
     * @return ViewHolder
     */
    @Override
    public CalendarRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.layout_event, parent, false);
        ViewHolder holder = new ViewHolder(v);
        clickHandler clickhandler = new clickHandler();
        holder.add.setOnClickListener(clickhandler);
        holder.del.setOnClickListener(clickhandler);

        holder.add.setTag(holder);
        holder.del.setTag(holder);
        return holder;
    }

    /** 
     * Replace the contents of a view (invoked by the layout manager)
     * @param holder ViewHolder to set events on
     * @param position of the event
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        this.holder = holder;
        if(calendarEvents != null) {
            CalendarEvent event = calendarEvents.get(position);
            setEventType(event.eventType);

            holder.title.setText(event.eventTitle);
            holder.description.setText(event.eventDescr);
            holder.start.setText(event.eventSTime);
            holder.end.setText(event.eventETime);
            holder.id = event.eventID;
            holder.type = event.eventType;
        }
    }
    
    /**
     * Click handler class to manage actions
     */
    private class clickHandler implements View.OnClickListener {
        private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
        
        /**
         * onClick handler
         * @param v view to apply input to
         */
        @Override
        public void onClick(View v) {
            //takes input
            ViewHolder holder = (ViewHolder) v.getTag();
            final int pos = holder.getAdapterPosition();
            CalendarEvent event = calendarEvents.get(pos);
            Log.d("onClick", "" + event.eventID);
            Log.d("onClick", "" +event.eventTitle);

            Bundle args = new Bundle();
            args.putInt("mode", UPDATE);
            args.putString("id", holder.id);
            final String hold_id = holder.id;
            final FragmentActivity activity = (FragmentActivity)context;
            
            //handles cases for add button and delete button
            switch (v.getId()) {
                case R.id.add_button:
                    v.startAnimation(buttonClick);
                    mCallback.onCreateEditDialog(hold_id);
                    break;
                case R.id.delete_button:
                    //asks for delete confirmation and deletes if ok
                    v.startAnimation(buttonClick);
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Are you sure you want to delete this?");
                    builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ContentResolver cr = activity.getContentResolver();
                            cr.delete(CalendarContentProvider.CONTENT_URI,
                                    CalendarInfo.FeedEntry._ID + "=?",
                                    new String[]{hold_id});

                            mCallback.onDeleteOK();
                            calendarEvents.remove(pos);
                            notifyItemRemoved(pos);
                        }
                    });
                    //handles cancel button for delete
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    break;
            }
        }
    }
    /** 
     * Return the size of your dataset (invoked by the layout manager)
     * @return int size of data set
     */
    @Override
    public int getItemCount() {
        if(calendarEvents!=null) {
            return calendarEvents.size();
        }
        return 0;
    }
    
    /**
     * For changing events in the calendar
     * @param events to be added
     */
    public void swap(ArrayList<CalendarEvent> events) {
        if(calendarEvents!=null) {
            calendarEvents.clear();
            calendarEvents.addAll(events);
        }
        else{
            calendarEvents = new ArrayList<CalendarEvent>();
            calendarEvents.addAll(events);
        }
        this.notifyDataSetChanged();
    }
    
    /**
     * For setting the type of event
     * @param type of event
     */
    private void setEventType(String type){
        //handles cases for if type is CLASS, EVENT, or HOMEWORK
        switch(type){
            case CLASS:
                holder.typeTitle.setText("Class");
                holder.typeBG.setBackgroundColor(ContextCompat
                        .getColor(context, R.color.calendar_23));
                break;
            case EVENT:
                holder.typeTitle.setText("Event");
                holder.typeBG.setBackgroundColor(ContextCompat
                        .getColor(context, R.color.colorMidBlue));
                break;
            case HOMEWORK:
                holder.typeTitle.setText("Hwk");
                holder.typeBG.setBackgroundColor(ContextCompat
                        .getColor(context, R.color.colorDarkMidBlue));
                break;

        }
    }
}
