package com.example.jakobsuell.spotd;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout.LayoutParams;

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
    private Button topButton;

    public ShowPetsFragment() {
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
        topButton = getView().findViewById(R.id.show_pets_top_button);
        layoutManager = new LinearLayoutManager(getContext());
        adapter = new PetsRecyclerAdapter(applicationController.firebaseURI, pets);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        setupTopButton();
        setupAddButton();
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

    private void setupAddButton() {
        if (petListType == PetListType.MyPets) {
            hideAddButtonDuringScroll();
            attachAddButtonListener();
        } else {
            floatingActionButton.hide();
        }
    }

    private void setupTopButton() {

        if (petListType != PetListType.FoundPetPicker && petListType != PetListType.LostPetPicker) {
            topButton.setVisibility(View.INVISIBLE);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            recyclerView.setLayoutParams(layoutParams);
        } else {
            topButton.setText(getActivity().getResources().getString(R.string.show_petlist_topbutton_not_found));
            attachTopButtonListener();
        }

    }

    private void attachTopButtonListener() {

        topButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Top button clicked", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    private void hideAddButtonDuringScroll() {

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

    private void attachAddButtonListener() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add button clicked", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public enum PetListType {
        MyPets,
        LostPetPicker,
        FoundPetPicker,
        Browse
    }

}
