package com.floridakeys.ui.fragment.artists.sub;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.floridakeys.R;
import com.floridakeys.model.artist.Artist;
import com.floridakeys.ui.fragment.BaseFragment;
import com.floridakeys.util.ImageUtil;

/**
 * @description Artist Biography Fragment
 *              This fragment has artists information items
 *
 * @author      Adrian
 */

public class ArtistBiographyFragment extends BaseFragment
{
    // UI Members
    ImageView ivImage;      // Artist Image

    TextView tvArtistName;
    TextView tvBiography;

    // Variables
    Artist mArtist;                 // Artist (will be get from parameter)
    String mBiography;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_artist_biography, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTitle(getString(R.string.artist_info_item_biography));

        mArtist = getArguments().getParcelable(PARAM_ARTIST);
        mBiography = getArguments().getString(PARAM_ARTIST_BIOGRAPHY);

        initUI();
    }

    private void initUI() {
        ivImage = (ImageView) mRootView.findViewById(R.id.ivImage);
        ImageUtil.displayArtistImage(ivImage, mArtist.getImageUrl(), null);

        tvArtistName = (TextView) mRootView.findViewById(R.id.tvArtistName);
        tvArtistName.setText(mArtist.getFullName());

        tvBiography = (TextView) mRootView.findViewById(R.id.tvBiography);
        tvBiography.setText(mBiography);
    }
}
