package com.example.jakobsuell.spotd;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import controllers.FirestoreController;
import controllers.ImageController;
import controllers.PetController;
import enums.AnimalStatus;
import enums.AnimalType;
import models.Pet;


public class PetDetailFragment extends Fragment implements PetPickerReturnHandler, PetDetailBottomSheetDialog.BottomSheetListener {

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
    private Button showActions;

    private String title;
    private Pet pet;
    private Bitmap petBitmap;
    private Uri cameraPhotoURI;

    private boolean searchMode;
    private static int returnCount = 0;

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
        if (!searchMode) {
            setActionButtonListener();
        }

    }

    private void setActionButtonListener() {
        showActions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PetDetailBottomSheetDialog petDetailBottomSheetDialog = configureContextButtonSheet();
                petDetailBottomSheetDialog.show(getActivity().getSupportFragmentManager(), "bottomSheet");
            }
        });
    }

    private PetDetailBottomSheetDialog configureContextButtonSheet() {
        PetDetailBottomSheetDialog petDetailBottomSheetDialog = new PetDetailBottomSheetDialog();
        if (pet == null) {
            return null;
        }
        AnimalStatus petStatus = pet.getStatus();
        if (isUserTheOwner(pet)) {
            petDetailBottomSheetDialog
                    .setDeletePetEnabled(true)
                    .setReportLostEnabled(petStatus == AnimalStatus.Home)
                    .setReturnHomeEnabled(petStatus == AnimalStatus.Found || petStatus == AnimalStatus.Lost);
        } else if (isUserTheFinder(pet)) {
            petDetailBottomSheetDialog
                    .setDeletePetEnabled(petStatus == AnimalStatus.Found);
        } else {
            petDetailBottomSheetDialog
                    .setReportFoundEnabled(petStatus == AnimalStatus.Home)
                    .setClaimPetEnabled(petStatus == AnimalStatus.Found)
                    .setReportFoundEnabled(petStatus == AnimalStatus.Lost);
        }
        petDetailBottomSheetDialog.setBottomSheetListener(this);
        return petDetailBottomSheetDialog;
    }

    private void setEditingState() {
        if (pet == null) {
            setEditable(true);
            showActions.setVisibility(View.INVISIBLE);
            return;
        }
        if (isUserTheOwner(pet)) {
            setEditable(true);
        } else {
            if (isUserTheFinder(pet) && pet.getStatus() == AnimalStatus.Found) {
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
        showActions = getActivity().findViewById(R.id.pet_detail_btn_show_actions);
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

    private boolean isUserTheFinder(Pet pet) {
        String userID = globals.currentUser.getUserID();
        String finderID = pet.getFinderID();
        return (userID.equals(finderID));
    }

    private boolean isUserTheOwner(Pet pet) {
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
                setSearchButtonListener();
            } else {
                setSaveButtonListener();
            }
            setPhotoClickListener();
        } else {
            saveInfo.setVisibility(View.INVISIBLE);
        }
    }

    private void setSearchButtonListener() {
        saveInfo.setVisibility(View.VISIBLE);
        petName.setVisibility(View.INVISIBLE);
        saveInfo.setText("Search...");
        saveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePetDataToInstance();
                searchLostPets();
            }
        });
    }

    private void setSaveButtonListener() {
        saveInfo.setVisibility(View.VISIBLE);
        saveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (petName.getText().toString().equals("") || petName.getText().toString() == null) {
                    Toast.makeText(getContext(), "A pet name is required.",  Toast.LENGTH_LONG);
                    return;
                }
                savePetDataToInstance();
                saveData();
            }
        });
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

    private void savePetDataToInstance() {
        petBitmap = ((BitmapDrawable)petPhoto.getDrawable()).getBitmap();
        if (searchMode) {
            pet = new Pet(
                    "",
                    AnimalType.valueOf(type.getSelectedItem().toString()),
                    new ArrayList<String>(),
                    AnimalStatus.Found,
                    null,
                    globals.currentUser.getUserID());
        } else {
            pet = new Pet(
                    "",
                    AnimalType.valueOf(type.getSelectedItem().toString()),
                    new ArrayList<String>(),
                    AnimalStatus.valueOf(status.getSelectedItem().toString()),
                    globals.currentUser.getUserID(),
                    null);
        }
    }

    private void saveData() {
        Log.d(TAG, "saveData: entered");
        FirestoreController.savePet(FirebaseFirestore.getInstance(), pet).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Log.d(TAG, "pet stored successfully");
                ImageController.storeImage(pet.getPetID(), petBitmap).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "pet image stored successfully");
                        Snackbar.make(getView(), "Pet has been saved.", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                        ((MainActivity)getActivity()).onBackPressed();
                    }
                });
            }
        });
    }

    private void searchLostPets() {
        Log.d(TAG, "searchLostPets: entered");
        final PetPickerReturnHandler petPickerReturnHandler = this;
        AnimalType animalType = AnimalType.valueOf(type.getSelectedItem().toString());
        FirestoreController.readPets(
                FirebaseFirestore.getInstance(),
                "animalType", animalType.name(),
                "status", AnimalStatus.Lost.name()
        ).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d(TAG, "searchLostPets: query succeeded");
                ArrayList<Pet> pets = FirestoreController.processPetsQuery(queryDocumentSnapshots);
                Log.d(TAG, "searchLostPets: found " + pets.size() + " pets");
                if (pets.size() == 0) {
                    OnPetPickResult(null);
                }
                ShowPetsFragment showLostPetsFragment = ShowPetsFragment.newInstance(pets, ShowPetsFragment.PetListOptions.TopButtonOnly, null,"Searching Pets");
                showLostPetsFragment.setPetPickerReturnHandler(petPickerReturnHandler);
                ((MainActivity)getActivity()).displayFragment(showLostPetsFragment);
            }
        });
    }

    private void searchHomePets() {
        Log.d(TAG, "searchHomePets: entered");
        final PetPickerReturnHandler petPickerReturnHandler = this;
        AnimalType animalType = AnimalType.valueOf(type.getSelectedItem().toString());
        FirestoreController.readPets(
                FirebaseFirestore.getInstance(),
                "animalType", animalType.name(),
                "status", AnimalStatus.Home.name()
        ).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d(TAG, "searchHomePets: query succeeded");
                ArrayList<Pet> pets = FirestoreController.processPetsQuery(queryDocumentSnapshots);
                Log.d(TAG, "searchHomePets: found " + pets.size() + " pets");
                if (pets.size() == 0) {
                    OnPetPickResult(null);
                }
                ShowPetsFragment showLostPetsFragment = ShowPetsFragment.newInstance(pets, ShowPetsFragment.PetListOptions.TopButtonOnly, null,"Searching Pets");
                showLostPetsFragment.setPetPickerReturnHandler(petPickerReturnHandler);
                ((MainActivity)getActivity()).displayFragment(showLostPetsFragment);
            }
        });
    }

    @Override
    public void OnPetPickResult(Pet pet) {
        if (pet == null) {
            Log.d(TAG, "OnPetPickResult: called with null pet");
            returnCount += 1;
            Log.d(TAG, "OnPetPickResult: count is " + returnCount);
            if (returnCount == 1) {
                Log.d(TAG, "OnPetPickResult: searching home pets");
                searchHomePets();
            } else if (returnCount == 2) {
                status.setSelection(AnimalStatus.Found.ordinal());
                saveData();
            }
        } else {
            Log.d(TAG, "OnPetPickResult: called with pet:");
            pet.show();
            PetDetailFragment petDetailFragment = PetDetailFragment.newInstance(pet, "Pet Detail", false);
            ((MainActivity)getContext()).displayFragment(petDetailFragment);
        }
    }

    @Override
    public void OnBottomSheetButtonClick(int id) {
        Log.d(TAG, "OnBottomSheetButtonClick: got click with id " + id);
        switch (id){
            case R.id.pet_detail_bottom_sheet_claim_pet:
                Log.d(TAG, "claim pet");
                break;
            case R.id.pet_detail_bottom_sheet_delete_pet:
                Log.d(TAG, "delete pet");
                FirestoreController.deletePet(FirebaseFirestore.getInstance(), pet.getPetID());
                ((MainActivity)getActivity()).onBackPressed();
                break;
            case R.id.pet_detail_bottom_sheet_report_found:
                Log.d(TAG, "report found");
                pet = PetController.makePetFound(pet, globals.currentUser);
                populateFieldValues();
                break;
            case R.id.pet_detail_bottom_sheet_report_lost:
                Log.d(TAG, "report lost");
                pet = PetController.makePetLost(pet);
                populateFieldValues();
                break;
            case R.id.pet_detail_bottom_sheet_return_home:
                Log.d(TAG, "return home");
                pet = PetController.makePetHome(pet);
                populateFieldValues();
                break;
        }

    }
}