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
    private Globals globals;
    private FloatingActionButton floatingActionButton;
    private PetListType petListType;
    private List<Pet> pets;

    public ShowPetsFragment() {
    }

    public void setPetList(List<Pet> pets) {
        this.pets = pets;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show_pets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        globals = (Globals)getActivity().getApplication();

        //TODO: Make sure title changes based on what screen is being displayed
        getActivity().setTitle("Pets List");

        floatingActionButton = getView().findViewById(R.id.show_pets_fab);
        recyclerView = getView().findViewById(R.id.show_pets_recyclerview);
        layoutManager = new LinearLayoutManager(getContext());
        adapter = new PetsRecyclerAdapter(globals.firebaseURI, pets);

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

    public enum PetListType {
        MyPets("My Pets");


        private final String description;

        PetListType(String description) {
            this.description = description;
        }

    }
}
