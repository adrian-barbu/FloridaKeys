package com.floridakeys.ui.fragment.search;

import android.os.Bundle;
import android.view.View;

import com.floridakeys.R;
import com.floridakeys.ui.fragment.BaseFragment;
import com.floridakeys.ui.fragment.venues.VenuesMainFragment;


/**
 * @description Search Fragment
  *
 * @author      Adrian
 */

public class SearchFragment extends BaseFragment
{

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTitle(getString(R.string.search_title));

        addFragment(new SearchMainFragment(), false /* No animation */);
    }
}
