package com.floridakeys.ui.fragment.settings;

import android.os.Bundle;
import android.view.View;

import com.floridakeys.R;
import com.floridakeys.ui.fragment.BaseFragment;


/**
 * @description Artists Fragment
  *
 * @author      Adrian
 */

public class SettingsFragment extends BaseFragment
{

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTitle(getString(R.string.sidebar_settings));

        addFragment(new SettingsMainFragment(), false /* No animation */);
    }
}
