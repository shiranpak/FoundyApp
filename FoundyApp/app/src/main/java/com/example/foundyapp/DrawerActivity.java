package com.example.foundyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.example.foundyapp.model.Model;
import com.example.foundyapp.model.ModelFirebase;
import com.example.foundyapp.model.UserSession;
import com.example.foundyapp.ui.home.HomeFragment;
import com.example.foundyapp.ui.home.HomeFragmentDirections;
import com.example.foundyapp.ui.login.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foundyapp.databinding.ActivityDrawerBinding;

public class DrawerActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityDrawerBinding binding;
    private NavController navController;
    private CurvedBottomNavigationView curvedBottomNavigationView;
    private FloatingActionButton addPostBtn;
    UserSession session;
    ModelFirebase db;
    private HomeFragment home;
    TextView name_drawer,email_drawer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserSession(getApplicationContext());
        db = new ModelFirebase();
        binding = ActivityDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarDrawer.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.MyDetailsFragment)
                .setOpenableLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_drawer);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        //create login session
        if (session.checkLogin())
        {
            finish();
        }

        navigationView.getMenu().findItem(R.id.Logout).setOnMenuItemClickListener(item -> {
            Model.instance.signOutFirebase(()->{});
            session.logoutUser();
            finish();
            return true;
        });

        //add users information on drawer
        View header=navigationView.getHeaderView(0);
        name_drawer=(TextView) header.findViewById(R.id.nameDrawer);
        email_drawer=(TextView) header.findViewById(R.id.emailText);
        /*Model.instance.getUser(new Model.GetUserByMail() {
            @Override
            public void onComplete(User user) {
                name_drawer.setText(user.getFullName());
                email_drawer.setText(user.getEmail());
            }
        });*/
        home = ((HomeFragment) getForegroundFragment());

        curvedBottomNavigationView = (CurvedBottomNavigationView)findViewById(R.id.bottom_navigation);
        curvedBottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_nav_losts_btn:
                    home.setType(false);
                    return true;

                case R.id.bottom_nav_findings_btn:
                    home.setType(true);
                    return true;
            }
            return false;
        });

    }
    public Fragment getForegroundFragment(){
        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_drawer);
        return navHostFragment == null ? null : navHostFragment.getChildFragmentManager().getFragments().get(0);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
//        search_btn = menu.findItem(R.id.search_menu_btn);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(!super.onOptionsItemSelected(item)) {
            switch (item.getItemId()) {

                case R.id.search_by_place_btn:
                    navController.navigate(R.id.searchByPlaceFragment);
                    return true;
                case R.id.advanced_search_btn:
                    navController.navigate(R.id.advancedSearchFragment);
                    return true;
                default:
                    NavigationUI.onNavDestinationSelected(item,navController);
            }
        }
        else{
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_drawer);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void onClickAdd(View view) {
        navController.navigate(HomeFragmentDirections.actionNavHomeToAddPostTypeSelectFragment());
    }
}