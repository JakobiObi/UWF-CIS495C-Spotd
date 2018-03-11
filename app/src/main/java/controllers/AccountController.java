package controllers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.InvalidParameterException;

import models.User;


public class AccountController {


    public User createUserFromFirebaseAuthentication(FirebaseAuth auth) {

        if (auth == null) {
            throw new InvalidParameterException("auth object can't be null");
        }

        FirebaseUser firebaseUser = auth.getCurrentUser();

        if (firebaseUser == null) {
            return null;
        }

        User user = new User();

        // interrogate auth object for data
        user.setDisplayName(firebaseUser.getDisplayName());
        user.setEmailAddress(firebaseUser.getEmail());
        if (firebaseUser.getMetadata() != null) {
            user.setLastLogin(firebaseUser.getMetadata().getLastSignInTimestamp());
            user.setCreationDate(firebaseUser.getMetadata().getCreationTimestamp());
        } else {
            user.setLastLogin(0);
            user.setCreationDate(0);
        }

        return user;

    }


}
