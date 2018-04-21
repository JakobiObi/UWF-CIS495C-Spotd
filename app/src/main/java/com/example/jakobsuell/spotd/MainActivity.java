package com.example.jakobsuell.spotd;

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
import com.squareup.picasso.Picasso;

import java.util.List;

import controllers.LoginController;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final int INITIAL_BACKSTACK_SIZE = 1;
    private final int NORMAL_BACKSTACK_SIZE = 2;
    private final String TAG = "MainActivity";
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private Globals globals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "entered MainActivity");

        LoginController.enforceSignIn(this);

        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        setSupportActionBar(toolbar);

        globals = (Globals)this.getApplication();
        if (!globals.isPicassoSingletonAssigned) {
            createPicassoSingleton();
            globals.isPicassoSingletonAssigned = true;
        }

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

        displayFragment(new HomeFragment());
    }

    public void createPicassoSingleton() {
        Picasso picassoInstance = new Picasso.Builder(this.getApplicationContext())
                .addRequestHandler(new FirebaseRequestHandler())
                .build();
        Picasso.setSingletonInstance(picassoInstance);
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
            userName.setText("");
            emailAccount.setText("");
        }
        putProfilePictureOnNavigationMenu(profileImageView);
    }

    private void putProfilePictureOnNavigationMenu(ImageView imageView) {
        Picasso.get()
                .load(LoginController.getUserPictureUri(FirebaseAuth.getInstance()))
                .placeholder(R.drawable.profile_placeholder)
                .into(imageView);
    }

    @Override
    public void onBackPressed() {
        // TODO: Investigate if this can be modified to not have the apparent workaround from Glide
        /*
            Specifically, the double popBackStack when there is only one fragment on the backstack
            is probably no longer necessary. This can eliminate the if-else also.
         */
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            int backStackSize = fragmentManager.getBackStackEntryCount();
            if (backStackSize >= NORMAL_BACKSTACK_SIZE) {
                fragmentManager.popBackStack();
            } else if(backStackSize == INITIAL_BACKSTACK_SIZE) {
                fragmentManager.popBackStack();
                fragmentManager.popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
        Log.d(TAG, "loading fragment " + fragment.toString() + " to " + R.id.fragment_container);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (fragmentManager.getFragments().size() < 1) {
            fragmentTransaction.add(R.id.fragment_container, fragment);
        } else {
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
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
            case R.id.mypets:
                Log.d(TAG, "found clicked on nav menu");
                Log.d(TAG, "Nav Menus:  btnSMyPets clicked");
                // TODO: change this so that it sends the appropriate query results
                MockDataGenerator mockDataGenerator = MockDataGenerator.make();
                ShowPetsFragment listFragment = ShowPetsFragment.newInstance(mockDataGenerator.pets, ShowPetsFragment.PetListOptions.AddButtonOnly,null, "My Pets" );
                displayFragment(listFragment);
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
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void actionBarClicked(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_bar_menu_about:
                Log.d(TAG, "action_bar_menu_about clicked");
                displayFragment(new AboutFragment());
                break;
            case R.id.action_bar_menu_help:
                Log.d(TAG, "action_bar_menu_help clicked");
                break;
        }
    }

    private void signOut() {
        Toast.makeText(MainActivity.this, "Signing you out...", Toast.LENGTH_SHORT).show();
        LoginController.signOut(this).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                loadLoginActivity();
            }
        });
    }

    public void loadLoginActivity() {
        Intent nextActivity = new Intent(this, LoginActivity.class);
        nextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(nextActivity);
    }


    private void showFragmentStack(FragmentManager fragmentManager) {

        Log.d(TAG, "Fragment Stacks:");
        List<Fragment> currentStack = fragmentManager.getFragments();

        Log.d(TAG, "Current: (size: " + currentStack.size() + ")");
        for (int i=0; i < currentStack.size(); i++) {
            Log.d(TAG, "(" + i + "): " +
                    currentStack.get(i).toString());
        }

        // have to iterate through backstack, can't pull as list
        int backStackSize = fragmentManager.getBackStackEntryCount();
        Log.d(TAG, "Backstack: (size: " + backStackSize + ")");
        for (int i=0; i < backStackSize; i++) {
            Log.d(TAG, "(" + i + "): " +
                    fragmentManager.getBackStackEntryAt(i).toString());
        }
    }

}
