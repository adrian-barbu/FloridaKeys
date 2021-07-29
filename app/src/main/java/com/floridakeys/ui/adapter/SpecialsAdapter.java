package com.floridakeys.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.floridakeys.R;
import com.floridakeys.model.venue.VenueMenu;
import com.floridakeys.model.venue.VenueSpecial;

import java.util.ArrayList;

/**
 * @description Specials Adapter
 *
 * @author      Adrian
 *
 * @param <T>
 */
public class SpecialsAdapter<T> extends ArrayAdapter<VenueSpecial>
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

    public SpecialsAdapter(Context context) {
        super(context, R.layout.list_item_special);

        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /*
     * Set List Item Datas
     *
     */
    public void setData(ArrayList<VenueSpecial> data) {
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
            convertView = mInflater.inflate(R.layout.list_item_special, parent, false);

            holder = new ViewHolder();
            holder.rootView = convertView;
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Show Data
        VenueSpecial special = getItem(position);

        holder.tvName.setText(special.getName());
        holder.tvDescription.setText(special.getDescription());

        return convertView;
    }
}



