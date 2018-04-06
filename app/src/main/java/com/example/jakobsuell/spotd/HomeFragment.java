package com.example.jakobsuell.spotd;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private String TAG = "Home Fragment";


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Home");

        setupLostMyPetButton();
        setupFoundAPetButton();
    }

    //Do something when "Lost My Pet" button is clicked
    private void setupLostMyPetButton() {
        Button btn = (Button) getView().findViewById(R.id.lostPetButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Clicked 'Lost My Pet'.", Toast.LENGTH_SHORT).show();


                displayFragment(new LostAPetFragment());
            }
        });
    }

    //Do something when "Found a Pet" button is clicked
    private void setupFoundAPetButton() {
        Button btn = (Button) getView().findViewById(R.id.foundPetButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Clicked 'Found a Pet'.", Toast.LENGTH_SHORT).show();

                displayFragment(new FoundAPetFragment());
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    /**
     * Show a fragment.
     * Loads the specified fragment into the specified container.  Tries to be smart about using
     * the correct transaction call depending on whether this is an initial call or not.
     *
     * @param fragment The fragment to display.
     */
    private void displayFragment(Fragment fragment) {

        FragmentManager fragmentManager = getFragmentManager();

        Log.d(TAG, "loading fragment " + fragment.toString() + " to " + R.id.fragment_container);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // check if there is already a fragment
        if (fragmentManager.getFragments().size() > 0) {
            // use replace to remove previous fragment
            Log.d(TAG, "replacing current fragment");
            fragmentTransaction.replace(R.id.fragment_container, fragment);
        } else {
            Log.d(TAG, "adding initial fragment");
            fragmentTransaction.add(R.id.fragment_container, fragment);
        }

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    public void onBackPressed() {
            FragmentManager fm = getFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack();
            } else {
                super.getActivity().onBackPressed();
            }
        }
}
