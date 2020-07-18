package com.manish.bazingalnmiit.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.manish.bazingalnmiit.Interface.ItemClickListener;
import com.manish.bazingalnmiit.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txtOrderStatus,txtOrderDate,txtOrdercontact,txtOrderprice,txtOrderTime,txtOrderAddress;

    private ItemClickListener itemClickListener;

    public OrderViewHolder(View itemView) {
        super(itemView);

        txtOrderStatus=(TextView)itemView.findViewById(R.id.OrderStatus);
        txtOrderAddress=(TextView)itemView.findViewById(R.id.OrderAddress);
        txtOrderTime=(TextView)itemView.findViewById(R.id.OrderTime);
        //txtOrderId=(TextView)itemView.findViewById(R.id.orderid);
        txtOrdercontact=(TextView)itemView.findViewById(R.id.OrderPhoneNumber);
        txtOrderDate=(TextView)itemView.findViewById(R.id.Orderdate);
        txtOrderprice=(TextView)itemView.findViewById(R.id.OrderTotalprice);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
