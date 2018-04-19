package com.example.jakobsuell.spotd;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import controllers.ImageController;
import enums.AnimalStatus;
import enums.AnimalType;
import models.Pet;


public class PetDetailFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "PetDetailFragment";
    private final int REQUEST_IMAGE_CAPTURE = 1313;
    private static final String PET_KEY = "pet";
    private final static String TITLE_KEY = "title";
    private Globals globals;
    private ImageView petPhoto;
    private EditText petName;
    private Spinner type;
    private Spinner status;
    private Button saveInfo;

    private String title;
    private Pet pet;

    public PetDetailFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pet_detail, container, false);
    }

    public static PetDetailFragment newInstance(Pet pet, String title) {
        PetDetailFragment petDetail = new PetDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PET_KEY, pet);
        bundle.putString(TITLE_KEY, title);
        petDetail.setArguments(bundle);
        return petDetail;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pet = getArguments().getParcelable(PET_KEY);
        title = getArguments().getParcelable(TITLE_KEY);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        globals = (Globals)getActivity().getApplication();
        getAllViews();

        setupSpinners();
        setFieldValues(pet);

        if (isThisUserOwnerOrFinder(FirebaseAuth.getInstance(), pet)) {
            setFieldEditing(true);
            setPhotoClickListener();
        } else {
            setFieldEditing(false);
        }

    }

    private void getAllViews() {
        petPhoto = getActivity().findViewById(R.id.pet_detail_pet_photo);
        petName = getActivity().findViewById(R.id.pet_detail_pet_name);
        status  = getActivity().findViewById(R.id.pet_detail_status_spinner);
        type = getActivity().findViewById(R.id.pet_detail_animaltype_spinner);
        saveInfo = getActivity().findViewById(R.id.btn_savePetInfo);
    }

    private void setFieldValues(Pet pet) {
        if (pet == null) {
            return;
        }
        ImageController.putImageIntoView(globals.firebaseURI, petPhoto, pet.getPetID());
        petName.setText(pet.getName());
        status.setSelection(pet.getStatus().ordinal());
        type.setSelection(pet.getAnimalType().ordinal());
    }

    private void setupSpinners() {
        ArrayAdapter<String> statusSpinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, AnimalStatus.getDescriptions());
        statusSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status.setAdapter(statusSpinnerAdapter);

        ArrayAdapter<String> typeSpinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, AnimalType.getDescriptions());
        typeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(typeSpinnerAdapter);
    }

    private boolean isThisUserOwnerOrFinder(FirebaseAuth firebaseAuth, Pet pet) {
        if (firebaseAuth == null) {
            return false;
        }
        String userID = firebaseAuth.getCurrentUser().getUid();
        return (userID.equals(pet.getOwnerID()) || userID.equals(pet.getFinderID()));
    }

    private void setFieldEditing(boolean enabled) {
        petName.setEnabled(enabled);
        status.setEnabled(enabled);
        type.setEnabled(enabled);
        if (enabled) {
            saveInfo.setVisibility(View.VISIBLE);
        } else {
            saveInfo.setVisibility(View.INVISIBLE);
        }
    }

    private void setPhotoClickListener() {
        petPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    invokeCamera();
                } else {
                    String[] permissionRequested = {android.Manifest.permission.CAMERA};
                    requestPermissions(permissionRequested, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                invokeCamera();
            }
        }
    }

    private void invokeCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            petPhoto.setImageBitmap(bitmap);
        } else {
            Log.e(TAG, "error getting image - camera activity returned null intent");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // TODO: call the save routine here
        Snackbar.make(view, "Save!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}