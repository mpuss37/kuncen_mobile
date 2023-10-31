package com.example.kuncen.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kuncen.Model.DataModel;
import com.example.kuncen.R;

import java.util.ArrayList;

public class DataPasswordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<DataModel> arrayListDataModel;
    private Context context;

    public DataPasswordAdapter(ArrayList<DataModel> arrayListDataModel, Context context) {
        this.arrayListDataModel = arrayListDataModel;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewWebsiteName, textViewUsername, textViewPassword;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewWebsiteName = itemView.findViewById(R.id.tvNameWebsite);
            textViewUsername = itemView.findViewById(R.id.tvUsername);
            textViewPassword = itemView.findViewById(R.id.tvPassword);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.password_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DataModel dataModel = arrayListDataModel.get(position);
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.textViewWebsiteName.setText(dataModel.getName_website());
            viewHolder.textViewUsername.setText(dataModel.getUsername());
            viewHolder.textViewPassword.setText(dataModel.getPassword());
        }
    }

    @Override
    public int getItemCount() {
        return arrayListDataModel.size();
    }

}
