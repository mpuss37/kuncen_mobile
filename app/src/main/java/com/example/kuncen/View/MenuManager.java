package com.example.kuncen.View;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kuncen.Adapter.DataPasswordAdapter;
import com.example.kuncen.Handler.DataPasswordHandler;
import com.example.kuncen.Model.DataModel;
import com.example.kuncen.R;

import java.util.ArrayList;


public class MenuManager extends MainActivity {
    private RecyclerView rvPassword;
    private ImageView imageViewAddItem;
    private Button buttonSave;
    private EditText editTextWebsite, editTextUsername, editTextPassword;
    private String name_website, username, pass;
    private DataPasswordHandler dataPasswordHandler;
    private DataPasswordAdapter dataPasswordAdapter;
    private ArrayList<DataModel> dataModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_manager);
        rvPassword = findViewById(R.id.rvData);
        dataModelArrayList = new ArrayList<>();
        dataPasswordHandler = new DataPasswordHandler(this);
        dataPasswordHandler.openRead();
        dataModelArrayList = dataPasswordHandler.displayData();
        dataPasswordAdapter = new DataPasswordAdapter(dataModelArrayList, MenuManager.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MenuManager.this, RecyclerView.VERTICAL, false);
        rvPassword.setLayoutManager(linearLayoutManager);
        rvPassword.setAdapter(dataPasswordAdapter);
        dataPasswordAdapter.notifyDataSetChanged();
        imageViewAddItem = findViewById(R.id.imageViewAddItem);
        imageViewAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);

                menuAddItem();
            }
        });
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
                if (name_website.equals("") && username.equals("") && pass.equals("")) {
                    Toast.makeText(MenuManager.this, "Input username/pass", Toast.LENGTH_SHORT).show();
                } else {
                    long insertData = DataPasswordHandler.insertDataPass(name_website, username, pass);
                    dataPasswordAdapter.notifyDataSetChanged();
                    Toast.makeText(MenuManager.this, "data has been add", Toast.LENGTH_SHORT).show();
                    if (insertData != -1) {
                        dataModelArrayList.clear();
                        dataModelArrayList.addAll(dataPasswordHandler.displayData());
                        editTextWebsite.setText("");
                        editTextUsername.setText("");
                        editTextPassword.setText("");
                        alertDialog.dismiss();
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