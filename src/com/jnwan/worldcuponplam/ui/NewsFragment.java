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



public class NewsFragment extends Fragment{

    static int NUM_ITEMS = 3;

    private SinaNewsFragment sinaNews;

    private TencentNewsFragment tencentNews;
    
    private SohuNewsFragment sohuNews;

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
                	if(sohuNews == null){
                		sohuNews = new SohuNewsFragment();
                	}
                	return sohuNews;
                }

                case 0: {

                    if (sinaNews == null) {

                        sinaNews = new SinaNewsFragment();

                    }

                    return sinaNews;

                }
                case 1: {

                    if (tencentNews == null) {

                    	tencentNews = new TencentNewsFragment();

                    }

                    return tencentNews;

                }

            }

        }
        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {

                default: {

                    return "搜狐体育";

                }

                case 0: {

                    return "新浪体育";

                }
                case 1: {

                    return "腾讯体育";

                }

            }

        }

    }

}
