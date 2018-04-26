package controllers;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import models.Pet;
import models.User;

public class FirestoreController {

    private static final String TAG = "FirestoreController";
    private static final String userDirectory = "users";
    private static final String petDirectory = "pets";


    /**
     * Writes a user object to the firestore, using the email address as a key.
     * The method will return a task object which can be attached with observers to detect
     * success or failure of the write operation.
     *
     * @param fb   A reference to the Firestore instance.
     * @param user The user object to store.
     * @return A Task object, onto which you may attach observer methods.
     */
    public static Task saveUser(FirebaseFirestore fb, User user) {
        Log.d(TAG, "Saving user...");
        if (fb == null || user == null) {
            String err = (fb == null) ? "firestore reference cannot be null" : "user cannot be null";
            throw new InvalidParameterException(err);
        }
        return fb.collection(userDirectory).document(user.getEmailAddress()).set(user);
    }

    public static Task saveUser(FirebaseFirestore fb, List<User> users) {
        Log.d(TAG, "Saving list of users...");
        if (fb == null || users == null) {
            String err = (fb == null) ? "firestore reference cannot be null" : "users cannot be null";
            throw new InvalidParameterException(err);
        }
        WriteBatch batch = fb.batch();
        DocumentReference docRef;
        for (User user : users) {
            docRef = fb.collection(userDirectory).document(user.getEmailAddress());
            batch.set(docRef, user);
        }
        return batch.commit();
    }

    /**
     * Reads a user from firestore.
     * To use this, attach an OnSuccessListener to the task returned by this method.
     * A documentSnapshot is returned, which you MUST test with the .exists() method prior
     * to casting the object to a user class object. If the .exists() method returns
     * false, this means that the user's information could not be found in firestore.
     *
     * @param fb    A reference to the Firestore instance.
     * @param email String. The email of the user whose data to retrieve.
     * @return A Task object, onto which you may attach observer methods.
     */
    public static Task<DocumentSnapshot> getUserByEmail(FirebaseFirestore fb, String email) {
        Log.d(TAG, "Attempting to read user with email (" + email + ")");
        if (fb == null || email == null || email.equals("")) {
            String err = (fb == null) ? "firestore reference cannot be null" : "email not provided";
            throw new InvalidParameterException(err);
        }
        DocumentReference userDoc = fb.collection(userDirectory).document(email);
        return userDoc.get();
    }


    /**
     * Saves a pet object to Firestore
     *
     * @param fb  A reference to the Firestore instance.
     * @param pet The pet object to store.
     * @return A Task object, onto which you may attach observer methods.
     */
    public static Task savePet(FirebaseFirestore fb, Pet pet) {
        Log.d(TAG, "Saving pet...");
        if (fb == null || pet == null) {
            String err = (fb == null) ? "firestore reference cannot be null" : "pet cannot be null";
            throw new InvalidParameterException(err);
        }
        DocumentReference docRef;
        docRef = fb.collection(petDirectory).document(pet.getPetID());
        return docRef.set(pet);
    }

    /**
     * Saves a list of pets to Firestore. This method will enumerate through each pet in the list,
     * saving each one to Firestore. A batch write is used for performance, and to minimize the
     * number of network calls to the database (a batch write only uses one)
     *
     * @param fb   A reference to the Firestore instance.
     * @param pets A List of pets to be stored.
     * @return A Task object, onto which you may attach observer methods.
     */
    public static Task savePet(FirebaseFirestore fb, List<Pet> pets) {
        Log.d(TAG, "Saving list of pets...");
        if (fb == null || pets == null || pets.size() == 0) {
            String err = (fb == null) ? "firestore reference cannot be null" : "pet list cannot be null or empty";
            throw new InvalidParameterException(err);
        }
        WriteBatch batch = fb.batch();
        DocumentReference docRef;
        for (Pet pet : pets) {
            docRef = fb.collection(petDirectory).document(pet.getPetID());
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
        Log.d(TAG, "Reading pets with id (" + petID + ")");
        if (fb == null || petID == null || petID.equals("")) {
            String err = (fb == null) ? "firestore reference cannot be null" : "petID cannot be null or empty";
            throw new InvalidParameterException(err);
        }
        DocumentReference docRef = fb.collection(petDirectory).document(petID);
        return docRef.get();
    }

    public static Task<QuerySnapshot> readPets(FirebaseFirestore fb, String fieldName, String fieldValue) {
        Log.d(TAG, "Reading pets with " + fieldName + " = " + fieldValue);
        if (fb == null || fieldName == null || fieldName.equals("")) {
            String err = (fb == null) ? "firestore reference cannot be null" : "fieldName cannot be null or empty";
            throw new InvalidParameterException(err);
        }
        // TODO:  Add a check to make sure the given fieldname is correct.
        CollectionReference petsRef = fb.collection(petDirectory);
        Query query = petsRef.whereEqualTo(fieldName, fieldValue);
        return query.get();
    }

    public static Task<QuerySnapshot> readPets(FirebaseFirestore fb, String firstField, String firstValue, String secondField, String secondValue) {
        Log.d(TAG, "Reading pets with " + firstField + "=" + firstValue + " and " + secondField + "=" + secondValue);
        if (fb == null) {
            throw new InvalidParameterException("firestore reference cannot be null");
        }
        CollectionReference petsRef = fb.collection(petDirectory);
        Query query = petsRef.whereEqualTo(firstField, firstValue).whereEqualTo(secondField, secondValue);
        return query.get();
    }

    public static void deletePet(FirebaseFirestore fb, String petID) {
        Log.d(TAG, "deletePet: deleting pet with id " + petID);
        if (fb == null || petID == null || petID.equals("")) {
            String err = (fb == null) ? "firestore reference cannot be null" : "petID cannot be null or empty";
            throw new InvalidParameterException(err);
        }
        fb.collection(petDirectory).document(petID).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "deletePet: ...successful deletion");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "deletePet: ...deletion failed: " + e);
                    }
                });
    }

    public static ArrayList<Pet> processPetsQuery(QuerySnapshot queryDocumentSnapshots) {
        ArrayList<Pet> pets = new ArrayList<>();
        for (DocumentSnapshot document : queryDocumentSnapshots) {
            pets.add(document.toObject(Pet.class));
        }
        return pets;
    }
    // TODO: Change User accounts so they are saved by an accountID rather than email.
    /*
        There should be a look-up table that maps email addresses to a accountID.
        This will allow for the following scenarios:
        - A person logs in with two diff providers, each provider has a diff email address associated
        - Multiple family members
     */

}
