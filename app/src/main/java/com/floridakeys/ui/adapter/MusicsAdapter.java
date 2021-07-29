package com.floridakeys.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.floridakeys.R;
import com.floridakeys.model.artist.ArtistMusic;
import com.floridakeys.util.DateTimeUtil;

import java.util.ArrayList;

/**
 * @description Musics Adapter (For Artist)
 *
 * @author      Adrian
 *
 * @param <T>
 */
public class MusicsAdapter<T> extends ArrayAdapter<ArtistMusic>
{
    private Context mContext;
    private LayoutInflater mInflater;

    private OnItemClickListener mOnItemClickListener;

    /**
     * View Holder
     */
    private static class ViewHolder {
        public View rootView;
        public TextView tvTitle;
        public TextView tvDuration;
        public TextView tvPublishDate;
    }

    public MusicsAdapter(Context context, OnItemClickListener listener) {
        super(context, R.layout.list_item_music);

        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mOnItemClickListener = listener;
    }

    /*
     * Set List Item Datas
     *
     */
    public void setData(ArrayList<ArtistMusic> data) {
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
            convertView = mInflater.inflate(R.layout.list_item_music, parent, false);

            holder = new ViewHolder();
            holder.rootView = convertView;
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.tvDuration = (TextView) convertView.findViewById(R.id.tvDuration);
            holder.tvPublishDate = (TextView) convertView.findViewById(R.id.tvPublishDate);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Show Data
        ArtistMusic item = getItem(position);
        holder.tvTitle.setText(item.getName());
        holder.tvDuration.setText(DateTimeUtil.makeTime(item.getDuration()));
        holder.tvPublishDate.setText(String.format("(%s)", item.getPublish()));

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



