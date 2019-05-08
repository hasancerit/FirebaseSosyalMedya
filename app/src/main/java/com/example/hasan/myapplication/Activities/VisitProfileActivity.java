package com.example.hasan.myapplication.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hasan.myapplication.Adapters.CustomRecycleAdapter;
import com.example.hasan.myapplication.Models.Post;
import com.example.hasan.myapplication.Models.User;
import com.example.hasan.myapplication.R;
import com.example.hasan.myapplication.Utils.NavigationHelper;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class VisitProfileActivity extends AppCompatActivity {
    Toolbar toolbar;
    BottomNavigationViewEx bottomNavigationView;
    RecyclerView recyclerView;
    TextView isimText,takipText,takipciText,postText,descText;
    CircleImageView ımageView;
    DatabaseReference mReference;
    FirebaseDatabase mFirebaseDatabase;
    ArrayList<Post> mPosts;
    CustomRecycleAdapter customRecycleAdapter;
    String ID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_profile);
        mPosts = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        ID = extras.getString("paylasanId");

        setup();
        getmPosts();

        recyclerView = findViewById(R.id.profileRecycler);
        customRecycleAdapter = new CustomRecycleAdapter(this, mPosts);
        recyclerView.setAdapter(customRecycleAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mReference = mFirebaseDatabase.getReference().child("Users").child(ID);

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User visitUser = dataSnapshot.getValue(User.class);
                /*
                //TAKİP ETTİĞİM ALT DALLARIMIN TUTULACAĞI LİSTE ATANIYOR
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    if(data.getKey().equals("listdallar")){
                        for(DataSnapshot data2:data.getChildren()){
                            visitUser.getmList().add(data2.getValue().toString());
                        }
                    }
                }*/
                yansit(visitUser);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }});

    }

    public void setup(){
        toolbar = findViewById(R.id.toolbarMProfile);
        bottomNavigationView =(BottomNavigationViewEx) findViewById(R.id.bottomnav);

        //ID
        isimText = findViewById(R.id.profileName);
        takipText = findViewById(R.id.profileTakip);
        takipciText = findViewById(R.id.profileTakipci);
        postText = findViewById(R.id.profilePost);
        descText = findViewById(R.id.profileAciklama);
        ımageView = findViewById(R.id.profieImage);

        //TOOLBAR
        setSupportActionBar(toolbar);

        //NAV BAR
        NavigationHelper.setupBottomNavigation(bottomNavigationView,3);
        NavigationHelper.onClickBottomNavigation(bottomNavigationView, VisitProfileActivity.this);
    }

    public void yansit(User mUser){
        isimText.setText(mUser.getName());
        takipciText.setText(mUser.getTakipci());
        takipText.setText(mUser.getTakip());
        postText.setText(mUser.getPost());
        descText.setText(mUser.getDesc());

        Picasso.with(VisitProfileActivity.this)
                .load(mUser.getImageUri())
                .placeholder(getDrawable(R.drawable.placeholder))
                .into(ımageView);

    }

    public void getmPosts(){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(ID).child("mPosts");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    Post post = data.getValue(Post.class);
                    mPosts.add(0,post);
                }
                customRecycleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }});
    }
}

