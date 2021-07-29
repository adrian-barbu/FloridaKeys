package com.floridakeys.ui.fragment.artists.sub;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.floridakeys.R;
import com.floridakeys.model.Event;
import com.floridakeys.model.artist.Artist;
import com.floridakeys.network.netConfig;
import com.floridakeys.ui.adapter.ArtistEventsAdapter;
import com.floridakeys.ui.adapter.OnItemClickListener;
import com.floridakeys.ui.control.LoadMoreListView;
import com.floridakeys.ui.fragment.BaseFragment;
import com.floridakeys.ui.fragment.event.EventDetailFragment;
import com.floridakeys.util.DateTimeUtil;
import com.floridakeys.util.ImageUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * @description Artist Event Fragment
 *              This fragment has event list
 *
 *
 * @author      Adrian
 */

public class ArtistEventFragment extends BaseFragment implements OnItemClickListener
{
    // UI Members
    ImageView ivImage;              // Artist Image
    TextView tvDateMonth;           // Event Month

    LoadMoreListView lvEvents;      // Event List View
    ArtistEventsAdapter mAdapter;   // List Adapter

    // Variables
    Artist mArtist;                 // Artist (will be get from parameter)
    ArrayList<Event> mEvents;

    String mCurrentDate;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_artist_event, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mArtist = getArguments().getParcelable(PARAM_ARTIST);
        mCurrentDate = DateTimeUtil.getCurrentDate();

        setTitle(mArtist.getFullName());

        tvDateMonth = (TextView) mRootView.findViewById(R.id.tvDateMonth);
        tvDateMonth.setText(DateTimeUtil.convertYearMonthString(mCurrentDate, true));

        ivImage = (ImageView) mRootView.findViewById(R.id.ivImage);
        ImageUtil.displayArtistImage(ivImage, mArtist.getImageUrl(), null);

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
        RequestParams params = new RequestParams();
        params.add("id", mArtist.getID());
        params.add("last", lastDateTime);
        params.add("date", mCurrentDate /* "2015-11" */);

        callWebService(netConfig.SERVICE_ARTIST_EVENTS, params, mResponseHandler);
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
        RequestParams params = new RequestParams();
        params.add("id", mArtist.getID());
        params.add("last", "-1");
        params.add("date", mCurrentDate /* "2015-11" */);

        callWebService(netConfig.SERVICE_ARTIST_EVENTS, params, mResponseHandler);
    }

    private void updateUI() {
        lvEvents.setVisibility(View.VISIBLE);

        mAdapter = new ArtistEventsAdapter(getActivity(), this);
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
        bundle.putInt(PARAM_EVENT_TYPE, EVENT_TYPE_FOR_ARTIST);
        bundle.putParcelable(PARAM_EVENT, mEvents.get(position));
        fragment.setArguments(bundle);

        addFragment(fragment, true);
    }
}
