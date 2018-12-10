package com.humber.bookmeup.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.facebook.login.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.humber.bookmeup.R;

import static android.widget.Toast.LENGTH_SHORT;
import static java.lang.Boolean.TRUE;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences pref;
    ActionBar bar;
    Switch theme;
    int theme_set;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = this.getSharedPreferences("SETTINGS", MODE_PRIVATE);
        setContentView(R.layout.activity_settings);
        theme = findViewById(R.id.switch3);
        Button log_out = findViewById(R.id.out_btn);
        Button saveP = findViewById(R.id.set_save_btn);
        bar = getSupportActionBar();
        bar.setTitle("Settings");
        theme_set = 0;
        settheMood();
        bar.setDisplayShowHomeEnabled(true);
        bar.setLogo(R.drawable.ic_settings);
        bar.setDisplayUseLogoEnabled(true);

        theme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
//                    bar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
//                    bar.setTitle(Html.fromHtml("<font color='#000000'>Settings</font>"));
                    theme_set =1;
                }else{
//                    bar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
//                    bar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Settings</font>"));
                    theme_set =0;
                }
            }
        });



        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getmeOut();
            }
        });

        saveP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applySettings();
            }
        });
    }

    public void getmeOut(){
        FirebaseAuth.getInstance().signOut();
        Intent iwantout = new Intent(SettingsActivity.this, LoginActivity.class);
        startActivity(iwantout);

    }

    public void applySettings(){
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("Theme", theme_set);
        editor.apply();
        Toast.makeText(SettingsActivity.this,"Settings Applied",LENGTH_SHORT).show();
    }

    public void settheMood(){
        try {
            int st = pref.getInt("Theme", 0);
            if (st == 1) {
                bar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                bar.setTitle(Html.fromHtml("<font color='#000000'>Settings</font>"));
                theme.setChecked(TRUE);
            } else {
                bar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                bar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Settings</font>"));
            }
        }catch (NullPointerException e){
            Log.e("Not Found","No Saved Settings Yet");
        }
    }

}
