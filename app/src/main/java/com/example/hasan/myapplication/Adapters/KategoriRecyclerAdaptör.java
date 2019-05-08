package com.example.hasan.myapplication.Adapters;

import android.app.AlertDialog;
import android.content.Context;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hasan.myapplication.Activities.SearchActivity;
import com.example.hasan.myapplication.Models.Kategori;
import com.example.hasan.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class KategoriRecyclerAdaptör extends RecyclerView.Adapter<KategoriRecyclerAdaptör.ViewHolder>{

    ArrayList<Kategori> mList;
    LayoutInflater layoutInflater;
    Context context;

    public KategoriRecyclerAdaptör(Context context, ArrayList<Kategori> mList){
        this.context =  context;
        layoutInflater = LayoutInflater.from(context);
        this.mList = mList;
    }

    @NonNull
    @Override
    public KategoriRecyclerAdaptör.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = layoutInflater.inflate(R.layout.kategori_satir,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull KategoriRecyclerAdaptör.ViewHolder viewHolder, int i) {
        viewHolder.setData(mList.get(i));
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView isim,sayi;
        ImageView imageView;
        View itemView;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            isim = itemView.findViewById(R.id.kategoriAdi);
            sayi = itemView.findViewById(R.id.kategoriSayi);
            imageView = itemView.findViewById(R.id.kategoriImage);
            cardView = itemView.findViewById(R.id.cardKat);
        }

        public void setData(final Kategori kategori) {
            sayi.setText(kategori.getSayi());
            isim.setText(kategori.getIsim());
            Picasso.with(context)
                    .load(kategori.getImage())
                    .placeholder(context.getDrawable(R.drawable.placeholder))
                    .into(imageView);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   dialogAc(view,kategori);
                }
            });
        }

        public void dialogAc(View v,Kategori kategori){
            final ArrayList<String> üstdallar = new ArrayList<>();
            final String üstDal = kategori.getIsim();
            final ArrayList<String> altdallar= new ArrayList<>();

            //VERİTABANINDAKİ ALTDALLARI ÇEKER
            DatabaseReference mReference = FirebaseDatabase.getInstance().getReference().child("Points").child(üstDal);
                mReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data:dataSnapshot.getChildren()){
                            if(!data.getKey().equals("özellikler")){
                                String altdal = data.child("isim").getValue().toString();
                                altdallar.add(altdal);
                                üstdallar.add(üstDal);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }});

            //VERİTABANINDAKİ ALTDALLARI ALIR VE EKRANA DİALOG OLARAK GETİRİR
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                View DallarDialog = layoutInflater.inflate(R.layout.alt_dal_dialog,null);
                RecyclerView recyclerView = DallarDialog.findViewById(R.id.altDalRecycler);

                DialogAdapter dialogAdapter = new DialogAdapter(context,altdallar,üstdallar);
                recyclerView.setAdapter(dialogAdapter);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);

                mBuilder.setView(DallarDialog);
                AlertDialog dialog = mBuilder.create();
            dialog.show();
        }
    }
}
