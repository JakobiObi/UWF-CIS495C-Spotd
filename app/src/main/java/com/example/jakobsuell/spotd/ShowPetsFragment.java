package com.example.jakobsuell.spotd;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

    public ShowPetsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_pets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO: Make sure title changes based on what screen is being displayed
        applicationController = (ApplicationController)getActivity().getApplication();

        getActivity().setTitle("Pets List");
        recyclerView = getView().findViewById(R.id.show_pets_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        List<Pet> input = getMockData();

        adapter = new PetsRecyclerAdapter(applicationController.firebaseURI, input);
        recyclerView.setAdapter(adapter);
    }

    private List<Pet> getMockData() {
        MockDataGenerator mockDataGenerator = MockDataGenerator.make();
        return mockDataGenerator.pets;
    }
}
