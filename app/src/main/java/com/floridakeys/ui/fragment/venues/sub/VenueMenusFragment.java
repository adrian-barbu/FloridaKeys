package com.floridakeys.ui.fragment.venues.sub;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.floridakeys.R;
import com.floridakeys.model.venue.Venue;
import com.floridakeys.model.venue.VenueMenu;
import com.floridakeys.ui.adapter.MenusAdapter;
import com.floridakeys.ui.adapter.ReviewsAdapter;
import com.floridakeys.ui.fragment.BaseFragment;
import com.floridakeys.util.ImageUtil;

import java.util.ArrayList;

/**
 * @description Venue Menus Fragment
 *              This fragment has menus list
 *
 *
 * @author      Adrian
 */

public class VenueMenusFragment extends BaseFragment
{
    // UI Members
    ImageView ivImage;              // Venue Image

    ListView lvMenus;               // Menu List View
    MenusAdapter mAdapter;          // List Adapter

    // Variables
    Venue mVenue;                   // Venue (will be get from parameter)
    ArrayList<VenueMenu> mMenus;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_venue_menus, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mVenue = getArguments().getParcelable(PARAM_VENUE);
        mMenus = getArguments().getParcelableArrayList(PARAM_VENUE_MENUS);

        setTitle(mVenue.getName());

        // Set Venue Image
        ivImage = (ImageView) mRootView.findViewById(R.id.ivImage);
        ImageUtil.displayArtistImage(ivImage, mVenue.getImageUrl(), null);

        // Setup ListView
        mAdapter = new MenusAdapter(getActivity());
        lvMenus = (ListView) mRootView.findViewById(R.id.lvMenus);
        lvMenus.setAdapter(mAdapter);
        mAdapter.setData(mMenus);
    }
}
