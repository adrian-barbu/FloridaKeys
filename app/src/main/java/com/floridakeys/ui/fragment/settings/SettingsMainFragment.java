package com.floridakeys.ui.fragment.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.floridakeys.R;
import com.floridakeys.ui.adapter.ArtistsAdapter;
import com.floridakeys.ui.fragment.BaseFragment;

import java.util.ArrayList;


/**
 * @description Settings Fragment
 *              This fragment has artists list
 *
 * @author      Adrian
 */

public class SettingsMainFragment extends BaseFragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_settings, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTitle(getString(R.string.sidebar_settings));

        isTopParent = true;

    }
}
