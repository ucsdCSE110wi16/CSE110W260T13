package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Calendar.CaldroidUtil;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;
import com.roomorama.caldroid.CellView;

import java.util.HashMap;
import java.util.Map;

import hirondelle.date4j.DateTime;
import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;

/**
 * Created by nick on 2/2/16.
 */
public class CustomCaldroidGridAdapter extends CaldroidGridAdapter {

    protected LayoutInflater localInflater;

    public CustomCaldroidGridAdapter(Context context, int month, int year,
                                       HashMap<String, Object> caldroidData,
                                       HashMap<String, Object> extraData) {
        super(context, month, year, caldroidData, extraData);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        localInflater = CaldroidFragment.getLayoutInflater(context, inflater, themeResource);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void setCustomResources(DateTime dateTime, View backgroundView,
                                      TextView textView) {
        // Set custom background resource
        Map<DateTime, Integer> backgroundForDateTimeMap = (Map<DateTime, Integer>) caldroidData
                .get(CaldroidFragment._BACKGROUND_FOR_DATETIME_MAP);
        if (backgroundForDateTimeMap != null) {
            // Get background resource for the dateTime
            Integer backgroundResource = backgroundForDateTimeMap.get(dateTime);

            // Set it
            if (backgroundResource != null) {
                try {
                    String tmp = this.context.getResources().getResourceName(backgroundResource);
                    // have resource
                    backgroundView.setBackgroundResource(backgroundResource);
                } catch (Resources.NotFoundException e) {
                    // doesn't have resource, use like color
                    backgroundView.setBackgroundColor(backgroundResource);
                }
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CellView cellView;

        // For reuse
        if (convertView == null) {
            final int squareDateCellResource = squareTextViewCell ? R.layout.square_date_cell : R.layout.layout_normal_date_cell;
            cellView = (CellView) localInflater.inflate(squareDateCellResource, parent, false);
        } else {
            cellView = (CellView) convertView;
        }

        customizeTextView(position, cellView);

        return cellView;
    }
}
