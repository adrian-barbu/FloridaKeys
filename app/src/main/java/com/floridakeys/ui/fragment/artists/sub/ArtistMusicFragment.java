package com.floridakeys.ui.fragment.artists.sub;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.floridakeys.R;
import com.floridakeys.model.artist.Artist;
import com.floridakeys.model.artist.ArtistMusic;
import com.floridakeys.network.netConfig;
import com.floridakeys.ui.adapter.MusicsAdapter;
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
 * @description Artist Music Fragment
 *              This fragment has music list
 *
 *
 * @author      Adrian
 */

public class ArtistMusicFragment extends BaseFragment implements OnItemClickListener
{
    // UI Members
    ImageView ivImage;          // Artist Image

    LoadMoreListView lvMusics;  // Music List View
    MusicsAdapter mAdapter;     // List Adapter

    // Variable
    Artist mArtist;             // Artist (will be get from parameter)
    ArrayList<ArtistMusic> mMusics;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_artist_music, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTitle(getString(R.string.artist_info_item_music));

        mArtist = getArguments().getParcelable(PARAM_ARTIST);

        ivImage = (ImageView) mRootView.findViewById(R.id.ivImage);
        ImageUtil.displayArtistImage(ivImage, mArtist.getImageUrl(), null);

        mMusics = new ArrayList<>();

        // Setup ListView & Adapter
        mAdapter = new MusicsAdapter(getActivity(), this);
        lvMusics = (LoadMoreListView) mRootView.findViewById(R.id.lvMusics);
        lvMusics.setAdapter(mAdapter);
        lvMusics.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
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
        if (mMusics.size() == 0)
            lastId = "-1";
        else
            lastId = mMusics.get(mMusics.size()-1).getID();

        RequestParams params = new RequestParams();
        params.add("id", mArtist.getID());
        params.add("last", lastId);

        callWebService(netConfig.SERVICE_ARTIST_MUSICS, params, mResponseHandler);
    }

    private void updateUI() {
        mAdapter.setData(mMusics);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Response Handler
     */
    private AsyncHttpResponseHandler mResponseHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            hideLoadingLayout();
            lvMusics.onLoadMoreComplete();

            JSONArray datas = getJsonArrayFromResponse(responseBody);
            if (datas != null) {
                for (int i = 0; i < datas.length(); i++) {
                    try {
                        JSONObject data = datas.getJSONObject(i);
                        ArtistMusic music = new ArtistMusic();
                        if (music.doParseJson(data))
                            mMusics.add(music);
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
            lvMusics.onLoadMoreComplete();
        }
    };

    @Override
    public void onItemClicked(int position) {
        // When user select music, then play it
    }
}
