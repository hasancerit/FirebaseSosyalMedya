package com.example.hasan.myapplication.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.hasan.myapplication.Utils.NavigationHelper;
import com.example.hasan.myapplication.R;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
public class MessagesActivity extends AppCompatActivity {
    Toolbar toolbar;
    BottomNavigationViewEx bottomNavigationView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        setup();
    }


    public void setup(){
        toolbar = findViewById(R.id.toolbarMessage);
        bottomNavigationView =(BottomNavigationViewEx) findViewById(R.id.bottomnav);


        //TOOLBAR
        setSupportActionBar(toolbar);

        //NAV BAR
        NavigationHelper.setupBottomNavigation(bottomNavigationView,2);
        NavigationHelper.onClickBottomNavigation(bottomNavigationView,MessagesActivity.this);
    }

}
