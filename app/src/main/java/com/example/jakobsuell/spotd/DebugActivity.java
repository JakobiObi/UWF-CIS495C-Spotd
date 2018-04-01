package com.example.jakobsuell.spotd;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;

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

    private ImageView profile_pic;
    private Uri userPicture;

    // these need to persist across several saves
    private Pet testPet;
    private List<Pet> pets;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        // set views
        profile_pic = findViewById(R.id.iv_user_profile_picture);

        auth = FirebaseAuth.getInstance();

        LoginController.enforceSignIn(this);
        userPicture = LoginController.getUserPictureUri(auth);

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
     * Save the current user to Firestore.
     *
     * @param view Required parameter to call this method from a button.
     */
    public void saveUser(View view) {

        // create user from the auth object
        user = new User().fromAuth();

        // show all the user info in logcat
        user.show();

        // attempt to write user photo to storage
        // the picture id is the crc32 of the user's email address
        CRC32 pictureUID = new CRC32();
        pictureUID.update(user.getEmailAddress().getBytes());

        Log.d(TAG, "user profile pic uid is: " + pictureUID.getValue());

        // determine user's profile picture URI.
        Uri userProfilePictureUri = LoginController.getUserPictureUri(auth);

        // attempt to write user info to the firestore
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
                    user.show();
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
        if (testPet == null) {
            testPet = getDummyPet();
        } else {
            // change something about the pet
            testPet.setName("Second");
        }

        FirestoreController.savePet(FirebaseFirestore.getInstance(), testPet).addOnSuccessListener(new OnSuccessListener() {
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

        // save a test list of pets to the firestore
        if (pets == null) {
            pets = getDummyPetList();
        }


        FirestoreController.savePetList(FirebaseFirestore.getInstance(), pets).addOnSuccessListener(new OnSuccessListener() {
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

    public void readPet(View view) {

        // read the pet that we have saved

        Log.d(TAG, "Attempting to read a test pet...");

        if (testPet == null || testPet.getPetID() == null || testPet.getPetID().equals("")) {
            // save a pet real quick, then re-call ourselves when it is done
            Log.d(TAG, "no pet object created...creating...");
            if (testPet == null)
                testPet = getDummyPet();
            FirestoreController.savePet(FirebaseFirestore.getInstance(), testPet).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    // call ourselves again
                    Log.d(TAG, "new pet object saved. recalling this...");
                    readPet(null);
                }
            });
            return;
        }

        FirestoreController.readPet(FirebaseFirestore.getInstance(), testPet.getPetID()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Pet pet;
                if (documentSnapshot.exists()) {
                    pet = documentSnapshot.toObject(Pet.class);
                    Log.d(TAG, "pet read success:");
                    pet.show();
                } else {
                    // this pet couldn't be found
                    Log.d(TAG, "pet with id " + testPet.getPetID() + " could not be found");
                    pet = null;
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // what to do when it fails
                Log.d(TAG, "pet read failed:" + e);
            }
        });


    }

    public void readPetList(View view) {

        Log.d(TAG, "Attempting to read a test pet...");

        if (pets == null) {
            pets = new ArrayList<>();
        } else {
            pets.clear();
        }

        // try getting all cats
        FirestoreController.readPets(FirebaseFirestore.getInstance(), "animalType", AnimalType.Cat.description()).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "got list of cats");
                    for (DocumentSnapshot document : task.getResult()) {
                        pets.add(document.toObject(Pet.class));
                    }
                    Log.d(TAG, "list has " + pets.size() + " items");
                    for (Pet p : pets) {
                        p.show();
                    }
                } else {
                    Log.d(TAG, "Error getting pets: ", task.getException());
                }
            }
        });

    }

    private void showPet(Pet pet) {

        pet.show();

    }

    public Pet getDummyPet() {

        ArrayList<String> keywords = new ArrayList<>();
        keywords.add("tuxedo");
        keywords.add("black");
        keywords.add("white");

        return new Pet("Fluffy", AnimalType.Cat, keywords, AnimalStatus.Home, null, null, null);

    }

    public List<Pet> getDummyPetList() {

        ArrayList<Pet> pets = new ArrayList<>();
        ArrayList<String> keywords = new ArrayList<>();

        keywords.add("tuxedo");
        keywords.add("black");
        keywords.add("white");
        pets.add(
                new Pet("Bella", AnimalType.Cat, keywords, AnimalStatus.Home, null, null, null)
        );

        keywords.clear();
        keywords.add("tabby");
        keywords.add("orange");
        keywords.add("white");
        pets.add(
                new Pet("Tiger", AnimalType.Cat, keywords, AnimalStatus.Home, null, null, null)
        );

        keywords.clear();
        keywords.add("st. bernard");
        keywords.add("white");
        keywords.add("brown");
        keywords.add("large");
        pets.add(
                new Pet("Cujo", AnimalType.Dog, keywords, AnimalStatus.Lost, null, null, null)
        );

        return pets;

    }

}
