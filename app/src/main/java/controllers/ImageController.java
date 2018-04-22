package controllers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ImageView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.security.InvalidParameterException;

/**
 * Used to manage images
 */

public class ImageController {


    private static final String TAG = "ImageController";
    private static FirebaseStorage firebaseStorage;
    private static boolean isInitialized = false;
    private static final String imageFileExtension = ".png";

    private static void initialize() {
        if (!isInitialized) {
            firebaseStorage = FirebaseStorage.getInstance();
            isInitialized = true;
        }
    }

    /**
     *  Fetches an image from Firestorage and places it directly into a specified imageView.
     * @param firebaseURI The URI associated with Firebase storage
     * @param imageView The imageView to load the image into.
     * @param id  The id of the file to load.
     */
    public static void putImageIntoView(String firebaseURI, ImageView imageView, String id) {
        initialize();
        String filePath = firebaseURI + id + imageFileExtension;
        Log.d(TAG, "loading image (" + filePath + ") into (" + imageView.toString() + ")");
        Picasso.get()
                .load(filePath)
                .into(imageView);
    }

    public static UploadTask storeImage(String id, ImageView imageView) {

        // pull bitmap from image
        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

        // call other overload method
        return storeImage(id, bitmap);

    }


     /*public static UploadTask storeImage(String id, Bitmap image) {

        initialize();

        if (id == null || id.equals(""))
            throw new InvalidParameterException("id can't be null");
        if (image == null)
            throw new InvalidParameterException("file can't be null");

        Log.d(TAG, "storing image with id: [" + id + "] from bitmap");

        // build new file name for storing in Firebase storage
        String fileName = id + imageFileExtension;
        Log.d(TAG, "new image name: " + fileName);

        // convert image to compressed png
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG,100, outStream);

        Bitmap compressedImage = BitmapFactory.decodeStream(new ByteArrayInputStream(outStream.toByteArray()));

        // Create storage reference and uploading task
        StorageReference fileReference = firebaseStorage.getReference(fileName);
        return fileReference.putBytes(byteArrayFromBitmap(compressedImage));
    }
*/

    public static UploadTask storeImage(String id, Bitmap image) {

        initialize();

        if (id == null || id.equals(""))
            throw new InvalidParameterException("id can't be null");
        if (image == null)
            throw new InvalidParameterException("file can't be null");

        Log.d(TAG, "storing image with id: [" + id + "] from bitmap");

        String fileName = id + imageFileExtension;
        Log.d(TAG, "new image name: " + fileName);

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG,100, outStream);

        StorageReference fileReference = firebaseStorage.getReference(fileName);
        return fileReference.putBytes(outStream.toByteArray());
    }


    /**
     * Creates a byte array from a bitmap. Uses a byte buffer.
     *
     * This *could* potentially cause an out of memory error if the image being converted is
     * exceptionally large.
     *
     * @param image The bitmap image to convert.
     * @return A byte array represening the bitmap.
     */
    private static byte[] byteArrayFromBitmap(Bitmap image) {

        int imageSize = image.getRowBytes() * image.getHeight();
        ByteBuffer b = ByteBuffer.allocate(imageSize);
        image.copyPixelsToBuffer(b);
        b.rewind(); // failure to do this will cause an underflow exception
        byte[] bytes = new byte[imageSize];
        b.get(bytes, 0, bytes.length);
        return bytes;

    }



}
