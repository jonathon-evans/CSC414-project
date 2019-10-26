package com.example.mood;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class settings extends AppCompatActivity {

    private Button btnPrevious; //created a button object to use to call our button id
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnPrevious = findViewById(R.id.homeBtn); //this gets the button's id and stores it in btnPrevious
        btnPrevious.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                moveToActivityOne();
            }
        }); //this is an event handler that tells what happens when btnPrevious (homeBtn) is clicked
        //in the settings activity
    }
    private void moveToActivityOne(){ //handles/creates the event
        Intent intent = new Intent(settings.this, MainActivity.class );
        startActivity(intent);
    }
}

/*
I created this file to navigate between activity_main & activity_settings
 */