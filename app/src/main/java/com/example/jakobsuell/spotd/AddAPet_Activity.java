package com.example.jakobsuell.spotd;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AddAPet_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_apet_);
    }

    //Encapsulates ability to create itself
    public static Intent makeAddAPetIntent(Context context) {
        return new Intent(context, AddAPet_Activity.class);
    }
}
