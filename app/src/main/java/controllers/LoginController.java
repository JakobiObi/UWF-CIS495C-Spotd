package controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.example.jakobsuell.spotd.LoginActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;

import java.security.InvalidParameterException;

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
    private static final String PROVIDER_FACEBOOK_ID = "facebook.com";
    private static final String PROVIDER_GOOGLE_ID = "google.com";
    private static final String FACEBOOK_BASE_URL = "https://graph.facebook.com/";
    private static final String FACEBOOK_PICTURE_DIRECTORY = "/picture?height=500";

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

    public static Uri getUserPictureUri(FirebaseAuth auth) {

        if (auth == null)
            throw new InvalidParameterException("auth can't be null");

        // ? this might pull from Google provider by default...
        String photoURL = auth.getCurrentUser().getPhotoUrl().toString();

        // look for facebook provider
        for (UserInfo profile : auth.getCurrentUser().getProviderData()) {
            if (profile.getProviderId().equals(PROVIDER_FACEBOOK_ID)) {
                String facebookUserId = profile.getUid();
                photoURL = FACEBOOK_BASE_URL + facebookUserId + FACEBOOK_PICTURE_DIRECTORY;
            }
        }

        Log.d(TAG, "user photo uri: " + photoURL);

        return Uri.parse(photoURL);

    }


}
