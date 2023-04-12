package com.example.dartero;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dartero.database.User;
import com.example.dartero.database.UserAPI;
import com.example.dartero.utils.RetrofitClient;
import com.example.dartero.utils.SharedPreferencesUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Entry point
 */
public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;

    private EditText name;
    private Button startBtn;

    private Retrofit retrofit;

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

        retrieveName();

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

    /**
     * Retrieve saved name using SharedPreferences and display it in name display TextView
     */
    private void retrieveName() {
        String savedName = SharedPreferencesUtils.getName(MainActivity.this);
        if (savedName == null || savedName.isEmpty()) {
            name.setHint("Enter your name");
            startBtn.setEnabled(false);
        } else {
            name.setText(savedName);
            name.setHint("");
            startBtn.setEnabled(true);
        }
    }

    /**
     * Check if user is registered, if not create an account
     * @param enteredName The User name
     */
    private void checkUsernameRegistered(String enteredName) {
        retrofit = RetrofitClient.getRetrofitInstance();

        UserAPI userAPI = retrofit.create(UserAPI.class);

        Call<List<User>> call = userAPI.getAllUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()) {
                    Log.d("Get users response", response.toString());
                    return ;
                }
                List<User> users = response.body();

                boolean userExists = false;
                for (User user: users) {
                    if (user.getUsername().equals(enteredName)) {
                        userExists = true;
                        break;
                    }
                }

                if (!userExists) {
                    createUser(enteredName);
                } else {
                    Toast.makeText(MainActivity.this, "User " + enteredName, Toast.LENGTH_SHORT).show();
                    Log.d("Response", enteredName + " is an existing user!");
                }
                Toast.makeText(MainActivity.this, "User " + enteredName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d("Get users failed response", t.toString());
            }
        });
    }

    /**
     * Create a user account using name
     * @param enteredName The User name
     */
    private void createUser(String enteredName) {
        retrofit = RetrofitClient.getRetrofitInstance();

        UserAPI userAPI = retrofit.create(UserAPI.class);
        Call<User> call = userAPI.createUser(new User(enteredName));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Log.d("Create user response", response.toString());
                }
                Toast.makeText(MainActivity.this, "User created", Toast.LENGTH_SHORT).show();
                return ;
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("Create user failed response", t.toString());
                return ;
            }
        });

    }

    /**
     * Save name into device's storage
     * @param name The User name
     */
    private void saveName(String name) {
        SharedPreferencesUtils.saveName(MainActivity.this, name);
    }

    /**
     * Handle start button clicked
     * @param view The View object
     */
    public void startBtnClicked(View view) {
        String enteredName = name.getText().toString();
        saveName(enteredName);

        checkUsernameRegistered(enteredName);

        Intent indent = new Intent(this, GameActivity.class);
        startActivity(indent);
    }

    /**
     * Handle start button clicked
     * @param view The View object
     */
    public void scoreBoardBtnClicked(View view) {
        Intent indent = new Intent(this, ScoreboardActivity.class);
        startActivity(indent);
    }
}