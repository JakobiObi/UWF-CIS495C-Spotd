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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.security.InvalidParameterException;

public class LoginController {

    private static final String TAG = "LoginController";
    private static final String PROVIDER_FACEBOOK_ID = "facebook.com";
    private static final String FACEBOOK_BASE_URL = "https://graph.facebook.com/";
    private static final String FACEBOOK_PICTURE_DIRECTORY = "/picture?height=500";

    public static void enforceSignIn(Activity context) {
        Log.d(TAG, "enforcing login on " + context.getComponentName());
        if (!LoginController.isUserSignedIn(null)) {
            Log.d(TAG, "no signed in user.");
            redirectToLoginActivity(context);
        }
    }

    public static Task signOut(Activity context) {
        Log.d(TAG, "Signing user out");
        return AuthUI.getInstance().signOut(context);
    }

    public static boolean isUserSignedIn(FirebaseAuth auth) {
        if (auth == null) {
            auth = FirebaseAuth.getInstance();
        }
        String userFound = auth.getCurrentUser() != null ? "found" : "none";
        Log.d(TAG, "checking for signed in user: " + userFound);
        return (auth.getCurrentUser() != null);
    }

    public static void redirectToLoginActivity(Context context) {
        Log.d(TAG, "sending to LoginActivity");
        Intent login = new Intent(context, LoginActivity.class);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(login);
    }

    public static Uri getUserPictureUri(FirebaseAuth auth) {
        Uri userPhotoURI;
        if (auth == null) {
            throw new InvalidParameterException("auth can't be null");
        }

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            userPhotoURI = user.getPhotoUrl();
            StringBuilder photoURL = new StringBuilder();
            for (UserInfo profile : user.getProviderData()) {
                if (profile.getProviderId().equals(PROVIDER_FACEBOOK_ID)) {
                    photoURL
                            .append(FACEBOOK_BASE_URL)
                            .append(profile.getUid())
                            .append(FACEBOOK_PICTURE_DIRECTORY);
                    userPhotoURI = Uri.parse(photoURL.toString());
                    break;
                }
            }
        } else {
            userPhotoURI = Uri.EMPTY;
        }
        return userPhotoURI;
    }


}
