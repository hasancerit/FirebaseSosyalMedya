package com.example.hasan.myapplication.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.hasan.myapplication.Adapters.SearchKisiListAdapter;
import com.example.hasan.myapplication.Models.User;
import com.example.hasan.myapplication.R;

import java.util.ArrayList;

public class FragmentSearchList extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("KONTROL","OLUŞTU");
        View v = inflater.inflate(R.layout.fr_search,container,false);
        return v;
    }
}
