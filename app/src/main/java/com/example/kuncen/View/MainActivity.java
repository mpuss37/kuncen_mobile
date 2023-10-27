package com.example.kuncen.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kuncen.Handler.UserHandler;
import com.example.kuncen.R;

public class MainActivity extends AppCompatActivity {
    Cursor cursor;
    private EditText etUsername, etPassword;
    private String username, pass;
    private TextView signup, donthaveAcc;
    private Button buttonSave;
    private Intent intent;
    private UserHandler userHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userHandler = new UserHandler(this);
        userHandler.openWrite();
        etUsername = findViewById(R.id.editTextUsername);
        etPassword = findViewById(R.id.editTextPassword);
        buttonSave = findViewById(R.id.buttonSave);
        signup = findViewById(R.id.textViewSignUp);
        donthaveAcc = findViewById(R.id.textViewDontHaveAcc);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSave.setEnabled(false);
                username = etUsername.getText().toString();
                pass = etPassword.getText().toString();
                if (buttonSave.getText().equals("Sign Up")) {
                    if (username.equals("") && pass.equals("")) {
                        Toast.makeText(MainActivity.this, "Input username/pass", Toast.LENGTH_SHORT).show();
                        buttonSave.setEnabled(true);
                    } else {
                        long insertUser = userHandler.insertUser(username, pass);
                        Toast.makeText(MainActivity.this, "data has been add", Toast.LENGTH_SHORT).show();
                        buttonSave.setText("Login");
                        donthaveAcc.setText("already have account, ");
                        signup.setText("Login");
                        if (insertUser != -1) {
                            etUsername.setText("");
                            etPassword.setText("");
                        }
                        buttonSave.setEnabled(true);
                    }
                } else if (buttonSave.getText().equals("Login")) {
                    if (username.equals("") && pass.equals("")) {
                        Toast.makeText(MainActivity.this, "Input username/pass", Toast.LENGTH_SHORT).show();
                        buttonSave.setEnabled(true);
                    } else {
                        if (userHandler.readUser(username, pass)) {
                            intent = new Intent(MainActivity.this, MenuManager.class);
                            startActivity(intent);
                            Toast.makeText(MainActivity.this, "login with " + username, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "error username/pass", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonSave.getText().equals("Sign Up")) {
                    buttonSave.setText("Login");
                    donthaveAcc.setText("already have account, ");
                    signup.setText("Login");
                    Toast.makeText(MainActivity.this, "ini menu login", Toast.LENGTH_SHORT).show();
                } else {
                    buttonSave.setText("Sign Up");
                    donthaveAcc.setText("dont have account, ");
                    signup.setText("Sign Up");
                    Toast.makeText(MainActivity.this, "ini menu signup", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userHandler.close();
    }
}