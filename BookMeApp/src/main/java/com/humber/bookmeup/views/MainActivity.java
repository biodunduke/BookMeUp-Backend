/**
 * David Uche
 * Abiodun Ojo
 * Elias
 * <p>
 * <p>
 * The purpose of this Activity is to hold the fragments all together.
 * It does this by implementing a fragmentpageradapter that loads each fragment
 * into a specific tab on the activity. To implement a tablayout, we use
 * a viewpager
 */


package com.humber.bookmeup.views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.humber.bookmeup.R;

public class MainActivity extends AppCompatActivity {
    FragmentPagerAdapter adapterViewPager;
    private String phoneNo = "6476881111";
    private String message = "Hello developers!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPagerProfile);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                Toast.makeText(MainActivity.this,"BookMeApp",Toast.LENGTH_LONG).show();
                return true;
            case R.id.help:
                return true;
            case R.id.sms:
                sendSMSMessage();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void sendSMSMessage() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
                Toast.makeText(MainActivity.this,"SMS message sent",Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        0);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private int NUM_ITEMS = 4; //Number of Fragment pages
        //Increase if you need to add more pages
        private String pageTitle;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return NewListingFragment.newInstance(0, getString(R.string.stringHolder));
                case 1: //
                    return AllAdsFragment.newInstance(2, getString(R.string.stringHolder));
                case 2: //
                    return ProfilePageFragment.newInstance(1, getString(R.string.stringHolder));
                case 3: //
                    return AboutFragment.newInstance(3, getString(R.string.stringHolder));
                //Add more pages here, also do same inside getPageTitle
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    pageTitle = getString(R.string.newListing);
                    break;
                case 1:
                    pageTitle = getString(R.string.allAds);
                    break;
                case 2:
                    pageTitle = getString(R.string.profilePage);
                    break;
                case 3:
                    pageTitle = getString(R.string.aboutPage);
                    break;
                default:
                    pageTitle = getString(R.string.stringHolder);
                    break;
            }
            return pageTitle;
        }
    }
}


