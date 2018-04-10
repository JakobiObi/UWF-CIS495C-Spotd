package com.example.jakobsuell.spotd;


import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {

    private TextView application_name;
    private TextView version_name;

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // assign views
        application_name = getView().findViewById(R.id.fragment_about_application_name);
        version_name = getView().findViewById(R.id.fragment_about_version_name);

        populateTextBoxes();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    private void populateTextBoxes() {
        version_name.setText(getVersionString());
    }

    private String getVersionString() {

        PackageManager packageManager = getContext().getPackageManager();
        String packageName = getContext().getPackageName();

        String myVersionName = "not available"; // initialize String

        try {
            myVersionName = packageManager.getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return myVersionName;

    }



}
