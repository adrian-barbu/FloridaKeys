package com.floridakeys.ui.fragment.venues;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.floridakeys.R;
import com.floridakeys.model.venue.Venue;
import com.floridakeys.model.venue.VenueMenu;
import com.floridakeys.model.venue.VenueSpecial;
import com.floridakeys.model.venue.VenueSummary;
import com.floridakeys.network.netConfig;
import com.floridakeys.ui.fragment.BaseFragment;
import com.floridakeys.ui.fragment.venues.sub.VenueEventFragment;
import com.floridakeys.ui.fragment.venues.sub.VenueMenusFragment;
import com.floridakeys.ui.fragment.venues.sub.VenueReviewFragment;
import com.floridakeys.ui.fragment.venues.sub.VenueSpecialsFragment;
import com.floridakeys.util.ActionUtil;
import com.floridakeys.util.ImageUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * @description Venue Summary Fragment
 *              This fragment has venue information items
 *
 * @author      Adrian
 */

public class VenueSummaryFragment extends BaseFragment implements View.OnClickListener
{
    // UI Members
    ImageView ivImage;      // Artist Image

    TextView tvLiveEvent;   // Live Events
    Button btnEventDetails;

    View layoutVenueReviews;      // Review Section
    View layoutVenueMenu;         // Menu Section
    View layoutVenueEvents;       // Events Section
    View layoutVenueSpecials;     // Specials Section

    // Variables
    Venue mVenue;                 // Venue (will be get from parameter)
    VenueSummary mVenueSummary;   // Venue Summary

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_venue_summary, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTitle(getString(R.string.venue_info_title));

        mVenue = getArguments().getParcelable(PARAM_VENUE);

        // Set Venue Image
        ivImage = (ImageView) mRootView.findViewById(R.id.ivImage);
        ImageUtil.displayArtistImage(ivImage, mVenue.getImageUrl(), null);

        showLoadingLayout();

        // Get data from server
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                params.add("id", mVenue.getID());

