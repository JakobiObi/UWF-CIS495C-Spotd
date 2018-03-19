package controllers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.InvalidParameterException;

import models.User;

public class FirestoreController {

    private static final String userDirectory = "users";
    private static final String petDirectory = "pets";

    /**
     * Writes a user object to the firestore.  The user is saved using the email as a key.
     * Subsequent calls to this method with the same email address will overwrite existing
     * data, and functions the same as you would expect an update method to work.
     *
     * The object is automatically serialized by Firebase.
     *
     * The method will return a task object which can be attached with observers to detect
     * success or failure of the write operation.
     *
     * @param fb    A reference to the Firestore instance.
     * @param user  The user object to store.
     * @return A Task object, onto which you may attach observer methods.
     */
    public static Task saveUser(FirebaseFirestore fb, User user) {

        // parameter checks
        if (fb == null || user == null) {
            String err = (fb == null) ? "firestore reference cannot be null" : "user cannot be null";
            throw new InvalidParameterException(err);
        }

        return fb.collection(userDirectory).document(user.getEmailAddress()).set(user);

    }

    /**
     * Reads a user from firestore.
     * <p>
     * The object is automatically de-serialized by Firebase, but you must cast it back to an
     * object of the appropriate class manually, as it is returned in a documentSnapshot.
     * <p>
     * To use this, attach an OnSuccessListener to the task that is returned by this method.
     * A documentSnapshot object will be returned through this listener, which you MUST test
     * with the .exists() method prior to casting the object to a user class object. If
     * the .exists() method returns false, this means that the user's information could not
     * be found in firestore.
     * <p>
     * An example usage:
     * <p>
     * User user = new User().fromAuth();
     * FirestoreController.getUserByEmail(FirebaseFirestore.getInstance(), user.getEmailAddress()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
     *
     * @param fs    A reference to the Firestore instance.
     * @param email String. The email of the user whose data to retrieve.
     * @return A Task object, onto which you may attach observer methods.
     * @Override public void onSuccess(DocumentSnapshot documentSnapshot) {
     * if (documentSnapshot.exists()) {
     * User user = documentSnapshot.toObject(User.class);
     * } else {
     * // no user found, perhaps the user is new
     * // do something for a new user
     * }
     * }
     * }).addOnFailureListener(new OnFailureListener() {
     * @Override public void onFailure(@NonNull Exception e) {
     * Log.d(TAG, "could not read user information. error: " + e);
     * }
     * });
     */
    public static Task<DocumentSnapshot> getUserByEmail(FirebaseFirestore fs, String email) {

        // create a reference
        DocumentReference userDoc = fs.collection(userDirectory).document(email);

        return userDoc.get();

    }


}
