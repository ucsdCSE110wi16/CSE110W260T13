package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Calendar.CaldroidUtil;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.roomorama.caldroid.CaldroidGridAdapter;
import com.roomorama.caldroid.CellView;

import java.util.Map;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;

public class CaldroidCustomAdapter extends CaldroidGridAdapter{
    public CaldroidCustomAdapter(Context context, int month, int year,
                                       Map<String, Object> caldroidData,
                                       Map<String, Object> extraData) {
        super(context, month, year, caldroidData, extraData);
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
