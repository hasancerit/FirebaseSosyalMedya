package com.example.hasan.myapplication.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.VideoView;

import com.example.hasan.myapplication.Activities.AyrPost;
import com.example.hasan.myapplication.Activities.HomeActivity;
import com.example.hasan.myapplication.Activities.ProfileActivity;
import com.example.hasan.myapplication.Activities.SearchActivity;
import com.example.hasan.myapplication.Activities.VisitProfileActivity;
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

public class CustomRecycleAdapter extends RecyclerView.Adapter<CustomRecycleAdapter.ViewHolder> {
    ArrayList<Post> mList;
    LayoutInflater layoutInflater;
    Context context;

    public CustomRecycleAdapter(Context context, ArrayList<Post> mList){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = layoutInflater.inflate(R.layout.mainpage_cardview_satir,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.setData(mList.get(i));
    }

    @Override
    public int getItemCount() {
       return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView isim,aciklama;
        ImageView imageView;
        VideoView videoView;
        TextView like,commentText;
        View itemView;
        ToggleButton likeButton;
        Button yorumAc;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;

            cardView = itemView.findViewById(R.id.post);
            yorumAc = itemView.findViewById(R.id.yorumAc);
            isim = itemView.findViewById(R.id.user);
            aciklama = itemView.findViewById(R.id.aciklama);
            imageView = itemView.findViewById(R.id.image);
            videoView = itemView.findViewById(R.id.video);
            like = itemView.findViewById(R.id.like);
            commentText = itemView.findViewById(R.id.comment);
            likeButton = itemView.findViewById(R.id.begenButon);
            likeButton.setEnabled(false);
        }

        public void setData(final Post post) {
            isim.setText(post.getKişi());
            aciklama.setText(post.getDesc());
            like.setText(post.getLikeSayi());
            commentText.setText(post.getYorumSayi());

            setOnclicks(post);
            postIVTbas(post);
            isLiked(post,likeButton);
        }


        /****************POST IMAGE-VİDEO-TEXT BAS***************************************/
        public void postIVTbas(Post post){
            if(post.getType().equals("fotoğraf")){
                imageView.setVisibility(View.VISIBLE);
                Picasso.with(context)
                        .load(post.getContent())
                        .placeholder(context.getDrawable(R.drawable.placeholder))
                        .into(imageView);
            }else if(post.getType().equals("video")){
                videoView.setVisibility(View.VISIBLE);
                String videoPath = post.getContent();
                Uri uri = Uri.parse(videoPath);
                videoView.setVideoURI(uri);

                MediaController mediaController = new MediaController(itemView.getContext());
                videoView.setMediaController(mediaController);
            }
        }


        /*********************OnClickler*******************************/
        public void setOnclicks(final Post post){
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentSearch = new Intent(context, AyrPost.class);
                    Log.d("IDalal",post.getId());
                    intentSearch.putExtra("PostId",post.getId());
                    context.startActivity(intentSearch);
                }
            });
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
}
