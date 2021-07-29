package com.floridakeys.ui.fragment.nearby;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.floridakeys.R;
import com.floridakeys.ui.fragment.BaseFragment;


/**
 * @description NearBy Fragment
  *
 * @author      Adrian
 */

public class NearByFragment extends BaseFragment
{

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTitle(getString(R.string.sidebar_nearby));

        addFragment(new NearByMainFragment(), false /* No animation */);
    }
}
