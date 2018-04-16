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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import controllers.LoginController;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {


    private final String TAG = "ProfileFragment";
    private Button showMyPets;
    private Button saveProfile;
    private Button addAPet;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("My Profile");

        // find views for buttons
        saveProfile = getView().findViewById(R.id.profileFragment_btnSaveProfile);
        showMyPets = getView().findViewById(R.id.profileFragment_btnShowMyPets);
        addAPet = getView().findViewById(R.id.profileFragment_btnAddAPet);

        // set click listeners
        saveProfile.setOnClickListener(this);
        showMyPets.setOnClickListener(this);
        addAPet.setOnClickListener(this);

        setProfileInfo();
    }

    private void setProfileInfo() {

        TextView userName = getActivity().findViewById(R.id.profileFragment_editName);
        TextView emailAccount = getActivity().findViewById(R.id.profileFragment_editEmail);
        ImageView profileImageView = getActivity().findViewById(R.id.profileFragment_profilePicture);

        try {
            userName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            emailAccount.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        } catch (NullPointerException ex) {
            // if we can't set them, just hide them
            userName.setText("");
            emailAccount.setText("");
        }

        putProfilePictureOnNavigationMenu(profileImageView);
    }

    private void putProfilePictureOnNavigationMenu(ImageView imageView) {
        Picasso.get()
                .load(LoginController.getUserPictureUri(FirebaseAuth.getInstance()))
                .placeholder(R.drawable.profile_placeholder)
                .into(imageView);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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
                ((MainActivity) getActivity()).displayFragment(new ShowPetsFragment());
                break;
            case R.id.profileFragment_btnAddAPet:
                Log.d(TAG, "profileFragment_btnAddAPet clicked");
                ((MainActivity) getActivity()).displayFragment(new AddAPetFragment());
                break;
        }
    }
}
