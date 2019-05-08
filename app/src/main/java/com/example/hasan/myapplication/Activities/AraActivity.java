package com.example.hasan.myapplication.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.LoginFilter;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.hasan.myapplication.Adapters.DialogAdapter;
import com.example.hasan.myapplication.Adapters.PagerAdapterSearch;
import com.example.hasan.myapplication.Adapters.SearchKisiListAdapter;
import com.example.hasan.myapplication.Fragments.FragmentSearchList;
import com.example.hasan.myapplication.Models.User;
import com.example.hasan.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AraActivity extends AppCompatActivity {
    Button back;
    TabLayout tabLayout;
    ViewPager viewPager;
    EditText editText;
    Context context = this;

    final ArrayList<User> users = new ArrayList<>();
    final ArrayList<String> points = new ArrayList<>();
    final ArrayList<String> üstPoints  = new ArrayList<>();

    PagerAdapterSearch pagerAdapterSearch;
    SearchKisiListAdapter searchKisiListAdapter;
    DialogAdapter dialogAdapter;
    FragmentSearchList listKisi,listPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ara);

        editText = findViewById(R.id.searchText);
        tabLayout = findViewById(R.id.searchTab);
        viewPager = findViewById(R.id.pager);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSearch = new Intent(context, SearchActivity.class);
                context.startActivity(intentSearch);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ara(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        listKisi = new FragmentSearchList();
        listPoint = new FragmentSearchList();

        pagerAdapterSearch = new PagerAdapterSearch(getSupportFragmentManager(),listKisi,listPoint);
        viewPager.setAdapter(pagerAdapterSearch);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);

    }

    public void ara(final String ara){
        users.clear();
        points.clear();
        üstPoints.clear();
        DatabaseReference searchReference;

        if(viewPager.getCurrentItem() ==  0){
            searchReference = FirebaseDatabase.getInstance().getReference().child("Users");

            searchReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot data:dataSnapshot.getChildren()){
                        if(data.child("name").getValue().toString().contains(ara) && !ara.equals("")){
                            User user = new User();
                            user.setName(data.child("name").getValue().toString());
                            user.setDesc(data.child("desc").getValue().toString());
                            user.setImageUri(data.child("imageUri").getValue().toString());
                            users.add(user);
                        }
                    }
                    RecyclerView list = listKisi.getView().findViewById(R.id.searchList);
                    Log.d("GİDEN",""+users.size());
                    searchKisiListAdapter = new SearchKisiListAdapter(context,users);
                    list.setAdapter(searchKisiListAdapter);

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                    linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
                    list.setLayoutManager(linearLayoutManager);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}});



        }else{
            searchReference = FirebaseDatabase.getInstance().getReference().child("Points");
            searchReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        for (DataSnapshot dataSnapshot2:dataSnapshot1.getChildren()){
                            if(dataSnapshot2.child("isim").getValue().toString().contains(ara)){
                                Log.d("Giriş","Girdi322");
                                points.add(dataSnapshot2.child("isim").getValue().toString());
                                üstPoints.add(dataSnapshot1.getKey());
                            }
                        }
                    }
                    RecyclerView list2 = listPoint.getView().findViewById(R.id.searchList);
                    dialogAdapter = new DialogAdapter(context,points,üstPoints);
                    list2.setAdapter(dialogAdapter);

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                    linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
                    list2.setLayoutManager(linearLayoutManager);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

}
