package com.example.mood;
import org.tensorflow.lite.Interpreter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.provider.MediaStore;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import androidx.appcompat.app.AppCompatActivity;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import androidx.appcompat.app.AppCompatActivity;

import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;


public class music extends AppCompatActivity {

    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_THEME = "dark_theme";

    private static final String CLIENT_ID = "54abaef1cc07451cbb9813346073de4c";
    private static final String REDIRECT_URI = "com.example.mood://callback";
    private SpotifyAppRemote mSpotifyAppRemote;

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
                        Log.d("MusicActivity", "Connected!");

                        connected(5); //make sure to put this function call wherever the camera gets called from
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("MusicActivity Error", throwable.getMessage(), throwable);
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    private void connected(Integer emotion) {

        /*
        0: 'angry'
        1: 'disgust'
        2: 'fear'
        3: 'happy'
        4: 'sad'
        5: 'surprise'
        6: 'neutral
        */


        switch (emotion) {
            case 0:
                //TODO
                //Whatever music everyone else wants can go here idrgaf

                break;
            case 1:
                //TODO
                mSpotifyAppRemote.getPlayerApi().play("spotify:track:7GhIk7Il098yCjg4BQjzvb");
                break;
            case 2:
                //TODO
                break;
            case 3:
                //TODO
                mSpotifyAppRemote.getPlayerApi().play("spotify:artist:7Ln80lUS6He07XvHI8qqHH");
                break;
            case 4:
                //TODO
                break;
            case 5:
                //TODO
                mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");
                break;
            case 6:
                //TODO
                break;
        }

        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    String message = "Now Playing: ";
                    final Track track = playerState.track;
                    if(track != null) {
                        message =track.name + "\nby\n" + track.artist.name;

                    }

                    TextView textView = (TextView) findViewById(R.id.textView);
                    textView.setText(message);
                });
    }

}
