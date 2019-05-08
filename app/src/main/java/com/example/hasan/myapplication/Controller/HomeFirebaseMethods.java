package com.example.hasan.myapplication.Controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.LoginFilter;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hasan.myapplication.Activities.HomeActivity;
import com.example.hasan.myapplication.Adapters.AlanSecAdapter;
import com.example.hasan.myapplication.Adapters.CustomRecycleAdapter;
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

import java.util.ArrayList;
import java.util.Random;

public class HomeFirebaseMethods {
    Context context;
    LayoutInflater layoutInflater;

    ArrayList<String> fitreTür;

    ArrayList<String> mPoints;
    ArrayList<Post> basilacak;

    CustomRecycleAdapter customRecycleAdapter;

    public HomeFirebaseMethods(Context context, ArrayList<String> mPoints,ArrayList<Post> basilacak,
                               CustomRecycleAdapter customRecycleAdapter){
        this.context = context;
        this.basilacak = basilacak;
        this.mPoints = mPoints;
        this.customRecycleAdapter = customRecycleAdapter;

        layoutInflater = LayoutInflater.from(context);
    }


    /********************************HOME PAGE POST*****************************************************/
    public void alanCek(final ArrayList<String> mPoints){
        FirebaseAuth firebaseAuthıd = FirebaseAuth.getInstance();
        final String mUid = firebaseAuthıd.getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mUid).child("listdallar");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    mPoints.add(data.getValue().toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }});
    }

    public void arrayListGuncelle()  {
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference().child("Posts");

        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    ArrayList<String> points = new ArrayList<>();
                    for (DataSnapshot dataAlan:data.child("points").getChildren()){
                        points.add(dataAlan.getValue().toString());
                    }
                    karsılastirBas(data,points);
                }
                filtreKontrol();
                customRecycleAdapter.notifyDataSetChanged();
            }@Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }});
    }

    public void karsılastirBas(final DataSnapshot post, final ArrayList<String> points){
        cik:
        for(int i = 0 ; i < mPoints.size() ;i++){
            for (int k = 0 ; k < points.size() ; k++){
                if(mPoints.get(i).equals(points.get(k))){
                    Post bas = post.getValue(Post.class);
                    bas.setPointler(points);
                    basilacak.add(0,bas);
                    break cik;
                }
            }
        }
    }

    /****************************************NEW POST DİALOG******************************************/
    public void newPostDialog(final Uri uri){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        View newPostDialog = layoutInflater.inflate(R.layout.yeni_post_dialog,null);

            final RecyclerView alanSec = newPostDialog.findViewById(R.id.alanSecList);

            final ArrayList<String> showPoints = new ArrayList<>();
            for(int i = 0 ; i < mPoints.size() ; i++) showPoints.add(mPoints.get(i));

            final AlanSecAdapter alanSecAdapter = new AlanSecAdapter(context,showPoints);
            alanSec.setAdapter(alanSecAdapter);
            GridLayoutManager linearLayoutManager = new GridLayoutManager(context,2);
            linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
            alanSec.setLayoutManager(linearLayoutManager);

            ImageView ımageView = newPostDialog.findViewById(R.id.newPostImageView);
            final EditText aciklama = newPostDialog.findViewById(R.id.newPostAciklama);
            final EditText alan = newPostDialog.findViewById(R.id.newPostAlan);
            Button paylas = newPostDialog.findViewById(R.id.newPostPaylas);
            alan.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    alanBak(alan.getText().toString(),showPoints,alanSecAdapter);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            final ProgressBar newPostProgress = newPostDialog.findViewById(R.id.newPostProgress);
            newPostProgress.setIndeterminate(false);
            ımageView.setImageURI(uri);
            mBuilder.setView(newPostDialog);
            final AlertDialog dialog = mBuilder.create();
            paylas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(alanSecAdapter.points.size() == 0) {
                            Toast.makeText(context, "En Az Bir Alan Seçin.", Toast.LENGTH_SHORT).show();
                        }else {
                            newPostPaylas(uri, aciklama.getText().toString(), alanSecAdapter.points, dialog);
                        }
                    }
                });

        dialog.show();
    }

    public void alanBak(String s,ArrayList<String> points,AlanSecAdapter adapter){
        ArrayList<String> yeniAlan = new ArrayList<>();
        String ks = s.toLowerCase();
        String bs = s.toUpperCase();

            for (int i = 0; i < mPoints.size(); i++) {
                if (mPoints.get(i).contains(ks) || mPoints.get(i).contains(bs)) {
                    yeniAlan.add(mPoints.get(i));
                }
            }

            points.clear();
            for (int i = 0; i < yeniAlan.size(); i++) {
                points.add(yeniAlan.get(i));
            }

        adapter.notifyDataSetChanged();
    }

    public void newPostPaylas(Uri uri, final String desc, final ArrayList<String> alan, final AlertDialog dialog){
        String mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("Posts").child(storagePush()+mUid+storagePush());

        storageReference.putFile(uri)
                .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) throw task.getException();
                        return storageReference.getDownloadUrl();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            final Uri downloadUri = task.getResult();
                            final String muid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference()
                                    .child("Posts").push();
                            final DatabaseReference profileRef = FirebaseDatabase.getInstance().getReference()
                                    .child("Users").child(muid).child("mPosts").child(mRef.getKey());
                            final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                                    .child("Users").child(muid);

                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot data:dataSnapshot.getChildren()) {
                                        if(data.getKey().equals("name")) {
                                            Post post = new Post();
                                            post.setId(mRef.getKey());
                                            post.setPaylasanId(muid);
                                            post.setLikeSayi("0");
                                            post.setYorumSayi("0");
                                            post.setContent(downloadUri.toString());
                                            post.setDesc(desc);
                                            post.setType("fotoğraf");
                                            String name = data.getValue().toString();
                                            post.setKişi(name);

                                            mRef.setValue(post);
                                            profileRef.setValue(post);

                                            for (int i = 0 ; i < alan.size() ; i++){
                                                mRef.child("points").push().setValue(alan.get(i));
                                                profileRef.child("points").push().setValue(alan.get(i));
                                            }
                                        }else if(data.getKey().equals("post")){
                                            String postS = data.getValue().toString();
                                            int post = Integer.parseInt(postS);
                                            post += 1;
                                            userRef.child("post").setValue(""+post);
                                        }
                                    }
                                }@Override
                                public void onCancelled(@NonNull DatabaseError databaseError) { }});


                            dialog.dismiss();
                            Intent intentHome = new Intent(context, HomeActivity.class);
                            context.startActivity(intentHome);
                        }
                    }
                });
    }

    public String storagePush(){
        Random rnd = new Random();
        String id="";
        for(int i = 0 ; i < 3 ; i++)
            id += ""+rnd.nextInt(120);

        return id;
    }

    /********************************FİLTRELEME*******************************************/
    public void filtreKontrol(){
        String mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mUid);
        final DatabaseReference filtreVarMiRef = mRef.child("filtreVarMi");
        final DatabaseReference filtreRef = mRef.child("filtre");

        filtreVarMiRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue().toString().equals("e")){
                    filtrele(filtreRef);
                }else{
                    /**/
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }});
    }

    public void filtrele(DatabaseReference filtreRef){
        /**************************FİLTRENECEK DEĞERLER ÇEKİLDİ************************************/
        final ArrayList<String> istenenTürler = new ArrayList<>();
        filtreRef.child("türler").getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot türler:dataSnapshot.getChildren()){
                        istenenTürler.add(türler.getValue().toString());
                        filtreleTur(istenenTürler);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }});

        final ArrayList<String> istenenAlanlar = new ArrayList<>();
        filtreRef.child("alanlar").getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot alanlar:dataSnapshot.getChildren()){
                    istenenAlanlar.add(alanlar.getValue().toString());
                }
                filtreleAlan(istenenAlanlar);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }});

    }

    public void filtreleAlan (ArrayList<String> istenenAlanlar){
        ArrayList<Post> yeniBasilacak = new ArrayList<>();

        for(int i = 0 ; i < basilacak.size() ; i++){
            for(int k = 0 ; k < istenenAlanlar.size() ; k++){
                for (int j = 0 ; j < basilacak.get(i).getPointler().size();j++){
                    if(basilacak.get(i).getPointler().get(j).equals(istenenAlanlar.get(k))){
                        yeniBasilacak.add(basilacak.get(i));
                        break;
                    }
                }
            }
        }

        basilacak.clear();
        for (int i = 0 ; i < yeniBasilacak.size() ; i++){
            basilacak.add(yeniBasilacak.get(i));
        }

        customRecycleAdapter.notifyDataSetChanged();
    }

    public void filtreleTur (ArrayList<String> istenenTurler){
        ArrayList<Post> yeniBasilacak = new ArrayList<>();

        for(int i = 0 ; i < basilacak.size() ; i++){
            for(int k = 0 ; k < istenenTurler.size() ; k++){
                if(basilacak.get(i).getType().equals(istenenTurler.get(k))){
                    yeniBasilacak.add(basilacak.get(i));
                    break;
                }
            }
        }

        basilacak.clear();
        for (int i = 0 ; i < yeniBasilacak.size() ; i++){
            basilacak.add(yeniBasilacak.get(i));
        }

        customRecycleAdapter.notifyDataSetChanged();
    }
}
