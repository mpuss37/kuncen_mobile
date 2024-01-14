package com.example.kuncen.Adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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

    private String username, pass, passDecrypt, passChecker, passHash1;

    public CheckerAdapter(ArrayList<DataModel> dataModelArrayList, Context context) {
        this.dataModelArrayList = dataModelArrayList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewIdUser, textViewWebsiteName, textViewUsername, textViewPassword;
        private ImageView imageViewCopyUsername, imageViewCopyPassword, imageViewRemove;
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
            imageViewCopyUsername.setImageResource(R.drawable.check);
            imageViewCopyPassword.setVisibility(View.GONE);
            imageViewRemove.setVisibility(View.GONE);
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
                passDecrypt = hashingKey.decrypt(dataModel.getPassword(), mainActivity.secretKey);
                viewHolder.textViewPassword.setText(passDecrypt);
                passHash1 = hashingKey.passToHash1(passDecrypt);
            } catch (Exception e) {
            }
            username = viewHolder.textViewUsername.getText().toString();
            pass = viewHolder.textViewPassword.getText().toString();

            viewHolder.imageViewCopyUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    saveClipboard(username);
//                    Toast.makeText(context, "akunmu kebegal, Ganti password", Toast.LENGTH_SHORT).show();
                }
            });

            viewHolder.constraintLayoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "" + passHash1.substring(0, 5), Toast.LENGTH_SHORT).show();
//                    if (hashingKey.checkerPass(passHash1).equals("")) {
//                        Toast.makeText(context, "Gk Anjay", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(context, "Anjay", Toast.LENGTH_SHORT).show();
//                    }
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
