package com.manish.bazingaadmin.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.manish.bazingaadmin.Interface.ItemClickListener;
import com.manish.bazingaadmin.R;
import com.manish.bazingaadmin.Interface.ItemClickListener;
import com.manish.bazingaadmin.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txtOrderName,txtOrderStatus,txtOrderDate,txtOrdercontact,txtOrderroom,txtOrderprice,txtOrderTime,txtOrderAddress;

    private ItemClickListener itemClickListener;

    public OrderViewHolder(View itemView) {
        super(itemView);

        txtOrderAddress=(TextView)itemView.findViewById(R.id.OrderAddress);
        txtOrderTime=(TextView)itemView.findViewById(R.id.OrderTime);
        //txtOrderId=(TextView)itemView.findViewById(R.id.orderid);
        txtOrdercontact=(TextView)itemView.findViewById(R.id.OrderPhoneNumber);
        txtOrderDate=(TextView)itemView.findViewById(R.id.Orderdate);
        txtOrderprice=(TextView)itemView.findViewById(R.id.OrderTotalprice);
        txtOrderStatus=(TextView)itemView.findViewById(R.id.OrderStatus);
        txtOrderName=itemView.findViewById(R.id.OrderName);

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
