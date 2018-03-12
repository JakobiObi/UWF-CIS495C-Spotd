package controllers;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.example.jakobsuell.spotd.GlideApp;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Used to manage images
 */

public class ImageController {

    private static final String imagePath = "images/";
    private static final String TAG = "ImageController";
    private static FirebaseStorage fbStorage;
    private static boolean isInitialized = false;

    private static void initialize() {
        fbStorage = FirebaseStorage.getInstance();
        isInitialized = true;
    }

    /**
     * This method is just to compress the code to ensure initialization into one line.
     */
    private static void setup() {
        if (!isInitialized)
            initialize();
    }

    /**
     * Fetches an image from Firestorage and places it directly into a specified imageView.
     * This uses the Glide framework.
     *
     * @param id
     * @param imageView
     * @param context
     */

    public static void placeImage(String id, ImageView imageView, Context context) {

        Log.d(TAG, "sending image [" + id + "] to view [" + imageView.toString() + "]");
        // Build reference to image file in storage
        StorageReference storageRef = getRefToImage(id);

        // Download directly from StorageReference using Glide
        // (See MyAppGlideModule for Loader registration)
        GlideApp.with(context)
                .load(storageRef)
                .into(imageView);

    }

    // TODO: Write storeImageFromBitmap

    /**
     * Save the specified image to the Firebase cloud storage.
     * File will be saved with given id as filename.
     *
     * @param id
     * @param file
     */
    public static void storeImageFromFile(String id, Uri file) {

        Log.d(TAG, "storing image with id: [" + id + "] from file: [" + file.getPath() + "]");

        // build new file name for storing in Firebase storage
        String storeName = getImageStoreName(id, file.getLastPathSegment());
        Log.d(TAG, "new image name: " + storeName);

        // Create storage reference and uploading task
        StorageReference fileReference = getRefToImage(storeName);
        UploadTask uploadTask = fileReference.putFile(file);

        // register observers to monitor status
        // right now these observers don't do anything, so we never know if this failed or succeeded
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // call callback method?
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // call callback method?
            }
        });

    }


    /**
     * Creates a new filename using the supplied ID as the filename and the extension
     * from the supplied filename.  For example, if id = "123456", and fileName = "ex.png",
     * the string "123456.png" will be returned.
     *
     * @param id       An alphanumeric string that will become the new filename.
     * @param fileName The existing filename, from which the extensione will be used.
     * @return A string of the form {id}.{filename_extension}
     */
    private static String getImageStoreName(String id, String fileName) {
        return id + fileName.split(".")[1];
    }

    private static StorageReference getRefToImage(String newFile) {

        String path = imagePath + "/" + newFile;
        StorageReference imageRef = fbStorage.getReference(path);
        return imageRef;

    }


}
