package com.example.jakobsuell.spotd;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import static android.app.Activity.RESULT_OK;
import static android.support.v4.content.PermissionChecker.checkSelfPermission;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddAPetFragment extends Fragment {
    public AddAPetFragment() {
        // Required empty public constructor
    }
    Button pictureButton;
    ImageView petPicture;
    private static final int REQUEST_IMAGE_CAPTURE=1313;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_apet, container, false);

    }
    private void invokeCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,0);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO: Make sure title changes based on what screen is being displayed
        getActivity().setTitle("Add a Pet");
        pictureButton = (Button) view.findViewById(R.id.takePictureButton);
        petPicture = (ImageView)view.findViewById(R.id.selectedPicture);

        pictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getActivity(),android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    invokeCamera();
                }
                else{
                    String[] permissionRequested = {android.Manifest.permission.CAMERA};
                    requestPermissions(permissionRequested, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_IMAGE_CAPTURE){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                invokeCamera();
            }
            else{

            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
        petPicture.setImageBitmap(bitmap);
    }
}