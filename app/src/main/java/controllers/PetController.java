package controllers;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import enums.AnimalStatus;
import models.Pet;
import models.User;

public class PetController {

    private static final String TAG = "PetController";

    public static Pet makePetLost(Pet pet){
        Log.d(TAG, "makePetLost: running on pet " + pet.getPetID());
        pet.setStatus(AnimalStatus.Lost);
        FirestoreController.savePet(FirebaseFirestore.getInstance(), pet)
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Log.d(TAG, "makePetLost: saved new status");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "makePetLost: failed to save new status: " + e);
                    }
                });
        NotificationController.notifyAllUsersInRange("pet " + pet.getPetID() + " is now lost");
        return pet;
    }

    public static Pet makePetFound(Pet pet, User user) {
        Log.d(TAG, "makePetFound: running on pet " + pet.getPetID());
        pet.setStatus(AnimalStatus.Found);
        pet.setFinderID(user.getUserID());
        FirestoreController.savePet(FirebaseFirestore.getInstance(), pet)
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Log.d(TAG, "makePetFound: saved new status");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "makePetFound: failed to save new status: " + e);
                    }
                });
        NotificationController.notifyUser(pet.getOwnerID(), "pet " + pet.getPetID() + " has been found");
        return pet;
    }

    public static Pet makePetHome(Pet pet) {
        Log.d(TAG, "makePetHome: running on pet " + pet.getPetID());
        pet.setStatus(AnimalStatus.Home);
        pet.setFinderID(null);
        FirestoreController.savePet(FirebaseFirestore.getInstance(), pet)
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Log.d(TAG, "makePetHome: saved new status");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "makePetHome: failed to save new status: " + e);
                    }
                });
        return pet;
    }

}
