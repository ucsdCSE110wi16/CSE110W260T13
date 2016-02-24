package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Home.HomeUtil;

/**
 * Created by Bryan Yang on 2/24/2016.
 */
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
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Calendar.EventUtil.CalendarEvent;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.calendar_database.CalendarContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.calendar_database.CalendarInfo;

public class HomeRecyclerAdapter
        extends RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder>{

    public static final int UPDATE = 0;
    public static final String EVENT = "event";


    private ArrayList<HomeEvent> HomeEvents = null;
    private Context context;
    private ViewHolder holder;

    RecyclerAdapterCallback raCallback;

    // Container Activity must implement this interface
    public interface RecyclerAdapterCallback {
        void onCreateEditDialog(String id);
        void onDeleteOK();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
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
        public ViewHolder(View v){
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

    public HomeRecyclerAdapter( RecyclerAdapterCallback callback,
                                    ArrayList<HomeEvent> HomeEvents,
                                    Context context ){
        this.HomeEvents = HomeEvents;
        this.context=context;
        raCallback = callback;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HomeRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
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

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        this.holder = holder;
        if(HomeEvents != null) {
            HomeEvent event = HomeEvents.get(position);
            setEventType(event.eventType);

            holder.title.setText(event.eventTitle);
            holder.description.setText(event.eventDescr);
            holder.start.setText(event.eventSTime);
            holder.end.setText(event.eventETime);
            holder.id = event.eventID;
            holder.type = event.eventType;
        }
    }

    private class clickHandler implements View.OnClickListener {
        private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
        @Override
        public void onClick(View v) {
            ViewHolder holder = (ViewHolder) v.getTag();
            final int pos = holder.getAdapterPosition();
            HomeEvent event = HomeEvents.get(pos);
            Log.d("onClick", "" + event.eventID);
            Log.d("onClick", "" +event.eventTitle);

            Bundle args = new Bundle();
            args.putInt("mode", UPDATE);
            args.putString("id", holder.id);
            final String hold_id = holder.id;
            final FragmentActivity activity = (FragmentActivity)context;

        }
    }

    // Return the size of dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(HomeEvents!=null) {
            return HomeEvents.size();
        }
        return 0;
    }

    public void swap(ArrayList<HomeEvent> events) {
        if(HomeEvents!=null) {
            HomeEvents.clear();
            HomeEvents.addAll(events);
        }
        else{
            HomeEvents = new ArrayList<HomeEvent>();
            HomeEvents.addAll(events);
        }
        this.notifyDataSetChanged();
    }

    private void setEventType(String type){
        switch(type){
            case EVENT:
                holder.typeTitle.setText("Event");
                holder.typeBG.setBackgroundColor(ContextCompat
                        .getColor(context, R.color.colorMidBlue));
                break;
        }
    }
}
