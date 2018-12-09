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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

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

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                Toast.makeText(MainActivity.this,"BookMeApp",Toast.LENGTH_LONG).show();
                return true;
            case R.id.help:
                Toast.makeText(MainActivity.this,"BookMeApp Help",Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                    return ProfilePageFragment.newInstance(1, getString(R.string.stringHolder));
                case 2: //
                    return AllAdsFragment.newInstance(2, getString(R.string.stringHolder));
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
                    pageTitle = getString(R.string.profilePage);
                    break;
                case 2:
                    pageTitle = getString(R.string.allAds);
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


