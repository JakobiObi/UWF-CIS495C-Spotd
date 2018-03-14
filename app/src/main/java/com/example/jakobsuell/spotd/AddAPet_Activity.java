package com.example.jakobsuell.spotd;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AddAPet_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_apet_);
        setupSavePetInfoButton();
    }

    //Do something when "Save Pet Info" button is clicked
    private void setupSavePetInfoButton() {
        Button btn = (Button) findViewById(R.id.btn_savePetInfo);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddAPet_Activity.this, "Clicked 'Save Pet Info'.\nNo Action tied to this button.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //Encapsulates ability to create itself
    public static Intent makeAddAPetIntent(Context context) {
        return new Intent(context, AddAPet_Activity.class);
    }
}
