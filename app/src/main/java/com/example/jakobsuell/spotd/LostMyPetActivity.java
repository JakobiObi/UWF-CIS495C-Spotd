package com.example.jakobsuell.spotd;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class LostMyPetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_my_pet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayout layout = (LinearLayout) findViewById(R.id.petLinearLayoutLost);
        for(int i = 1; i < 5; i++){
            Button testButton = new Button(this);
            testButton.setText("pet1");
            testButton.setCompoundDrawablesWithIntrinsicBounds( R.mipmap.sample_1, 0, 0, 0);
            layout.addView(testButton);
        }
    }

    //Encapsulates ability to create itself
    public static Intent makeLostMyPetIntent(Context context) {
     return new Intent(context, LostMyPetActivity.class);
    }
}
