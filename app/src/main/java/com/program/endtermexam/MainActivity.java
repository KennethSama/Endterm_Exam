package com.program.endtermexam;

import static android.view.Gravity.LEFT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitializeValues();

//        ReplaceFragment(new MessageFragment());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void InitializeValues(){
        drawerLayout = findViewById(R.id.drawerLayout_main);
        navigationView = findViewById(R.id.navigationView_main);
        toolbar = findViewById(R.id.actionbar_pacefy);

        setSupportActionBar(toolbar);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.menu_Open, R.string.menu_Close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(item -> {

            switch(item.getItemId()){
                case R.id.nav_dashboard:
                    Log.d("MENU_NAME", "DASHBOARD");
                    drawerLayout.closeDrawer(GravityCompat.END);
                    break;
                case R.id.nav_profile:
                    Log.d("MENU_NAME", "PROFILE");
                    drawerLayout.closeDrawer(GravityCompat.END);
                    break;
                case R.id.nav_settings:
                    Log.d("MENU_NAME", "SETTINGS");
                    drawerLayout.closeDrawer(GravityCompat.END);
                    break;
                case R.id.nav_help:
                    Log.d("MENU_NAME", "HELP");
                    drawerLayout.closeDrawer(GravityCompat.END);
                    break;
                case R.id.nav_logout:
                    Log.d("MENU_NAME", "LOGOUT");
                    drawerLayout.closeDrawer(GravityCompat.END);
                    break;
            }
            return true;
        });
    }
    private void ReplaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout_main, fragment);
        fragmentTransaction.commit();
    }
}