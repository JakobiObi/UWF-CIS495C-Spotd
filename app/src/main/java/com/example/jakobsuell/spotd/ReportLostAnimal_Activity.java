package com.example.jakobsuell.spotd;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ReportLostAnimal_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_lost_animal_);
    }

    //Encapsulates ability to create itself
    public static Intent makeReportLostAnimalActivityIntent(Context context) {
        return new Intent(context, ReportLostAnimal_Activity.class);
    }
}