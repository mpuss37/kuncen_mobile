package com.example.kuncen.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private LocalDate localDate;
    private String username, pass, parseDate;
    public String passEncypt, passDecrypt;
    private TextView signup, donthaveAcc, textViewTittle;
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
        getSupportActionBar().hide();
        localDate = LocalDate.now();
        parseDate = String.valueOf(localDate);
        userHandler = new UserHandler(this);
        adminHandler = new AdminHandler(this);
        userHandler.openWrite();
        adminHandler.openRead();
        userHandler.openRead();
        etUsername = findViewById(R.id.editTextUsername);
        etPassword = findViewById(R.id.editTextPassword);
        buttonSave = findViewById(R.id.buttonSave);
        signup = findViewById(R.id.textViewSignUp);
        donthaveAcc = findViewById(R.id.textViewDontHaveAcc);
        constraintLayoutMain = findViewById(R.id.clMain);
        textViewTittle = findViewById(R.id.textView);
    }

    @Override
    protected void onStart() {
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
                userHandler.openRead();
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
                            long insertUser = userHandler.insertUser(username, passEncypt, parseDate);
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
                        intent = new Intent(MainActivity.this, MenuManager.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        if (id_user != -1) {
                            intent.putExtra("key_id_user", id_user);
                            intent.putExtra("key_username", username);
                            startActivity(intent);
                            Toast.makeText(MainActivity.this, "login with " + username, Toast.LENGTH_SHORT).show();
                        } else if (id_admin != -1) {
                            intent.putExtra("key_id_admin", id_admin);
                            intent.putExtra("key_username", username);
                            startActivity(intent);
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