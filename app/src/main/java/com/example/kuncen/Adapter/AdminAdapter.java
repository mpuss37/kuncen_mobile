package com.example.kuncen.Adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kuncen.Handler.UserHandler;
import com.example.kuncen.Model.UserModel;
import com.example.kuncen.R;
import com.example.kuncen.View.MenuManager;

import java.util.ArrayList;

public class AdminAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<UserModel> userModelArrayList;
    private Context context;
    private UserHandler userHandler;
    private MenuManager menuManager;

    public AdminAdapter(ArrayList<UserModel> userModelArrayList, Context context) {
        this.userModelArrayList = userModelArrayList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewIdUser, textViewUsername, textViewPassword;
        private ImageView imageViewRemove;
        private ConstraintLayout constraintLayoutItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayoutItem = itemView.findViewById(R.id.clItem);
            textViewIdUser = itemView.findViewById(R.id.tvIdUser);
            textViewUsername = itemView.findViewById(R.id.tvUsername);
            textViewPassword = itemView.findViewById(R.id.tvPassword);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        UserModel userModel = userModelArrayList.get(position);
        userHandler = new UserHandler(context);
        menuManager = new MenuManager();
        String username = null;
        userHandler.openWrite();
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.textViewUsername.setText(userModel.getUsername());
            viewHolder.textViewPassword.setText(userModel.getPassword());

            String website;
            String password;
            username = viewHolder.textViewUsername.getText().toString();
            password = viewHolder.textViewPassword.getText().toString();
//            viewHolder.imageViewRemove.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dataModelArrayList.remove(holder.getAdapterPosition());
//                    userHandler.deleteData(((id)));
//                    Toast.makeText(context, "data has been delete, Username : " + id, Toast.LENGTH_SHORT).show();
//                    notifyItemRemoved(holder.getAdapterPosition());
//                }
//            });
//
//            viewHolder.imageViewCopyUsername.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    saveClipboard(username);
//                    Toast.makeText(context, "data username has been copy, " + username, Toast.LENGTH_SHORT).show();
//                }
//            });
//
//            viewHolder.imageViewCopyPassword.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    saveClipboard(password);
//                    Toast.makeText(context, "data password has been copy, Username : " + username, Toast.LENGTH_SHORT).show();
//                }
//            });

//            viewHolder.constraintLayoutItem.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    saveClipboard(password);
//                    Toast.makeText(context, "data password has been copy, Username : " + username, Toast.LENGTH_SHORT).show();
//                }
//            });
        }

    }

    private void saveClipboard(String item) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("username_clipboard", item);
        clipboardManager.setPrimaryClip(clipData);
    }

    @Override
    public int getItemCount() {
        return userModelArrayList.size();
    }

}
