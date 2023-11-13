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
import com.example.kuncen.Handler.UserHandler;
import com.example.kuncen.Model.UserModel;
import com.example.kuncen.R;
import com.example.kuncen.View.MenuManager;

import java.util.ArrayList;

public class AdminAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<UserModel> userModelArrayList;
    private final Context context;
    private UserHandler userHandler;
    private DataPasswordHandler dataPasswordHandler;
    private MenuManager menuManager;

    public AdminAdapter(ArrayList<UserModel> userModelArrayList, Context context) {
        this.userModelArrayList = userModelArrayList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewIdUser, textViewUsername, textViewData, textViewPremium;
        private ImageView imageViewCopy, imageViewCopy1, imageViewRemove;
        private ConstraintLayout constraintLayoutItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayoutItem = itemView.findViewById(R.id.clItem);
            textViewIdUser = itemView.findViewById(R.id.tvIdUser);
            textViewUsername = itemView.findViewById(R.id.tvWebsite);
            textViewData = itemView.findViewById(R.id.tvPassword);
            textViewPremium = itemView.findViewById(R.id.tvPremium);
            imageViewRemove = itemView.findViewById(R.id.imageViewRemove);
            imageViewCopy1 = itemView.findViewById(R.id.imageViewCopyUsername);
            imageViewCopy = itemView.findViewById(R.id.imageViewCopyPassword);
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
        dataPasswordHandler = new DataPasswordHandler(context);
        userHandler.openRead();
        menuManager = new MenuManager();
        int countData = dataPasswordHandler.countData(userModel.getId_user());
        userHandler.openWrite();
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.textViewUsername.setText(userModel.getUsername());
            viewHolder.textViewData.setText("total data : "+String.valueOf(countData));
            viewHolder.textViewPremium.setText("gurung premium");
            viewHolder.textViewIdUser.setVisibility(View.GONE);
            viewHolder.imageViewCopy1.setVisibility(View.GONE);
            viewHolder.imageViewCopy.setVisibility(View.GONE);
            String username;
            username = viewHolder.textViewUsername.getText().toString();
            viewHolder.imageViewRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userModelArrayList.remove(holder.getAdapterPosition());
                    userHandler.deleteUser(username);
                    Toast.makeText(context, "delete, Username : " + username, Toast.LENGTH_SHORT).show();
                    notifyItemRemoved(holder.getAdapterPosition());
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
        return userModelArrayList.size();
    }

}
