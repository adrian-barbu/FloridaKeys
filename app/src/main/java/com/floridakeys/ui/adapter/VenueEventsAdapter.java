package com.floridakeys.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.floridakeys.R;
import com.floridakeys.model.Event;
import com.floridakeys.util.DateTimeUtil;
import com.floridakeys.util.ImageUtil;

import java.util.ArrayList;

/**
 * @description Venue Event Adapter (For Venue)
 *
 * @author      Adrian
 *
 * @param <T>
 */
public class VenueEventsAdapter<T> extends ArrayAdapter<Event>
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
        public TextView tvDay, tvTime;
        public TextView tvPlaceName;
        public TextView tvPlaceAddress;
        public TextView tvPlaceTicket;
    }

    public VenueEventsAdapter(Context context, OnItemClickListener listener) {
        super(context, R.layout.list_item_event_1);

        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mOnItemClickListener = listener;
    }

    /*
     * Set List Item Datas
     *
     */
    public void setData(ArrayList<Event> data) {
        clear();
        if (data != null) {
            addAll(data);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // Define a way to determine which layout to use, here it's just evens and odds.
        return position % 2;
    }

    @Override
    public int getViewTypeCount() {
        return 2; // Count of different layouts
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null)
        {
            if (getItemViewType(position) == 0)
                convertView = mInflater.inflate(R.layout.list_item_event_1, parent, false);
            else
                convertView = mInflater.inflate(R.layout.list_item_event_2, parent, false);

            holder = new ViewHolder();
            holder.rootView = convertView;
            holder.ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
            holder.tvDay = (TextView) convertView.findViewById(R.id.tvDay);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            holder.tvPlaceName = (TextView) convertView.findViewById(R.id.tvPlaceName);
            holder.tvPlaceAddress = (TextView) convertView.findViewById(R.id.tvPlaceAddress);
            holder.tvPlaceTicket = (TextView) convertView.findViewById(R.id.tvPlaceTicket);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Show Data
        Event event = getItem(position);

        String date = DateTimeUtil.convertDate(event.getDate(), false);
        holder.tvDay.setText(date.toUpperCase());
        holder.tvTime.setText(DateTimeUtil.convertTime(event.getTime(), false));

        // This is artist name
        holder.tvPlaceName.setText(event.getArtistName());

        // This is title
        holder.tvPlaceAddress.setText(event.getTitle());

        holder.tvPlaceTicket.setText(String.format("%s %s", mContext.getString(R.string.artist_events_ticket), event.getTickets()));

        // This is artist image
        ImageUtil.displayVenueImage(holder.ivImage, event.getArtistImage(), null);

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



