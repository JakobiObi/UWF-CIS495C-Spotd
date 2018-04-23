package com.example.jakobsuell.spotd;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class PetDetailBottomSheetDialog extends BottomSheetDialogFragment {

    BottomSheetListener bottomSheetListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pet_detail_bottom_sheet, container, false);
        return v;
    }

    public void setBottomSheetListener(BottomSheetListener bottomSheetListener) {
        this.bottomSheetListener = bottomSheetListener;
    }

    public interface BottomSheetListener {
        void OnBottomSheetButtonClick(int id);
    }

}
