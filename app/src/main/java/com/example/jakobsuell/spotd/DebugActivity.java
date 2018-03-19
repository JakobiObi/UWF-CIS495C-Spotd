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

import java.util.ArrayList;
import java.util.List;

import controllers.FirestoreController;
import controllers.LoginController;
import enums.AnimalStatus;
import enums.AnimalType;
import models.Pet;
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

    /**
     * Save the current user to the Firestore.
     *
     * @param view Required parameter to call this method from a button.
     */
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

    public void savePet(View view) {

        // save a test pet to the firestore
        FirestoreController.savePet(FirebaseFirestore.getInstance(), getDummyPet()).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Log.d(TAG, "pet write successful");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "pet write failed: " + e);
            }
        });
    }

    public void savePetList(View view) {

        // save a test pet to the firestore
        FirestoreController.savePet(FirebaseFirestore.getInstance(), getDummyPetList()).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Log.d(TAG, "pet list write successful");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "pet list write failed: " + e);
            }
        });
    }


    public Pet getDummyPet() {

        return new Pet("Fluffy", AnimalType.Cat, new String[]{"long haired", "black", "white"}, AnimalStatus.Home, null, null);

    }

    public List<Pet> getDummyPetList() {

        ArrayList<Pet> pets = new ArrayList<>();

        pets.add(
                new Pet("Bella", AnimalType.Cat, new String[]{"tuxedo", "black", "white"}, AnimalStatus.Home, null, null)
        );
        pets.add(
                new Pet("Tiger", AnimalType.Cat, new String[]{"tabby", "orange", "white"}, AnimalStatus.Home, null, null)
        );
        pets.add(
                new Pet("Cujo", AnimalType.Dog, new String[]{"st. bernard", "white", "brown", "large"}, AnimalStatus.Lost, null, null)
        );

        return pets;

    }

}
