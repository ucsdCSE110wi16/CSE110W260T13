package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Courses.CoursePages.GradeScale;

import android.content.ClipData;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.calendar_database.CalendarContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.calendar_database.CalendarInfo;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.course_database.CourseCalendarContentProvider;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.local_database.course_database.CourseCalendarInfo;

public class ScaleRecyclerAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public final static int ITEM = 0;
    public final static int FOOTER = 1;


    private String genCourseName;
    private ArrayList<Category> scaleCategories = null;
    private Context context;
    private ItemViewHolder holder;
    private AppCompatActivity activity;



    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        Button button;

        public FooterViewHolder(View v){
            super(v);
            button = (Button) v.findViewById(R.id.add_category_button);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        EditText category;
        EditText weight;
        CategoryListener categoryListener;
        WeightListener weightListener;
        ImageButton del;
        String course;
        String categoryID;

        public ItemViewHolder(View v, CategoryListener cListener, WeightListener wListener){
            super(v);

            this.categoryListener = cListener;
            this.weightListener = wListener;

            category = (EditText) v.findViewById(R.id.edit_category);
            weight = (EditText) v.findViewById(R.id.edit_weight);
            del = (ImageButton) v.findViewById(R.id.delete_button);

            category.addTextChangedListener(categoryListener);
            weight.addTextChangedListener(weightListener);
        }
    }

    public ScaleRecyclerAdapter(ArrayList<Category> scaleCategories,
                                 Context context){
        Log.i("ScaleRecyclerAdapter ", "created");
        this.scaleCategories = scaleCategories;
        this.context=context;
        activity = (AppCompatActivity)context;

        if(scaleCategories!=null)
            genCourseName = scaleCategories.get(0).courseName;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(viewType == ITEM) {
            View v = inflater.inflate(R.layout.layout_gradescale_card, parent, false);
            ItemViewHolder holder = new ItemViewHolder(v, new CategoryListener(), new WeightListener());
            ClickHandler clickhandler = new ClickHandler();
            holder.del.setOnClickListener(clickhandler);
            holder.del.setTag(holder);
            return holder;
        }
        else if (viewType == FOOTER){
            View v = inflater.inflate(R.layout.layout_gradescale_button, parent, false);
            FooterViewHolder holder = new FooterViewHolder(v);
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
                    switch(v.getId()){
                        case R.id.add_category_button:
                            v.startAnimation(buttonClick);
                            scaleCategories.add(new Category(null, genCourseName, null, 0));
                            notifyItemInserted(scaleCategories.size() - 1);
                            break;
                    }
                }
            });
            return holder;
        }
        Log.i("ScaleRecyclerAdapter ", "created new views");

        return null;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemViewHolder) {
            this.holder = (ItemViewHolder) holder;
            Log.i("ScaleRecyclerAdapter ", "onBindView");
            if (scaleCategories != null) {
                Category item = scaleCategories.get(position);
                ((ItemViewHolder)holder).categoryListener.updatePosition(position);
                ((ItemViewHolder)holder).weightListener.updatePosition(position);
                ((ItemViewHolder)holder).category.setText(item.category);
                ((ItemViewHolder)holder).weight.setText(""+item.weight);
                ((ItemViewHolder)holder).course = item.courseName;
                ((ItemViewHolder)holder).categoryID = item.catID;
                Log.i("ScaleRecyclerAdapter ", " info: " + item.category + " " + item.weight);
            }
        }
        else if (holder instanceof FooterViewHolder){
        }
    }

    private class ClickHandler implements View.OnClickListener {
        private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
        @Override
        public void onClick(View v) {
            ItemViewHolder holder = (ItemViewHolder) v.getTag();
            final int pos = holder.getAdapterPosition();
            final AppCompatActivity activity = (AppCompatActivity)context;

            switch (v.getId()) {
                case R.id.delete_button:
                    v.startAnimation(buttonClick);
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Are you sure you want to delete this?");
                    builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            scaleCategories.remove(pos);
                            notifyItemRemoved(pos);
                        }
                    });
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
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return scaleCategories.size() + 1;
    }

    private Category getItem(int position) {
        return scaleCategories.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == (getItemCount() - 1)){
            return FOOTER;
        }
        return ITEM;
    }


    private class CategoryListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            Log.i("charseq text ", charSequence.toString());
            scaleCategories.get(position).category = charSequence.toString();
        }
        @Override
        public void afterTextChanged(Editable editable) {}
    }

    private class WeightListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if(charSequence.toString().isEmpty())
                scaleCategories.get(position).weight = 0;
            else{
                Log.i("charseq ", charSequence.toString());
                scaleCategories.get(position).weight = Integer.parseInt(charSequence.toString());
                Log.i("charseq", ""+ scaleCategories.get(position).weight);

            }
        }
        @Override
        public void afterTextChanged(Editable editable) {}
    }
}