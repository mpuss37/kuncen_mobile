package com.example.kuncen.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kuncen.R;

import java.util.ArrayList;

public class AdapterPassword extends RecyclerView.Adapter<AdapterPassword.ViewHolder> {
    private Context context;
    private ArrayList<String> arrayListUsername;
    private ArrayList<String> arrayListNumber;

    public AdapterPassword(Context context, ArrayList<String> arrayListUsername, ArrayList<String> arrayListNumber) {
        this.context = context;
        this.arrayListUsername = arrayListUsername;
        this.arrayListNumber = arrayListNumber;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.password_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String number = arrayListUsername.get(position);
        holder.tvUsername.setText(arrayListUsername.get(position));
        holder.imageViewRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeData(number);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayListUsername.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvPassword, tvNumber;
        ImageView imageViewRemove, imageViewCopy;

        public ViewHolder(View view) {
            super(view);
            tvUsername = itemView.findViewById(R.id.textView2);
            tvPassword = itemView.findViewById(R.id.textView3);
            imageViewRemove = itemView.findViewById(R.id.imageViewRemove);
            imageViewCopy = itemView.findViewById(R.id.imageViewCopyPass);
        }
    }

    private void removeData(String item) {
        int position = arrayListUsername.indexOf(item);
        if (position != -1) {
            arrayListUsername.remove(position);
            notifyItemRemoved(position);
        }
    }
}
