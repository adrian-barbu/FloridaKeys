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
import com.floridakeys.model.venue.Venue;
import com.floridakeys.util.CommonUtil;
import com.floridakeys.util.ImageUtil;
import com.floridakeys.util.MyLocation;

import java.util.ArrayList;

/**
 * @description Venues Adapter
 *
 * @author      Adrian
 *
 * @param <T>
 */
public class VenuesAdapter<T> extends ArrayAdapter<Venue>
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
        public TextView tvVenueName;
        public TextView tvAddress1, tvAddress2;
        public TextView tvDistanceInMiles;
        public Button btnDetail;
    }

    public VenuesAdapter(Context context, OnItemClickListener listener) {
        super(context, R.layout.list_item_venue);

        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mOnItemClickListener = listener;
    }

    /*
     * Set List Item Datas
     *
     */
    public void setData(ArrayList<Venue> data) {
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
            convertView = mInflater.inflate(R.layout.list_item_venue, parent, false);

            holder = new ViewHolder();
            holder.rootView = convertView;
            holder.ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
            holder.tvVenueName = (TextView) convertView.findViewById(R.id.tvVenueName);
            holder.tvAddress1 = (TextView) convertView.findViewById(R.id.tvAddress1);
            holder.tvAddress2 = (TextView) convertView.findViewById(R.id.tvAddress2);
            holder.tvDistanceInMiles = (TextView) convertView.findViewById(R.id.tvDistanceInMiles);
            holder.btnDetail = (Button) convertView.findViewById(R.id.btnDetail);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Show Data
        Venue venue = getItem(position);

        holder.tvVenueName.setText(venue.getName());
        holder.tvAddress1.setText(venue.getAddress());
        holder.tvAddress2.setText(String.format("%s %s", venue.getCity(), venue.getState()));

        // Calculate distance
        double myLat, myLong;
        myLat = MyLocation.getInstance(mContext).getLatitude();
        myLong = MyLocation.getInstance(mContext).getLongitude();
        double distance = CommonUtil.distanceInMiles(myLat, myLong, venue.getLatitude(), venue.getLongitude());
        holder.tvDistanceInMiles.setText(String.format("%d", Math.round(distance)));

        ImageUtil.displayVenueImage(holder.ivImage, venue.getImageUrl(), null);

        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClicked(position);
            }
        });

        return convertView;
    }
}



