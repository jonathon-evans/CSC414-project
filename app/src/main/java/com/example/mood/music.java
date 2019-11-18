package com.example.mood;
import org.tensorflow.lite.Interpreter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.provider.MediaStore;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import androidx.appcompat.app.AppCompatActivity;

public class music extends AppCompatActivity {

    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_THEME = "dark_theme";

    private Button btnMoveToHome;

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
        setContentView(R.layout.activity_music);

        //for Switch Theme Toggle
        Switch toggle = findViewById(R.id.lightDarkMusicSwitch);
        toggle.setChecked(useDarkTheme);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                toggleTheme(isChecked);
            }
        });

        btnMoveToHome = findViewById(R.id.musicHomeBtn);
        btnMoveToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToHome();
            }
        });

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

    private void moveToHome(){
        Intent moveToHomeIntent = new Intent(music.this, MainActivity.class);
        startActivity(moveToHomeIntent);
    }

}
