package com.example.jakobsuell.spotd;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MyPets_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pets_);

        ImageButton btn[] = new ImageButton[10];

        LinearLayout layout = (LinearLayout) findViewById(R.id.petLinearLayout);

        for(int i = 1; i < 5; i++){
            Button testButton = new Button(this);
            layout.addView(testButton);




        }
    }

    //Do something when "Add a Pet" button is clicked
    private void setupAddAPetButton() {
        Button btn = (Button) findViewById(R.id.btn_addAPet);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyPets_Activity.this, "Clicked 'Add a Pet'.", Toast.LENGTH_SHORT).show();

                //Launch lost_my_pet activitiy
//                Intent intent = new Intent(StartMenu.this, LostMyPetActivity.class);
                Intent intent = AddAPet_Activity.makeAddAPetIntent(MyPets_Activity.this);
                startActivity(intent);
            }
        });

    }

    //Encapsulates ability to create itself
    public static Intent makeMyPetsIntent(Context context) {
        return new Intent(context, MyPets_Activity.class);
    }
}
