/**
 * David Uche
 * Abiodun Ojo
 * Elias
 *
 * The purpose of this class is to display the user profile.
 * The model User is used to fetch and post data from the endpoint
 *
 * */

package com.humber.bookmeup.views;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.humber.bookmeup.R;
import com.humber.bookmeup.controllers.AdapterAdvert;
import com.humber.bookmeup.models.Advert;
import com.humber.bookmeup.models.User;
import com.jacksonandroidnetworking.JacksonParserFactory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProfilePageFragment extends Fragment {
    // Store instance variables
    private String title;
    private String name,email,uid,providerId = "";
    private Uri photoUrl;
    public static final String TAG = "CONSOLE:";
    private int page;
    private String api = "https://57bf80c9.ngrok.io";
    // newInstance constructor for creating fragment with arguments
    public static ProfilePageFragment newInstance(int page, String title) {
        ProfilePageFragment fragmentFirst = new ProfilePageFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 1);
        title = getArguments().getString("someTitle");
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
                // Id of the provider (ex: google.com)
                providerId = profile.getProviderId();
                // UID specific to the provider
                uid = profile.getUid();
                // Name, email address, and profile photo Url from facebook.
                name = profile.getDisplayName();
                email = profile.getEmail();
                photoUrl = profile.getPhotoUrl();
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
                        // handle error
                        Log.d("USER",anError.toString());
                        Toast.makeText(getActivity(), anError.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        ArrayList<Advert> arrayOfUsers = new ArrayList<Advert>();
        // Create the adapter to convert the array to views
        final AdapterAdvert adapter = new AdapterAdvert(getActivity(), arrayOfUsers);
        // Attach the adapter to a ListView
        ListView listView = (ListView) view.findViewById(R.id.adsCurrent);
        listView.setAdapter(adapter);

        /**!! VOLATILE !!*/
        /**NGROK  tunnel to localhost. Change this url when needed since we are running on a free version */
        AndroidNetworking.get(api+"/api/ad?userId={userId}")
                .addPathParameter("userId", uid)
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObjectList(Advert.class, new ParsedRequestListener<List<Advert>>() {
                    @Override
                    public void onResponse(List<Advert> adverts) {
                        // do anything with response
                        //Toast.makeText(getActivity(),adverts.toString(),Toast.LENGTH_LONG).show();
                        for (Advert ad : adverts) {
                            Advert mAd = new Advert(ad.getBookAuthor(),ad.getBookCondition(),ad.getBookName(),ad.getBookPicUrl(),ad.getBookPrice());
                            adapter.addAll(mAd);
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        Toast.makeText(getActivity(),anError.toString(),Toast.LENGTH_LONG).show();
                        Log.d(TAG,anError.toString());
                    }
                });
        return view;
    }
}