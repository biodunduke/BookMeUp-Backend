package com.humber.bookmeup;

import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.jacksonandroidnetworking.JacksonParserFactory;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import okhttp3.OkHttpClient;

public class ProfilePage extends AppCompatActivity {
    private String name,email,uid,providerId = "";
    private Uri photoUrl;
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
        TextView country = findViewById(R.id.country);
        TextView city = findViewById(R.id.city);
        TextView address = findViewById(R.id.address);

        ImageView profilePic = findViewById(R.id.imageView);
        Picasso.get().load(photoUrl).into(profilePic);
        userName.setText(name);
        country.setText(uid);

    }
}
