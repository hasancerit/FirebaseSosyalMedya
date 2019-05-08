package com.example.hasan.myapplication.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.hasan.myapplication.R;

import java.util.ArrayList;

public class AlanSecAdapter extends RecyclerView.Adapter<AlanSecAdapter.ViewHolder> {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<String> alan;
    public ArrayList<String> points;

    public AlanSecAdapter(Context context, ArrayList<String> alan){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.alan = alan;

        points = new ArrayList<>();
    }

    @NonNull
    @Override
    public AlanSecAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = layoutInflater.inflate(R.layout.new_post_alan_satir,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AlanSecAdapter.ViewHolder viewHolder, int i) {
        viewHolder.setData(alan.get(i));
    }

    @Override
    public int getItemCount() {
        return alan.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{
        CheckBox checkBox;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.alanİsaretle);
            textView = itemView.findViewById(R.id.alanAdi);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(checkBox.isChecked()){
                        points.add(textView.getText().toString());
                    }else{
                        points.remove(textView.getText().toString());
                    }
                }
            });
        }

        public void setData(String alanAdi){
            textView.setText(alanAdi);
            for(int i = 0 ; i < points.size() ; i++){
                if(textView.getText().equals(points.get(i))){
                    checkBox.setChecked(true);
                    break;
                }else{
                    checkBox.setChecked(false);
                }
            }
        }
    }
}
