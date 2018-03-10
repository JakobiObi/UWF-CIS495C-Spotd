package com.example.jakobsuell.spotd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;


public class AccountController {

    private static String TAG = "AccountController";


    public static void enforceSignIn(Activity context) {

        if (!AccountController.isUserSignedIn(null)) {
            Log.d(TAG, "no signed in user. sending to login");
            signUserIn(context);
        }

    }

    /**
     * Checks the Firebase authentication object instance to see if a user is currently
     * signed in and authenticated.
     *
     * @return Boolean true if a user is logged in, false otherwise.
     */
    public static boolean isUserSignedIn(FirebaseAuth auth) {

        if (auth == null) {
            auth = FirebaseAuth.getInstance();
        }
        Log.d(TAG, "checking for signed in user");
        return (auth.getCurrentUser() != null);

    }

    /**
     * Use the Navigation controller to go to the Login activity.
     *
     * @param context An activity context to use to start another activity.
     */
    public static void signUserIn(Context context) {

        // TODO: Replace this nonsense with a call to the Navigation controller's method to go to LoginActivity

        // launch the next activity
        Intent login = new Intent(context, LoginActivity.class);

        // don't allow user to return to login screen
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        context.startActivity(login);

    }

}
