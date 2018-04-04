package com.example.jakobsuell.spotd;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

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
        displayFragment(R.id.fragment_container, new HomeFragment());

    }

    public void setHeaderViewOnNavDrawer() {

        View headerView = navigationView.getHeaderView(0);
        TextView userName = headerView.findViewById(R.id.textView_NavUserName);
        TextView emailAccount = headerView.findViewById(R.id.textView_NavEmail);

        try {
            userName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            emailAccount.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        } catch (NullPointerException ex) {
            // if we can't set them, just hide them
            userName.setText("");
            emailAccount.setText("");
        }

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu_selector, menu);
        return true;
    }

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }*/

    /**
     * Show a fragment.
     * Loads the specified fragment into the specified container.  Tries to be smart about using
     * the correct transaction call depending on whether this is an initial call or not.
     *
     * @param containerID
     * @param fragment
     */
    private void displayFragment(int containerID, Fragment fragment) {

        FragmentManager fragmentManager = getFragmentManager();

        Log.d(TAG, "loading fragment " + fragment.toString() + " to " + containerID);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // check if there is already a fragment
        if (fragmentManager.getFragments().size() > 0) {
            // use replace to remove previous fragment
            Log.d(TAG, "replacing current fragment");
            fragmentTransaction.replace(containerID, fragment);
        } else {
            Log.d(TAG, "adding initial fragment");
            fragmentTransaction.add(containerID, fragment);
        }

        fragmentTransaction.commit();

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
                Log.d(TAG, "home clicked on nav menu");
                displayFragment(R.id.fragment_container, new HomeFragment());
                break;
            case R.id.profile:
                Log.d(TAG, "profile clicked on nav menu");
                displayFragment(R.id.fragment_container, new MyProfileFragment());
                break;
            case R.id.found:
                Log.d(TAG, "found clicked on nav menu");
                displayFragment(R.id.fragment_container, new FoundAPetFragment());
                break;
            case R.id.lost:
                Log.d(TAG, "lost clicked on nav menu");
                displayFragment(R.id.fragment_container, new LostAPetFragment());
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

    // TODO: These methods need to be moved into the Home Fragment

   /* //Do something when "Lost My Pet" button is clicked
    private void setupLostMyPetButton() {
        Button btn = (Button) findViewById(R.id.lostPetButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.Fragment fragment = null;
                fragment = new LostAPetFragment();

                if (fragment != null) {
                    android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_main, fragment);
                    ft.commit();
                }
            }
        });
    }

    //Do something when "Found a Pet" button is clicked
    private void setupFoundAPetButton() {
        Button btn = (Button) findViewById(R.id.foundPetButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.Fragment fragment = null;
                fragment = new FoundAPetFragment();

                if (fragment != null) {
                    android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_main, fragment);
                    ft.commit();
                }
            }
        });
    }*/


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
