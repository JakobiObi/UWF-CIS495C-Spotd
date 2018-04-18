package com.example.jakobsuell.spotd;

import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestHandler;

import java.io.IOException;
import java.io.InputStream;


class FirebaseRequestHandler extends RequestHandler {

    private final String SCHEME_FIREBASE_STORAGE = "gs";
    private final String TAG = "FirebaseRequestHandler";

    @Override
    public boolean canHandleRequest(Request data) {
        String scheme = data.uri.getScheme();
        return (SCHEME_FIREBASE_STORAGE.equals(scheme));
    }

    @Nullable
    @Override
    public Result load(Request request, int networkPolicy) throws IOException {

        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(request.uri.toString());
        StreamDownloadTask streamDownloadTask;
        InputStream inputStream;
        streamDownloadTask = storageReference.getStream();

        try {
            inputStream = Tasks.await(streamDownloadTask).getStream();
            Log.d(TAG, "Loaded " + storageReference.getPath());
            return new Result(BitmapFactory.decodeStream(inputStream), Picasso.LoadedFrom.NETWORK);
        } catch (Exception e) {
            throw new IOException();
        }

    }
}
