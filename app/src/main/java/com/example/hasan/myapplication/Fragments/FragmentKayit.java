package com.example.hasan.myapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hasan.myapplication.Activities.HomeActivity;
import com.example.hasan.myapplication.Models.User;
import com.example.hasan.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FragmentKayit extends Fragment {
    private Button giris;
    private EditText email,sifre,isim,soyisim;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_kayit,container, false);

        giris = v.findViewById(R.id.kayıtOnayButon);
        email = v.findViewById(R.id.emailkayit);
        sifre = v.findViewById(R.id.sifrekayit);
        isim = v.findViewById(R.id.kayitİsim);
        soyisim = v.findViewById(R.id.kayitSoyisim);

        giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth();
            }
        });

        return v;
    }

    private void auth(){
        String mail = email.getText().toString();
        String sif = sifre.getText().toString();
        KayıtOl(mail,sif);
    }

    private void KayıtOl(String mail, String sifre) {
        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(mail,sifre)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            bilgiYolla();
                            startActivity(new Intent(getActivity().getApplicationContext(), HomeActivity.class));
                        }else{
                            Log.w("HATA", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void bilgiYolla(){
        User mUser = new User();
        mUser.setName(isim.getText().toString()+" "+soyisim.getText().toString());
        mUser.setDesc("");
        mUser.setEmail(email.getText().toString());
        mUser.setPost("0");
        mUser.setTakip("0");
        mUser.setTakipci("0");
        mUser.setFiltreVarMi("h");
        mUser.setImageUri("https://cdn-images-1.medium.com/max/1200/1*MccriYX-ciBniUzRKAUsAw.png");

        FirebaseAuth firebaseAuthıd = FirebaseAuth.getInstance();
        String mUid = firebaseAuthıd.getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(mUid);

        databaseReference.setValue(mUser);
    }
}
