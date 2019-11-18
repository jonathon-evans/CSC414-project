package com.example.mood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import androidx.appcompat.app.AppCompatActivity;

import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;


public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_THEME = "dark_theme";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Interpreter tflite;
    private Button btnForward; //button object created for the forwardBtn on activity_main
    private Button btnForward1; //click listener for tflite
    private Button toMusic;

    private static final String CLIENT_ID = "54abaef1cc07451cbb9813346073de4c";
    private static final String REDIRECT_URI = "com.example.mood://callback";
    private SpotifyAppRemote mSpotifyAppRemote;

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

        btnForward = findViewById(R.id.forwardBtn); //finds the forwardBtn id & assigns it to btnForward
        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToActivityTwo();
            }
        }); //handles the event for when you click the settings button on activity_main

        toMusic = findViewById(R.id.musicBtn);
        toMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToMusic();
            }
        });

        btnForward1 = findViewById(R.id.submitBtn);
        btnForward1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCameraApp();

            }

        });

        try {
            tflite = new Interpreter(loadModelFile());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void openCameraApp() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
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

    private void moveToMusic(){
        Intent moveMusicIntent = new Intent(MainActivity.this, music.class);
        startActivity(moveMusicIntent);
    }
    public MappedByteBuffer loadModelFile() throws IOException
    {
        AssetFileDescriptor fileDescriptor=this.getAssets().openFd("converted_ERCNN.tflite");
        FileInputStream inputStream=new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel=inputStream.getChannel();
        long startOffset =fileDescriptor.getStartOffset();
        long declaredLength=fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffset,declaredLength);
    }

    @Override
    protected void onStart() {
        super.onStart();

        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                    .setRedirectUri(REDIRECT_URI)
                    .showAuthView(true)
                    .build();
        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {
                    @Override
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("MainActivity", "Connected!");

                        connected(0); //make sure to put this function call wherever the camera gets called from
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("MainActivity ErrorError", throwable.getMessage(), throwable);
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    private void connected(Integer emotion) {

        switch (emotion) {
            case 0:
                //TODO
                //This is just a test btw
                mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");
                break;
            case 1:
                //TODO
                break;
            case 2:
                //TODO
                break;
            case 3:
                //TODO
                break;
            case 4:
                //TODO
                break;
            case 5:
                //TODO
                break;
            case 6:
                //TODO
                break;
        }

        mSpotifyAppRemote.getPlayerApi()
                    .subscribeToPlayerState()
                    .setEventCallback(playerState -> {
                       final Track track = playerState.track;
                       if(track != null) {
                           Log.d("MainActivity", track.name + " by " + track.artist.name);
                       }
                    });
    }

}
