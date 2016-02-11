package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Calendar.CaldroidUtil;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

import io.github.cse110w260t13.ucsdcse110wi16.classplanner.R;

/**
 * Created by nick on 2/2/16.
 */
public class CustomCaldroidFragment extends CaldroidFragment {

    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        return new CustomCaldroidGridAdapter(getActivity(), month, year, getCaldroidData(), extraData);
    }

}
