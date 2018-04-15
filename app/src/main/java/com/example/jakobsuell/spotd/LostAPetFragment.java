package com.example.jakobsuell.spotd;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class LostAPetFragment extends Fragment {

    private LinearLayout linearLayout;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Lost a Pet");

        linearLayout = getActivity().findViewById(R.id.petLinearLayoutLost);

        for(int i = 1; i < 5; i++){
            Button testButton = new Button(getActivity());
            testButton.setText("pet1");
            testButton.setCompoundDrawablesWithIntrinsicBounds( R.mipmap.sample_1, 0, 0, 0);
            linearLayout.addView(testButton);
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lost_apet, container, false);
    }

}
