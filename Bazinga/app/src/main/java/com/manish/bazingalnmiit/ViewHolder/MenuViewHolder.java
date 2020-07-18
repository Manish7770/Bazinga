package com.manish.bazingalnmiit.ViewHolder;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.manish.bazingalnmiit.Interface.ItemClickListener;
import com.manish.bazingalnmiit.R;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView menuitemname;
    public ImageView imageView;

    private ItemClickListener itemClickListener;

    public MenuViewHolder(View itemView) {
        super(itemView);

        menuitemname= itemView.findViewById(R.id.menuitemname);
        imageView= itemView.findViewById(R.id.imageviewmenu);

        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);

    }
}
