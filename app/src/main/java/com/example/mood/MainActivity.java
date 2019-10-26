package com.example.mood;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnForward; //button object created for the forwardBtn on activity_main
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button submitBtn = (Button) findViewById(R.id.submitBtn); //this will handle the GET MOODY
        //button once facial recognition is implemented
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText welcomeMsgEditTxt = (EditText) findViewById(R.id.welcomeMsgEditTxt);
            }
        });

        btnForward = findViewById(R.id.forwardBtn); //finds the forwardBtn id & assigns it to btnForward
        btnForward.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                moveToActivityTwo();
            }
        }); //handles the event for when you click the settings button on activity_main
    }
    private void moveToActivityTwo(){ //creates the event when you click the settings button on
        //activity main
        Intent intent = new Intent(MainActivity.this, settings.class );
        startActivity(intent);
    }
}
