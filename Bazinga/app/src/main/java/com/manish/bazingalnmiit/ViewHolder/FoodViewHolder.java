package com.manish.bazingalnmiit.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.manish.bazingalnmiit.Interface.ItemClickListener;
import com.manish.bazingalnmiit.R;

import info.hoang8f.widget.FButton;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView nameoffood,priceoffood;
    public ImageView foodicon;
    public FButton addtocart,outofstock;
    public ElegantNumberButton quantity;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public FoodViewHolder(View itemView) {
        super(itemView);

        foodicon=(ImageView)itemView.findViewById(R.id.foodicon);
        nameoffood=(TextView) itemView.findViewById(R.id.name_of_food);
        priceoffood= (TextView) itemView.findViewById(R.id.price_of_food);
        addtocart=(FButton) itemView.findViewById(R.id.add_to_cart);
        quantity=(ElegantNumberButton)itemView.findViewById(R.id.elegantnumberbutton);
        outofstock=(FButton) itemView.findViewById(R.id.outofstock);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
