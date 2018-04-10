package com.example.jakobsuell.spotd;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {


    private final String TAG = "ProfileFragment";
    private Button showMyPets;
    private Button saveProfile;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("My Profile");

        // find views for buttons
        saveProfile = getView().findViewById(R.id.profileFragment_btnSaveProfile);
        showMyPets = getView().findViewById(R.id.profileFragment_btnShowMyPets);

        // set click listeners
        saveProfile.setOnClickListener(this);
        showMyPets.setOnClickListener(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profileFragment_btnSaveProfile:
                Log.d(TAG, "profileFragment_btnSaveProfile clicked");
                break;
            case R.id.profileFragment_btnShowMyPets:
                Log.d(TAG, "profileFragment_btnShowMyPets clicked");
                //TODO: Put in call to MyPets Fragment
                //((MainActivity) getActivity()).displayFragment(new PetListFragment());
                break;
        }
    }
}
