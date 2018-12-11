/**
 * Team LastMinSub:
 * Abiodun Ojo
 * David Uche
 * Elias Sabbagh
 *
 * Settings Activity
 * Purpose is to give the user the ability to change some settings and save
 * them in the shared preference and retrieve those settings once the app is loaded
 *
 * */


package com.humber.bookmeup.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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



        saveP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applySettings();
            }
        });
    }

    public void getmeOut(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setTitle(getResources().getString(R.string.logout_title));
        builder.setMessage(getString(R.string.logout_msg))
                .setCancelable(true)
                .setIcon(R.drawable.ic_logout_rounded)
                .setPositiveButton(getString(R.string.logout_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().signOut();
                        Intent iwantout = new Intent(SettingsActivity.this, LoginActivity.class);
                        startActivity(iwantout);
                        finish();
                    }
                })
                .setNegativeButton(getString(R.string.logout_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onResume();
                    }
                });
        builder.show();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        getmeOut();
        return super.onOptionsItemSelected(item);

    }


}
