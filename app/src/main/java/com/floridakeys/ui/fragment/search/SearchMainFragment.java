package com.floridakeys.ui.fragment.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.floridakeys.R;
import com.floridakeys.ui.fragment.BaseFragment;

/**
 * @description Search Main Fragment
  *
 * @author      Adrian
 */

public class SearchMainFragment extends BaseFragment
{
    EditText etKeyword;      // Search Keyword

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_search, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTitle(getString(R.string.search_title));

        isTopParent = true;

        etKeyword = (EditText) mRootView.findViewById(R.id.etKeyword);


        initUI();
    }

    private void initUI() {
    }
}
