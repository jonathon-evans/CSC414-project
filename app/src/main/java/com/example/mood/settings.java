package com.example.mood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class settings extends AppCompatActivity {

    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_THEME = "dark_theme";


    private Button btnPrevious; //created a button object to use to call our button id
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //use the chosen theme
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);

        if (useDarkTheme) {
            setTheme(R.style.DarkTheme);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //for Switch Theme Toggle
        Switch toggle = findViewById(R.id.settingsThemeSwitch);
        toggle.setChecked(useDarkTheme);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                toggleTheme(isChecked);
            }
        });


        btnPrevious = findViewById(R.id.homeBtn); //this gets the button's id and stores it in btnPrevious
        btnPrevious.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                moveToActivityOne();
            }
        }); //this is an event handler that tells what happens when btnPrevious (homeBtn) is clicked
        //in the settings activity
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

    private void moveToActivityOne(){ //handles/creates the event
        Intent intent = new Intent(settings.this, MainActivity.class );
        startActivity(intent);
    }
}

/*
I created this file to navigate between activity_main & activity_settings
 */