package com.manish.bazingalnmiit.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.manish.bazingalnmiit.Interface.ItemClickListener;
import com.manish.bazingalnmiit.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView cart_name,cart_price,packingcharge;
    public ElegantNumberButton btnquantity;

    public ImageView cartImage;

    public RelativeLayout viewbackground;
    public LinearLayout viewforeground;

    private ItemClickListener itemClickListener;

    public void setCart_name(TextView cart_name) {
        this.cart_name = cart_name;
    }

    public CartViewHolder(View itemView) {
        super(itemView);

        cartImage=(ImageView)itemView.findViewById(R.id.cartimage);
        cart_name=(TextView)itemView.findViewById(R.id.cartitemname);
        cart_price=(TextView)itemView.findViewById(R.id.cartitemprice);
        packingcharge=(TextView)itemView.findViewById(R.id.packingcharges);
        btnquantity=(ElegantNumberButton) itemView.findViewById(R.id.buttonquantitycart);

        viewbackground=(RelativeLayout)itemView.findViewById(R.id.deletebground);
        viewforeground=(LinearLayout)itemView.findViewById(R.id.viewforeground);


    }

    @Override
    public void onClick(View v) {

    }
}
