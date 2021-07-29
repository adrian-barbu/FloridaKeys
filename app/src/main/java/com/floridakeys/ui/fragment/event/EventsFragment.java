package com.floridakeys.ui.fragment.event;

import android.os.Bundle;
import android.view.View;

import com.floridakeys.R;
import com.floridakeys.ui.fragment.BaseFragment;
import com.floridakeys.ui.fragment.venues.VenuesMainFragment;


/**
 * @description Events Fragment
  *
 * @author      Adrian
 */

public class EventsFragment extends BaseFragment
{

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTitle(getString(R.string.sidebar_events));

        addFragment(new EventMainFragment(), false /* No animation */);
    }
}
