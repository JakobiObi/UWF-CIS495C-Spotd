package com.example.jakobsuell.spotd;

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
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "entered MainActivity");

        // make sure user is logged in
        LoginController.enforceSignIn(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

        setHeaderViewOnNavDrawer();
    }

    public void setHeaderViewOnNavDrawer() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        TextView userName = (TextView) headerView.findViewById(R.id.textView_NavUserName);
        userName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        TextView emailAccount = (TextView)headerView.findViewById(R.id.textView_NavEmail);
        emailAccount.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displaySelectedFragment(int id) {
        android.support.v4.app.Fragment fragment = null;

        switch (id) {
            case R.id.home:
                Log.d(TAG, "home clicked on nav menu");
                fragment = new HomeFragment();
                break;
            case R.id.profile:
                Log.d(TAG, "profile clicked on nav menu");
                fragment = new MyProfileFragment();
                break;
            case R.id.found:
                Log.d(TAG, "found clicked on nav menu");
                fragment = new FoundAPetFragment();
                break;
            case R.id.lost:
                Log.d(TAG, "lost clicked on nav menu");
                fragment = new LostAPetFragment();
                break;
            case R.id.log:
                Toast.makeText(MainActivity.this, "Signing you out...", Toast.LENGTH_SHORT).show();
                LoginController.signOut(this).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        // TODO:  Replace with a call to the NavigationController.
                        loadLoginActivity();
                    }
                });
                break;
            case R.id.quit:
                finish();
                System.exit(0);
        }

        if (fragment != null) {
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        displaySelectedFragment(id);

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


    /**
     * This method is invoked when the user clicks the Settings menu option.
     *
     * @param menuItem
     */
    public void settingsClicked(MenuItem menuItem) {

        Toast.makeText(MainActivity.this, "Clicked 'Settings'.", Toast.LENGTH_SHORT).show();

        Intent intent = ToolbarMenu_Activity.makeToolbarMenuActivityIntent(MainActivity.this);
        startActivity(intent);
        finish();
    }

    /**
     * This method is invoked when the user clicks the About menu option.
     *
     * @param menuItem
     */
    public void aboutClicked(MenuItem menuItem) {

        Toast.makeText(MainActivity.this, "Clicked 'About'.", Toast.LENGTH_SHORT).show();

        Intent intent = ToolbarMenu_Activity.makeToolbarMenuActivityIntent(MainActivity.this);
        startActivity(intent);
        finish();
    }

    /**
     * This method is invoked when the user clicks the Help menu option.
     *
     * @param menuItem
     */
    public void helpClicked(MenuItem menuItem) {

        Toast.makeText(MainActivity.this, "Clicked 'Help'.", Toast.LENGTH_SHORT).show();

        Intent intent = ToolbarMenu_Activity.makeToolbarMenuActivityIntent(MainActivity.this);
        startActivity(intent);
        finish();
    }

    //Encapsulates ability to create itself
    public static Intent makeMainActivityIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    /*********************************************************
     * Helper Functions
     * *******************************************************
     */

    private void loadLoginActivity() {

        // launch login activity
        Intent nextActivity = new Intent(this, LoginActivity.class);

        // don't allow user to return to login screen
        nextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        this.startActivity(nextActivity);
    }

}
