package com.example.hasan.myapplication.Utils;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hasan.myapplication.R;

public class ViewHolderSearcKisi extends RecyclerView.ViewHolder {
    ImageView ımageView;
    TextView isim,aciklama;

    public ViewHolderSearcKisi(@NonNull View itemView) {
        super(itemView);

        ımageView = itemView.findViewById(R.id.searchSatirImage);
        isim = itemView.findViewById(R.id.searchSatirIsim);
        aciklama = itemView.findViewById(R.id.searchSatirAciklama);
    }



}