                callWebService(netConfig.SERVICE_VENUE_INFORMATION, params, mResponseHandler);

            }
        }, DELAY_FOR_UI);
    }

    /**
     * Response Handler
     */
    private AsyncHttpResponseHandler mResponseHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            hideLoadingLayout();

            JSONObject data = getJsonFromResponse(responseBody);
            if (data != null) {
                mVenueSummary = new VenueSummary();
                if (mVenueSummary.doParseJson(data))
                    updateUI();

            } else {

            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            hideLoadingLayout();
        }
    };

    private void updateUI() {
        // Update Summary Info

        TextView tvVenueName = (TextView) mRootView.findViewById(R.id.tvVenueName);
        tvVenueName.setText(mVenue.getName());

        // Manipulate Address
        String venueAddress = mVenue.getAddress();
        if (!mVenue.getCity().isEmpty()) venueAddress += ", " + mVenue.getCity();
        if (!mVenue.getState().isEmpty()) venueAddress += ", " + mVenue.getState();

        TextView tvVenueAddress = (TextView) mRootView.findViewById(R.id.tvVenueAddress);
        tvVenueAddress.setText(venueAddress);

        Button btnVenueDirection = (Button) mRootView.findViewById(R.id.btnVenueDirection);
        btnVenueDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionUtil.navigateTo(getActivity(), mVenue.getLatitude(), mVenue.getLongitude());
            }
        });

        // Manipulate Phone Number
        final String phoneNumber = mVenueSummary.getPhoneNumber();
        View layoutCall = (View) mRootView.findViewById(R.id.layoutCall);
        if (!phoneNumber.isEmpty()) {
            TextView tvVenuePhoneNumber = (TextView) mRootView.findViewById(R.id.tvVenuePhoneNumber);
            tvVenuePhoneNumber.setText(phoneNumber);

            Button btnCall = (Button) mRootView.findViewById(R.id.btnCall);
            btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActionUtil.callPhoneNumber(getActivity(), phoneNumber);
                }
            });
        } else {
            layoutCall.setVisibility(View.GONE);
        }

        // Manipulate WebSite
        final String webSiteUrl = mVenueSummary.getWebsite();
        View layoutWebsite = (View) mRootView.findViewById(R.id.layoutWebsite);
        if (!webSiteUrl.isEmpty()) {
            TextView tvVenueSite = (TextView) mRootView.findViewById(R.id.tvVenueSite);
            tvVenueSite.setText(webSiteUrl);

            Button btnVisit = (Button) mRootView.findViewById(R.id.btnVisit);
            btnVisit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActionUtil.callBrowser(getActivity(), webSiteUrl);
                }
            });
        } else {
            layoutWebsite.setVisibility(View.GONE);
        }

        // Update related items

        TextView tvVenueReviews = (TextView) mRootView.findViewById(R.id.tvVenueReviews);
        tvVenueReviews.setText(String.format("%d %s", mVenueSummary.getReviewCount(), getString(R.string.venue_reviews)));

        RatingBar rbVenueReviews = (RatingBar) mRootView.findViewById(R.id.rbVenueReviews);
        rbVenueReviews.setRating(mVenueSummary.getRating());

        TextView tvVenueMenuBrief = (TextView) mRootView.findViewById(R.id.tvVenueMenuBrief);
        tvVenueMenuBrief.setText(getMenuSummary());

        TextView tvVenueSpecialsBrief = (TextView) mRootView.findViewById(R.id.tvVenueSpecialsBrief);
        tvVenueSpecialsBrief.setText(getSpecialSummary());

        // Layout Actions
        layoutVenueReviews = (View) mRootView.findViewById(R.id.layoutVenueReviews);
        layoutVenueReviews.setOnClickListener(this);

        layoutVenueMenu = (View) mRootView.findViewById(R.id.layoutVenueMenu);
        layoutVenueMenu.setOnClickListener(this);

        layoutVenueEvents = (View) mRootView.findViewById(R.id.layoutVenueEvents);
        layoutVenueEvents.setOnClickListener(this);

        layoutVenueSpecials = (View) mRootView.findViewById(R.id.layoutVenueSpecials);
        layoutVenueSpecials.setOnClickListener(this);

        // Show Layout
        View layoutContents = (View) mRootView.findViewById(R.id.layoutContents);
        layoutContents.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        BaseFragment fragment = null;
        Bundle bundle = new Bundle();

        switch (v.getId()) {
            case R.id.layoutVenueReviews:
                if (mVenueSummary.getReviewCount() == 0) {
                    showToast(R.string.warning_no_venue_reviews);
                }
                else {
                    fragment = new VenueReviewFragment();
                    bundle.putFloat(PARAM_VENUE_OVERAL_RATINGS, mVenueSummary.getRating());
                }
                break;

            case R.id.layoutVenueMenu:
                ArrayList<VenueMenu> menus = mVenueSummary.getMenus();
                if (menus == null || menus.size() == 0) {
                    showToast(R.string.warning_no_venue_menus);
                } else {
                    fragment = new VenueMenusFragment();
                    bundle.putParcelableArrayList(PARAM_VENUE_MENUS, menus);
                }
                break;

            case R.id.layoutVenueEvents:
                fragment = new VenueEventFragment();
                break;

            case R.id.layoutVenueSpecials:
                ArrayList<VenueSpecial> specials = mVenueSummary.getSpecials();
                if (specials == null || specials.size() == 0) {
                    showToast(R.string.warning_no_venue_specials);
                } else {
                    fragment = new VenueSpecialsFragment();
                    bundle.putParcelableArrayList(PARAM_VENUE_SPECIALS, specials);
                }
                break;
        }

        if (fragment != null) {
            // Set params
            bundle.putParcelable(PARAM_VENUE, mVenue);
            fragment.setArguments(bundle);
            addFragment(fragment, true);
        }
    }

    private String getMenuSummary() {
        String menuSummary = "";

        ArrayList<VenueMenu> menus = mVenueSummary.getMenus();
        if (menus != null && menus.size() > 0) {
            for(VenueMenu menu : menus) {
                menuSummary += String.format("%s - %s, ", menu.getName(), menu.getDescription());
            }
        }

        if (menuSummary.isEmpty())
            menuSummary = getString(R.string.no_venue_menus);

        return menuSummary;
    }

    private String getSpecialSummary() {
        String specialSummary = "";

        ArrayList<VenueSpecial> specials = mVenueSummary.getSpecials();
        if (specials != null && specials.size() > 0) {
            for(VenueSpecial special : specials) {
                specialSummary += String.format("%s - %s, ", special.getName(), special.getDescription());
            }
        }

        if (specialSummary.isEmpty())
            specialSummary = getString(R.string.no_venue_specials);

        return specialSummary;
    }
}
