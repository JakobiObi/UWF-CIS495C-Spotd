package com.example.jakobsuell.spotd;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;

import controllers.LoginController;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String TAG = "MainActivity";


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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setupLostMyPetButton();
        setupFoundAPetButton();
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
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            Toast.makeText(MainActivity.this, "Clicked 'Home'.\nNo Action tied to this button.", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.profile) {
            Toast.makeText(MainActivity.this, "Clicked 'My Profile'.", Toast.LENGTH_SHORT).show();
            loadMyProfileActivity();
        } else if (id == R.id.found) {
            Toast.makeText(MainActivity.this, "Clicked 'Report Found'.", Toast.LENGTH_SHORT).show();
            loadFoundPetActivity();
        } else if (id == R.id.lost) {
            Toast.makeText(MainActivity.this, "Clicked 'Report Lost'.", Toast.LENGTH_SHORT).show();
            loadReportLostAnimalActivity();
        } else if (id == R.id.log) {
            Toast.makeText(MainActivity.this, "Signing you out...", Toast.LENGTH_SHORT).show();
            LoginController.signOut(this).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    // TODO:  Replace with a call to the NavigationController.
                    loadLoginActivity();
                }
            });


        } else if (id == R.id.quit) {
            Toast.makeText(MainActivity.this, "Clicked 'Quit'.", Toast.LENGTH_SHORT).show();
            finish();
            System.exit(0);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Do something when "Lost My Pet" button is clicked
    private void setupLostMyPetButton() {
        Button btn = (Button) findViewById(R.id.lostPetButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Clicked 'Lost My Pet'.", Toast.LENGTH_SHORT).show();

                //Launch lost_my_pet activitiy
//                Intent intent = new Intent(StartMenu.this, LostMyPetActivity.class);
                Intent intent = LostMyPetActivity.makeLostMyPetIntent(MainActivity.this);
                startActivity(intent);
            }
        });

    }

    //Do something when "Found a Pet" button is clicked
    private void setupFoundAPetButton() {
        Button btn = (Button) findViewById(R.id.foundPetButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Clicked 'Found a Pet'.", Toast.LENGTH_SHORT).show();

                //Launch found_a_pet activitiy
//                Intent intent = new Intent(StartMenu.this, FoundAPet.class);
//                Intent intent = FoundAPet.makeFoundAPetIntent(MainActivity.this);
//                startActivity(intent);
                loadFoundPetActivity();
            }
        });

    }

//    /**
//     * This method is invoked when the user clicks the My Pets menu option.
//     * @param menuItem
//     */
//    public void myPetsClicked(MenuItem menuItem) {
//
//        Toast.makeText(MainActivity.this, "Clicked 'My Pets'.", Toast.LENGTH_SHORT).show();
//
//        Intent intent = MyPets_Activity.makeMyPetsIntent(MainActivity.this);
//        startActivity(intent);
//    }

    /**
     * This method is invoked when the user clicks the Settings menu option.
     * @param menuItem
     */
    public void settingsClicked(MenuItem menuItem) {

        Toast.makeText(MainActivity.this, "Clicked 'Settings'.", Toast.LENGTH_SHORT).show();

        Intent intent = Settings_Activity.makeSettingsActivityIntent(MainActivity.this);
        startActivity(intent);
    }

    /**
     * This method is invoked when the user clicks the About menu option.
     * @param menuItem
     */
    public void aboutClicked(MenuItem menuItem) {

        Toast.makeText(MainActivity.this, "Clicked 'About'.", Toast.LENGTH_SHORT).show();

        Intent intent = About_Activity.makeAboutActivityIntent(MainActivity.this);
        startActivity(intent);
    }

    /**
     * This method is invoked when the user clicks the Help menu option.
     * @param menuItem
     */
    public void helpClicked(MenuItem menuItem) {

        Toast.makeText(MainActivity.this, "Clicked 'Help'.", Toast.LENGTH_SHORT).show();

        Intent intent = Help_Activity.makeHelpActivityIntent(MainActivity.this);
        startActivity(intent);
    }


    /*********************************************************
     * Helper Functions
     * *******************************************************
     */

    private void loadFoundPetActivity() {

        Intent intent = FoundAPet.makeFoundAPetIntent(MainActivity.this);
        startActivity(intent);
    }

    private void loadMyProfileActivity() {

        Intent intent = MyProfile_Activity.makeMyProfileIntent(MainActivity.this);
        startActivity(intent);
    }

    private void loadReportLostAnimalActivity() {

        Intent intent = ReportLostAnimal_Activity.makeReportLostAnimalActivityIntent(MainActivity.this);
        startActivity(intent);
    }

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
