package com.floridakeys.ui.fragment.venues;

import android.os.Bundle;
import android.view.View;

import com.floridakeys.R;
import com.floridakeys.ui.fragment.BaseFragment;
import com.floridakeys.ui.fragment.nearby.NearByMainFragment;


/**
 * @description Venues Fragment
  *
 * @author      Adrian
 */

public class VenuesFragment extends BaseFragment
{

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTitle(getString(R.string.sidebar_venues));

        addFragment(new VenuesMainFragment(), false /* No animation */);
    }
}
