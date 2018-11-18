/**
 * David Uche
 * Abiodun Ojo
 * Elias
 *
 * */

package com.humber.bookmeup.controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.humber.bookmeup.R;
import com.humber.bookmeup.models.Advert;

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
        TextView bookAuthor = (TextView) convertView.findViewById(R.id.bookAuthor);
        TextView bookName = (TextView) convertView.findViewById(R.id.bookName);
        TextView bookPicUrl = (TextView) convertView.findViewById(R.id.bookPicUrl);
        TextView bookCondition = (TextView) convertView.findViewById(R.id.bookCondition);
        TextView bookPrice = (TextView) convertView.findViewById(R.id.bookPrice);

        // Populate the data into the template view using the data object
        bookAuthor.setText(advert.bookName);
        bookCondition.setText(advert.bookCondition);
        bookPicUrl.setText(advert.bookPicUrl);
        bookPrice.setText(advert.bookPrice);
        bookName.setText(advert.bookName);
        // Return the completed view to render on screen
        return convertView;
    }
}