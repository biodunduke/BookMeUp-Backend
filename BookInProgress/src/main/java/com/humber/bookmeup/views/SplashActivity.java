/**
 *  * Team LastMinSub:
 *  * Abiodun Ojo
 *  * David Uche
 *  * Elias Sabbagh
 *
 *  This Activity shows up at the start of the app and lasts 3 seconds, used to distract user while server connection is being established
 */
package com.humber.bookmeup.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.humber.bookmeup.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    //3 second splash screen
    private final int STR_SPLASH_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //sign out for testing purposes
        //FirebaseAuth.getInstance().signOut();
        startSplashTimer();

    }

    private void startSplashTimer() {
        try {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, STR_SPLASH_TIME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}