package com.example.jakobsuell.spotd;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import controllers.LoginController;
import models.User;

/**
 * This activity is merely a placeholder for the Homescreen/Main menu UI to allow for
 * triggering of code by the developer to perform debugging.
 */

public class DebugActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private String TAG = "DebugActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        auth = FirebaseAuth.getInstance();

        Log.d(TAG, "entering");

        LoginController.enforceSignIn(this);
        logUserInfo();

    }


    /**
     * pull user information from authentication object and log it
     */
    private void logUserInfo() {

        if (auth.getCurrentUser() != null) {
            String displayName;
            try {
                displayName = auth.getCurrentUser().getDisplayName();
            } catch (NullPointerException e) {
                displayName = "null";
            }
            Log.d(TAG, "username: " + displayName);
            Log.d(TAG, "user id: " + auth.getCurrentUser().getUid());
            Log.d(TAG, "email: " + auth.getCurrentUser().getEmail());
        }
    }


    /***
     * Sign the current user out of the app, using the built-in FirebaseUI
     */
    public void signOut(View view) {

        Log.d(TAG, "Signing user out");
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "User is now signed out.");
                        LoginController.redirectToLoginActivity(getApplication().getApplicationContext());
                        finish(); // allow this callback to kill itself after starting activity
                    }
                });
    }


    public void createUser(View view) {

        // create user from the auth object
        User user = new User().fromAuth();

        user.dumpUserData();



    }

    public void saveTestImage(View view) {

        // TODO: Use a ContentProvider to find a test image to use with the ImageController
        // This will be done when I figure out how to use ContentProviders to find an image
        // on the device.

    }

}
