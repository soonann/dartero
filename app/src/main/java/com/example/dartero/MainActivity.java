package com.example.dartero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Entry point
 */
public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;

    private EditText name;
    private Button startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.name);
        startBtn = findViewById(R.id.startBtn);

        // Retrieve saved name using SharedPreferences and display it in name display TextView
        SharedPreferences prefs = getSharedPreferences("name_saved", MODE_PRIVATE);
        String savedName = prefs.getString("name", "");
        if (!savedName.isEmpty()) {
            name.setText(savedName);
            name.setHint("");
            startBtn.setEnabled(true);
        } else {
            name.setHint("Enter your name");
            startBtn.setEnabled(false);
        }

        /**
         * Allow name text field to store scores in database
         */
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No implementation needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    startBtn.setEnabled(false); // Disable start button if no name
                } else {
                    startBtn.setEnabled(true);  // Enable start button if there is name
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No implementation needed
            }
        });
    }

    private void saveName(String name) {
        SharedPreferences prefs = getSharedPreferences("name_saved", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("name", name);
        editor.apply();
    }

    public void startBtnClicked(View view) {
        String enteredName = name.getText().toString();
        saveName(enteredName);
        Intent indent = new Intent(this, GameActivity.class);
        startActivity(indent);
    }
}