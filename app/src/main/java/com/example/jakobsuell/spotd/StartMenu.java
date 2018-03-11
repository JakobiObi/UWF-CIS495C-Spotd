package com.example.jakobsuell.spotd;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class StartMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupLostMyPetButton();
        setupFoundAPetButton();
    }

    //Do something when "Lost My Pet" button is clicked
    private void setupLostMyPetButton() {
        Button btn = (Button) findViewById(R.id.lostPetButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StartMenu.this, "Clicked 'Lost My Pet'.", Toast.LENGTH_SHORT).show();

                //Launch lost_my_pet activitiy
//                Intent intent = new Intent(StartMenu.this, LostMyPetActivity.class);
                Intent intent = LostMyPetActivity.makeLostMyPetIntent(StartMenu.this);
                startActivity(intent);
            }
        });

    }

    //Do something when "Found a Pet" button is clicked
    private void setupFoundAPetButton() {
        Button btn = (Button) findViewById(R.id.foundPetButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StartMenu.this, "Clicked 'Found a Pet'.", Toast.LENGTH_SHORT).show();

                //Launch found_a_pet activitiy
//                Intent intent = new Intent(StartMenu.this, FoundAPet.class);
                Intent intent = FoundAPet.makeFoundAPetIntent(StartMenu.this);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
