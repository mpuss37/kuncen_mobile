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
import android.widget.ImageView;

import com.example.kuncen.Adapter.AdapterPassword;
import com.example.kuncen.R;

import java.util.ArrayList;

public class MenuManager extends MainActivity {
    private RecyclerView rvPassword;
    private ArrayList<String> listDataUsername, listDataNumber;
    private Button buttonAdd;
    private ImageView imageViewAddItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_manager);
        rvPassword = findViewById(R.id.rvData);
        imageViewAddItem = findViewById(R.id.imageViewAddItem);
        listDataUsername = new ArrayList<>();
        listDataNumber = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            listDataUsername.add("Herdi" + i);
            listDataNumber.add("" + i);
        }
        AdapterPassword adapterPassword = new AdapterPassword(this, listDataUsername, listDataNumber);
        rvPassword.setAdapter(adapterPassword);
        rvPassword.setLayoutManager(new LinearLayoutManager(this));

        imageViewAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuAddItem();
                //menu untuk menampilkan alert dialog menuadditem
            }
        });
    }

    private void menuAddItem() {
        ConstraintLayout constraintLayoutAddMenu = findViewById(R.id.clAddItem);
        View view = LayoutInflater.from(MenuManager.this).inflate(R.layout.activity_add_item, constraintLayoutAddMenu);
        AlertDialog.Builder builder = new AlertDialog.Builder(MenuManager.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
//        buttonAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.dismiss();
//                Toast.makeText(MenuManager.this, "Berhasil", Toast.LENGTH_SHORT).show();
//
//            }
//        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
}