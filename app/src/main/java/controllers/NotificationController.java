package controllers;

import android.util.Log;

public class NotificationController {

    private static final String TAG = "NotificationController";

    /*
        This is a logging stub.
     */
    public static void notifyUser(String userID, String text) {
        // only a specific user will get this
        Log.d(TAG, "notifyUser("+ userID +"): " + text);
    }


    public static void notifyAllUsersInRange(String text) {
        // everyone within geographic range will get this
        Log.d(TAG, "notifyAllUsersInRange: " + text);
    }

}
