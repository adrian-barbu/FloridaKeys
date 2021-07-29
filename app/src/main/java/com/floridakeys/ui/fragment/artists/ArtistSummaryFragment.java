package com.floridakeys.ui.fragment.artists;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.floridakeys.R;
import com.floridakeys.model.artist.Artist;
import com.floridakeys.model.artist.ArtistSummary;
import com.floridakeys.network.netConfig;
import com.floridakeys.ui.fragment.BaseFragment;
import com.floridakeys.ui.fragment.artists.sub.ArtistBiographyFragment;
import com.floridakeys.ui.fragment.artists.sub.ArtistEventFragment;
import com.floridakeys.ui.fragment.artists.sub.ArtistMusicFragment;
import com.floridakeys.util.ImageUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * @description Artist Summary Fragment
 *              This fragment has artists information items
 *
 * @author      Adrian
 */

public class ArtistSummaryFragment extends BaseFragment implements View.OnClickListener
{
    // UI Members
    ImageView ivImage;      // Artist Image

    TextView tvLiveEvent;   // Live Events
    Button btnEventDetails;

    View layoutArtistMusic;         // Music Section
    TextView tvArtistMusicBrief;

    View layoutArtistBiography;         // Biography Section
    TextView tvArtistBiographyBrief;

    View layoutArtistEvents;         // Events Section
    TextView tvArtistEventsBrief;

    // Variables
    Artist mArtist;                 // Artist (will be get from parameter)
    ArtistSummary mArtistSummary;   // Artist Summary

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_artist_summary, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTitle(getString(R.string.artist_info_title));

        mArtist = getArguments().getParcelable(PARAM_ARTIST);

        // Set Artist Image
        ivImage = (ImageView) mRootView.findViewById(R.id.ivImage);
        ImageUtil.displayArtistImage(ivImage, mArtist.getImageUrl(), null);

        showLoadingLayout();

        // Get data from server
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                params.add("id", mArtist.getID());

                callWebService(netConfig.SERVICE_ARTIST_INFORMATION, params, mResponseHandler);

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
                mArtistSummary = new ArtistSummary();
                if (mArtistSummary.doParseJson(data))
                    updateUI();

            } else {

            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            hideLoadingLayout();
        }
    };

    /**
     * Update UI
     */
    private void updateUI() {
        TextView tvArtistName = (TextView) mRootView.findViewById(R.id.tvArtistName);
        tvArtistName.setText(mArtist.getFullName());

        TextView tvArtistType = (TextView) mRootView.findViewById(R.id.tvArtistType);
        tvArtistType.setText(mArtistSummary.getType());

        TextView tvArtistStyle = (TextView) mRootView.findViewById(R.id.tvArtistStyle);
        tvArtistStyle.setText(mArtistSummary.getStyle());

        TextView tvArtistMusicBrief = (TextView) mRootView.findViewById(R.id.tvArtistMusicBrief);
        tvArtistMusicBrief.setText(String.format("%s %s", mArtistSummary.getMusicCount(), getString(R.string.artist_music_tracks)));

        layoutArtistMusic = (View) mRootView.findViewById(R.id.layoutArtistMusic);
        layoutArtistMusic.setOnClickListener(this);

        layoutArtistBiography = (View) mRootView.findViewById(R.id.layoutArtistBiography);
        layoutArtistBiography.setOnClickListener(this);

        layoutArtistEvents = (View) mRootView.findViewById(R.id.layoutArtistEvents);
        layoutArtistEvents.setOnClickListener(this);

        // Show Layout
        View layoutContents = (View) mRootView.findViewById(R.id.layoutContents);
        layoutContents.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        BaseFragment fragment = null;

        switch (v.getId()) {
            case R.id.layoutArtistBiography:
                fragment = new ArtistBiographyFragment();
                break;

            case R.id.layoutArtistMusic:
                if (mArtistSummary.getMusicCount().equals("0"))
                    showToast(R.string.warning_no_artist_musics);
                else
                    fragment = new ArtistMusicFragment();
                break;

            case R.id.layoutArtistEvents:
                fragment = new ArtistEventFragment();
                break;
        }

        if (fragment != null) {
            // Set params
            Bundle bundle = new Bundle();
            bundle.putParcelable(PARAM_ARTIST, mArtist);
            bundle.putString(PARAM_ARTIST_BIOGRAPHY, mArtistSummary.getBiography());
            fragment.setArguments(bundle);
            addFragment(fragment, true);
        }
    }
}
