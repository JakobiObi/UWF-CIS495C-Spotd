package controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.jakobsuell.spotd.LoginActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Implements a wrapper class for the LoginActivity.
 * <p>
 * To enforce authentication for an activity, simply put a call to
 * LoginController.enforceSignIn() in the OnCreate method of the activity.
 * <p>
 * This class will check if the user is authenticated, and if not, redirect them
 * to the login activity automatically.
 */
public class LoginController {

    private static String TAG = "LoginController";


    public static void enforceSignIn(Activity context) {

        Log.d(TAG, "enforcing login on " + context.getComponentName());
        if (!LoginController.isUserSignedIn(null)) {
            Log.d(TAG, "no signed in user. sending to login");
            redirectToLoginActivity(context);
        }
    }

    public static Task signOut(Activity context) {

        Log.d(TAG, "Signing user out");
        return AuthUI.getInstance().signOut(context);

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
        String userFound = auth.getCurrentUser() != null ? "found" : "none";
        Log.d(TAG, "checking for signed in user: " + userFound);
        return (auth.getCurrentUser() != null);

    }

    /**
     * Use the Navigation controller to go to the Login activity.
     *
     * @param context An activity context to use to start another activity.
     */
    public static void redirectToLoginActivity(Context context) {

        // TODO: Replace this nonsense with a call to the Navigation controller's method to go to LoginActivity
        Log.d(TAG, "sending to LoginActivity");
        // launch the next activity
        Intent login = new Intent(context, LoginActivity.class);

        // don't allow user to return to login screen
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        context.startActivity(login);

    }


}