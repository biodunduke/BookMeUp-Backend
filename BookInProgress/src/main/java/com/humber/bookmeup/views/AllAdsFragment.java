/**
 * Team LastMinSub:
 * Abiodun Ojo
 * David Uche
 * Elias Sabbagh
 *
 *
 * The purpose of this Activity is to display all listings of books.
 * It does so by getting a reference to all ads created by users and
 * returning a json payload containing the data. This data is then
 * added to the corresponing model(the book object in this case) and rendered to an adapter
 *
 * */


package com.humber.bookmeup.views;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.humber.bookmeup.R;
import com.humber.bookmeup.controllers.AdapterBooks;
import com.humber.bookmeup.models.Book;

import java.util.ArrayList;
import java.util.List;

public class AllAdsFragment extends Fragment {
    /** Instance variables*/
    private String title;
    private int page;

    /** Recycler view*/
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    /**Data to load into the adapter. This is fetched from the api */
    private List<Book> myDataset=new ArrayList<>();

    /**!! VOLATILE !!*/
    /**NGROK  tunnel to localhost. Change this url when needed since we are running on a free version */
    public String api = "https://57bf80c9.ngrok.io";


    public static AllAdsFragment newInstance(int page, String title) {
        AllAdsFragment fragmentFirst = new AllAdsFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 2);
        title = getArguments().getString("someTitle");

    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        /**Api call to get all ads from firebase. For docs, check out https://github.com/amitshekhariitbhu/Fast-Android-Networking */
        /**Sample data returned:
         * [{"bookAuthor":"qqqqq","bookName":"ssdf",
         * "bookPrice":22,"bookCondition":"hhhhh",
         * "bookPicUrl":"https://firebasestorage.googleapis.com/v0/b/bookmeup
         * -d232a.appspot.com/o/gggg?alt=media&token=b01c35e4-60ad-48e9
         * -8581-4d206c7fdd9a"}]
         * */

        AndroidNetworking.get(api+"/api/ads")
                .build()
                .getAsObjectList(Book.class, new ParsedRequestListener<List<Book>>() {
                    @Override
                    public void onResponse(List<Book> books) {
                        for (Book b : books) {
                            /**Add each book returned in the payload to our dataset by populating our book model*/
                            myDataset.add(new Book(b.getBookName(), b.getBookAuthor(),b.getBookPicUrl(),b.getBookPrice()));
                            Log.d("BOOK","BOOKS FETCHED");
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.d("BOOK",anError.toString());
                    }
                });

    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_ads, container, false);
        mRecyclerView = view.findViewById(R.id.my_recycler_view);

        /** Prevent layout from changing size*/
        mRecyclerView.setHasFixedSize(true);

        /**Use linear layout. could use gridlayout as well */
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        /**Load our adapter with the fetched data */
        mAdapter = new AdapterBooks(myDataset);

        /**Set our adapter*/
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

}