package com.example.hasan.myapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.hasan.myapplication.Activities.ActivityGozAt;
import com.example.hasan.myapplication.Models.User;
import com.example.hasan.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DialogAdapter extends RecyclerView.Adapter<DialogAdapter.ViewHolder> {
    Context context;
    ArrayList<String> altdal;
    LayoutInflater layoutInflater;
    ArrayList<String> üstDal;

    public DialogAdapter(Context context,ArrayList<String>altdal,ArrayList<String> üstDal){
        super();
        this.context = context;
        this.altdal = altdal;
        this.üstDal = üstDal;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = layoutInflater.inflate(R.layout.alt_dal_satir,null);
        ViewHolder viewHolder = new ViewHolder(v,i);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.setData(altdal.get(i));
    }

    @Override
    public int getItemCount() {
        return altdal.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ToggleButton toggleButton;

        public ViewHolder(@NonNull View itemView, final int i) {
            super(itemView);
            textView = itemView.findViewById(R.id.altDalText);
            toggleButton = itemView.findViewById(R.id.altDalTakip);

            toggleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toggleOnClick(i);
                }
            });
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textOnClick(textView.getText().toString());
                }
            });
        }

        public void setData(String isim){
            textView.setText(isim);
            String mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            //TAKİP EDİLİYORSA TOGGLE AKTİF
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mUid).child("listdallar");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot data:dataSnapshot.getChildren()){
                        if(data.getValue().equals(textView.getText())){
                            toggleButton.setChecked(true);
                            break;
                        }
                        toggleButton.setChecked(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }});
        }

        /*********************TEXT VİEW ONCLİCK********************************/
        public void textOnClick(String dal){
            Intent ıntent = new Intent(context, ActivityGozAt.class);
            ıntent.putExtra("istenen",dal);
            context.startActivity(ıntent);
        }

        /**************************TAKİP BUTON ONCLİCK**********************/
        public void toggleOnClick(int i){
            final String basılan = textView.getText().toString();
            String mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            if (toggleButton.isChecked()){
                //EKLER
                FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(mUid).child("listdallar").push().setValue(basılan);

                //SAYİ ARTIRIR
                final DatabaseReference mÜstDal = FirebaseDatabase.getInstance().getReference().child("Points")
                        .child(üstDal.get(i)).child("özellikler").child("sayi");

                mÜstDal.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     int sayi = Integer.parseInt(dataSnapshot.getValue().toString());
                     sayi++;
                     mÜstDal.setValue(""+sayi);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}});

                //PROFİLDEKİ TAKİP SAYISI ARTAR
                final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mUid).child("takip");
                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int takip = Integer.parseInt(dataSnapshot.getValue().toString());
                        takip++;
                        mRef.setValue(""+takip);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }});

            }else{
                //SİLER
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(mUid).child("listdallar");

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data:dataSnapshot.getChildren()){
                            if(basılan.equals(data.getValue().toString()))
                                data.getRef().removeValue();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }});

                //SAYİ AZALTIR
                final DatabaseReference mÜstDal = FirebaseDatabase.getInstance().getReference().child("Points")
                        .child(üstDal.get(i)).child("özellikler").child("sayi");

                mÜstDal.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int sayi = Integer.parseInt(dataSnapshot.getValue().toString());
                        sayi--;
                        mÜstDal.setValue(""+sayi);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}});

                //PROFİLDEKİ TAKİP SAYISI AZALIR
                final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mUid).child("takip");
                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int takip = Integer.parseInt(dataSnapshot.getValue().toString());
                        takip--;
                        mRef.setValue(""+takip);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }});

            }
        }
    }

}
