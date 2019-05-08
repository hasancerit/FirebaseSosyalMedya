package com.example.hasan.myapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.hasan.myapplication.Models.Kategori;
import com.example.hasan.myapplication.Adapters.KategoriRecyclerAdaptör;
import com.example.hasan.myapplication.Utils.NavigationHelper;
import com.example.hasan.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    Toolbar toolbar;
    BottomNavigationViewEx bottomNavigationView;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setup();
        recyclerView = findViewById(R.id.kategoriRecycler);

        kategoriCek();
    }

    public void RecyclerViewKur(ArrayList<Kategori> mList){
        KategoriRecyclerAdaptör kategoriRecyclerAdaptör = new KategoriRecyclerAdaptör(this, mList);
        recyclerView.setAdapter(kategoriRecyclerAdaptör);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
    }


    public void setup(){
        toolbar = findViewById(R.id.toolbarSearch);
        bottomNavigationView =findViewById(R.id.bottomnav);

        //TOOLBAR
        setSupportActionBar(toolbar);

        //NAV BAR
        NavigationHelper.setupBottomNavigation(bottomNavigationView,1);
        NavigationHelper.onClickBottomNavigation(bottomNavigationView,SearchActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu2,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.searchButton:
                Intent intentAra = new Intent(this, AraActivity.class);
                this.startActivity(intentAra);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void kategoriCek(){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Points");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<Kategori> kategoriler = new ArrayList<>();

                for(DataSnapshot data: dataSnapshot.getChildren()){
                    Kategori temp = data.child("özellikler").getValue(Kategori.class);
                    kategoriler.add(temp);
                }

                RecyclerViewKur(kategoriler);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }});
    }

}
