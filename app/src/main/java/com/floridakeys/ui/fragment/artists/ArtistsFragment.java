package com.floridakeys.ui.fragment.artists;

import android.os.Bundle;
import android.view.View;

import com.floridakeys.R;
import com.floridakeys.ui.fragment.BaseFragment;


/**
 * @description Artists Fragment
  *
 * @author      Adrian
 */

public class ArtistsFragment extends BaseFragment
{

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTitle(getString(R.string.sidebar_artists));

        addFragment(new ArtistsMainFragment(), false /* No animation */);
    }
}
