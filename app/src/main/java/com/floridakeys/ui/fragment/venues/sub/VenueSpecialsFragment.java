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
import com.floridakeys.model.venue.VenueSpecial;
import com.floridakeys.ui.adapter.SpecialsAdapter;
import com.floridakeys.ui.fragment.BaseFragment;
import com.floridakeys.util.ImageUtil;

import java.util.ArrayList;

/**
 * @description Venue Specials Fragment
 *              This fragment has specials list
 *
 *
 * @author      Adrian
 */

public class VenueSpecialsFragment extends BaseFragment
{
    // UI Members
    ImageView ivImage;              // Venue Image

    ListView lvSpecials;               // Menu List View
    SpecialsAdapter mAdapter;          // List Adapter

    // Variables
    Venue mVenue;                   // Venue (will be get from parameter)
    ArrayList<VenueSpecial> mSpecials;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_venue_specials, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mVenue = getArguments().getParcelable(PARAM_VENUE);
        mSpecials = getArguments().getParcelableArrayList(PARAM_VENUE_SPECIALS);

        setTitle(mVenue.getName());

        // Set Venue Image
        ivImage = (ImageView) mRootView.findViewById(R.id.ivImage);
        ImageUtil.displayArtistImage(ivImage, mVenue.getImageUrl(), null);

        // Setup ListView
        mAdapter = new SpecialsAdapter(getActivity());
        lvSpecials = (ListView) mRootView.findViewById(R.id.lvSpecials);
        lvSpecials.setAdapter(mAdapter);
        mAdapter.setData(mSpecials);
    }
}
