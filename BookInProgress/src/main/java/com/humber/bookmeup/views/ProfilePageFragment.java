/**
 * Team LastMinSub:
 * Abiodun Ojo
 * David Uche
 * Elias Sabbagh
 *
 * The purpose of this class is to display the user profile.
 * The model User is used to fetch and post data from the endpoint
 */

package com.humber.bookmeup.views;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.humber.bookmeup.R;
import com.humber.bookmeup.controllers.AdapterBooks;
import com.humber.bookmeup.models.Book;
import com.humber.bookmeup.models.User;
import com.jacksonandroidnetworking.JacksonParserFactory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProfilePageFragment extends Fragment {
    // Store instance variables
    private String title;
    private String name, email, uid, providerId = "";
    private Uri photoUrl;
    public static final String TAG = "CONSOLE:";
    private int page;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Book> myDataset = new ArrayList<>();
    private String api = "https://booktemp.herokuapp.com";

    // newInstance constructor for creating fragment with arguments
    public static ProfilePageFragment newInstance(int page, String title) {
        ProfilePageFragment fragmentFirst = new ProfilePageFragment();
        Bundle args = new Bundle();
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_page, container, false);
        AndroidNetworking.setParserFactory(new JacksonParserFactory());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                Log.d("AUTH_RPOVIDER",profile.getProviderId());
                // Id of the provider (ex: google.com)
                //handle anonymous users
                providerId = profile.getProviderId();

                if(!providerId.equals("facebook.com")){
                    name = getString(R.string.anonUser);
                    email = "anonymousUser@gmail.com";
                    photoUrl = Uri.parse("https://cdn.onlinewebfonts.com/svg/img_163051.png");
                    uid = "9";
                }
                else{
                    // UID specific to the provider
                    uid = profile.getUid();
                    // Name, email address, and profile photo Url from facebook.
                    name = profile.getDisplayName();
                    email = profile.getEmail();
                    photoUrl = profile.getPhotoUrl();
                }
            }
        }
        TextView userName = view.findViewById(R.id.userName);
        final TextView country = view.findViewById(R.id.country);
        final TextView city = view.findViewById(R.id.city);
        final TextView address = view.findViewById(R.id.address);
        final RatingBar rating = view.findViewById(R.id.ratingBar);

        ImageView profilePic = view.findViewById(R.id.imageView);
        /**Picasso library to help load and manipulate the images.
         * http://square.github.io/picasso/
         * The .fit() function makes the image fit within the dimensions of the specified image view
         */

        Picasso.get().load(photoUrl).fit().into(profilePic);
        userName.setText(name);

        /**Api to fetch the data for this specific user. Do note that since edit profile is not yet
         * implemented, default Textview will be rendered. However, hardcoding data into the database will be pulled and displayed here*/
        AndroidNetworking.get(api+"/api/users?userId={userId}")
                .addPathParameter("userId", uid)
                .setTag(this)
                .build()
                .getAsObject(User.class, new ParsedRequestListener<User>() {
                    @Override
                    public void onResponse(User user) {
                        country.setText(user.getCountry());
                        city.setText(user.getCity());
                        address.setText(user.getAddress());
                        rating.setRating(user.getRating());
                    }

                    @Override
                    public void onError(ANError anError) {
                        //Load fake data for anonymous users
                        country.setText(R.string.default_country);
                        city.setText(R.string.Toronto);
                        address.setText(R.string.Address);
                        rating.setRating(4);
                        //Log.d("USER", anError.toString());
                    }
                });

        ArrayList<Book> arrayOfUsers = new ArrayList<Book>();
        // Create the adapter to convert the array to views
        final AdapterBooks adapter = new AdapterBooks(arrayOfUsers);
        //        // Attach the adapter to a ListView

        AndroidNetworking.get(api + "/api/ad?userId={userId}")
                .addPathParameter("userId", uid)
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObjectList(Book.class, new ParsedRequestListener<List<Book>>() {
                    @Override
                    public void onResponse(List<Book> adverts) {
                        // do anything with response
                        //Toast.makeText(getActivity(),adverts.toString(),Toast.LENGTH_LONG).show();
                        for (Book ad : adverts) {
                            myDataset.add(new Book(ad.getBookName(), ad.getBookAuthor(), ad.getBookPicUrl(), ad.getBookPrice()));
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        Log.d(TAG, anError.toString());
                    }
                });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.adsCurrent);

        /** Prevent layout from changing size*/
        mRecyclerView.setHasFixedSize(true);

        /**Use linear layout. could use gridlayout as well */
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);


        /**Load our adapter with the fetched data */
        mAdapter = new AdapterBooks(myDataset);

        /**Set our adapter*/
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }
}