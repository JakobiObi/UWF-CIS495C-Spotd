package com.example.jakobsuell.spotd;

import android.content.Intent;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DebugActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private String TAG = "DebugActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        Log.d(TAG, "entering");

        // TODO: Better sign in check here, to help debug

        String displayName;
        try {
            displayName = auth.getCurrentUser().getDisplayName();
        } catch (NullPointerException e) {
            displayName = "null";
        }

        // pull user id and log
        Log.d(TAG, "username: " + displayName);
        Log.d(TAG, "user id: " + auth.getCurrentUser().getUid());
        Log.d(TAG, "email: " + auth.getCurrentUser().getEmail());

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
                        // user is now signed out
                        Log.d(TAG, "User is now signed out.");
                        goLoginActivity();
                        finish();
                    }
                });

    }

    public void createUser(View view) {

        // create user hashmap object
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Joe");
        user.put("last", "Smith");
        user.put("userid", 123456);

        //store it
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "added user");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "error adding user", e);
                    }
                });

    }

    private void goLoginActivity() {

        // launch the next activity
        Intent login = new Intent(this, LoginActivity.class);

        // don't allow user to return to login screen
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        this.startActivity(login);
    }

}
