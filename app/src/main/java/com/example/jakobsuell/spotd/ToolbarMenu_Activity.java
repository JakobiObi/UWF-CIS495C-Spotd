package com.example.jakobsuell.spotd;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import controllers.LoginController;

public class ToolbarMenu_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar_menu_);
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

        TextView userName = (TextView) headerView.findViewById(R.id.textView_ToolbarUserName);
        userName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        TextView emailAccount = (TextView)headerView.findViewById(R.id.textView_ToolbarEmail);
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

        Intent intent = MainActivity.makeMainActivityIntent(ToolbarMenu_Activity.this);
        startActivity(intent);
        finish();
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
            case R.id.tool_home:
                Toast.makeText(ToolbarMenu_Activity.this, "Clicked 'Home'", Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "home clicked on nav menu");
//                fragment = new HomeFragment();
                break;
            case R.id.tool_profile:
                Toast.makeText(ToolbarMenu_Activity.this, "Clicked 'Profile'", Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "profile clicked on nav menu");
//                fragment = new MyProfileFragment();
                break;
            case R.id.tool_found:
                Toast.makeText(ToolbarMenu_Activity.this, "Clicked 'Found'", Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "found clicked on nav menu");
//                fragment = new FoundAPetFragment();
                break;
            case R.id.tool_lost:
                Toast.makeText(ToolbarMenu_Activity.this, "Clicked 'Lost'", Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "lost clicked on nav menu");
//                fragment = new LostAPetFragment();
                break;
            case R.id.tool_log:
                Toast.makeText(ToolbarMenu_Activity.this, "Signing you out...", Toast.LENGTH_SHORT).show();
//                LoginController.signOut(this).addOnSuccessListener(new OnSuccessListener() {
//                    @Override
//                    public void onSuccess(Object o) {
//                        // TODO:  Replace with a call to the NavigationController.
//                        loadLoginActivity();
//                    }
//                });
                break;
            case R.id.tool_quit:
                finish();
                System.exit(0);
        }

        if (fragment != null) {
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_toolbarMain, fragment);
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

    //Encapsulates ability to create itself
    public static Intent makeToolbarMenuActivityIntent(Context context) {
        return new Intent(context, ToolbarMenu_Activity.class);
    }
}
