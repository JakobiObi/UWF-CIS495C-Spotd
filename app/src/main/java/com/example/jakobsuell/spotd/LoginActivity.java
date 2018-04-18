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
    private final String TAG = "LoginActivity";
    private final boolean GO_DEBUG_ACTIVITY = false;     // shows debug activity instead of toolbar_menu_selector
    private FirebaseAuth auth;
    private TextView tvMessage;
    private Button btSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvMessage = findViewById(R.id.txtMessage);
        btSignIn = findViewById(R.id.btnSignIn);

        auth = FirebaseAuth.getInstance();
        if (LoginController.isUserSignedIn(auth)) {
            doPostSignIn();
        } else {
            Log.d(TAG, "no signed in user.");
            signUserIn();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                doPostSignIn();
            } else {
                Log.d(TAG, "sign-in failed.");
                setFailureMessage(getFailureMessage(response));
            }
        }
    }

    private String getFailureMessage(IdpResponse response) {
        StringBuilder msg = new StringBuilder(getString(R.string.signin_fail_generic));
        if (response == null) {
            Log.d(TAG, "user cancelled sign-in");
            msg.append(" ");
            msg.append(getString(R.string.signin_fail_cancelled));
        } else {
            if (response.getError()!= null && response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                Log.d(TAG, "network unavailable");
                msg.append( getString(R.string.signin_fail_network));
            } else {
                Log.e(TAG, "Unhandled sign-in error: " + response.getError().getMessage());
            }
        }
        return msg.toString();
    }

    /**
     *  Sign in process using pre-built FirebaseUI.
     */
    private void signUserIn() {
        Log.d(TAG, "launching sign-in UI");

        tvMessage.setText("Signing in...");
        btSignIn.setVisibility(View.INVISIBLE);

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build());

        // create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    private void doPostSignIn() {

        if (auth == null) {
            auth = FirebaseAuth.getInstance();
        }

        if (auth.getCurrentUser() != null) {
            Log.d(TAG, "user signed in: " + auth.getCurrentUser().getDisplayName());
            logUserInfo();
            //noinspection ConstantConditions
            Intent nextActivity;
            if (GO_DEBUG_ACTIVITY) {
                nextActivity = new Intent(this, DebugActivity.class);
            } else {
                nextActivity = new Intent(this, MainActivity.class);
            }
            goToActivity(nextActivity);
        }
    }

    private void setFailureMessage(String msg) {
        btSignIn.setVisibility(View.VISIBLE);
        btSignIn.requestFocus();
        tvMessage.setText(msg);
    }

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

    private void goToActivity(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(intent);
    }

    public void signInUser_click(View view) {
        signUserIn();
    }
}
