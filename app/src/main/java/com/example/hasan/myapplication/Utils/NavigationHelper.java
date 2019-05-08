package com.example.hasan.myapplication.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.hasan.myapplication.Activities.HomeActivity;
import com.example.hasan.myapplication.Activities.MessagesActivity;
import com.example.hasan.myapplication.Activities.ProfileActivity;
import com.example.hasan.myapplication.Activities.SearchActivity;
import com.example.hasan.myapplication.R;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
public class NavigationHelper {
    static View[] views = new View[4];

    public static void setupBottomNavigation(BottomNavigationViewEx bottomNavigationViewEx,int position){
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableAnimation(
                false);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(position);
        menuItem.setChecked(true);
    }

    public static void onClickBottomNavigation(BottomNavigationViewEx bottomNavigationViewEx,final Context context){
        bottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navHouseButton:
                        Intent intentHome = new Intent(context, HomeActivity.class);
                        context.startActivity(intentHome);
                        break;
                    case R.id.navSearchButton:
                        Intent intentSearch = new Intent(context, SearchActivity.class);
                        context.startActivity(intentSearch);
                        break;
                    case R.id.navAnnButton:
                        Intent intentAnn = new Intent(context, MessagesActivity.class);
                        context.startActivity(intentAnn);
                        break;
                    case R.id.navProfileButton:
                        Intent intentProfile = new Intent(context, ProfileActivity.class);
                        context.startActivity(intentProfile);
                        break;
                }

                return false;
            }
        });
    }

}
