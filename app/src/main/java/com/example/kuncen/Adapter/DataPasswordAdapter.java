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

import com.example.kuncen.Handler.DataPasswordHandler;
import com.example.kuncen.Model.DataModel;
import com.example.kuncen.Model.UserModel;
import com.example.kuncen.R;
import com.example.kuncen.View.MenuManager;

import java.util.ArrayList;

public class DataPasswordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<DataModel> dataModelArrayList;
    private ArrayList<UserModel> userModelArrayList;
    private Context context;
    private DataPasswordHandler dataPasswordHandler;
    private MenuManager menuManager;

    public DataPasswordAdapter(ArrayList<DataModel> dataModelArrayList, Context context) {
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
        DataModel dataModel = dataModelArrayList.get(position);
        dataPasswordHandler = new DataPasswordHandler(context);
        menuManager = new MenuManager();
        dataPasswordHandler.openWrite();
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.textViewIdUser.setText(String.valueOf(dataModel.getId_user()));
            viewHolder.textViewWebsiteName.setText(dataModel.getName_website());
            viewHolder.textViewUsername.setText(dataModel.getUsername());
            viewHolder.textViewPassword.setText(dataModel.getPassword());

            String website, username, password;
            website = viewHolder.textViewWebsiteName.getText().toString();
            username = viewHolder.textViewUsername.getText().toString();
            password = viewHolder.textViewPassword.getText().toString();
            int id = dataPasswordHandler.checkIdData(website, username, password);
            viewHolder.imageViewRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dataModelArrayList.remove(holder.getAdapterPosition());
                    dataPasswordHandler.deleteData(((id)));
                    Toast.makeText(context, "data has been delete, Username : " + id, Toast.LENGTH_SHORT).show();
                    notifyItemRemoved(holder.getAdapterPosition());
                }
            });

            viewHolder.imageViewCopyUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveClipboard(username);
                    Toast.makeText(context, "data username has been copy, " + username, Toast.LENGTH_SHORT).show();
                }
            });

            viewHolder.imageViewCopyPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveClipboard(password);
                    Toast.makeText(context, "data password has been copy, Username : " + username, Toast.LENGTH_SHORT).show();
                }
            });

            viewHolder.constraintLayoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveClipboard(password);
                    Toast.makeText(context, "data password has been copy, Username : " + username, Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void saveClipboard(String item) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("username_clipboard", item);
        clipboardManager.setPrimaryClip(clipData);
    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }

}
