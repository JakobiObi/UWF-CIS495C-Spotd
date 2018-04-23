package com.example.jakobsuell.spotd;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import enums.AnimalStatus;


public class PetDetailBottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener {

    private BottomSheetListener bottomSheetListener;
    private Button reportLost;
    private Button reportFound;
    private Button returnHome;
    private Button claimPet;
    private Button deletePet;

    private boolean isReportLostEnabled = false;
    private boolean isReportFoundEnabled = false;
    private boolean isReturnHomeEnabled = false;
    private boolean isClaimPetEnabled = false;
    private boolean isDeletePetEnabled = false;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pet_detail_bottom_sheet, container, false);
        getViews(v);
        setListeners();
        setButtonVisibility();
        return v;
    }

    public void setBottomSheetListener(BottomSheetListener bottomSheetListener) {
        this.bottomSheetListener = bottomSheetListener;
    }

    @Override
    public void onClick(View view) {
        bottomSheetListener.OnBottomSheetButtonClick(view.getId());
        dismiss();
    }

    private void setButtonVisibility() {
        if (!isReportLostEnabled)
            reportLost.setVisibility(View.INVISIBLE);
        if (!isReportFoundEnabled)
            reportFound.setVisibility(View.INVISIBLE);
        if (!isReturnHomeEnabled)
            returnHome.setVisibility(View.INVISIBLE);
        if (!isClaimPetEnabled)
            claimPet.setVisibility(View.INVISIBLE);
        if (!isDeletePetEnabled)
            deletePet.setVisibility(View.INVISIBLE);
    }

    private void getViews(View view) {
        reportLost = view.findViewById(R.id.pet_detail_bottom_sheet_report_lost);
        reportFound = view.findViewById(R.id.pet_detail_bottom_sheet_report_found);
        returnHome = view.findViewById(R.id.pet_detail_bottom_sheet_return_home);
        claimPet = view.findViewById(R.id.pet_detail_bottom_sheet_claim_pet);
        deletePet = view.findViewById(R.id.pet_detail_bottom_sheet_delete_pet);
    }

    private void setListeners() {
        reportLost.setOnClickListener(this);
        reportFound.setOnClickListener(this);
        returnHome.setOnClickListener(this);
        claimPet.setOnClickListener(this);
        deletePet.setOnClickListener(this);
    }

    public interface BottomSheetListener {
        void OnBottomSheetButtonClick(int id);
    }

    public PetDetailBottomSheetDialog setReportLostEnabled(boolean reportLostEnabled) {
        isReportLostEnabled = reportLostEnabled;
        return this;
    }

    public PetDetailBottomSheetDialog setReportFoundEnabled(boolean reportFoundEnabled) {
        isReportFoundEnabled = reportFoundEnabled;
        return this;
    }

    public PetDetailBottomSheetDialog setReturnHomeEnabled(boolean returnHomeEnabled) {
        isReturnHomeEnabled = returnHomeEnabled;
        return this;
    }

    public PetDetailBottomSheetDialog setClaimPetEnabled(boolean claimPetEnabled) {
        isClaimPetEnabled = claimPetEnabled;
        return this;
    }

    public PetDetailBottomSheetDialog setDeletePetEnabled(boolean deletePetEnabled) {
        isDeletePetEnabled = deletePetEnabled;
        return this;
    }
}
