package com.floridakeys.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.floridakeys.R;
import com.floridakeys.model.artist.Artist;
import com.floridakeys.util.ImageUtil;

import java.util.ArrayList;

/**
 * @description Artists Adapter
 *
 * @author      Adrian
 *
 * @param <T>
 */
public class ArtistsAdapter<T> extends ArrayAdapter<Artist>
{
    private Context mContext;
    private LayoutInflater mInflater;

    private OnItemClickListener mOnItemClickListener;

    /**
     * View Holder
     */
    private static class ViewHolder {
        public View rootView;
        public ImageView ivImage;
        public TextView tvArtistName;
        public Button btnFollow;
    }

    public ArtistsAdapter(Context context, OnItemClickListener listener) {
        super(context, R.layout.list_item_artist);

        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mOnItemClickListener = listener;
    }

    /*
     * Set List Item Datas
     *
     */
    public void setData(ArrayList<Artist> data) {
        clear();
        if (data != null) {
            addAll(data);
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.list_item_artist, parent, false);

            holder = new ViewHolder();
            holder.rootView = convertView;
            holder.ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
            holder.tvArtistName = (TextView) convertView.findViewById(R.id.tvArtistName);
            holder.btnFollow = (Button) convertView.findViewById(R.id.btnFollow);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Show Data
        Artist artist = getItem(position);

        holder.tvArtistName.setText(artist.getFullName());
        ImageUtil.displayArtistImage(holder.ivImage, artist.getImageUrl(), null);

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClicked(position);
            }
        });
        return convertView;
    }
}



