package com.humber.bookmeup;

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
                // Name, email address, and profile photo Url
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
        Picasso.get().load(photoUrl).into(profilePic);
        userName.setText(name);

        AndroidNetworking.get("https://bb57afc5.ngrok.io/api/users?userId={userId}")
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
                    }
                });



        // Construct the data source
        ArrayList<Advert> arrayOfUsers = new ArrayList<Advert>();
        // Create the adapter to convert the array to views
        final AdapterAdvert adapter = new AdapterAdvert(getActivity(), arrayOfUsers);
        // Attach the adapter to a ListView
        ListView listView = (ListView) view.findViewById(R.id.adsCurrent);
        listView.setAdapter(adapter);


        AndroidNetworking.get("https://bb57afc5.ngrok.io/api/ads?userId={userId}")
                .addPathParameter("userId", uid)
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObjectList(Advert.class, new ParsedRequestListener<List<Advert>>() {
                    @Override
                    public void onResponse(List<Advert> adverts) {
                        // do anything with response
                        Toast.makeText(getActivity(),adverts.toString(),Toast.LENGTH_LONG).show();
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