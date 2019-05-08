package com.example.hasan.myapplication.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.hasan.myapplication.Models.Comment;
import com.example.hasan.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentDialogAdapter extends RecyclerView.Adapter<CommentDialogAdapter.ViewHolder> {
    Context context;
    ArrayList<Comment> comments;
    LayoutInflater layoutInflater;

    public CommentDialogAdapter(Context context, ArrayList<Comment>comments){
        super();
        this.context = context;
        this.comments= comments;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = layoutInflater.inflate(R.layout.yorum_satir,null);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.setData(comments.get(i));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView yorum,isim;
        CircleImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            yorum = itemView.findViewById(R.id.yorumSatirYorum);
            isim = itemView.findViewById(R.id.yorumSatirIsim);
            imageView = itemView.findViewById(R.id.yorumSatirImage);
        }

        public void setData(Comment comment){
            Log.d("YORUM",comment.getComment());
            yorum.setText(comment.getComment());
            isim.setText(comment.getSendByIsim());
            Picasso.with(context)
                    .load(comment.getSendByImage())
                    .placeholder(context.getDrawable(R.drawable.placeholder))
                    .into(imageView);

            Log.d("yorum",comment.getComment());
        }
    }

}
