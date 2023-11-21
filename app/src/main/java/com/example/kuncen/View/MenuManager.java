package com.example.kuncen.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kuncen.Adapter.AdminAdapter;
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

import javax.crypto.SecretKey;


public class MenuManager extends MainActivity {
    private RecyclerView recyclerView;
    private DrawerLayout drawerLayoutSideMenu;
    private ScrollView scrollView;
    private NavigationView navigationViewSideMenu;
    Intent intent;
    private ImageView imageViewAddItem, imageViewHome, imageViewProfile;
    private TextView textViewPassword, textViewHome, textViewProfile, textViewMainAddItem, textViewDaySubs, textViewUsernameSideHeader, textViewTierSideHeader;
    private Button buttonSave, buttonGenerator;
    private EditText editTextWebsite, editTextUsername, editTextPassword;
    private TextInputLayout textInputLayoutWebsite, textInputLayoutUsername, textInputLayoutPassword;
    String name_website, username, pass, keyUsername;
    private int id_user;
    private DataPasswordHandler dataPasswordHandler;
    private SubscriptionHandler subscriptionHandler;
    private UserHandler userHandler;
    private DataPasswordAdapter dataPasswordAdapter;
    private AdminAdapter adminAdapter;
    private ArrayList<DataModel> dataModelArrayList;
    private ArrayList<UserModel> userModelArrayList;
    private View view;
    private LocalDate dateStart = LocalDate.now(), datePlus30 = dateStart.plusDays(2), dateEnd;
    private Random randomText;
    private HashingKey hashingKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_manager);
        getSupportActionBar().show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        recyclerView = findViewById(R.id.rvData);
        scrollView = findViewById(R.id.svMenuManager);
        dataModelArrayList = new ArrayList<>();
        dataPasswordHandler = new DataPasswordHandler(this);
        subscriptionHandler = new SubscriptionHandler(this);
        subscriptionHandler.openRead();
        hashingKey = new HashingKey();
        userHandler = new UserHandler(this);
        dataPasswordHandler.openRead();
        userHandler.openRead();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MenuManager.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        int id_admin = bundle.getInt("key_id_admin");
        keyUsername = bundle.getString("key_username");
        id_user = bundle.getInt("key_id_user");
        navigationViewSideMenu = findViewById(R.id.navSideView);
        view = navigationViewSideMenu.getHeaderView(0);
        textViewUsernameSideHeader = view.findViewById(R.id.textView9);
        textViewDaySubs = findViewById(R.id.textViewDaySubs);
        textViewTierSideHeader = view.findViewById(R.id.textViewTier);
        checkDate(id_user);
        textViewUsernameSideHeader.setText(keyUsername);

        imageViewAddItem = findViewById(R.id.imageViewAdd);
        imageViewHome = findViewById(R.id.imageViewHome);
        imageViewProfile = findViewById(R.id.imageViewProfile);
        imageViewAddItem = findViewById(R.id.imageViewAdd);
        textViewPassword = findViewById(R.id.textViewAdd);
        textViewHome = findViewById(R.id.textViewHome);
        textViewProfile = findViewById(R.id.textViewProfile);

        checkMenu(id_user, id_admin);

        drawerLayoutSideMenu = findViewById(R.id.drawerSideMain);
        drawerLayoutSideMenu.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        navigationViewSideMenu = findViewById(R.id.navSideView);

        navigationViewSideMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int idPosition = item.getItemId();
                if (idPosition == R.id.logout) {
                    intent = new Intent(MenuManager.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else if (idPosition == R.id.subscription) {
                    menuAddItem("subscription");
                }
                return false;
            }
        });

        imageViewHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MenuManager.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                MenuManager.this.finish();
            }
        });

        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scrollView.getVisibility() == View.VISIBLE) {
                    drawerLayoutSideMenu.openDrawer(GravityCompat.START);
                    scrollView.setVisibility(View.GONE);
                } else {
                    scrollView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public int checkDate(int id_user) {
        int id_subs = subscriptionHandler.readId(id_user);
        LocalDate localeDateSubs = subscriptionHandler.readDateEnd(id_user);
        String localDateParse = localeDateSubs.toString();
        if (id_subs != -1) {
//            2023-09-09
            if (dateStart.isBefore(localeDateSubs)) {
                textViewDaySubs.setText(localeDateSubs.toString());
                textViewTierSideHeader.setText("Premium");
            } else {
                subscriptionHandler.deleteSubs(id_user);
                textViewDaySubs.setText("0");
                textViewTierSideHeader.setText("Standard");
            }
        }
        return id_subs;
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
                    menuAddItem("user");
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
            imageViewAddItem.setImageResource(R.drawable.shield);
        }
    }

    private void insertData(int id_user, String name_website, String username, String pass, AlertDialog alertDialog) {
        long insertData = DataPasswordHandler.insertDataPass(id_user, name_website, username, pass);
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

    private void menuAddItem(String page) {
        ConstraintLayout constraintLayoutAddMenu = findViewById(R.id.clAddItem);
        View view = LayoutInflater.from(MenuManager.this).inflate(R.layout.activity_add_item, constraintLayoutAddMenu);
        dataPasswordHandler = new DataPasswordHandler(this);
        dataPasswordHandler.openWrite();
        subscriptionHandler = new SubscriptionHandler(this);
        subscriptionHandler.openWrite();
        AlertDialog.Builder builder = new AlertDialog.Builder(MenuManager.this);
        builder.setView(view);
        buttonSave = view.findViewById(R.id.btnSave);
        buttonGenerator = view.findViewById(R.id.btnGenerator);
        textViewMainAddItem = view.findViewById(R.id.textViewMainAddItem);
        editTextWebsite = view.findViewById(R.id.etWebsite);
        editTextUsername = view.findViewById(R.id.etUsername);
        editTextPassword = view.findViewById(R.id.etPass);
        textInputLayoutWebsite = view.findViewById(R.id.textInputLayoutWebsite);
        textInputLayoutUsername = view.findViewById(R.id.textInputLayoutUsername);
        textInputLayoutPassword = view.findViewById(R.id.textInputLayoutPassword);
        final AlertDialog alertDialog = builder.create();

        if (page.equals("user")) {
            buttonGenerator.setVisibility(View.GONE);
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
                        int id_subs = subscriptionHandler.readId(id_user);
                        if (id_subs != -1) {
                            //next input your data subs is actived
                            int id_data = dataPasswordHandler.readData(username);
                            if (id_data != -1) {
                                Toast.makeText(MenuManager.this, "data with username " + username, Toast.LENGTH_SHORT).show();
                            } else {
                                insertData(id_user, name_website, username, passEncypt, alertDialog);
                            }
                        } else {
                            int id_data = dataPasswordHandler.countData(id_user);
                            if (id_data < 3) {
                                insertData(id_user, name_website, username, passEncypt, alertDialog);
                            } else {
                                Toast.makeText(MenuManager.this, "paid for access", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });
        } else if (page.equals("subscription")) {
            String key = "a3271802a5318fb310c94d6f28943212";
            SecretKey secretKey = hashingKey.keyFromHexString(key);
            textInputLayoutPassword.setVisibility(View.GONE);
            textViewMainAddItem.setText("Subscription");
            textInputLayoutWebsite.setHint("Id-User");
            textInputLayoutUsername.setHint("Code");
            buttonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    name_website = editTextWebsite.getText().toString();
                    username = editTextUsername.getText().toString();
                    if (name_website.equals("")) {
                        Toast.makeText(MenuManager.this, "Input id-user/code", Toast.LENGTH_SHORT).show();
                    } else {
                        int id_subs = subscriptionHandler.readId(id_user);
                        if (id_subs == -1) {
                            try {
                                String encyrpt = hashingKey.encrypt(name_website, secretKey);
                                String decrypt = hashingKey.decrypt(encyrpt, secretKey);
                                if (name_website.equals(decrypt)) {
                                    long insertSubs = SubscriptionHandler.insertDataSubs(id_user, name_website, encyrpt, dateStart.toString(), datePlus30.toString());
                                    Toast.makeText(MenuManager.this, "subscription active", Toast.LENGTH_SHORT).show();
                                    checkDate(id_user);
                                    if (insertSubs != -1) {
                                        editTextWebsite.setText("");
                                        editTextUsername.setText("");
                                    }
                                } else {
                                    Toast.makeText(MenuManager.this, "wrong code", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(MenuManager.this, "error", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MenuManager.this, "subscription still active", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

            buttonGenerator.setOnClickListener(v -> {
                String plainText = "1iq23tw793";
                randomText = new Random();
                StringBuilder randomWord = new StringBuilder();
                for (int i = 0; i < 10; i++) {
                    int randomIndex = randomText.nextInt(plainText.length());
                    randomWord.append(plainText.charAt(randomIndex));
                }
                editTextWebsite.setText(randomWord.toString());
            });

        }
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
}