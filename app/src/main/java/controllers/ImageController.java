package controllers;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.example.jakobsuell.spotd.GlideApp;
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

    public static void getImageIntoView(String id, ImageView imageView, Context context) {

        setup();

        Log.d(TAG, "sending image [" + id + "] to view [" + imageView.toString() + "]");
        // Build reference to image file in storage
        StorageReference storageRef = getRefToImage(id);

        // Download directly from StorageReference using Glide
        // (See MyAppGlideModule for Loader registration)
        GlideApp.with(context)
                .load(storageRef)
                .into(imageView);

    }



    /**
     * This saves a local file (from the device) into the cloud store (Firebase storage).
     * This method will rename the file to whatever id you provide it, while preserving the
     * file extension.
     *
     * The method will return an UploadTask object which can be used to control the upload, or
     * to create observers on for monitoring the upload.
     *
     * @param id    The id of the file.
     * @param file  The local file to upload.
     * @return An UploadTask object.
     */
    public static UploadTask storeImageFromFile(String id, Uri file) {

        setup();

        Log.d(TAG, "storing image with id: [" + id + "] from file: [" + file.getPath() + "]");

        // build new file name for storing in Firebase storage
        String storeName = getImageStoreName(id, file.getLastPathSegment());
        Log.d(TAG, "new image name: " + storeName);

        // Create storage reference and uploading task
        StorageReference fileReference = getRefToImage(storeName);
        return fileReference.putFile(file);

    }

    // TODO: Write storeImageFromBitmap

    /**
     * Creates a new filename using the supplied ID as the filename and the extension
     * from the supplied filename.  For example, if id = "123456", and fileName = "ex.png",
     * the string "123456.png" will be returned.
     *
     * @param id       An alphanumeric string that will become the new filename.
     * @param fileName The existing filename, from which the extensione will be used.
     * @return A string of the form {id}.{filename_extension}
     */
    public static String getImageStoreName(String id, String fileName) {

        return id + fileName.substring(fileName.indexOf('.'));
    }

    private static StorageReference getRefToImage(String newFile) {

        String path = imagePath + "/" + newFile;
        StorageReference imageRef = fbStorage.getReference(path);
        return imageRef;

    }


}
