package com.example.jakobsuell.spotd;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import models.Pet;


public class ShowPetsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ApplicationController applicationController;
    private FloatingActionButton floatingActionButton;
    private PetListType petListType;
    private List<Pet> pets;
    private String title = "Pets List";

    public ShowPetsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show_pets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        applicationController = (ApplicationController)getActivity().getApplication();

        getActivity().setTitle(title);

        floatingActionButton = getView().findViewById(R.id.show_pets_fab);
        recyclerView = getView().findViewById(R.id.show_pets_recyclerview);
        layoutManager = new LinearLayoutManager(getContext());
        adapter = new PetsRecyclerAdapter(applicationController.firebaseURI, pets);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // hide floating action bar when scrolling
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && floatingActionButton.isShown()) {
                    floatingActionButton.hide();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    floatingActionButton.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    public ShowPetsFragment setPetList(List<Pet> pets) {
        this.pets = pets;
        return this;
    }

    public ShowPetsFragment setPetListType(PetListType type) {
        this.petListType = type;
        return this;
    }

    public ShowPetsFragment setTitle(String title) {
        this.title = title;
        return this;
    }

    public enum PetListType {
        MyPets,
        LostPetPicker,
        FoundPetPicker
    }

}
