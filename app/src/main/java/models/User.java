package models;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * This implements the User model.
 * <p>
 * Note:  This class must have a public default constructor and public getters for each property
 * in order to work with Firestore.
 * <p>
 * To quickly create a new user based on the currently logged in user, use
 * User user = new User().fromAuth();
 * <p>
 * When the fromAuth() method is used and it is not the first sign-in session for the user, the
 * creation and last login timestamps will likely be null. This is due to a known persistence
 * bug in Android SDK that is not fixed as of the time of this writing.
 * (see https://stackoverflow.com/questions/48079683/firebase-user-returns-null-metadata-for-already-signed-up-users)
 */
public class User {

    private String displayName;     // the display name (not username)
    private String emailAddress;    // this is used to uniquely identify the user account
    private long creationDate;      // timestamp of account creation
    private long lastLogin;         // timestamp of last login (zero if brand new account)
    private String profilePhoto;    // UID to user photo


    // default constructor (required)
    public User() {
    }


    // other constructors

    public User(String displayName, String emailAddress, long creationDate, long lastLogin, String profilePhoto) {
        this.displayName = displayName;
        this.emailAddress = emailAddress;
        this.creationDate = creationDate;
        this.lastLogin = lastLogin;
        this.profilePhoto = profilePhoto;
    }

    public User fromAuth() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        FirebaseUserMetadata meta;

        if (user != null) {
            meta = user.getMetadata();
            this.displayName = user.getDisplayName();
            this.emailAddress = user.getEmail();
        } else {
            this.displayName = "Not supplied";
            this.emailAddress = "Not supplied";
            meta = null;
        }

        if (meta != null) {
            this.creationDate = meta.getCreationTimestamp();
            this.lastLogin = meta.getLastSignInTimestamp();
        } else {
            this.creationDate = 0;
            this.lastLogin = 0;
        }

        return this;

    }

    // getters/setters (getters are required)

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public long getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    /**
     * Writes this user objects data to the debugging log.
     */
    public void show() {

        String logMsg = "name: " + this.displayName
                + " " + "email: " + this.emailAddress
                + " " + "created: " + this.creationDate + " (" + getShortDateFromTimestamp(this.creationDate) + ")"
                + " " + "last login: " + this.lastLogin + " (" + getShortDateFromTimestamp(this.lastLogin) + ")"
                + " " + "photo: " + this.profilePhoto;


        Log.d("User", logMsg);

    }

    /**
     * Convert a timestamp into a string short date. This uses java time stuff that is
     * deprecated but to use the modern java.time.* stuff would require a minimum target
     * API of 26.
     *
     * @param time A timestamp in milliseconds.
     * @return A string short date.
     */
    private String getShortDateFromTimestamp(long time) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return formatter.format(cal.getTime());
    }

}
