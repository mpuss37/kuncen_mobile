package com.example.kuncen.Item;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kuncen.Handler.DataPasswordHandler;
import com.example.kuncen.View.MainActivity;
import com.example.kuncen.R;

public class AddItem extends MainActivity {
    private EditText editTextWebsite, editTextUsername, editTextPassword;
    private String name_website, username, pass;
    private Button buttonSave, buttonGenerator;
    private DataPasswordHandler dataPasswordHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        dataPasswordHandler = new DataPasswordHandler(this);
        editTextWebsite = findViewById(R.id.etWebsite);
        editTextUsername = findViewById(R.id.etUsername);
        editTextPassword = findViewById(R.id.etPass);
        buttonSave = findViewById(R.id.btnSave);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInput();
            }
        });
    }

    private void saveInput() {
        if (editTextWebsite.getText().toString().equals("") && editTextUsername.getText().toString().equals("") && editTextPassword.getText().toString().equals("")) {
            Toast.makeText(this, "Input username/pass", Toast.LENGTH_SHORT).show();
        } else {
            name_website = editTextWebsite.getText().toString();
            username = editTextUsername.getText().toString();
            pass = editTextPassword.getText().toString();
            dataPasswordHandler.insertDataPass(name_website, username, pass);
            Toast.makeText(this, "data has been add", Toast.LENGTH_SHORT).show();

        }
    }
}