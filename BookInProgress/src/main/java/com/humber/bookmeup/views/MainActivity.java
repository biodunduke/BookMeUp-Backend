/**
 * Team LastMinSub:
 * Abiodun Ojo
 * David Uche
 * Elias Sabbagh
 *
 *
 * The purpose of this Activity is to hold the fragments all together.
 * It does this by implementing a fragmentpageradapter that loads each fragment
 * into a specific tab on the activity. To implement a tablayout, we use
 * a viewpager
 */


package com.humber.bookmeup.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.humber.bookmeup.R;

import static java.lang.Boolean.TRUE;

public class MainActivity extends AppCompatActivity {
    FragmentPagerAdapter adapterViewPager;
    SharedPreferences pref;
    ActionBar bar;
    int theme_set = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = this.getSharedPreferences("SETTINGS", MODE_PRIVATE);
        bar = getSupportActionBar();
        checkTheme();
        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPagerProfile);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent takeme = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(takeme);
        return super.onOptionsItemSelected(item);

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
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
                    return AllAdsFragment.newInstance(1, getString(R.string.stringHolder));
                case 2: //
                    return ProfilePageFragment.newInstance(2, getString(R.string.stringHolder));
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

    @Override
    public void onResume(){
        super.onResume();
        checkTheme();

    }

    public void checkTheme(){
        try {
            int st = pref.getInt("Theme", 0);
            if (st == 1) {
                bar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                bar.setTitle(Html.fromHtml("<font color='#000000'>BookMeUp</font>"));
            } else {
                bar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                bar.setTitle(Html.fromHtml("<font color='#FFFFFF'>BookMeUp</font>"));
            }
        }catch (NullPointerException e){
            Log.e("Not Found","No Saved Settings Yet");
        }
    }

}


