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
import com.floridakeys.model.venue.VenueMenu;
import com.floridakeys.model.venue.VenueReview;
import com.floridakeys.util.ImageUtil;

import java.util.ArrayList;

/**
 * @description Menus Adapter
 *
 * @author      Adrian
 *
 * @param <T>
 */
public class MenusAdapter<T> extends ArrayAdapter<VenueMenu>
{
    private Context mContext;
    private LayoutInflater mInflater;

    /**
     * View Holder
     */
    private static class ViewHolder {
        public View rootView;
        public TextView tvName;
        public TextView tvDescription;
    }

    public MenusAdapter(Context context) {
        super(context, R.layout.list_item_menu);

        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /*
     * Set List Item Datas
     *
     */
    public void setData(ArrayList<VenueMenu> data) {
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
            convertView = mInflater.inflate(R.layout.list_item_menu, parent, false);

            holder = new ViewHolder();
            holder.rootView = convertView;
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Show Data
        VenueMenu menu = getItem(position);

        holder.tvName.setText(menu.getName());
        holder.tvDescription.setText(menu.getDescription());

        return convertView;
    }
}



