package com.example.quizapp.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.Interface.ItemClickListener;
import com.example.quizapp.R;

public class CatagoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView catagory_name;
    public ImageView catagory_image;

    private ItemClickListener itemClickListener;

    public CatagoryViewHolder(@NonNull View itemView) {
        super(itemView);
        catagory_image = (ImageView)itemView.findViewById(R.id.catagory_image);
        catagory_name = (TextView) itemView.findViewById(R.id.catagory_name);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(),false);
    }
}
