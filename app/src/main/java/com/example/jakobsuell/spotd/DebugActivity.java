package com.example.jakobsuell.spotd;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import controllers.FirestoreController;
import controllers.ImageController;
import controllers.LoginController;
import enums.AnimalType;
import models.Pet;
import models.User;

/**
 * As the name implies, this activity is strictly for debugging and shouldn't be visible to a
 * normal user.
 */

@SuppressWarnings("unused")
public class DebugActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private final String TAG = "DebugActivity";
    private String firebase_uri;
    private final String testImage = "dubstep.PNG";

    private ImageView profile_pic;
    private Uri userPicture;
    private Pet testPet;
    private List<Pet> pets;
    private User user;

    private Target profilePicTarget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        LoginController.enforceSignIn(this);
        createPicassoSingleton();

        profile_pic = findViewById(R.id.iv_user_profile_picture);
        auth = FirebaseAuth.getInstance();
        firebase_uri = this.getResources().getString(R.string.firebase_storage_uri);
        userPicture = LoginController.getUserPictureUri(auth);

    }

    public void signUserOut(View view) {
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

    public void saveCurrentUser(View view) {
        Log.d(TAG, "Saving current user");
        user = new User().fromAuth();
        user.show();
        Uri userProfilePictureUri = LoginController.getUserPictureUri(auth);
        FirestoreController.saveUser(FirebaseFirestore.getInstance(), user)
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Log.d(TAG, "user write successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "user write failed: " + e);
                    }
                });
        profilePicTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.d(TAG, "Saving user's profile photo...");
                ImageController.storeImage(user.getProfilePhotoId(), bitmap)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.d(TAG, "Profile picture saved.");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "error during save: " + e);
                            }
                        });
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };

        Picasso.get()
                .load(userProfilePictureUri)
                .into(profilePicTarget);

    }

    /**
     * Attempt to read the currently logged in user's data.
     *
     * @param view This is required if this is called by a button.
     */
    public void readCurrentUser(View view) {
        Log.d(TAG, "attempting to read current user account...");
        User user = new User().fromAuth(); // to get current user's email addy
        FirestoreController.getUserByEmail(FirebaseFirestore.getInstance(), user.getEmailAddress())
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            User user = documentSnapshot.toObject(User.class);
                            Log.d(TAG, "user read success:");
                            user.show();
                        } else {
                            Log.d(TAG, "no user found. probably a new account.");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "could not read user information. error: " + e);
                    }
                });
    }

    public void getTestImage(View view) {
        Log.d(TAG, "fetching testimage [" + testImage + "] from storage...");
        Picasso.get()
                .load(firebase_uri + testImage)
                .into(profile_pic);
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

    public void mockData(View view) {
        Log.d(TAG, "Creating mock data...");
        MockDataGenerator mockDataGenerator = MockDataGenerator.make();
        mockDataGenerator.saveData();
    }

    /**
     * This also exists in MainActivity, and runs when OnCreate is called.  When debugging is on,
     * MainActivity is bypassed and this does not get run, therefore it is here also.  Please ensure
     * changes to the other instance of this method propagate to this method also.
     */
    public void createPicassoSingleton() {
        Picasso picassoInstance = new Picasso.Builder(this.getApplicationContext())
                .addRequestHandler(new FirebaseRequestHandler())
                .build();
        Picasso.setSingletonInstance(picassoInstance);
    }
}
