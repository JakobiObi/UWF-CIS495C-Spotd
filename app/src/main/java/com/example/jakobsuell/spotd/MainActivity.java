package com.example.jakobsuell.spotd;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import controllers.LoginController;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String TAG = "MainActivity";
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "entered MainActivity");

        // make sure user is logged in
        LoginController.enforceSignIn(this);

        // find view instances.
        /*
            Doing this here and then using the private instance means you only have to find the
            view from the id once.
        */

        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // create toolbar
        setSupportActionBar(toolbar);

        // create and setup navigation drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        setHeaderViewOnNavDrawer();

        // ensure navigation drawer gets the click event from the mouse
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                navigationView.bringToFront();
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });

        // initial load of Home Fragment
        displayFragment(new HomeFragment());

    }

    public void setHeaderViewOnNavDrawer() {

        View headerView = navigationView.getHeaderView(0);
        TextView userName = headerView.findViewById(R.id.textView_NavUserName);
        TextView emailAccount = headerView.findViewById(R.id.textView_NavEmail);
        ImageView profileImageView = headerView.findViewById(R.id.image_home_fragment_profile_photo);

        try {
            userName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            emailAccount.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        } catch (NullPointerException ex) {
            // if we can't set them, just hide them
            userName.setText("");
            emailAccount.setText("");
        }

        // load profile image
        GlideApp.with(this)
                .load(LoginController.getUserPictureUri(FirebaseAuth.getInstance()))
                .placeholder(R.drawable.profile_placeholder)
                .into(profileImageView);
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "back button pressed");

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            Log.d(TAG, "nav drawer is open, closing");
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Log.d(TAG, "nav drawer is not open");
            FragmentManager fm = getSupportFragmentManager();
            Log.d(TAG,fm.getBackStackEntryCount() + " fragments on stack");
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack();
            } else {
                return;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu_selector, menu);
        return true;
    }


    /**
     * Show a fragment.
     * Loads the specified fragment into the specified container.  Tries to be smart about using
     * the correct transaction call depending on whether this is an initial call or not.
     *
     * @param fragment The fragment to display.
     */
    public void displayFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();

        Log.d(TAG, "loading fragment " + fragment.toString() + " to " + R.id.fragment_container);
        Log.d(TAG,fragmentManager.getBackStackEntryCount() + " fragments already on stack");

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // check if there is already a fragment
        if (fragmentManager.getFragments().size() > 0) {
            // use replace to remove previous fragment
            Log.d(TAG, "replacing current fragment");
            fragmentTransaction.replace(R.id.fragment_container, fragment);
        } else {
            Log.d(TAG, "adding initial fragment");
            fragmentTransaction.add(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.commit();
        Log.d(TAG,fragmentManager.getBackStackEntryCount() + " fragments on stack afterwards");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
                Log.d(TAG, "home clicked on nav menu");
                displayFragment(new HomeFragment());
                break;
            case R.id.profile:
                Log.d(TAG, "profile clicked on nav menu");
                displayFragment(new ProfileFragment());
                break;
            case R.id.found:
                Log.d(TAG, "found clicked on nav menu");
                displayFragment(new FoundAPetFragment());
                break;
            case R.id.lost:
                Log.d(TAG, "lost clicked on nav menu");
                displayFragment(new LostAPetFragment());
                break;
            case R.id.log:
                signOut();
                break;
            case R.id.quit:
                finish();
                System.exit(0);
        }

        // close the drawer, we don't live in a barn!
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // TODO: Combine these into one method with a switch.

    public void actionBarClicked(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.action_bar_menu_about:
                Log.d(TAG, "action_bar_menu_about clicked");
                break;
            case R.id.action_bar_menu_help:
                Log.d(TAG, "action_bar_menu_help clicked");
                break;
            case R.id.action_bar_menu_settings:
                Log.d(TAG, "action_bar_menu_settings clicked");
                break;
        }

    }

    //Encapsulates ability to create itself
    public static Intent makeMainActivityIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    /*********************************************************
     * Helper Functions
     * *******************************************************
     */

    private void signOut() {

        Toast.makeText(MainActivity.this, "Signing you out...", Toast.LENGTH_SHORT).show();
        LoginController.signOut(this).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                // TODO:  Replace with a call to the NavigationController.
                loadLoginActivity();
            }
        });
    }

    public void loadLoginActivity() {

        // launch login activity
        Intent nextActivity = new Intent(this, LoginActivity.class);

        // don't allow user to return to login screen
        nextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        this.startActivity(nextActivity);
    }


}
