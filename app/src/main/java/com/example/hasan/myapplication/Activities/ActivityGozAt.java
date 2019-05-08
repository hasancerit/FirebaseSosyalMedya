package com.example.hasan.myapplication.Activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.hasan.myapplication.Adapters.CustomRecycleAdapter;
import com.example.hasan.myapplication.Adapters.GozAtAdapter;
import com.example.hasan.myapplication.Models.Post;
import com.example.hasan.myapplication.R;
import com.example.hasan.myapplication.Utils.NavigationHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class ActivityGozAt extends AppCompatActivity {
    Toolbar toolbar;
    BottomNavigationViewEx bottomNavigationView;
    RecyclerView recyclerView;
    GozAtAdapter gozAtAdapter;
    ArrayList<Post> basılacak;
    String istenenDal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goz_at);

        Bundle extras = getIntent().getExtras();
        istenenDal = extras.getString("istenen");

        setup();

        recyclerView.setAdapter(gozAtAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        postBas();
    }

    private void postBas() {
        DatabaseReference databaseReference =FirebaseDatabase.getInstance().getReference().child("Posts");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    if(data.child("point").getValue().toString().equals(istenenDal)){
                        Log.d("İSTENEN",data.child("point").getValue().toString());
                        Post post = data.getValue(Post.class);
                        basılacak.add(post);
                    }
                }
                gozAtAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }});
    }

    public void setup(){
        //FİND ID
        recyclerView = findViewById(R.id.gozAtRecycle);
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView =(BottomNavigationViewEx) findViewById(R.id.bottomnav);

        //NEWx
        basılacak = new ArrayList<>();
        gozAtAdapter = new GozAtAdapter(this,basılacak);


        //TOOLBAR
        setSupportActionBar(toolbar);

        //NAV BAR
        NavigationHelper.setupBottomNavigation(bottomNavigationView,1);
        NavigationHelper.onClickBottomNavigation(bottomNavigationView,ActivityGozAt.this);

    }
}

