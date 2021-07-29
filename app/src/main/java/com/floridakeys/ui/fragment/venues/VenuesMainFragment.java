package com.floridakeys.ui.fragment.venues;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.floridakeys.R;
import com.floridakeys.autocomplete.SuggestionAdapter;
import com.floridakeys.autocomplete.SuggestionType;
import com.floridakeys.model.artist.Artist;
import com.floridakeys.model.venue.Venue;
import com.floridakeys.network.netConfig;
import com.floridakeys.ui.adapter.OnItemClickListener;
import com.floridakeys.ui.adapter.VenuesAdapter;
import com.floridakeys.ui.control.LoadMoreListView;
import com.floridakeys.ui.fragment.BaseFragment;
import com.floridakeys.ui.fragment.artists.ArtistSummaryFragment;
import com.floridakeys.ui.fragment.event.EventDetailFragment;
import com.floridakeys.util.MyLocation;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * @description Venues Main Fragment
 *              This fragment has venues list
 *
 * @author      Adrian
 */

public class VenuesMainFragment extends BaseFragment implements OnItemClickListener
{
    // UI Members
    AutoCompleteTextView etKeyword;     // Search Keyword
    LoadMoreListView lvVenues;          // List View

    // Variables
    VenuesAdapter mAdapter;             // List Adapter
    ArrayList<Venue> mVenues;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_venues, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTitle(getString(R.string.sidebar_venues));

        isTopParent = true;

        etKeyword = (AutoCompleteTextView) mRootView.findViewById(R.id.etKeyword);
        etKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etKeyword.getWindowToken(), 0);

                    requestUpdateData(true /* need clear */);
                    return true;
                }
                return false;
            }
        });
        etKeyword.setAdapter(new SuggestionAdapter(getActivity(), SuggestionType.VENUE, etKeyword.getText().toString()));

        ImageView ivClearKeyword = (ImageView) mRootView.findViewById(R.id.ivClearKeyword);
        ivClearKeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear keyword
                if (etKeyword.getText().toString().isEmpty())
                    return;

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etKeyword.getWindowToken(), 0);

                etKeyword.setText("");
                requestUpdateData(true /* need clear */);
            }
        });

        mVenues = new ArrayList<>();

        // Manage List View
        mAdapter = new VenuesAdapter(getActivity(), this);
        lvVenues = (LoadMoreListView) mRootView.findViewById(R.id.lvVenues);
        lvVenues.setAdapter(mAdapter);
        lvVenues.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                requestUpdateData(false);
            }
        });

        // Get data from server
        showLoadingLayout();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestUpdateData(true  /* need clear */);
            }
        }, DELAY_FOR_UI);
    }

    /**
     * Request update data
     */
    private void requestUpdateData(boolean needClear) {
        if (needClear) {
            mVenues.clear();
            updateUI();
        }

        double lastDistance = -1;
        if (mVenues.size() != 0)
            lastDistance = mVenues.get(mVenues.size()-1).getDistance();

        // Prepare to get data from filter keys
        double latitude, longitude;
        latitude = MyLocation.getInstance(getActivity()).getLatitude();
        longitude = MyLocation.getInstance(getActivity()).getLongitude();

        String keyword = etKeyword.getText().toString();

        RequestParams params = new RequestParams();
        params.add("key", keyword);
        params.add("lat", String.valueOf(latitude));
        params.add("lng", String.valueOf(longitude));
        params.add("last", String.valueOf(lastDistance));       // This is last distance to get next group
        params.add("count", String.valueOf(PAGE_COUNT));

        if (mVenues.size() == 0)
            showLoadingLayout();

        // Start loading data
        callWebService(netConfig.SERVICE_VENUES, params, mResponseHandler);
    }

    /**
     * Response Handler
     */
    private AsyncHttpResponseHandler mResponseHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            hideLoadingLayout();
            lvVenues.onLoadMoreComplete();

            JSONArray datas = getJsonArrayFromResponse(responseBody);
            if (datas != null) {
                for (int i = 0; i < datas.length(); i++) {
                    try {
                        JSONObject data = datas.getJSONObject(i);
                        Venue venue = new Venue();
                        if (venue.doParseJson(data))
                            mVenues.add(venue);
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
            lvVenues.onLoadMoreComplete();
        }
    };

    private void updateUI() {
        mAdapter.setData(mVenues);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClicked(int position) {
        // Set params
        Venue venue = mVenues.get(position);

        BaseFragment fragment = new VenueSummaryFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARAM_VENUE, venue);
        fragment.setArguments(bundle);

        addFragment(fragment, true);
    }
}
