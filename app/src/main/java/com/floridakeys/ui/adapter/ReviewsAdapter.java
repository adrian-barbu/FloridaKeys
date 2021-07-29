package com.floridakeys.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.floridakeys.R;
import com.floridakeys.model.venue.VenueReview;
import com.floridakeys.util.ImageUtil;

import java.util.ArrayList;

/**
 * @description Reviews Adapter
 *
 * @author      Adrian
 *
 * @param <T>
 */
public class ReviewsAdapter<T> extends ArrayAdapter<VenueReview>
{
    private Context mContext;
    private LayoutInflater mInflater;

    /**
     * View Holder
     */
    private static class ViewHolder {
        public View rootView;
        public ImageView ivUserImage;
        public TextView tvUserName;
        public TextView tvReview;
        public RatingBar ratingBar;
    }

    public ReviewsAdapter(Context context) {
        super(context, R.layout.list_item_review);

        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /*
     * Set List Item Datas
     *
     */
    public void setData(ArrayList<VenueReview> data) {
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
            convertView = mInflater.inflate(R.layout.list_item_review, parent, false);

            holder = new ViewHolder();
            holder.rootView = convertView;
            holder.ivUserImage = (ImageView) convertView.findViewById(R.id.ivUserImage);
            holder.tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
            holder.tvReview = (TextView) convertView.findViewById(R.id.tvReview);
            holder.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Show Data
        VenueReview review = getItem(position);

        holder.tvUserName.setText(review.getUserName());
        holder.tvReview.setText(review.getComment());
        holder.ratingBar.setRating(review.getRating());
        ImageUtil.displayUserImage(holder.ivUserImage, review.getImageUrl(), null);

        return convertView;
    }
}



