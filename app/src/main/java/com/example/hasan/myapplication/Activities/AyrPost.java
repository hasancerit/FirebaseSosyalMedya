package com.example.hasan.myapplication.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.VideoView;

import com.example.hasan.myapplication.Adapters.CommentDialogAdapter;
import com.example.hasan.myapplication.Models.Comment;
import com.example.hasan.myapplication.Models.Post;
import com.example.hasan.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AyrPost extends AppCompatActivity {
    TextView isim,aciklama;
    ImageView imageView;
    VideoView videoView;
    TextView like,commentText;
    ToggleButton likeButton;
    Button yorumAc;
    String PostId;
    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayr_post);

        Bundle extras = getIntent().getExtras();
        PostId = extras.getString("PostId");

        yorumAc = findViewById(R.id.yorumAc);
        isim = findViewById(R.id.user);
        aciklama = findViewById(R.id.aciklama);
        imageView = findViewById(R.id.image);
        videoView = findViewById(R.id.video);
        like = findViewById(R.id.like);
        commentText = findViewById(R.id.comment);
        likeButton = findViewById(R.id.begenButon);

        postBul();
    }

    private void postBul() {
        FirebaseDatabase.getInstance().getReference().child("Posts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    if(PostId.equals(dataSnapshot1.getKey())){
                        post = dataSnapshot1.getValue(Post.class);
                    }
                }
                setData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setData() {
        isim.setText(post.getKişi());
        aciklama.setText(post.getDesc());
        like.setText(post.getLikeSayi());
        commentText.setText(post.getYorumSayi());

        postIVTbas();

        yorumAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yorumDialogAc(post);
            }
        });

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(likeButton.isChecked()) begenOnClick(post,like,1);
                else begenOnClick(post,like,-1);
            }
        });

        isim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isimOnClick(post.getPaylasanId());
            }
        });
    }

    public void postIVTbas(){
        if(post.getType().equals("fotoğraf")){
            imageView.setVisibility(View.VISIBLE);
            Picasso.with(this)
                    .load(post.getContent())
                    .placeholder(this.getDrawable(R.drawable.placeholder))
                    .into(imageView);
        }else if(post.getType().equals("video")){
            videoView.setVisibility(View.VISIBLE);
            String videoPath = post.getContent();
            Uri uri = Uri.parse(videoPath);
            videoView.setVideoURI(uri);

            MediaController mediaController = new MediaController(this);
            videoView.setMediaController(mediaController);
        }
    }





    /***************Yorum**********************/
    private void yorumDialogAc(final Post post) {
        /**************DİALOG İNFLATE VE VİEW****************************************/
        final ArrayList<Comment> comments = new ArrayList<>();
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View DallarDialog = LayoutInflater.from(this).inflate(R.layout.dialog_comment,null);
        RecyclerView recyclerView = DallarDialog.findViewById(R.id.commentList);
        final EditText editText = DallarDialog.findViewById(R.id.commentYorumYap);
        final Button yorumGönder = DallarDialog.findViewById(R.id.yorumGönder);
        final CommentDialogAdapter dialogAdapter = new CommentDialogAdapter(this,comments);

        /***********************Yorumları Çek****************************************/
        DatabaseReference comRef = FirebaseDatabase.getInstance().getReference()
                .child("Posts").child(post.getId()).child("comments");

        comRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    comments.add(0,data.getValue(Comment.class));
                    dialogAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }});

        /*********************DAİLOG ÇIKAR VE YORUMLARA GÖNDER*****************************/
        yorumGönder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yorumYap(editText,post,dialogAdapter,comments);
            }
        });

        recyclerView.setAdapter(dialogAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        mBuilder.setView(DallarDialog);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    private void yorumYap(final EditText yorum, final Post post, final CommentDialogAdapter dialogAdapter, final ArrayList<Comment> comments ) {
        FirebaseAuth firebaseAuthıd = FirebaseAuth.getInstance();
        final String mUid = firebaseAuthıd.getCurrentUser().getUid();

        /**************************POSTS DATABASE'E EKLE VE SAYİ ARTIR*********************************************/
        final DatabaseReference cRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(post.getId()).child("comments").push();
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mUid);
        final Comment comment = new Comment();

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String mName = dataSnapshot.child("name").getValue().toString();
                String mImage = dataSnapshot.child("imageUri").getValue().toString();
                comment.setComment(yorum.getText().toString());
                comment.setCommentId(cRef.getKey());
                comment.setSendById(mUid);
                comment.setSendByImage(mImage);
                comment.setSendByIsim(mName);
                cRef.setValue(comment);
                yorum.setText("");
                comments.add(0,comment);
                dialogAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        final DatabaseReference yorumSayi = FirebaseDatabase.getInstance().getReference()
                .child("Posts").child(post.getId()).child("yorumSayi");
        yorumSayi.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int sayi = Integer.parseInt(dataSnapshot.getValue().toString());
                sayi++;
                yorumSayi.setValue(""+sayi);
                FirebaseDatabase.getInstance().getReference().child("Users").child(post.getPaylasanId()).child("mPosts").child(post.getId())
                        .child("yorumSayi").setValue(""+sayi);
                commentText.setText(""+sayi);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }});
    }


    /****************Visit Profile********************************/
    private void isimOnClick(String paylasanId) {
        Intent visitProfile = new Intent(this, VisitProfileActivity.class);
        visitProfile.putExtra("paylasanId",paylasanId);
        this.startActivity(visitProfile);
    }


    /*******************Like*************************************/
    public void begenOnClick(final Post post, final TextView like, final int deger){
        String muid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final DatabaseReference postReference = FirebaseDatabase.getInstance().getReference()
                .child("Posts").child(post.getId());
        final DatabaseReference paylasanRef = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(post.getPaylasanId()).child("mPosts").child(post.getId());
        //DATABASEDEKİ LİKE SAYISI ARTMASI
        postReference.child("likeSayi").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String likeSayisiS = dataSnapshot.getValue().toString();
                int likeSayisi = Integer.parseInt(likeSayisiS)+deger;
                postReference.child("likeSayi").setValue(""+likeSayisi);//POSTLAR DALINDAKİ SAYIYI ARTIR
                paylasanRef.child("likeSayi").setValue(""+likeSayisi);//KİŞİNİN POSTLAR DALINDAKİ SAYIYI ARTIR
                like.setText(""+likeSayisi);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }});

        //BEĞENEN KİŞİNİN İD'SİNİN DATABESE'E KAYDEDİLMESİ
        if(deger == 1){
            postReference.child("likeID").child(muid).setValue(muid);//POSTLAR DALINDAKİ LİKEID
            paylasanRef.child("likeID").child(muid).setValue(muid);//KİŞİNİN POSTLAR DALINDAKİ LİKEID

        }else{
            postReference.child("likeID").child(muid).removeValue();
            paylasanRef.child("likeID").child(muid).removeValue();
        }
    }

    public void isLiked(Post post, final ToggleButton likeButton){
        final DatabaseReference postReference = FirebaseDatabase.getInstance().getReference()
                .child("Posts").child(post.getId()).child("likeID");
        final String muid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        postReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    if(muid.equals(data.getKey())){
                        likeButton.setChecked(true);
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }});
    }

}
