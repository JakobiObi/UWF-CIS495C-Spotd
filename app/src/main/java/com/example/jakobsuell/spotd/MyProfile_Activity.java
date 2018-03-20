package com.example.jakobsuell.spotd;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MyProfile_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_);

        setupMyPetsButton();
    }

    //Encapsulates ability to create itself
    public static Intent makeMyProfileIntent(Context context) {
        return new Intent(context, MyProfile_Activity.class);
    }

    //Do something when "My Pets" button is clicked
    private void setupMyPetsButton() {
        Button btn = (Button) findViewById(R.id.btn_MyPets);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyProfile_Activity.this, "Clicked 'My Pets'.", Toast.LENGTH_SHORT).show();

                //Launch lost_my_pet activitiy
//                Intent intent = new Intent(StartMenu.this, LostMyPetActivity.class);
                Intent intent = MyPets_Activity.makeMyPetsIntent(MyProfile_Activity.this);
                startActivity(intent);
            }
        });

    }
}
