package com.example.jakobsuell.spotd;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MyPets_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pets_);
    }

    //Encapsulates ability to create itself
    public static Intent makeMyPetsIntent(Context context) {
        return new Intent(context, MyPets_Activity.class);
    }
}
