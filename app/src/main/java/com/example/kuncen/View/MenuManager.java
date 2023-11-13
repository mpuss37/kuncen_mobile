package com.example.kuncen.View;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kuncen.Adapter.AdminAdapter;
import com.example.kuncen.Adapter.DataPasswordAdapter;
import com.example.kuncen.Handler.DataPasswordHandler;
import com.example.kuncen.Handler.UserHandler;
import com.example.kuncen.Model.DataModel;
import com.example.kuncen.Model.UserModel;
import com.example.kuncen.R;

import java.util.ArrayList;


public class MenuManager extends MainActivity {
    private RecyclerView recyclerView;
    private Intent intent;
    private ImageView imageViewAddItem, imageViewHome, imageViewProfile;
    private TextView textViewPassword, textViewHome, textViewProfile;
    private Button buttonSave;
    private EditText editTextWebsite, editTextUsername, editTextPassword;
    private String name_website, username, pass;
    private int id_user, id_admin, colorOne;
    private DataPasswordHandler dataPasswordHandler;
    private UserHandler userHandler;
    private DataPasswordAdapter dataPasswordAdapter;
    private AdminAdapter adminAdapter;
    private ArrayList<DataModel> dataModelArrayList;
    private ArrayList<UserModel> userModelArrayList;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_manager);
        bundle = getIntent().getExtras();
        recyclerView = findViewById(R.id.rvData);
        dataModelArrayList = new ArrayList<>();
        dataPasswordHandler = new DataPasswordHandler(this);
        userHandler = new UserHandler(this);
        dataPasswordHandler.openRead();
        userHandler.openRead();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MenuManager.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        id_user = bundle.getInt("key_id_user");
        id_admin = bundle.getInt("key_id_admin");
        imageViewAddItem = findViewById(R.id.imageViewAdd);
        imageViewHome = findViewById(R.id.imageViewHome);
        imageViewProfile = findViewById(R.id.imageViewProfile);
        imageViewAddItem = findViewById(R.id.imageViewAdd);
        textViewPassword = findViewById(R.id.textViewAdd);
        textViewHome = findViewById(R.id.textViewHome);
        textViewProfile = findViewById(R.id.textViewProfile);
        checkMenu(id_user, id_admin);

        imageViewHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MenuManager.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void checkMenu(int id_user, int id_admin) {
        if (id_user > 0) {
            dataModelArrayList = dataPasswordHandler.displayData(id_user);
            dataPasswordAdapter = new DataPasswordAdapter(dataModelArrayList, MenuManager.this);
            recyclerView.setAdapter(dataPasswordAdapter);
            dataPasswordAdapter.notifyDataSetChanged();
            imageViewAddItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideKeyboard(v);
                    menuAddItem();
                }
            });
        } else if (id_admin > 0) {
            userModelArrayList = userHandler.displayUser();
            ArrayList<UserModel> userModelArrayList1 = new ArrayList<>();
            for (int i = 1; i < userModelArrayList.size(); i++) {
                userModelArrayList1.add(userModelArrayList.get(i));
            }
            adminAdapter = new AdminAdapter(userModelArrayList1, MenuManager.this);
            recyclerView.setAdapter(adminAdapter);
            adminAdapter.notifyDataSetChanged();
            textViewPassword.setText("Password");
            colorOne = Color.parseColor("#e07d7d");
            imageViewAddItem.setImageResource(R.drawable.shield);
            imageViewProfile.setColorFilter(colorOne);
            imageViewHome.setColorFilter(colorOne);
            textViewProfile.setTextColor(colorOne);
            textViewHome.setTextColor(colorOne);
        }
    }

    private void menuAddItem() {
        ConstraintLayout constraintLayoutAddMenu = findViewById(R.id.clAddItem);
        dataPasswordHandler = new DataPasswordHandler(this);
        dataPasswordHandler.openWrite();
        View view = LayoutInflater.from(MenuManager.this).inflate(R.layout.activity_add_item, constraintLayoutAddMenu);
        buttonSave = view.findViewById(R.id.btnSave);
        editTextWebsite = view.findViewById(R.id.etWebsite);
        editTextUsername = view.findViewById(R.id.etUsername);
        editTextPassword = view.findViewById(R.id.etPass);
        AlertDialog.Builder builder = new AlertDialog.Builder(MenuManager.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_website = editTextWebsite.getText().toString();
                username = editTextUsername.getText().toString();
                pass = editTextPassword.getText().toString();
                try {
                    passEncypt = hashingKey.encrypt(pass, secretKey);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                if (name_website.equals("") && username.equals("") && pass.equals("")) {
                    Toast.makeText(MenuManager.this, "Input username/pass", Toast.LENGTH_SHORT).show();
                } else {
//                    if (dataPasswordHandler.checkData(id_user)) {
                    if (false) {
                        Toast.makeText(MenuManager.this, "bayar dulu", Toast.LENGTH_SHORT).show();
                    } else {
                        int id_data = dataPasswordHandler.readData(username);
                        //if (userHandler.readUser(username)) {
                        if (id_data != -1) {
//                            Toast.makeText(MenuManager.this, "data with username " + username, Toast.LENGTH_SHORT).show();
                        } else {
                            long insertData = DataPasswordHandler.insertDataPass(id_user, name_website, username, passEncypt);
                            dataPasswordAdapter.notifyDataSetChanged();
                            if (insertData != -1) {
                                dataModelArrayList.clear();
                                dataModelArrayList.addAll(dataPasswordHandler.displayData(id_user));
                                editTextWebsite.setText("");
                                editTextUsername.setText("");
                                editTextPassword.setText("");
                                alertDialog.dismiss();
                            }
                        }
                    }
                }
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }


}