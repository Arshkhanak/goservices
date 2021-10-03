package com.tecmanic.goservices.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tecmanic.goservices.fragment.HistoryBookingFragment;
import com.tecmanic.goservices.fragment.OnGoingFragment;


/**
 * Created by wolfsoft5 on 7/4/18.
 */

public class CategoryPagerAdapterBooking extends FragmentPagerAdapter {

        int mNoOfTabs;

        public CategoryPagerAdapterBooking(FragmentManager fm, int NumberOfTabs)

        {
                super(fm);
                this.mNoOfTabs = NumberOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
                switch (position) {

                        case 0:
                                OnGoingFragment tab1 = new OnGoingFragment();
                                return tab1;
                        case 1:
                                HistoryBookingFragment tab2 = new HistoryBookingFragment();
                                return tab2;



                        default:
                                return null;

                }
        }

        @Override
        public int getCount() {
                return mNoOfTabs;

        }
}

