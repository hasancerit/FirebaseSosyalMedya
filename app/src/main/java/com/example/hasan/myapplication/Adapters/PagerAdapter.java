package com.example.hasan.myapplication.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.hasan.myapplication.Fragments.FragmentKayit;
import com.example.hasan.myapplication.Fragments.FragmentLogin;

public class PagerAdapter extends FragmentPagerAdapter {

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return new FragmentLogin();
        }else{
            return new FragmentKayit();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
