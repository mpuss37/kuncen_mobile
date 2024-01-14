package com.example.kuncen.View;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.kuncen.Adapter.AdminAdapter;
import com.example.kuncen.Adapter.CheckerAdapter;
import com.example.kuncen.Adapter.DataPasswordAdapter;
import com.example.kuncen.EncryptionKey.HashingKey;
import com.example.kuncen.Handler.DataPasswordHandler;
import com.example.kuncen.Handler.SubscriptionHandler;
import com.example.kuncen.Handler.UserHandler;
import com.example.kuncen.Model.DataModel;
import com.example.kuncen.Model.UserModel;
import com.example.kuncen.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;


public class MenuChecker extends MainActivity {
    private RecyclerView recyclerView;
    String name_website, username, pass, keyUsername;
    private int id_user, id_admin;
    private DataPasswordHandler dataPasswordHandler;
    private UserHandler userHandler;
    private CheckerAdapter checkerAdapter;
    private ArrayList<DataModel> dataModelArrayList;
    private LocalDate dateStart = LocalDate.now(), datePlus30 = dateStart.plusDays(2), dateEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checker);
        getSupportActionBar().show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        recyclerView = findViewById(R.id.rvData);
        dataModelArrayList = new ArrayList<>();
        dataPasswordHandler = new DataPasswordHandler(this);
        hashingKey = new HashingKey();
        userHandler = new UserHandler(this);
        dataPasswordHandler.openRead();
        userHandler.openRead();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MenuChecker.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        id_user = bundle.getInt("key_id_user");
        keyUsername = bundle.getString("key_username");
        checkMenu(id_user);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(MenuChecker.this, MenuManager.class);
                intent.putExtra("key_id_user", id_user);
                intent.putExtra("key_username", keyUsername);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void checkMenu(int id_user) {
        if (id_user > 0) {
            dataModelArrayList = dataPasswordHandler.displayData(id_user);
            checkerAdapter = new CheckerAdapter(dataModelArrayList, MenuChecker.this);
            recyclerView.setAdapter(checkerAdapter);
            checkerAdapter.notifyDataSetChanged();
        }
    }

}