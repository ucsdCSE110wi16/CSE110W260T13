package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Calendar.CaldroidUtil;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;

/**
 * CaldroidCustomFragment class implements some functionalities of the calendar fragment.
 */
public class CaldroidCustomFragment extends CaldroidFragment {
    
    /**
     * getNewDatesGridAdapter gets an adapter for the gird for month and year specified.
     * @param month
     * @param year
     * @return CaldroidGridAdapter for the month an year specified with the Caldroid data.
     */
    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        return new CaldroidCustomAdapter(getActivity(), month, year,
                getCaldroidData(), extraData);
    }

    /**
     * getGridViewRes returns a grid layout
     * @return int that represents the grid layout.
     */
    @Override
    protected int getGridViewRes() {
        return R.layout.layout_grid;
    }
}
