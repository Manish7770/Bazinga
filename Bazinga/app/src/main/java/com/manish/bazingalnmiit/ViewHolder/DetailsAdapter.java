package com.manish.bazingalnmiit.ViewHolder;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.manish.bazingalnmiit.Model.Order;
import com.manish.bazingalnmiit.OrderDetails;
import com.manish.bazingalnmiit.R;

import java.util.ArrayList;
import java.util.List;

class MyViewHolder extends RecyclerView.ViewHolder{

    public TextView txtitemname,txtfoodprice,txtpacking,txtquantity,txttotalprice;

    public MyViewHolder(View itemView) {
        super(itemView);

        txtitemname=(TextView)itemView.findViewById(R.id.ItemName);
        txtfoodprice=(TextView)itemView.findViewById(R.id.ItemFoodPrice);
        txtpacking=(TextView)itemView.findViewById(R.id.ItemPackingCost);
        txtquantity=(TextView)itemView.findViewById(R.id.ItemQuantity);
        txttotalprice=(TextView)itemView.findViewById(R.id.ItemTotalprice);

    }
}

public class DetailsAdapter extends RecyclerView.Adapter<MyViewHolder>{

    List<Order> myOrders;

    public DetailsAdapter(List<Order> myOrders) {
        this.myOrders = myOrders;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View itemView= inflater.inflate(R.layout.orderdetailslayout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Order order=myOrders.get(position);
        holder.txtitemname.setText(order.getProductName());
        holder.txtfoodprice.setText("Rs. "+order.getPrice()+".00");
        holder.txtpacking.setText("Rs. "+order.getPackingCharge()+".00");
        holder.txtquantity.setText(order.getQuantity());
        holder.txttotalprice.setText("Rs. "+String.valueOf(((order.getPrice())+(order.getPackingCharge()))*Integer.parseInt(order.getQuantity()))+".00");
    }

    @Override
    public int getItemCount() {
        return myOrders.size();
    }
}
