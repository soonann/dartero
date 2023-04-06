package com.example.dartero;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * Entry point
 */
public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(new Game(this));

        // Initialize the MediaPlayer with background music; MediaPlayer class uses separate thread by default.
        mediaPlayer = MediaPlayer.create(this, R.raw.kirby);

        // Start playing the music
        mediaPlayer.setLooping(true); // Loop music
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Release MediaPlayer resources when activity is destroyed
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}