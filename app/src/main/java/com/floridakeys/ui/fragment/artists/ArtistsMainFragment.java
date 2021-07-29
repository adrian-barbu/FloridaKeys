package com.floridakeys.ui.fragment.artists;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.floridakeys.R;
import com.floridakeys.autocomplete.SuggestionAdapter;
import com.floridakeys.autocomplete.SuggestionType;
import com.floridakeys.model.artist.Artist;
import com.floridakeys.network.netConfig;
import com.floridakeys.ui.adapter.ArtistsAdapter;
import com.floridakeys.ui.adapter.OnItemClickListener;
import com.floridakeys.ui.control.LoadMoreListView;
import com.floridakeys.ui.fragment.BaseFragment;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * @description Artist Main Fragment
 *              This fragment has artists list
 *
 * @author      Adrian
 */

public class ArtistsMainFragment extends BaseFragment implements OnItemClickListener
{
    // UI Members
    AutoCompleteTextView etKeyword;             // Search Keyword
    LoadMoreListView lvArtists;     // List View

    // Variables
    ArtistsAdapter mAdapter;        // List Adapter
    ArrayList<Artist> mArtists;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_artists, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTitle(getString(R.string.sidebar_artists));

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
        etKeyword.setAdapter(new SuggestionAdapter(getActivity(), SuggestionType.ARTIST, etKeyword.getText().toString()));

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

        // Set List & Adapter
        mAdapter = new ArtistsAdapter(getActivity(), this);

        lvArtists = (LoadMoreListView) mRootView.findViewById(R.id.lvArtists);
        lvArtists.setAdapter(mAdapter);
        lvArtists.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                requestUpdateData(false);
            }
        });

        mArtists = new ArrayList<>();

        showLoadingLayout();

        // Get data from server
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestUpdateData(true /* need clear */);
            }
        }, DELAY_FOR_UI);
    }

    /**
     * Request update data
     */
    private void requestUpdateData(boolean needClear) {
        if (needClear) {
            mArtists.clear();
            updateUI();
        }

        String lastId;
        if (mArtists.size() == 0)
            lastId = "-1";
        else
            lastId = mArtists.get(mArtists.size()-1).getID();

        // Prepare to get data from filter keys
        String keyword = etKeyword.getText().toString();

        RequestParams params = new RequestParams();
        params.add("key", keyword);
        params.add("last", lastId);
        params.add("count", String.valueOf(PAGE_COUNT));

        if (mArtists.size() == 0)
            showLoadingLayout();

        // Start loading data
        callWebService(netConfig.SERVICE_ARTISTS, params, mResponseHandler);
    }

    /**
     * Response Handler
     */
    private AsyncHttpResponseHandler mResponseHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            hideLoadingLayout();
            lvArtists.onLoadMoreComplete();

            JSONArray datas = getJsonArrayFromResponse(responseBody);
            if (datas != null) {
                for (int i = 0; i < datas.length(); i++) {
                    try {
                        JSONObject data = datas.getJSONObject(i);
                        Artist artist = new Artist();
                        if (artist.doParseJson(data))
                            mArtists.add(artist);
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
            lvArtists.onLoadMoreComplete();
        }
    };

    /**
     * Update UI
     */
    private void updateUI() {
        mAdapter.setData(mArtists);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClicked(int position) {
        // Set params
        Artist artist = mArtists.get(position);

        BaseFragment fragment = new ArtistSummaryFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARAM_ARTIST, artist);
        fragment.setArguments(bundle);

        addFragment(fragment, true);
    }
}
