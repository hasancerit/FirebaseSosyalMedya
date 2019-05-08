package com.example.hasan.myapplication.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.example.hasan.myapplication.Fragments.FragmentKayit;
import com.example.hasan.myapplication.Fragments.FragmentLogin;
import com.example.hasan.myapplication.Fragments.FragmentSearchList;

public class PagerAdapterSearch extends FragmentPagerAdapter {

    FragmentSearchList listKisi,listPoint;

    public PagerAdapterSearch(FragmentManager fm,FragmentSearchList listKisi,FragmentSearchList listPoint) {
        super(fm);
        this.listPoint = listPoint;
        this.listKisi = listKisi;
    }

    @Override
    public Fragment getItem(int i) {
        if(i == 0 ) return listKisi;
        else return listPoint;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
