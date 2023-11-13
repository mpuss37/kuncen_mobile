package com.example.kuncen.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kuncen.EncryptionKey.HashingKey;
import com.example.kuncen.Handler.AdminHandler;
import com.example.kuncen.Handler.UserHandler;
import com.example.kuncen.R;

import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private String username, pass;
    public String passEncypt, passDecrypt;
    private TextView signup, donthaveAcc;
    private Button buttonSave;
    private Intent intent;
    private UserHandler userHandler;
    private AdminHandler adminHandler;
    public HashingKey hashingKey = new HashingKey();
    private ConstraintLayout constraintLayoutMain;
    private final String key = "a3271802a5318fb310c94d6f28943212";
    public final SecretKey secretKey = hashingKey.keyFromHexString(key);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userHandler = new UserHandler(this);
        adminHandler = new AdminHandler(this);
        userHandler.openWrite();
        adminHandler.openWrite();
        etUsername = findViewById(R.id.editTextUsername);
        etPassword = findViewById(R.id.editTextPassword);
        buttonSave = findViewById(R.id.buttonSave);
        signup = findViewById(R.id.textViewSignUp);
        donthaveAcc = findViewById(R.id.textViewDontHaveAcc);
        constraintLayoutMain = findViewById(R.id.clMain);
    }

    @Override
    protected void onStart() {
        userHandler.openRead();
        super.onStart();
        constraintLayoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = etUsername.getText().toString();
                pass = etPassword.getText().toString();
                try {
                    passEncypt = hashingKey.encrypt(pass, secretKey);
                } catch (Exception e) {
                }
                if (buttonSave.getText().equals("Sign Up")) {
                    if (username.equals("") && pass.equals("")) {
                        Toast.makeText(MainActivity.this, "Input username/pass", Toast.LENGTH_SHORT).show();
                    } else {
                        int id_user = userHandler.readUser(username, "null");
                        if (id_user != -1) {
                            Toast.makeText(MainActivity.this, "username is already in use", Toast.LENGTH_SHORT).show();
                            etUsername.setText("");
                            etPassword.setText("");
                        } else {
                            long insertUser = userHandler.insertUser(username, passEncypt);
                            Toast.makeText(MainActivity.this, "data has been successfully added", Toast.LENGTH_SHORT).show();
                            buttonSave.setText("Login");
                            donthaveAcc.setText("already have account, ");
                            signup.setText("Login");
                            if (insertUser != -1) {
                                etUsername.setText("");
                                etPassword.setText("");
                            }
                        }
                    }
                } else if (buttonSave.getText().equals("Login")) {
                    if (username.equals("") && pass.equals("")) {
                        Toast.makeText(MainActivity.this, "Input username/pass", Toast.LENGTH_SHORT).show();
                    } else {
                        int id_user = userHandler.readUser(username, passEncypt);
                        int id_admin = adminHandler.readAdmin(username, pass);
                        if (id_user != -1) {
                            intent = new Intent(MainActivity.this, MenuManager.class);
                            intent.putExtra("key_id_user", id_user);
                            startActivity(intent);
                            finish();
                            Toast.makeText(MainActivity.this, "login with " + username, Toast.LENGTH_SHORT).show();
                        } else if (id_admin != -1) {
                            intent = new Intent(MainActivity.this, MenuManager.class);
                            intent.putExtra("key_id_admin", id_admin);
                            startActivity(intent);
                            finish();
                            Toast.makeText(MainActivity.this, "Selamat datang " + username, Toast.LENGTH_SHORT).show();
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

    public void hideKeyboard(View view) {
        view = getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userHandler.close();
    }
}