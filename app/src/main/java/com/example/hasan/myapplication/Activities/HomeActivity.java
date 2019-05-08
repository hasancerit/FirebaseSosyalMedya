package com.example.hasan.myapplication.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hasan.myapplication.Adapters.AlanSecAdapter;
import com.example.hasan.myapplication.Adapters.CustomRecycleAdapter;
import com.example.hasan.myapplication.Controller.HomeFirebaseMethods;
import com.example.hasan.myapplication.Models.Filter;
import com.example.hasan.myapplication.Utils.NavigationHelper;
import com.example.hasan.myapplication.Models.Post;
import com.example.hasan.myapplication.R;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity {
    ProgressBar mProgressBar;
    Toolbar toolbar;
    BottomNavigationViewEx bottomNavigationView;
    RecyclerView recyclerView;
    CustomRecycleAdapter customRecycleAdapter;
    FloatingActionButton fab;
    HomeFirebaseMethods firebaseMethods;
    Context context = this;

    ArrayList<String> filtreAlan;
    ArrayList<String> mPoints;
    ArrayList<Post> basılacak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setup();

        firebaseMethods.alanCek(mPoints);
        firebaseMethods.arrayListGuncelle();

        recyclerView.setAdapter(customRecycleAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void setup(){
        //FİND İD
        recyclerView = findViewById(R.id.homeRecycle);
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView =(BottomNavigationViewEx) findViewById(R.id.bottomnav);
        fab = findViewById(R.id.fab);
        mProgressBar = findViewById(R.id.homeProgress);
        basılacak = new ArrayList<>();
        mPoints = new ArrayList<>();

        // NEW
        customRecycleAdapter = new CustomRecycleAdapter(this, basılacak);
        firebaseMethods = new HomeFirebaseMethods(this,mPoints,basılacak,customRecycleAdapter);
        filtreAlan = new ArrayList<>();

        //PROGRESSBAR
        mProgressBar.setIndeterminate(false);

        //TOOLBAR
        setSupportActionBar(toolbar);

        //NAV BAR
        NavigationHelper.setupBottomNavigation(bottomNavigationView,0);
        NavigationHelper.onClickBottomNavigation(bottomNavigationView,HomeActivity.this);

        //FAB
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ımageCrop();
            }
        });
    }

    public void ımageCrop(){
        ActivityCompat.requestPermissions(HomeActivity.this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},1);

        CropImage.activity()
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(HomeActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
            onActivityResultCrop(requestCode,resultCode,data);
    }

    public void onActivityResultCrop(int requestCode, int resultCode, @Nullable Intent data){
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        if (resultCode == RESULT_OK) {
            Uri resultUri = result.getUri();
            firebaseMethods.newPostDialog(resultUri);
        }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) { }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.toolbarFilterButton:
                filter();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void filter() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View newPostDialog = getLayoutInflater().inflate(R.layout.filter_dialog,null);

        final RecyclerView filterAlanSec = newPostDialog.findViewById(R.id.alanFilterList);
        final RecyclerView filterTürSec = newPostDialog.findViewById(R.id.türFilterList);

        final ArrayList<String> alanlar = new ArrayList<>();
        final ArrayList<String> türler = new ArrayList<>();

        for (int i = 0  ; i<mPoints.size() ; i++){
            alanlar.add(mPoints.get(i));
        }

        türler.add("fotoğraf");
        türler.add("video");

        final AlanSecAdapter alanSecAdapter = new AlanSecAdapter(this,alanlar);
        final AlanSecAdapter türSecAdapter = new AlanSecAdapter(this,türler);
        filterAlanSec.setAdapter(alanSecAdapter);
        filterTürSec.setAdapter(türSecAdapter);

        GridLayoutManager linearLayoutManager = new GridLayoutManager(this,2);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        GridLayoutManager linearLayoutManager2 = new GridLayoutManager(this,2);
        linearLayoutManager2.setOrientation(LinearLayout.VERTICAL);

        filterAlanSec.setLayoutManager(linearLayoutManager);
        filterTürSec.setLayoutManager(linearLayoutManager2);

        final EditText alan = newPostDialog.findViewById(R.id.filterAlan);

        Button filtrele = newPostDialog.findViewById(R.id.filtrele);
        Button filtreKaldir = newPostDialog.findViewById(R.id.filtreKaldır);
        alan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                alanBak(alan.getText().toString(),alanlar,alanSecAdapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mBuilder.setView(newPostDialog);
        final AlertDialog dialog = mBuilder.create();

        final ArrayList<String> istenenAlanlar = new ArrayList<>();
        final ArrayList<String> istenenTür = new ArrayList<>();

        filtreKaldir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseDatabase.getInstance().getReference().child("Users").child(mUid).child("filtreVarMi").setValue("h");
                FirebaseDatabase.getInstance().getReference().child("Users").child(mUid).child("filtre").removeValue();

                Intent intentHome = new Intent(context, HomeActivity.class);
                context.startActivity(intentHome);
            }
        });

        filtrele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(alanSecAdapter.points.size() == 0 || türSecAdapter.points.size() == 0) {
                    Toast.makeText(context,"Alan ve tür seçin.",Toast.LENGTH_LONG).show();
                }else {
                    istenenAlanlar.clear();
                    istenenTür.clear();

                    String mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    FirebaseDatabase.getInstance().getReference().child("Users").child(mUid).child("filtreVarMi").setValue("e");
                    FirebaseDatabase.getInstance().getReference().child("Users").child(mUid).child("filtre").removeValue();

                    for (int i = 0 ; i < alanSecAdapter.points.size();i++){
                        istenenAlanlar.add(alanSecAdapter.points.get(i));
                    }
                    for (int i = 0 ; i < türSecAdapter.points.size();i++){
                        istenenTür.add(türSecAdapter.points.get(i));
                    }
                    /**********************************FİLTRELERİ VERİTABANINA GÖNDER***********************************************/
                    DatabaseReference filterRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mUid).child("filtre");

                    DatabaseReference filterAlanRef = filterRef.child("alanlar");
                    for (int i = 0 ; i < istenenAlanlar.size() ; i++){
                        filterAlanRef.child("filterDal"+i).setValue(istenenAlanlar.get(i));
                    }

                    DatabaseReference filterTürRef = filterRef.child("türler");
                    for (int i = 0 ; i < istenenTür.size() ; i++){
                        filterTürRef.child("filterTür"+i).setValue(istenenTür.get(i));
                    }
                    Intent intentHome = new Intent(context, HomeActivity.class);
                    context.startActivity(intentHome);
                }
            }
        });
        dialog.show();
    }

    public void alanBak(String s,ArrayList<String> points,AlanSecAdapter adapter){
        ArrayList<String> yeniAlan = new ArrayList<>();
        String ks = s.toLowerCase();
        String bs = s.toUpperCase();

        for (int i = 0; i < mPoints.size(); i++) {
            if (mPoints.get(i).contains(ks) || mPoints.get(i).contains(bs)) {
                yeniAlan.add(mPoints.get(i));
            }
        }

        points.clear();
        for (int i = 0; i < yeniAlan.size(); i++) {
            points.add(yeniAlan.get(i));
        }

        adapter.notifyDataSetChanged();
    }


}
