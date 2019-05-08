package com.example.hasan.myapplication.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import com.example.hasan.myapplication.Models.Post;
import com.example.hasan.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GozAtAdapter extends RecyclerView.Adapter<GozAtAdapter.ViewHolder>{
    ArrayList<Post> mList;
    LayoutInflater layoutInflater;
    Context context;

    public GozAtAdapter(Context context, ArrayList<Post> mList){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.mList = mList;
    }

    @NonNull
    @Override
    public GozAtAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = layoutInflater.inflate(R.layout.gozat_cardview_satir,viewGroup,false);
        GozAtAdapter.ViewHolder viewHolder = new GozAtAdapter.ViewHolder(v);
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
        View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;

            isim = itemView.findViewById(R.id.userGozat);
            aciklama = itemView.findViewById(R.id.aciklamaGozAt);
            imageView = itemView.findViewById(R.id.imageGozAt);
            videoView = itemView.findViewById(R.id.videoGozAt);
        }

        public void setData(final Post post) {
            isim.setText(post.getKişi());
            aciklama.setText(post.getDesc());

            postIVTbas(post);
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
    }
}
