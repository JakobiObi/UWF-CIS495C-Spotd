package models;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.zip.CRC32;

/**
 * The User model.
 *
 * Note:  This class must have a public default constructor and public getters for each property
 * in order to work with Firestore.
 *
 * To quickly create a new user based on the currently logged in user, use
 * User user = new User().fromAuth();
 *
 */
public class User {

    private String displayName;
    private String emailAddress;
    private long creationDate;
    private long lastLogin;
    private String profilePhotoId;
    private String userID;

    public User() {
    }

    public User(String displayName, String emailAddress, long creationDate, long lastLogin) {
        this.displayName = displayName;
        this.emailAddress = emailAddress;
        this.creationDate = creationDate;
        this.lastLogin = lastLogin;
        this.profilePhotoId = generatePictureIdFrom(emailAddress);
    }

    public User fromAuth() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        FirebaseUserMetadata metaUserData;

        if (user != null) {
            metaUserData = user.getMetadata();
            this.displayName = user.getDisplayName();
            this.emailAddress = user.getEmail();
        } else {
            this.displayName = "Not supplied";
            this.emailAddress = "Not supplied";
            metaUserData = null;
        }

        if (metaUserData != null) {
            this.creationDate = metaUserData.getCreationTimestamp();
            this.lastLogin = metaUserData.getLastSignInTimestamp();
        } else {
            this.creationDate = 0;
            this.lastLogin = 0;
        }
        return this;
    }

    // getters/setters (getters required by Firestore)

    public String getUserID() {
        return emailAddress;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }

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
        this.profilePhotoId = generatePictureIdFrom(emailAddress);
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


    public String getProfilePhotoId() {
        // make sure the thing exists before we try to return it
        if (this.profilePhotoId == null || this.profilePhotoId.equals("")) {
            if (this.emailAddress.equals("")) {
                throw new IllegalStateException("user profile photo id not available when no email address set");
            }
            this.profilePhotoId = generatePictureIdFrom(emailAddress);
        }
        return profilePhotoId;
    }


    /**
     * Writes this user objects data to the debugging log.
     */
    public void show() {
        String logMsg = "name: " + this.displayName
                + " " + "email: " + this.emailAddress
                + " " + "created: " + this.creationDate + " (" + getShortDateFromTimestamp(this.creationDate) + ")"
                + " " + "last login: " + this.lastLogin + " (" + getShortDateFromTimestamp(this.lastLogin) + ")"
                + " " + "photo: " + this.profilePhotoId;
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

    /**
     * Generate a unique id from the user's email address.
     * @return A unique string.
     */
    private String generatePictureIdFrom(String value) {
        CRC32 pictureIdFromCrc32 = new CRC32();
        pictureIdFromCrc32.update(value.getBytes());
        return String.valueOf(pictureIdFromCrc32.getValue());
    }

}
