package com.mychatter.android.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.mychatter.android.R;

import java.util.List;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.array.tab_text_1, R.array.tab_text_2};
    //private final Context mContext;
    //private final List<Contact> contactList;

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
        //mContext = context;
        //this.contactList = contactList;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        //return PlaceholderFragment.newInstance(position + 1);
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new FragmentHistory();
        }
        else if (position == 1)
        {
            fragment = new FragmentContact();
        }
//        else if (position == 2)
//        {
//            fragment = new FragmentC();
//        }
        return fragment;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "History";
        }
        else if (position == 1)
        {
            title = "Contact";
        }

        return title;
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }




}