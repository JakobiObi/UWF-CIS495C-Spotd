package controllers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.jakobsuell.spotd.GlideApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
    private static final String imageFileExtension = "png";

    private static void initialize() {
        if (!isInitialized) {
            firebaseStorage = FirebaseStorage.getInstance();
            isInitialized = true;
        }
    }

    /**
     *  Fetches an image from Firestorage and places it directly into a specified imageView.
     *
     * @param context   The context associated with the specified imageView.
     * @param imageView The imagView to load the image into.
     * @param id  The id of the file to load.
     */
    public static void putImageIntoView(Context context, ImageView imageView, String id) {

        initialize();

        String fileName = id + imageFileExtension;
        Log.d(TAG, "loading image (" + fileName + ") into (" + imageView.toString() + ")");

        GlideApp.with(context)
                .load(firebaseStorage.getReference(fileName))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e(TAG, "failed to load image:" + e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imageView);

    }




    public static UploadTask storeImageFromBitmap(String id, Bitmap image) {

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
