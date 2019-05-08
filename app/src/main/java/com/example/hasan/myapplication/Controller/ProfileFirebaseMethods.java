package com.example.hasan.myapplication.Controller;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hasan.myapplication.Activities.ProfileActivity;
import com.example.hasan.myapplication.Adapters.CustomRecycleAdapter;
import com.example.hasan.myapplication.Adapters.DialogAdapter;
import com.example.hasan.myapplication.Models.Post;
import com.example.hasan.myapplication.Models.User;
import com.example.hasan.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileFirebaseMethods {
    String mUid;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mReference,mPostReference;
    ArrayList<TextView> textViews;
    ImageView ımageView;
    Context context;
    CustomRecycleAdapter customRecycleAdapter;
    ArrayList<Post> mPosts;

    public ProfileFirebaseMethods(Context context, ArrayList<TextView> textViews, ImageView ımageView,
                                  CustomRecycleAdapter customRecycleAdapter, ArrayList<Post> mPosts){

        mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mReference = mFirebaseDatabase.getReference().child("Users").child(mUid);
        mPostReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mUid).child("mPosts");

        this.ımageView = ımageView;
        this.textViews = textViews;
        this.context = context;
        this.mPosts = mPosts;
        this.customRecycleAdapter = customRecycleAdapter;
    }

    public void mUserCek(){
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //USER ÇEKİLİYOR
                User mUser = dataSnapshot.getValue(User.class);

                //TAKİP ETTİĞİM ALT DALLARIMIN TUTULACAĞI LİSTE ATANIYOR
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    if(data.getKey().equals("listdallar")){
                        for(DataSnapshot data2:data.getChildren()){
                            mUser.getmList().add(data2.getValue().toString());
                        }
                    }
                }
                yansit(mUser,textViews);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }});
    }


    public void yansit(final User mUser, ArrayList<TextView> textViews){
        textViews.get(0).setText(mUser.getName());
        textViews.get(1).setText(mUser.getTakip());
        textViews.get(2).setText(mUser.getTakipci());
        textViews.get(3).setText(mUser.getPost());
        textViews.get(4).setText(mUser.getDesc());

        Picasso.with(context)
                .load(mUser.getImageUri())
                .placeholder(context.getDrawable(R.drawable.placeholder))
                .into(ımageView);


        textViews.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAc(mUser);
            }
        });
        textViews.get(5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAc(mUser);
            }
        });

    }

    public void getmPosts(){
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
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

    public void dialogAc(final User mUser){
        final ArrayList<String> üstDallar = new ArrayList<>();
        for(int i = 0 ; i<mUser.getmList().size() ; i++){
            DatabaseReference pointsRef = FirebaseDatabase.getInstance().getReference().child("Points");
            final String altDal = mUser.getmList().get(i);
            pointsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataÜstDallar:dataSnapshot.getChildren()){
                        for(DataSnapshot dataAltDallar:dataÜstDallar.getChildren() ){
                            String altDalIsım = dataAltDallar.child("isim").getValue().toString();
                            if(altDalIsım.equals(altDal)){
                                String üstDal = dataÜstDallar.getKey().toString();
                                üstDallar.add(üstDal);
                                break;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }});
        }

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        View DallarDialog = LayoutInflater.from(context).inflate(R.layout.alt_dal_dialog,null);
        RecyclerView recyclerView = DallarDialog.findViewById(R.id.altDalRecycler);

        DialogAdapter dialogAdapter = new DialogAdapter(context,mUser.getmList(),üstDallar);
        recyclerView.setAdapter(dialogAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        mBuilder.setView(DallarDialog);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }


}
