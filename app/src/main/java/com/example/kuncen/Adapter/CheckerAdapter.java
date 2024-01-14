package com.example.kuncen.Adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kuncen.EncryptionKey.HashingKey;
import com.example.kuncen.Handler.DataPasswordHandler;
import com.example.kuncen.Model.DataModel;
import com.example.kuncen.R;
import com.example.kuncen.View.MainActivity;
import com.example.kuncen.View.MenuManager;

import java.util.ArrayList;

public class CheckerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<DataModel> dataModelArrayList;
    private Context context;
    private DataPasswordHandler dataPasswordHandler;
    private MenuManager menuManager;
    private HashingKey hashingKey;
    private MainActivity mainActivity;

    public CheckerAdapter(ArrayList<DataModel> dataModelArrayList, Context context) {
        this.dataModelArrayList = dataModelArrayList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewIdUser, textViewWebsiteName, textViewUsername, textViewPassword, textViewPasswordChecker;
        private ImageView imageViewCopyUsername, imageViewCopyPassword, imageViewRemove, imageViewWebsite;
        private ConstraintLayout constraintLayoutItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayoutItem = itemView.findViewById(R.id.clItem);
            textViewIdUser = itemView.findViewById(R.id.tvIdUser);
            textViewWebsiteName = itemView.findViewById(R.id.tvWebsite);
            textViewUsername = itemView.findViewById(R.id.tvUsername);
            textViewPassword = itemView.findViewById(R.id.tvPassword);
            textViewPasswordChecker = itemView.findViewById(R.id.tvPasswordChecker);
            imageViewCopyUsername = itemView.findViewById(R.id.imageViewCopyUsername);
            imageViewCopyPassword = itemView.findViewById(R.id.imageViewCopyPassword);
            imageViewRemove = itemView.findViewById(R.id.imageViewRemove);
            imageViewWebsite = itemView.findViewById(R.id.imageViewWebsite);
            imageViewRemove.setVisibility(View.GONE);
            imageViewCopyPassword.setVisibility(View.GONE);
            imageViewCopyUsername.setImageResource(R.drawable.check);
            constraintLayoutItem = itemView.findViewById(R.id.clItem);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pass_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        hashingKey = new HashingKey();
        mainActivity = new MainActivity();
        DataModel dataModel = dataModelArrayList.get(position);
        dataPasswordHandler = new DataPasswordHandler(context);
        menuManager = new MenuManager();
        dataPasswordHandler.openWrite();
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.textViewIdUser.setText(String.valueOf(dataModel.getId_user()));
            viewHolder.textViewWebsiteName.setText(dataModel.getName_website());
            viewHolder.textViewUsername.setText(dataModel.getUsername());
            try {
                String passDecrypt = hashingKey.decrypt(dataModel.getPassword(), mainActivity.secretKey);
                viewHolder.textViewPassword.setText(passDecrypt);
            } catch (Exception e) {
            }
            String username, password, passwordChecker, website;
            website = viewHolder.textViewWebsiteName.getText().toString();
            username = viewHolder.textViewUsername.getText().toString();
            password = viewHolder.textViewPassword.getText().toString();

            viewHolder.textViewPasswordChecker.setText(hashingKey.passToHash1(password));
            passwordChecker = viewHolder.textViewPasswordChecker.getText().toString();

            if (website.toLowerCase().contains("google")) {
                viewHolder.imageViewWebsite.setImageResource(R.drawable.google);
            } else if (website.toLowerCase().contains("facebook")) {
                viewHolder.imageViewWebsite.setImageResource(R.drawable.facebook);
            } else {
                int tint = Color.parseColor("#f34235");
                viewHolder.imageViewWebsite.setColorFilter(tint);
            }

            viewHolder.constraintLayoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new CheckerPassTask().execute(passwordChecker);
                }
            });
        }

    }

    private class CheckerPassTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String passHash = params[0];
            return hashingKey.checkerPass(passHash);
        }

        @Override
        protected void onPostExecute(String result) {
            // Tangani hasilnya di thread utama
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
        }
    }

    public void saveClipboard(String item) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("username_clipboard", item);
        clipboardManager.setPrimaryClip(clipData);
    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }

}
