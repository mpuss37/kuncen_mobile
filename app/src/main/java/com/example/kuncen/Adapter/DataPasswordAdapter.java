package com.example.kuncen.Adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
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

public class DataPasswordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<DataModel> dataModelArrayList;
    private Context context;
    private DataPasswordHandler dataPasswordHandler;
    private MenuManager menuManager;
    private HashingKey hashingKey;
    private MainActivity mainActivity;

    public DataPasswordAdapter(ArrayList<DataModel> dataModelArrayList, Context context) {
        this.dataModelArrayList = dataModelArrayList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewIdUser, textViewWebsiteName, textViewUsername, textViewPassword;
        private ImageView imageViewCopyUsername, imageViewCopyPassword, imageViewRemove, imageViewWebsite;
        private ConstraintLayout constraintLayoutItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayoutItem = itemView.findViewById(R.id.clItem);
            textViewIdUser = itemView.findViewById(R.id.tvIdUser);
            textViewWebsiteName = itemView.findViewById(R.id.tvWebsite);
            textViewUsername = itemView.findViewById(R.id.tvUsername);
            textViewPassword = itemView.findViewById(R.id.tvPassword);
            imageViewCopyUsername = itemView.findViewById(R.id.imageViewCopyUsername);
            imageViewCopyPassword = itemView.findViewById(R.id.imageViewCopyPassword);
            imageViewRemove = itemView.findViewById(R.id.imageViewRemove);
            imageViewWebsite = itemView.findViewById(R.id.imageViewWebsite);
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
        menuManager = new MenuManager();
        DataModel dataModel = dataModelArrayList.get(position);
        dataPasswordHandler = new DataPasswordHandler(context);
        dataPasswordHandler.openWrite();
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.textViewIdUser.setText(String.valueOf(dataModel.getId_user()));
            viewHolder.textViewWebsiteName.setText(dataModel.getName_website());
            viewHolder.textViewUsername.setText(dataModel.getUsername());
            try {
                String passDecrypt = hashingKey.decrypt(dataModel.getPassword(), mainActivity.getSecretKey());
                viewHolder.textViewPassword.setText(passDecrypt);
            } catch (Exception e) {
            }
            String username, password, website;
            int id_user;
            id_user = Integer.parseInt(viewHolder.textViewIdUser.getText().toString());
            website = viewHolder.textViewWebsiteName.getText().toString();
            username = viewHolder.textViewUsername.getText().toString();
            password = viewHolder.textViewPassword.getText().toString();

            if (website.toLowerCase().contains("google")) {
                viewHolder.imageViewWebsite.setImageResource(R.drawable.google);
            } else if (website.toLowerCase().contains("facebook")) {
                viewHolder.imageViewWebsite.setImageResource(R.drawable.google);
            } else {
                int tint = Color.parseColor("#f34235");
                viewHolder.imageViewWebsite.setColorFilter(tint);
            }

            viewHolder.imageViewRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dataModelArrayList.remove(holder.getAdapterPosition());
                    dataPasswordHandler.deleteData(dataModel.getUsername());
                    Toast.makeText(context, "delete, Username : " + username, Toast.LENGTH_SHORT).show();
                    notifyItemRemoved(holder.getAdapterPosition());
                }
            });

            viewHolder.imageViewCopyUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveClipboard(username);
                    Toast.makeText(context, "username has been copy", Toast.LENGTH_SHORT).show();
                }
            });

            viewHolder.imageViewCopyPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveClipboard(password);
                    Toast.makeText(context, "password has been copy, Username : " + username, Toast.LENGTH_SHORT).show();
                }
            });

            viewHolder.constraintLayoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveClipboard(password);
                    Toast.makeText(context, "password has been copy, Username : " + username, Toast.LENGTH_SHORT).show();
                }
            });

            viewHolder.constraintLayoutItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    menuManager = new MenuManager();
                    menuManager.menuAddItem("edit_user", "adapter", context, id_user, website, username, password);
                    dataModelArrayList.clear();
                    dataModelArrayList.addAll(dataPasswordHandler.displayData(id_user));
                    notifyDataSetChanged();
                    return false;
                }
            });
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
