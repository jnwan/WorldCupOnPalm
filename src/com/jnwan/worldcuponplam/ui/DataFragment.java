package com.jnwan.worldcuponplam.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jnwan.worldcuponplam.R;
import com.jnwan.worldcuponplam.model.DataShooterDetail;



public class DataFragment extends Fragment{

    static int NUM_ITEMS = 3;
    
    private DataPointCursor dataPointCursor;
    
    private DataShooterCursor dataShooterCursor;
    
    private DataAssistCursor dataAssistCursor;

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
                	if(dataAssistCursor == null){
                		dataAssistCursor = new DataAssistCursor();
                	}
                	return dataAssistCursor;
                }

                case 0:{
                	if(dataPointCursor == null){
                		dataPointCursor = new DataPointCursor();
                	}
                	return dataPointCursor;
                }
                case 1:{
                	if(dataShooterCursor == null){
                		dataShooterCursor = new DataShooterCursor();
                	}
                	return dataShooterCursor;
                }

            }

        }
        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {

                default: {

                    return "Öú¹¥°ñ";

                }

                case 0: {

                    return "»ý·Ö°ñ";

                }
                case 1: {

                    return "ÉäÊÖ°ñ";

                }

            }

        }

    }

}
