package com.example.jakobsuell.spotd;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import controllers.FirestoreController;
import enums.AnimalStatus;
import enums.AnimalType;
import models.Pet;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "Home Fragment";
    private Button lostPetButton;
    private Button foundPetButton;
    private Button lostPetsNotifierButton;
    private ArrayList<Pet> lostPets;
    private boolean isQueryStagnant;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Home");

        lostPetButton = getView().findViewById(R.id.btn_home_lost_pet);
        foundPetButton = getView().findViewById(R.id.btn_home_found_pet);
        lostPetsNotifierButton = getView().findViewById(R.id.btn_home_browse_lost_pets);

        setupButtonListeners();
        queryForLostPets();
        isQueryStagnant = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isQueryStagnant) {
            queryForLostPets();
            isQueryStagnant = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isQueryStagnant = true;
    }

    private void setupButtonListeners() {
        lostPetButton.setOnClickListener(this);
        foundPetButton.setOnClickListener(this);
        lostPetsNotifierButton.setOnClickListener(this);
    }

    private void queryForLostPets() {
        FirestoreController.readPets(
                FirebaseFirestore.getInstance(),
                "status",
                AnimalStatus.Lost.name()
        ).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                lostPets = queryResultToPetList(task.getResult());
                if (lostPets.size() == 0) {
                    lostPetsNotifierButton.setVisibility(View.INVISIBLE);
                } else {
                    lostPetsNotifierButton.setVisibility(View.VISIBLE);
                    lostPetsNotifierButton.setText(lostPets.size() + " pets reported missing");
                }

            }
        });
    }

    private ArrayList<Pet> queryResultToPetList(QuerySnapshot snapshot) {
        ArrayList<Pet> pets = new ArrayList<>();
        for (DocumentSnapshot document : snapshot) {
            pets.add(document.toObject(Pet.class));
        }
        return pets;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.btn_home_lost_pet):
                ((MainActivity)getActivity()).displayFragment(new LostAPetFragment());
                break;
            case (R.id.btn_home_found_pet):
                PetDetailFragment petDetailFragment = PetDetailFragment.newInstance(null, "Found a Pet", true);
                ((MainActivity)getActivity()).displayFragment(petDetailFragment);
                break;
            case (R.id.btn_home_browse_lost_pets):
                if (lostPets == null) {
                    return;
                } else {
                    ShowPetsFragment listFragment = ShowPetsFragment.newInstance(lostPets, ShowPetsFragment.PetListOptions.NoButtons,null, "Pets Reported Missing");
                    ((MainActivity) getActivity()).displayFragment(listFragment);
                }
                break;
        }
    }
}
