package com.example.mood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_THEME = "dark_theme";

    private Button btnForward; //button object created for the forwardBtn on activity_main
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //use the chosen theme
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);

        if (useDarkTheme) {
            setTheme(R.style.DarkTheme);
        }

        ////////////////////////////////////////


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //for Switch Theme Toggle
        Switch toggle = findViewById(R.id.themeSwitch);
        toggle.setChecked(useDarkTheme);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                toggleTheme(isChecked);
            }
        });

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
        //Used to Save User's Theme choice
        public void toggleTheme(boolean darkTheme){
            SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
            editor.putBoolean(PREF_DARK_THEME, darkTheme);
            editor.apply();

            Intent intent = getIntent();
            finish();

            startActivity(intent);
        }

        /////////////////////////////////////////////////

    private void moveToActivityTwo(){ //creates the event when you click the settings button on
        //activity main
        Intent intent = new Intent(MainActivity.this, settings.class );
        startActivity(intent);
    }
}
