/**
 * David Uche
 * Abiodun Ojo
 * Elias
 *
 * This adapter is used to bind the data to the listings view. It is the page described
 * via the All Ads tab. Different from the profile page which uses adapterbooks
 * */

package com.humber.bookmeup.controllers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.humber.bookmeup.R;
import com.humber.bookmeup.models.Book;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterBooks extends
        RecyclerView.Adapter<AdapterBooks.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    // Pass in the book array into the constructor
    private List<Book> mDataset;

    public AdapterBooks(List<Book> books) {
        mDataset = books;
    }



    @Override
    public AdapterBooks.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View bookView = inflater.inflate(R.layout.item_book, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(bookView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(AdapterBooks.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Book book = mDataset.get(position);
        TextView textView = viewHolder.nameTextView;
        TextView textView1 = viewHolder.authorTextView;
        TextView textView3 = viewHolder.priceTextView;
        ImageView imageView = viewHolder.downloadUrlTextView;
        textView.setText(book.getBookName());
        textView1.setText(book.getBookAuthor());
        textView3.setText(String.valueOf(book.getBookPrice()));
        /** Set the imageview to the image url of the reference to the storage bucket*/
        Picasso.get().load(book.getBookPicUrl()).into(imageView);

    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public ImageView downloadUrlTextView;
        public TextView priceTextView;
        public TextView authorTextView;


        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.mbookName);
            downloadUrlTextView = itemView.findViewById(R.id.mbookPicUrl);
            priceTextView = itemView.findViewById(R.id.mbookPrice);
            authorTextView = itemView.findViewById(R.id.mbookAuthor);
        }
    }
}