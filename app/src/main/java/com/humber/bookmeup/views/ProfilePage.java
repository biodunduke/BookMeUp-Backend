/**
 * David Uche
 * Abiodun Ojo
 * Elias
 *
 * This class is no longer valid in favour of ProfilePageFragment.
 * Please delete/ignore
 *
 * */


package com.humber.bookmeup.views;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class ProfilePage extends AppCompatActivity  {
    private String name,email,uid,providerId = "";
    private Uri photoUrl;
    public static final String TAG = "CONSOLE:";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Adding an Network Interceptor for Debugging purpose :
        //OkHttpClient okHttpClient = new OkHttpClient() .newBuilder().addNetworkInterceptor(new StethoInterceptor()).build();
        //AndroidNetworking.initialize(getApplicationContext(),okHttpClient);
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        TextView userName = findViewById(R.id.userName);
        final TextView country = findViewById(R.id.country);
        final TextView city = findViewById(R.id.city);
        final TextView address = findViewById(R.id.address);
        final RatingBar rating = findViewById(R.id.ratingBar);

        ImageView profilePic = findViewById(R.id.imageView);
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
        final AdapterAdvert adapter = new AdapterAdvert(this, arrayOfUsers);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.adsCurrent);
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
                        Toast.makeText(ProfilePage.this,adverts.toString(),Toast.LENGTH_LONG).show();
                        for (Advert ad : adverts) {
                            Advert mAd = new Advert(ad.getBookAuthor(),ad.getBookCondition(),ad.getBookName(),ad.getBookPicUrl(),ad.getBookPrice());
                            adapter.addAll(mAd);
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        Toast.makeText(ProfilePage.this,anError.toString(),Toast.LENGTH_LONG).show();
                        Log.d(TAG,anError.toString());
                    }
                });
    }
}
