package models;

/**
 * Note:  This class must have a public default constructor and public getters for each property
 * in order to work with Firestore.
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

    public User(String displayName, String emailAddress, long creationDate, long lastLogin, String profilePhoto) {
        this.displayName = displayName;
        this.emailAddress = emailAddress;
        this.creationDate = creationDate;
        this.lastLogin = lastLogin;
        this.profilePhoto = profilePhoto;
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
}
