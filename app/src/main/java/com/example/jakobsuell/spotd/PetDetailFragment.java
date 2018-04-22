package com.example.jakobsuell.spotd;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import controllers.ImageController;
import enums.AnimalStatus;
import enums.AnimalType;
import models.Pet;


public class PetDetailFragment extends Fragment {

    private static final String TAG = "PetDetailFragment";
    private static final int REQUEST_TAKE_PHOTO = 1;
    private final int REQUEST_IMAGE_CAPTURE = 1313;
    private static final String PET_KEY = "pet";
    private final static String TITLE_KEY = "title";
    private final static String DISPLAY_MODE = "mode";

    private Globals globals;

    private ImageView petPhoto;
    private EditText petName;
    private Spinner type;
    private Spinner status;
    private Button saveInfo;

    private String title;
    private Pet pet;
    private Uri cameraPhotoURI;

    private boolean searchMode;

    public PetDetailFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pet_detail, container, false);
    }

    public static PetDetailFragment newInstance(Pet pet, String title, boolean searchMode) {
        PetDetailFragment petDetail = new PetDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PET_KEY, pet);
        bundle.putString(TITLE_KEY, title);
        bundle.putBoolean(DISPLAY_MODE, searchMode);
        petDetail.setArguments(bundle);
        return petDetail;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pet = getArguments().getParcelable(PET_KEY);
        title = getArguments().getString(TITLE_KEY);
        searchMode = getArguments().getBoolean(DISPLAY_MODE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        globals = (Globals)getActivity().getApplication();

        getActivity().setTitle(title);
        getAllViews();
        setupSpinners();
        populateFieldValues();

        setEditingState();

        // determine what actions are available

    }

    private void setEditingState() {
        if (pet == null) {
            setEditable(true);
            return;
        }
        if (isOwner(pet)) {
            setEditable(true);
        } else {
            if (isFinder(pet) && pet.getStatus() == AnimalStatus.Found) {
                setEditable(true);
            } else {
                setEditable(false);
            }
        }
    }

    private void getAllViews() {
        petPhoto = getActivity().findViewById(R.id.pet_detail_pet_photo);
        petName = getActivity().findViewById(R.id.pet_detail_pet_name);
        status  = getActivity().findViewById(R.id.pet_detail_status_spinner);
        type = getActivity().findViewById(R.id.pet_detail_animaltype_spinner);
        saveInfo = getActivity().findViewById(R.id.btn_savePetInfo);
    }

    private void populateFieldValues() {
        if (pet == null) {
            return;
        }
        ImageController.putImageIntoView(globals.firebaseURI, petPhoto, pet.getPetID());
        petName.setText(pet.getName());
        status.setSelection(pet.getStatus().ordinal());
        type.setSelection(pet.getAnimalType().ordinal());
    }

    private void setupSpinners() {

        if (searchMode) {
            status.setVisibility(View.INVISIBLE);
        } else {
            ArrayAdapter<String> statusSpinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, AnimalStatus.getDescriptions());
            statusSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            status.setAdapter(statusSpinnerAdapter);
        }

        ArrayAdapter<String> typeSpinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, AnimalType.getDescriptions());
        typeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(typeSpinnerAdapter);
    }

    private boolean isFinder(Pet pet) {
        String userID = globals.currentUser.getUserID();
        String finderID = pet.getFinderID();
        return (userID.equals(finderID));
    }

    private boolean isOwner(Pet pet) {
        String userID = globals.currentUser.getUserID();
        String ownerID = pet.getOwnerID();
        return (userID.equals(ownerID));
    }

    private void setEditable(boolean enabled) {
        petName.setEnabled(enabled);
        status.setEnabled(enabled);
        type.setEnabled(enabled);
        if (enabled) {
            if (searchMode) {
                saveInfo.setVisibility(View.VISIBLE);
                saveInfo.setText("Search...");
                saveInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        searchPet();
                    }
                });
            }
            saveInfo.setVisibility(View.VISIBLE);
            saveInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveData();
                }
            });
            setPhotoClickListener();
        } else {
            saveInfo.setVisibility(View.INVISIBLE);
        }
    }

    private void setPhotoClickListener() {
        petPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (isPermissionGivenForCamera()) {
                   invokeCamera();
               } else {
                   askForCameraPermission();
               }
            }
        });
    }

    private boolean isPermissionGivenForCamera() {
        return (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void askForCameraPermission() {
        String[] permissionRequested = {android.Manifest.permission.CAMERA};
        requestPermissions(permissionRequested, REQUEST_IMAGE_CAPTURE);
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
        Log.d(TAG, "invoking camera");

        Intent cameraTakePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (cameraTakePicture.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                Log.e(TAG, "could not create photo file: " + e);
                return;
            }
            cameraPhotoURI = FileProvider.getUriForFile(getContext(),
                    "com.example.android.fileprovider",
                    photoFile);
            cameraTakePicture.putExtra(MediaStore.EXTRA_OUTPUT, cameraPhotoURI);
            startActivityForResult(cameraTakePicture, REQUEST_TAKE_PHOTO);
        } else {
            Log.e(TAG, "no app available to handle camera intent");
        }
    }

    private File createImageFile() throws IOException {
        long timeStamp = System.currentTimeMillis();
        String imageFileName = "pet_" + timeStamp;
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Picasso.get()
                .load(cameraPhotoURI)
                .into(petPhoto);
    }

    private void saveData() {
        Log.d(TAG, "saved");
        Snackbar.make(getView(), "Save!", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    private void searchPet() {

    }

}