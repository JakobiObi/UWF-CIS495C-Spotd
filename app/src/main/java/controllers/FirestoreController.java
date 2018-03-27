package controllers;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.security.InvalidParameterException;
import java.util.List;

import models.Pet;
import models.User;

public class FirestoreController {

    private static final String userDirectory = "users";
    private static final String petDirectory = "pets";

    private static final String TAG = "FirestoreController";

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
     * @param fb    A reference to the Firestore instance.
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
    public static Task<DocumentSnapshot> getUserByEmail(FirebaseFirestore fb, String email) {

        // parameter checks
        if (fb == null || email == null || email.equals("")) {
            String err = (fb == null) ? "firestore reference cannot be null" : "email not provided";
            throw new InvalidParameterException(err);
        }

        // create a reference
        DocumentReference userDoc = fb.collection(userDirectory).document(email);

        return userDoc.get();

    }

    /**
     * Saves a pet object to Firestore.
     *
     * The pet will be saved under either the petID (if it already has one) or an auto-generated ID.
     *
     * @param fb    A reference to the Firestore instance.
     * @param pet   The pet object to store.
     * @return A Task object, onto which you may attach observer methods.
     */
    public static Task savePet(FirebaseFirestore fb, Pet pet) {

        Log.d(TAG, "Saving pet...");

        // parameter checks
        if (fb == null || pet == null) {
            String err = (fb == null) ? "firestore reference cannot be null" : "pet cannot be null";
            throw new InvalidParameterException(err);
        }

        DocumentReference docRef;

        // check if this pet has an associated id already, if not, generate one
        if (pet.getPetID() == null) {
            // create an auto-generated id and set on pet object
            Log.d(TAG, "no id yet, creating");
            docRef = fb.collection(petDirectory).document();
            pet.setPetID(docRef.getId());
        } else {
            docRef = fb.collection(petDirectory).document(pet.getPetID());
        }

        return docRef.set(pet);

    }

    /**
     * Saves a list of pets to Firestore. This method will enumerate through each pet in the list,
     * saving each one to Firestore.
     * <p>
     * A batch write is used to enhance performance, and minimize the amount of network traffic
     * required for the transaction.
     *
     * @param fb   A reference to the Firestore instance.
     * @param pets A List of pets to be stored.
     * @return A Task object, onto which you may attach observer methods.
     */
    public static Task savePetList(FirebaseFirestore fb, List<Pet> pets) {

        // parameter checks
        if (fb == null || pets == null || pets.size() == 0) {
            String err = (fb == null) ? "firestore reference cannot be null" : "pet list cannot be null or empty";
            throw new InvalidParameterException(err);
        }

        // perform this as a batch write
        WriteBatch batch = fb.batch();
        DocumentReference docRef;

        for (Pet pet : pets) {
            // check if this pet has an associated id already, if not, generate one
            if (pet.getPetID() == null) {
                // create an auto-generated id and set on pet object
                docRef = fb.collection(petDirectory).document();
                pet.setPetID(docRef.getId());
            } else {
                docRef = fb.collection(petDirectory).document(pet.getPetID());
            }
            batch.set(docRef, pet);
        }

        return batch.commit();
    }

    /**
     * Fetches a specified pet from Firestore.
     *
     * @param fb    A reference to the Firestore instance.
     * @param petID The petID of the pet to get.
     * @return A Task object, onto which you may attach observer methods.
     */
    public static Task<DocumentSnapshot> readPet(FirebaseFirestore fb, String petID) {

        // parameter checks
        if (fb == null || petID == null || petID.equals("")) {
            String err = (fb == null) ? "firestore reference cannot be null" : "petID cannot be null or empty";
            throw new InvalidParameterException(err);
        }

        DocumentReference docRef = fb.collection(petDirectory).document(petID);

        return docRef.get();

    }

    public static Task<DocumentSnapshot> readPets(FirebaseFirestore fb, String ownerID) {
        throw new UnsupportedOperationException("readPets() is not yet implemented");
    }

    // TODO: Change User accounts so they are saved by an accountID rather than email.
    /*
        There should be a look-up table that maps email addresses to a accountID.
        This will allow for the following scenarios:
        - A person logs in with two diff providers, each provider has a diff email address associated
        - Multiple family members
     */

    // TODO: Explore the possibility of returning actual data model objects instead of task objects
}
