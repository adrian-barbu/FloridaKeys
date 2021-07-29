package com.floridakeys.ui.fragment.event;

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
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.floridakeys.R;
import com.floridakeys.autocomplete.SuggestionAdapter;
import com.floridakeys.autocomplete.SuggestionType;
import com.floridakeys.model.Event;
import com.floridakeys.model.venue.Venue;
import com.floridakeys.network.netConfig;
import com.floridakeys.ui.adapter.OnItemClickListener;
import com.floridakeys.ui.adapter.VenueEventsAdapter;
import com.floridakeys.ui.control.LoadMoreListView;
import com.floridakeys.ui.fragment.BaseFragment;
import com.floridakeys.util.DateTimeUtil;
import com.floridakeys.util.MyLocation;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * @description Event Main Fragment
 *              This fragment has event list
 *
 *
 * @author      Adrian
 */

public class EventMainFragment extends BaseFragment implements OnItemClickListener
{
    // UI Members
    TextView tvDateMonth;               // Event Month

    LoadMoreListView lvEvents;          // Event List View
    VenueEventsAdapter mAdapter;        // List Adapter

    // Variables
    AutoCompleteTextView etKeyword;     // Search Keyword

    Venue mVenue;                       // Venue (will be get from parameter)
    ArrayList<Event> mEvents;

    String mCurrentDate;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_events, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCurrentDate = DateTimeUtil.getCurrentDate();

        setTitle(getString(R.string.sidebar_events));

        tvDateMonth = (TextView) mRootView.findViewById(R.id.tvDateMonth);
        tvDateMonth.setText(DateTimeUtil.convertYearMonthString(mCurrentDate, true));

        etKeyword = (AutoCompleteTextView) mRootView.findViewById(R.id.etKeyword);
        etKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etKeyword.getWindowToken(), 0);

                    doRequestData();
                    return true;
                }
                return false;
            }
        });
        etKeyword.setAdapter(new SuggestionAdapter(getActivity(), SuggestionType.EVENT, etKeyword.getText().toString()));

        ImageView ivClearKeyword = (ImageView) mRootView.findViewById(R.id.ivClearKeyword);
        ivClearKeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etKeyword.getText().toString().isEmpty())
                    return;

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etKeyword.getWindowToken(), 0);

                // Clear keyword
                etKeyword.setText("");
                doRequestData();
            }
        });

        // Increase or decrease button
        ImageView ivPrev = (ImageView) mRootView.findViewById(R.id.ivPrev);
        ivPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentDate = DateTimeUtil.decreaseOneMonth(mCurrentDate);
                tvDateMonth.setText(DateTimeUtil.convertYearMonthString(mCurrentDate, true));
                doRequestData();
            }
        });

        ImageView ivNext = (ImageView) mRootView.findViewById(R.id.ivNext);
        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentDate = DateTimeUtil.increaseOneMonth(mCurrentDate);
                tvDateMonth.setText(DateTimeUtil.convertYearMonthString(mCurrentDate, true));
                doRequestData();
            }
        });

        lvEvents = (LoadMoreListView) mRootView.findViewById(R.id.lvEvents);
        lvEvents.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                doRequestLoadMore();
            }
        });

        // Get data from server
        showLoadingLayout();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doRequestData();
            }
        }, DELAY_FOR_UI);
    }

    /**
     * Request load more data
     */
    private void doRequestLoadMore() {
        String lastDateTime;
        if (mEvents.size() == 0) {
            lastDateTime = "-1";
        }
        else {
            Event event = mEvents.get(mEvents.size() - 1);
            lastDateTime = String.format("%s %s", event.getDate(), event.getTime());
        }

        // Send Request to Server
        double latitude, longitude;
        latitude = MyLocation.getInstance(getActivity()).getLatitude();
        longitude = MyLocation.getInstance(getActivity()).getLongitude();

        String keyword = etKeyword.getText().toString();

        RequestParams params = new RequestParams();
        params.add("key", keyword);
        params.add("lat", String.valueOf(latitude));
        params.add("lng", String.valueOf(longitude));
        params.add("last", lastDateTime);
        params.add("date", mCurrentDate /* "2015-11" */);

        callWebService(netConfig.SERVICE_EVENTS, params, mResponseHandler);
    }

    /**
     * Request update data
     */
    private void doRequestData() {
        showLoadingLayout();
        lvEvents.setVisibility(View.INVISIBLE);

        // Manage Event Array
        if (mEvents == null)
            mEvents = new ArrayList<>();

        mEvents.clear();
        System.gc();

        // Send Request to Server
        double latitude, longitude;
        latitude = MyLocation.getInstance(getActivity()).getLatitude();
        longitude = MyLocation.getInstance(getActivity()).getLongitude();

        String keyword = etKeyword.getText().toString();

        RequestParams params = new RequestParams();
        params.add("key", keyword);
        params.add("lat", String.valueOf(latitude));
        params.add("lng", String.valueOf(longitude));
        params.add("last", "-1");
        params.add("date", mCurrentDate /* "2015-11" */);

        callWebService(netConfig.SERVICE_EVENTS, params, mResponseHandler);
    }

    private void updateUI() {
        lvEvents.setVisibility(View.VISIBLE);

        mAdapter = new VenueEventsAdapter(getActivity(), this);
        lvEvents.setAdapter(mAdapter);

        // Set data
        mAdapter.setData(mEvents);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Response Handler
     */
    private AsyncHttpResponseHandler mResponseHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            hideLoadingLayout();
            lvEvents.onLoadMoreComplete();

            JSONArray datas = getJsonArrayFromResponse(responseBody);
            if (datas != null) {
                for (int i = 0; i < datas.length(); i++) {
                    try {
                        JSONObject data = datas.getJSONObject(i);
                        Event event = new Event();
                        if (event.doParseJson(data))
                            mEvents.add(event);
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
            lvEvents.onLoadMoreComplete();
        }
    };

    @Override
    public void onItemClicked(int position) {
        // When user select event, then go event details
        BaseFragment fragment = new EventDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_EVENT_TYPE, EVENT_TYPE_FOR_VENUE);
        bundle.putParcelable(PARAM_EVENT, mEvents.get(position));
        fragment.setArguments(bundle);

        addFragment(fragment, true);
    }
}
