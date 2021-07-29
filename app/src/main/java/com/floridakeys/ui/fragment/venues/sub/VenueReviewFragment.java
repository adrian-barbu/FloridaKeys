package com.floridakeys.ui.fragment.venues.sub;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.floridakeys.R;
import com.floridakeys.model.venue.Venue;
import com.floridakeys.model.venue.VenueReview;
import com.floridakeys.network.netConfig;
import com.floridakeys.ui.adapter.ReviewsAdapter;
import com.floridakeys.ui.adapter.OnItemClickListener;
import com.floridakeys.ui.control.LoadMoreListView;
import com.floridakeys.ui.fragment.BaseFragment;
import com.floridakeys.util.ImageUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * @description Venue Review Fragment
 *              This fragment has review list
 *
 *
 * @author      Adrian
 */

public class VenueReviewFragment extends BaseFragment
{
    // UI Members
    ImageView ivImage;              // Venue Image

    LoadMoreListView lvReviews;     // Review View
    ReviewsAdapter mAdapter;        // List Adapter

    // Variables
    Venue mVenue;                   // Venue (will be get from parameter)
    ArrayList<VenueReview> mReviews;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_venue_reviews, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mVenue = getArguments().getParcelable(PARAM_VENUE);

        setTitle(mVenue.getName());

        // Set Venue Image
        ivImage = (ImageView) mRootView.findViewById(R.id.ivImage);
        ImageUtil.displayArtistImage(ivImage, mVenue.getImageUrl(), null);

        RatingBar rbOveralRating = (RatingBar) mRootView.findViewById(R.id.rbOveralRating);
        rbOveralRating.setRating(getArguments().getFloat(PARAM_VENUE_OVERAL_RATINGS));

        mReviews = new ArrayList<>();

        // Setup ListView
        mAdapter = new ReviewsAdapter(getActivity());
        lvReviews = (LoadMoreListView) mRootView.findViewById(R.id.lvReviews);
        lvReviews.setAdapter(mAdapter);
        lvReviews.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                requestUpdateData();
            }
        });

        showLoadingLayout();

        // Get data from server
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestUpdateData();

            }
        }, DELAY_FOR_UI);
    }

    /**
     * Request to update data
     */
    private void requestUpdateData() {
        String lastId;
        if (mReviews.size() == 0)
            lastId = "-1";
        else
            lastId = mReviews.get(mReviews.size()-1).getID();

        RequestParams params = new RequestParams();
        params.add("id", mVenue.getID());
        params.add("last", lastId);

        callWebService(netConfig.SERVICE_VENUE_REVIEWS, params, mResponseHandler);
    }

    /**
     * Response Handler
     */
    private AsyncHttpResponseHandler mResponseHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            hideLoadingLayout();
            lvReviews.onLoadMoreComplete();

            JSONArray datas = getJsonArrayFromResponse(responseBody);
            if (datas != null) {
                for (int i = 0; i < datas.length(); i++) {
                    try {
                        JSONObject data = datas.getJSONObject(i);
                        VenueReview review = new VenueReview();
                        if (review.doParseJson(data))
                            mReviews.add(review);
                    } catch (Exception e) {

                    }
                }

                // Update ui
                if (datas.length() > 0)
                    updateUI();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            hideLoadingLayout();
            lvReviews.onLoadMoreComplete();
        }
    };

    private void updateUI() {
        mAdapter.setData(mReviews);
        mAdapter.notifyDataSetChanged();
    }
}
