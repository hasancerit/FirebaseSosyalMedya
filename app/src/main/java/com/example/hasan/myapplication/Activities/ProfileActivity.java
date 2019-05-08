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
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.hasan.myapplication.Adapters.CustomRecycleAdapter;
import com.example.hasan.myapplication.Controller.ProfileFirebaseMethods;
import com.example.hasan.myapplication.Models.User;
import com.example.hasan.myapplication.Utils.NavigationHelper;
import com.example.hasan.myapplication.Models.Post;
import com.example.hasan.myapplication.R;
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

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    Toolbar toolbar;
    BottomNavigationViewEx bottomNavigationView;
    RecyclerView recyclerView;
    TextView isimText,takipText,takipciText,postText,descText,textTakip;
    CircleImageView ımageView;
    DatabaseReference mReference;
    String mUid;
    FirebaseDatabase mFirebaseDatabase;
    ArrayList<Post> mPosts;
    CustomRecycleAdapter customRecycleAdapter;
    ArrayList<TextView> textViews;
    ProfileFirebaseMethods profileFirebaseMethods;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setup();

        recyclerView = findViewById(R.id.profileRecycler);
        recyclerView.setAdapter(customRecycleAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mReference = mFirebaseDatabase.getReference().child("Users").child(mUid);

        profileFirebaseMethods.getmPosts();
        profileFirebaseMethods.mUserCek();
    }

    public void setup(){
        toolbar = findViewById(R.id.toolbarMProfile);
        bottomNavigationView =(BottomNavigationViewEx) findViewById(R.id.bottomnav);

        //NEW
        mPosts = new ArrayList<>();
        textViews = new ArrayList<>();
        customRecycleAdapter = new CustomRecycleAdapter(this, mPosts);

        //ID
        isimText = findViewById(R.id.profileName); textViews.add(isimText);
        takipText = findViewById(R.id.profileTakip); textViews.add(takipText);
        takipciText = findViewById(R.id.profileTakipci); textViews.add(takipciText);
        postText = findViewById(R.id.profilePost); textViews.add(postText);
        descText = findViewById(R.id.profileAciklama); textViews.add(descText);
        textTakip = findViewById(R.id.textTakip); textViews.add(textTakip);
        ımageView = findViewById(R.id.profieImage);

        profileFirebaseMethods = new ProfileFirebaseMethods(this,textViews,ımageView,customRecycleAdapter,mPosts);

        //OnClick
        ımageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePP();
            }
        });

        //TOOLBAR
        setSupportActionBar(toolbar);

        //NAV BAR
        NavigationHelper.setupBottomNavigation(bottomNavigationView,3);
        NavigationHelper.onClickBottomNavigation(bottomNavigationView,ProfileActivity.this);
    }

    public void changePP(){
            ActivityCompat.requestPermissions(ProfileActivity.this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},1);
            CropImage.activity()
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(ProfileActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
            onActivityResultCrop(requestCode,resultCode,data);
    }

    public void onActivityResultCrop(int requestCode, int resultCode, @Nullable Intent data){
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child("Profile_Images").child(mUid+"profileImage");

        if (resultCode == RESULT_OK) {
            Uri resultUri = result.getUri();
            mStorageRef.putFile(resultUri)
            .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) throw task.getException();
                    return mStorageRef.getDownloadUrl();
                }
            })
            .addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("Users").child(mUid).child("imageUri");
                        mReference.setValue(downloadUri.toString());
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
        }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            Exception error = result.getError();
        }
    }

}

