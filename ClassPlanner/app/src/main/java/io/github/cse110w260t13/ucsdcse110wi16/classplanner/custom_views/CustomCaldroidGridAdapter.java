package io.github.cse110w260t13.ucsdcse110wi16.classplanner.custom_views;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

import java.util.HashMap;
import java.util.Map;

import hirondelle.date4j.DateTime;

/**
 * Created by nick on 2/2/16.
 */
public class CustomCaldroidGridAdapter extends CaldroidGridAdapter {

    public CustomCaldroidGridAdapter(Context context, int month, int year,
                                       HashMap<String, Object> caldroidData,
                                       HashMap<String, Object> extraData) {
        super(context, month, year, caldroidData, extraData);
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
}
