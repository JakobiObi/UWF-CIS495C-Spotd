package com.example.jakobsuell.spotd;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import controllers.FirestoreController;
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

        // show all the user info in logcat
        user.dumpUserData();

        // attempt to write the info to the firestore
        FirestoreController.saveUser(FirebaseFirestore.getInstance(), user).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Log.d(TAG, "user write successful");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "user write failed: " + e);
            }
        });

    }

    /**
     * Attempt to read the currently logged in user's data.
     *
     * @param view This is required if this is called by a button.
     */
    public void readUser(View view) {

        Log.d(TAG, "attempting to read current user account...");

        // create user from the auth object
        User user = new User().fromAuth();

        FirestoreController.getUserByEmail(FirebaseFirestore.getInstance(), user.getEmailAddress()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    User user = documentSnapshot.toObject(User.class);
                    Log.d(TAG, "user read success:");
                    user.dumpUserData();
                } else {
                    // there is no user stored here.
                    Log.d(TAG, "no user found. probably a new account.");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "could not read user information. error: " + e);
            }
        });


    }

    public void saveTestImage(View view) {

        // TODO: Use a ContentProvider to find a test image to use with the ImageController
        // This will be done when I figure out how to use ContentProviders to find an image
        // on the device.

    }

}
