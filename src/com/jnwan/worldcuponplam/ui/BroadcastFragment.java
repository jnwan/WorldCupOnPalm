package com.jnwan.worldcuponplam.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jnwan.worldcuponplam.R;



public class BroadcastFragment extends Fragment{

    static int NUM_ITEMS = 2;

    private BroadcastGroupMatch broadcastGroupStage;

    private BroadcastNotice broadcastNotice;

    private ViewPagerAdapter mAdapter;

    private ViewPager mPager;

    private View rootView;
    

    

    @Override

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {

        rootView = layoutInflater.inflate(R.layout.viewpager, viewGroup, false);

        mAdapter = new ViewPagerAdapter(getChildFragmentManager());

        mPager = (ViewPager) rootView.findViewById(R.id.pager);
        
        mPager.setAdapter(mAdapter);

        return rootView;

    }

    

    private class ViewPagerAdapter extends FragmentPagerAdapter{

        public ViewPagerAdapter(FragmentManager fragmentManager) {

            super(fragmentManager);

        }

        

        @Override

        public int getCount() {

            return NUM_ITEMS;

        }

        

        @Override

        public Fragment getItem(int position) {

            switch (position) {

                default: {

                    if (broadcastGroupStage == null) {

                    	broadcastGroupStage =  new BroadcastGroupMatch();

                    }

                    return broadcastGroupStage;

                }

                case 0: {

                    if (broadcastNotice == null) {

                        broadcastNotice = new BroadcastNotice();

                    }

                    return broadcastNotice;

                }

            }

        }
        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {

                default: {

                    return "小组赛完整赛程";

                }

                case 0: {

                    return "直播预告";

                }

            }

        }

    }

}
