package com.example.hasan.myapplication.Utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hasan.myapplication.R;

public class ViewHolderSearchKat extends RecyclerView.ViewHolder {
    TextView textView;
    Button button;
    public ViewHolderSearchKat(@NonNull View itemView) {
        super(itemView);

        textView = itemView.findViewById(R.id.altDalText);
        button = itemView.findViewById(R.id.altDalTakip);
    }

}
