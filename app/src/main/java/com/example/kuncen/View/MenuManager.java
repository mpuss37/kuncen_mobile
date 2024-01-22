package com.example.kuncen.View;

import static com.example.kuncen.Handler.DataPasswordHandler.editDataPass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    private ImageView imageViewAddItem, imageViewHome, imageViewProfile, imageViewProfilePicture, imageViewAddProfilePicture;
    private TextView textViewPassword, textViewHome, textViewProfile, textViewMainAddItem, textViewDaySubs, textViewUsernameSideHeader, textViewTierSideHeader;
    private Button buttonSave, buttonGenerator;
    private EditText editTextWebsite, editTextUsername, editTextPassword;
    private TextInputLayout textInputLayoutWebsite, textInputLayoutUsername, textInputLayoutPassword;
    String name_website, username, pass, keyUsername;
    private int id_user, id_admin, PICK_IMAGE_REQUEST;
    private byte[] byteImage;
    private ByteArrayOutputStream stream;

    private MainActivity mainActivity;
    private DataPasswordHandler dataPasswordHandler;
    private SubscriptionHandler subscriptionHandler;
    private UserHandler userHandler;
    private ConstraintLayout constraintLayoutAddMenu;
    private DataPasswordAdapter dataPasswordAdapter;
    private AdminAdapter adminAdapter;
    private CheckerAdapter checkerAdapter;
    private ArrayList<DataModel> dataModelArrayList;
    private ArrayList<UserModel> userModelArrayList;
    private View view;
    private LocalDate dateStart = LocalDate.now(), datePlus30 = dateStart.plusDays(2), dateEnd;
    private Random randomText;
    private HashingKey hashingKey;
    private Bundle bundle;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_manager);
        getSupportActionBar().show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bundle = this.getIntent().getExtras();
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
        imageViewAddProfilePicture = findViewById(R.id.imageViewAddProfilePicture);
        id_admin = bundle.getInt("key_id_admin");
        keyUsername = bundle.getString("key_username");
        id_user = bundle.getInt("key_id_user");
        navigationViewSideMenu = findViewById(R.id.navSideView);
        view = navigationViewSideMenu.getHeaderView(0);
        imageViewProfilePicture = view.findViewById(R.id.imageViewProfilePicture);
        textViewUsernameSideHeader = view.findViewById(R.id.textView9);
        textViewTierSideHeader = view.findViewById(R.id.textViewTier);

        checkDate(id_user);
        byteImage = userHandler.checkImage(id_user);
        checkImage(byteImage);

        textViewUsernameSideHeader.setText(keyUsername);
        textViewDaySubs = findViewById(R.id.textViewDaySubs);
        imageViewAddProfilePicture = findViewById(R.id.imageViewAddProfilePicture);
        imageViewAddItem = findViewById(R.id.imageViewAdd);
        imageViewHome = findViewById(R.id.imageViewHome);
        imageViewProfile = findViewById(R.id.imageViewProfile);
        imageViewAddItem = findViewById(R.id.imageViewAdd);
        textViewPassword = findViewById(R.id.textViewAdd);
        textViewHome = findViewById(R.id.textViewHome);
        textViewProfile = findViewById(R.id.textViewProfile);

        checkMenu(this, id_user, id_admin);

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
                }
                if (id_admin > 0) {
                    item.setVisible(false);
                } else {
                    if (idPosition == R.id.subscription) {
                        menuAddItem("subscription", "null", MenuManager.this, id_user, null, null, null);
                    } else if (idPosition == R.id.checker) {
                        intent = new Intent(MenuManager.this, MenuChecker.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("key_id_user", id_user);
                        intent.putExtra("key_username", keyUsername);
                        startActivity(intent);
                    }
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

        imageViewAddProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePick(id_user);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 0, stream);
                byteImage = stream.toByteArray();
                userHandler.updateImage(id_user, byteImage);
                imageViewProfilePicture.setImageBitmap(bitmap);
            } catch (IOException e) {
            }
        }
    }

    private void checkImage(byte [] byteImage){
        if (byteImage != null) {
            bitmap = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
            stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 0, stream);
            imageViewProfilePicture.setImageBitmap(bitmap);
        }
    }

    private void imagePick(int id_user) {
        byteImage = userHandler.checkImage(id_user);
        intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
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
                textViewTierSideHeader.setTextColor(Color.RED);
            } else {
                subscriptionHandler.deleteSubs(id_user);
                textViewDaySubs.setText("");
                textViewTierSideHeader.setText("Standard");
            }
        }
        return id_subs;
    }


    private void checkMenu(Context context, int id_user, int id_admin) {
        if (id_user >= 0) {
            dataModelArrayList = dataPasswordHandler.displayData(id_user);
            dataPasswordAdapter = new DataPasswordAdapter(dataModelArrayList, context);
            recyclerView.setAdapter(dataPasswordAdapter);
            dataPasswordAdapter.notifyDataSetChanged();
            imageViewAddItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideKeyboard(v);
                    menuAddItem("add_user", "null", MenuManager.this, id_user, null, null, null);
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
        if (insertData != -1) {
            dataModelArrayList.addAll(dataPasswordHandler.displayData(id_user));
            editTextWebsite.setText("");
            editTextUsername.setText("");
            editTextPassword.setText("");
            alertDialog.dismiss();
        }
    }

    private void editData(Context context, int id_user, String name_website, String username, String pass, AlertDialog alertDialog) {
        long editDataPass = editDataPass(id_user, name_website, username, pass);
        if (editDataPass > 0) {
            Toast.makeText(context, "" + editDataPass, Toast.LENGTH_SHORT).show();
            editTextWebsite.setText("");
            editTextUsername.setText("");
            editTextPassword.setText("");
        } else {
            Toast.makeText(context, "gagal", Toast.LENGTH_SHORT).show();
        }
//        alertDialog.dismiss();
    }

    public void menuAddItem(String page, String adapter, Context context, int id_user, String data1, String data2, String data3) {
        if (context instanceof MenuManager) {
            MenuManager activity = (MenuManager) context;
            if (activity.isFinishing() || activity.isDestroyed()) {
                return;
            }
        }
        if (adapter.equals("null")) {
            view = LayoutInflater.from(context).inflate(R.layout.activity_add_item, null);
            view = LayoutInflater.from(context).inflate(R.layout.activity_add_item, null);
            constraintLayoutAddMenu = view.findViewById(R.id.clAddItem);
        } else if (adapter.equals("adapter")) {
            view = LayoutInflater.from(context).inflate(R.layout.activity_add_item, constraintLayoutAddMenu);
        }
        mainActivity = new MainActivity();
        hashingKey = new HashingKey();
        dataPasswordHandler = new DataPasswordHandler(context);
        dataPasswordHandler.openWrite();
        subscriptionHandler = new SubscriptionHandler(context);
        subscriptionHandler.openWrite();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        buttonSave = view.findViewById(R.id.btnSave);
        buttonGenerator = view.findViewById(R.id.btnGenerator);
        buttonGenerator.setVisibility(View.GONE);
        textViewMainAddItem = view.findViewById(R.id.textViewMainAddItem);
        editTextWebsite = view.findViewById(R.id.etWebsite);
        editTextUsername = view.findViewById(R.id.etUsername);
        editTextPassword = view.findViewById(R.id.etPass);

        editTextWebsite.setText(data1);
        editTextUsername.setText(data2);
        editTextPassword.setText(data3);

        textInputLayoutWebsite = view.findViewById(R.id.textInputLayoutWebsite);
        textInputLayoutUsername = view.findViewById(R.id.textInputLayoutUsername);
        textInputLayoutPassword = view.findViewById(R.id.textInputLayoutPassword);
        final AlertDialog alertDialog = builder.create();

        if (page.equals("add_user")) {
            buttonGenerator.setVisibility(View.GONE);
            buttonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    name_website = editTextWebsite.getText().toString();
                    username = editTextUsername.getText().toString();
                    pass = editTextPassword.getText().toString();
                    try {
                        passEncypt = hashingKey.encrypt(pass, getSecretKey());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    if (name_website.equals("") && username.equals("") && pass.equals("")) {
                        Toast.makeText(context, "Input username/pass", Toast.LENGTH_SHORT).show();
                    } else {
                        int id_subs = subscriptionHandler.readId(id_user);
                        if (id_subs != -1) {
                            //next input your data subs is actived
                            int id_data = dataPasswordHandler.readData(username);
                            if (id_data != -1) {
                                Toast.makeText(context, "data with username " + username, Toast.LENGTH_SHORT).show();
                            } else {
                                insertData(id_user, name_website, username, passEncypt, alertDialog);
                            }
                        } else {
                            int id_data = dataPasswordHandler.countData(id_user);
                            if (id_data < 3) {
                                insertData(id_user, name_website, username, passEncypt, alertDialog);
                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "paid for access", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                                toast.show();
                            }
                        }
                    }
                }
            });
        } else if (page.equals("edit_user")) {
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
                        Toast.makeText(context, "Input username/pass", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "" + pass, Toast.LENGTH_SHORT).show();
                        editData(context, id_user, name_website, username, passEncypt, alertDialog);
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
                    if (name_website.equals("") || username.equals("")) {
                        Toast.makeText(context, "Input id-user/code", Toast.LENGTH_SHORT).show();
                    } else {
                        int id_subs = subscriptionHandler.readId(id_user);
                        if (id_subs == -1) {
                            try {
                                String encrypt = hashingKey.encrypt(name_website, secretKey);
//                                String decrypt = hashingKey.decrypt(encrypt, secretKey);
                                if (true) {
                                    long insertSubs = SubscriptionHandler.insertDataSubs(id_user, name_website, encrypt, dateStart.toString(), datePlus30.toString());
                                    Toast.makeText(context, "subscription active", Toast.LENGTH_SHORT).show();
                                    checkDate(id_user);
                                    if (insertSubs != -1) {
                                        editTextWebsite.setText("");
                                        editTextUsername.setText("");
                                    }
                                } else {
                                    Toast.makeText(context, "wrong code", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "subscription still active", Toast.LENGTH_SHORT).show();
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
                editTextWebsite.setText(randomWord);
            });

        }
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
}