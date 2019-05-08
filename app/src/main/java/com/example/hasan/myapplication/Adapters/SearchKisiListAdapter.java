package com.example.hasan.myapplication.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hasan.myapplication.Models.User;
import com.example.hasan.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchKisiListAdapter extends RecyclerView.Adapter<SearchKisiListAdapter.ViewHolder> {
    LayoutInflater layoutInflater;
    Context context;
    ArrayList<User> users;

    public SearchKisiListAdapter(Context context, ArrayList<User> users){
        super();
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = layoutInflater.inflate(R.layout.search_kisi_satir,viewGroup,false);
        SearchKisiListAdapter.ViewHolder viewHolder = new SearchKisiListAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.setData(users.get(i));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView isim,aciklama;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.searchSatirImage);
            isim = itemView.findViewById(R.id.searchSatirIsim);
            aciklama = itemView.findViewById(R.id.searchSatirAciklama);
        }

        public void setData(User u){
            isim.setText(u.getName());
            aciklama.setText(u.getDesc());

            imageView.setVisibility(View.VISIBLE);
            Picasso.with(context)
                    .load(u.getImageUri())
                    .placeholder(context.getDrawable(R.drawable.placeholder))
                    .into(imageView);
        }
    }
}
