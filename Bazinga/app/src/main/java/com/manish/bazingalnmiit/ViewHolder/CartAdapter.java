package com.manish.bazingalnmiit.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.manish.bazingalnmiit.Cart;
import com.manish.bazingalnmiit.Database.Database;
import com.manish.bazingalnmiit.Interface.ItemClickListener;
import com.manish.bazingalnmiit.Model.Order;
import com.manish.bazingalnmiit.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder>{

    private List<Order> listData=new ArrayList<>();
    private Cart cart;

    private int deliverycharge;

    public CartAdapter(List<Order> listData, Cart cart, int deliverycharge) {
        this.listData = listData;
        this.cart = cart;
        this.deliverycharge = deliverycharge;
    }


    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(cart);
        View itemView= inflater.inflate(R.layout.cartlayout,parent,false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder holder, final int position) {

        holder.btnquantity.setNumber(listData.get(position).getQuantity());
        holder.btnquantity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Order order=listData.get(position);
                order.setQuantity(String.valueOf(newValue));
                if(order.getQuantity().equals("0"))
                {
                    listData.remove(position);
                    notifyItemRemoved(position);
                    new Database(cart).removeFromCart(order.getProductId(),order.getUid());
                    cart.loadListFood();

                }
                else {
                    new Database(cart).updateCart(order);
                    int total=0,packingcost=0,foodtotal=0;

                    int delivery=0;

                    List<Order> orders=new Database(cart).getCarts(order.getUid());
                    if(orders.size()!=0) {
                        delivery = deliverycharge;
                        cart.deliverycharges.setText("Rs. "+(delivery)+".00");
                    }
                    for (Order item:orders) {
                        packingcost+=(item.getPackingCharge())*(Integer.parseInt(item.getQuantity()));
                        foodtotal+=(item.getPrice())*(Integer.parseInt(item.getQuantity()));
                    }
                    total=packingcost+foodtotal+delivery;

                    cart.billAmt=total+"";

                    cart.foodprice.setText("Rs. "+(foodtotal+packingcost)+".00");
                    cart.totalprice.setText("Rs. "+(total)+".00");

                    long price= (listData.get(position).getPrice())*(Integer.parseInt(listData.get(position).getQuantity()));
                    long packing=(listData.get(position).getPackingCharge())*(Integer.parseInt(listData.get(position).getQuantity()));
                    holder.cart_price.setText("Rs. "+(price)+".00");
                    holder.packingcharge.setText("Rs. "+(packing)+".00");

                }

            }
        });


        long price= (listData.get(position).getPrice())*(Integer.parseInt(listData.get(position).getQuantity()));
        long packing=(listData.get(position).getPackingCharge())*(Integer.parseInt(listData.get(position).getQuantity()));
        holder.cart_price.setText("Rs. "+(price)+".00");
        holder.packingcharge.setText("Rs. "+(packing)+".00");
        holder.cart_name.setText(listData.get(position).getProductName());

        int address = cart.getResources().getIdentifier( "food"+listData.get(position).getProductId(), "drawable", cart.getPackageName());
        if(address==0)
        {
            address= cart.getResources().getIdentifier( "blank", "drawable", cart.getPackageName());
        }
        Picasso.get().load(address).fit().centerCrop().into(holder.cartImage);

    }

    @Override
    public int getItemCount() {

        return listData.size();
    }

    public Order getItem(int positon)
    {
        return listData.get(positon);
    }

    public void removeItem(int position)
    {
        listData.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Order item,int position)
    {
        listData.add(position,item);
        notifyItemInserted(position);
    }
}
