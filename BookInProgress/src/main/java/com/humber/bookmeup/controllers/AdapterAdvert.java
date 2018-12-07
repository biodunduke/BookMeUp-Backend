/**
 * Team LastMinSub:
 * Abiodun Ojo
 * David Uche
 * Elias Sabbagh
 ** This adapter is used to bind the data to the profile page view.
 * Different from the profile page which uses adapterAdvert
 * */

package com.humber.bookmeup.controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.humber.bookmeup.R;
import com.humber.bookmeup.models.Advert;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterAdvert extends ArrayAdapter<Advert> {
    public AdapterAdvert(Context context, ArrayList<Advert> adverts) {
        super(context, 0, adverts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Advert advert = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_advert, parent, false);
        }
        // Lookup view for data population
        ImageView imageView = convertView.findViewById(R.id.bookImage);

        // Populate the data into the template view using the data object
        Picasso.get().load(advert.getBookPicUrl()).fit().into(imageView);
        // Return the completed view to render on screen
        return convertView;
    }
}