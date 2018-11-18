/**
 * David Uche
 * Abiodun Ojo
 * Elias
 *
 *
 * The purpose of this Activity is to hold the fragments all together.
 * It does this by implementing a fragmentpageradapter that loads each fragment
 * into a specific tab on the activity. To implement a tablayout, we use
 * a viewpager
 *
 * */


package com.humber.bookmeup.views;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.humber.bookmeup.R;

public class MainActivity extends AppCompatActivity {
    FragmentPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPagerProfile);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 3;
        private static String pageTitle;

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
                    return NewListingFragment.newInstance(0, "New Ad");
                case 1: //
                    return ProfilePageFragment.newInstance(1, "New Ad");
                case 2: //
                    return AllAdsFragment.newInstance(2, "New Ad");
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {

            switch(position){
                case 0:
                    pageTitle = "New Listing";
                    break;
                case 1:
                    pageTitle = "Profile Page";
                    break;
                case 2:
                    pageTitle = "All Ads";
                    break;
                    default:
                        pageTitle="Title";
                        break;
            }
            return pageTitle;
        }

    }
}


