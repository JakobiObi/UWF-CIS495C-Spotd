package controllers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.InvalidParameterException;

import models.User;

public class AccountController {

    private static final String userDirectory = "users";
    private static final String petDirectory = "pets";

    /**
     * Writes a supplied user to the firestore. The method will return a task object which
     * can be attached with observers to detect success or failure of the write operation.
     *
     * @param fb
     * @param user
     * @return A Task object.
     */
    public static Task saveUser(FirebaseFirestore fb, User user) {

        // parameter checks
        if (fb == null || user == null) {
            String err = (fb == null) ? "firestore reference cannot be null" : "user cannot be null";
            throw new InvalidParameterException(err);
        }

        return fb.collection(userDirectory).document(user.getEmailAddress()).set(user);

    }

    public static Task<DocumentSnapshot> getUserByEmail(FirebaseFirestore fb, String email) {

        // create a reference
        DocumentReference userDoc = fb.collection(userDirectory).document(email);

        return userDoc.get();

    }
    /* TODO:  Write getUser(String email)
        Method will attempt to read the corresponding user object indicated by email parameter.
        Hopefully, will return an object that can be attached with observers. It should be noted
        that the failure of this method could be used to detect if dealing with a new user (and
        thus kick into the account creation process)
    */



}
