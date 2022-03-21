package com.example.foundyapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.example.foundyapp.model.ModelFirebase;
import com.example.foundyapp.model.UserSession;
import com.example.foundyapp.ui.home.HomeFragment;
import com.example.foundyapp.ui.home.HomeFragmentDirections;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
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
        db=new ModelFirebase();
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
        session.checkLogin();
        //logout from app TOdo:create listener that checks if user looged in
        db.checkIfLoggedIn();
        navigationView.getMenu().findItem(R.id.Logout).setOnMenuItemClickListener(item -> {
            session.logoutUser();

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


        curvedBottomNavigationView = (CurvedBottomNavigationView)findViewById(R.id.bottom_navigation);
        curvedBottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_nav_losts_btn:
//                    getSupportFragmentManager().beginTransaction().replace(R.id.container, firstFragment).commit();
                    return true;

                case R.id.bottom_nav_findings_btn:
//                    getSupportFragmentManager().beginTransaction().replace(R.id.container, thirdFragment).commit();
                    return true;
            }
            return false;
        });

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
//                    search_btn.setVisible(false);
                    return true;
                case R.id.advanced_search_btn:
                    navController.navigate(R.id.advancedSearchFragment);
//                    search_btn.setVisible(false);
                    return true;
                default:
//                    search_btn.setVisible(true);
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