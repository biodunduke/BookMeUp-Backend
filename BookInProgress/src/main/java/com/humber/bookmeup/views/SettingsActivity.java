package com.humber.bookmeup.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.login.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.humber.bookmeup.R;

import static java.lang.Boolean.TRUE;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Button log_out = findViewById(R.id.out_btn);
        getSupportActionBar().setTitle("Settings");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(TRUE);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_settings);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        pref = getSharedPreferences("SETTINGS", MODE_PRIVATE);


        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getmeOut();
            }
        });
    }

    public void getmeOut(){
        FirebaseAuth.getInstance().signOut();
        Intent iwantout = new Intent(SettingsActivity.this, LoginActivity.class);
        startActivity(iwantout);

    }

}
