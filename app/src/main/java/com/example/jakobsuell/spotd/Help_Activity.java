package com.example.jakobsuell.spotd;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Help_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_);
    }

    //Encapsulates ability to create itself
    public static Intent makeHelpActivityIntent(Context context) {
        return new Intent(context, Help_Activity.class);
    }
}
