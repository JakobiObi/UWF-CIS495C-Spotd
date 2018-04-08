package com.example.jakobsuell.spotd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

import controllers.LoginController;


public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private final boolean GO_DEBUG_ACTIVITY = true;     // shows debug activity instead of toolbar_menu_selector
    private FirebaseAuth auth;
    private String TAG = "LoginActivity";
    private TextView tvMessage;
    private Button btSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // assign views
        tvMessage = findViewById(R.id.txtMessage);
        btSignIn = findViewById(R.id.btnSignIn);

        // check if user signed in
        auth = FirebaseAuth.getInstance();
        if (!LoginController.isUserSignedIn(auth)) {
            Log.d(TAG, "no signed in user.");
            signUserIn();
        } else {
            onSignIn();
        }

    }

    /**
     * Callback for Firebase pre-built login UI.
     *
     * @param requestCode   Indicates what action was performed. Should always be RC_SIGN_IN
     * @param resultCode    Returned by Firebase and indicates whether sign-in was successful.
     * @param data          The intent returned by the login activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successful sign in
                onSignIn();
            } else {
                // sign in failed, check reason and set message
                Log.d(TAG, "sign-in failed.");

                String msg = getString(R.string.signin_fail_generic);

                if (response == null) {
                    // User pressed back button
                    Log.d(TAG, "user cancelled sign-in");
                    msg = msg + " " + getString(R.string.signin_fail_cancelled);
                } else {
                    if (response.getError()!= null && response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                        Log.d(TAG, "network unavailable");
                        msg = msg + " " + getString(R.string.signin_fail_network);
                    } else {
                        Log.e(TAG, "Unhandled sign-in error: " + response.getError().getMessage());
                    }

                }
                onSignInFail(msg);
            }
        }
    }

    /**
     * Signs users in using pre-built FirebaseUI.
     *
     * This is the only method that is possible to run when a new user is signing up.
     *
     */
    private void signUserIn() {

        Log.d(TAG, "launching sign-in UI");

        // show signing in message and hide button
        tvMessage.setText("Signing in...");
        btSignIn.setVisibility(View.INVISIBLE);

        // attach authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build());

        // create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        // TODO: Enable the display of privacy policy when we have one.
                        //.setPrivacyPolicyUrl()
                        .build(),
                RC_SIGN_IN);
    }

    /**
     * Perform post-sign-in actions.
     */
    private void onSignIn() {

        if (auth == null) {
            auth = FirebaseAuth.getInstance();
        }

        if (auth.getCurrentUser() != null) {
            Log.d(TAG, "user signed in: " + auth.getCurrentUser().getDisplayName());
            logUserInfo();
            if (GO_DEBUG_ACTIVITY) {
                goDebugActivity();
            } else {
                goMainActivity();
            }

        }

    }

    /**
     * Inform the user that an error has occurred while logging in.
     * @param msg   The error message to display to the user.
     */

    private void onSignInFail(String msg) {

        // show button initially
        btSignIn.setVisibility(View.VISIBLE);
        btSignIn.requestFocus();

        tvMessage.setText(msg);

    }

    private void goDebugActivity() {

        // TODO: Replace this with a call to the Navigation controller

        // launch debug activity
        Intent nextActivity = new Intent(this, DebugActivity.class);

        // don't allow user to return to login screen
        nextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        this.startActivity(nextActivity);

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

    private void goMainActivity() {

        // TODO: Replace this with a call to the Navigation controller

        // launch the toolbar_menu_selector activity
        Intent nextActivity = new Intent(this, MainActivity.class);

        // don't allow user to return to login screen
        nextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        this.startActivity(nextActivity);

    }

    public void signInUser_click(View view) {
        signUserIn();
    }
}
