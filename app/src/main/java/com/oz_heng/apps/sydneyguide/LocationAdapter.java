package com.oz_heng.apps.sydneyguide;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * {@link LocationAdapter} is an {@link ArrayAdapter} that can provide the layout for each
 * list item based on a data source, which is an {@link ArrayList<Location>}.
 */
class LocationAdapter extends ArrayAdapter<Location> {

    LocationAdapter(@NonNull Context context, @NonNull ArrayList<Location> locations) {
        super(context, 0, locations);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if(view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(getContext()).
                    inflate(R.layout.list_location_item, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        Location location = getItem(position);
        if (location != null) {
            holder.name.setText(location.getName());
            holder.image.setImageResource(location.getDrawableId());
        }

        return view;
    }

    static class ViewHolder{
        @BindView(R.id.name) TextView name;
        @BindView(R.id.picture) ImageView image;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
