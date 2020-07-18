package com.manish.bazingaadmin.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.manish.bazingaadmin.Interface.ItemClickListener;
import com.manish.bazingaadmin.R;
import info.hoang8f.widget.FButton;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView nameoffood,status;
    public ImageView background;
    public FButton addstock,deletestock;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public FoodViewHolder(View itemView) {
        super(itemView);
        background=(ImageView) itemView.findViewById(R.id.background);
        nameoffood=(TextView) itemView.findViewById(R.id.name_of_food);
        status= (TextView) itemView.findViewById(R.id.status_of_food);
        addstock=(FButton) itemView.findViewById(R.id.addstock);
        deletestock=(FButton) itemView.findViewById(R.id.deletestock);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
