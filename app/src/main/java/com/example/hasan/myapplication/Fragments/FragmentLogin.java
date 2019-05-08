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
import com.example.hasan.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FragmentLogin extends Fragment {

    private EditText email,sifre;
    private Button giris;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_login,container,false);

        mAuth = FirebaseAuth.getInstance();
        email = v.findViewById(R.id.emailtext);
        sifre = v.findViewById(R.id.sifretext);
        giris = v.findViewById(R.id.butonGiris);

        giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                girisYap();
            }
        });

        return v;
    }

    private void girisYap(){
        String em = email.getText().toString();
        String sif = sifre.getText().toString();

        mAuth.signInWithEmailAndPassword(em,sif)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(getActivity().getApplicationContext(), HomeActivity.class));
                        }else {
                            // If sign in fails, display a message to the user.
                            Log.w("HATA", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
