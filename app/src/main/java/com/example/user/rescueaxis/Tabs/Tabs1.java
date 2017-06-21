package com.example.user.rescueaxis.Tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.rescueaxis.Fragment.Ambulance;
import com.example.user.rescueaxis.Fragment.FireBrigade;
import com.example.user.rescueaxis.Fragment.Police;
import com.example.user.rescueaxis.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class Tabs1 extends Fragment {

    public Tabs1() {
    }
public static TabLayout tabLayout;
public static ViewPager viewPager;
public static int int_items = 3 ;

@Nullable
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Emergency Services");
        LayoutInflater mInflator = LayoutInflater.from(getActivity());
        View mCustomView = inflater.inflate(R.layout.fragment_tabs,null);
        tabLayout = (TabLayout) mCustomView.findViewById(R.id.tabs);
        viewPager = (ViewPager) mCustomView.findViewById(R.id.viewpager);

        /**
         *Set an Apater for the View Pager
         */
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
        viewPager.setOffscreenPageLimit(3);

        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
        return mCustomView;
    }
class MyAdapter extends FragmentPagerAdapter {

    public MyAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * Return fragment with respect to Position .
     */

    @Override
    public Fragment getItem(int position)
    {
        switch (position){
            case 0 : return new Police();
            case 1 : return new Ambulance();
            case 2 : return new FireBrigade();
        }
        return null;
    }

    @Override
    public int getCount() {

        return int_items;

    }

    /**
     * This method returns the title of the tab according to the position.
     */

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "Police";
            case 1:
                return "Ambulance";
            case 2:
                return "FireBrigade";
        }
        return null;
    }
}
}

