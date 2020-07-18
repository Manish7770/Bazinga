package com.manish.bazingalnmiit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.manish.bazingalnmiit.Commondata.Commondata;
import com.manish.bazingalnmiit.Database.Database;
import com.manish.bazingalnmiit.Helper.RecyclerItemTouchHelper;
import com.manish.bazingalnmiit.Interface.RecyclerItemTouchHelperListener;
import com.manish.bazingalnmiit.Model.Order;
import com.manish.bazingalnmiit.ViewHolder.CartAdapter;
import com.manish.bazingalnmiit.ViewHolder.CartViewHolder;
import java.util.ArrayList;
import java.util.List;
import info.hoang8f.widget.FButton;

public class Cart extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference reference,foodlist;

    public TextView totalprice, deliverycharges, foodprice;
    FButton placeorder;
    public String classnumber = "", MenuItemsId = "";

    private TextView Id, emptyText;

    int total = 0, packingcost = 0, foodtotal = 0, deliverycharge = 0;

    public String billAmt = "";

    RelativeLayout rootLayout;

    List<Order> cart = new ArrayList<>();

    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            classnumber = bundle.getString("ClassNo");
            MenuItemsId = bundle.getString("MenuItemsId");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.cartToolbar);
        toolbar.setTitle("Cart");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView = (RecyclerView) findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        totalprice = (TextView) findViewById(R.id.total);
        deliverycharges = (TextView) findViewById(R.id.deliverycharges);
        foodprice = (TextView) findViewById(R.id.foodcharges);
        placeorder = (FButton) findViewById(R.id.buttonplaceorder);
        emptyText = (TextView) findViewById(R.id.emptycarttext);

        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/NABILA.TTF");
        emptyText.setTypeface(typeface);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("DeliveryCharge");
        foodlist=database.getReference("Food");

        rootLayout = (RelativeLayout) findViewById(R.id.rootlayout);

        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Commondata.isConnectedToInternet(Cart.this)) {

                    if (!(totalprice.getText().toString().equals("Rs. 0.00"))) {

                        Commondata.cartforpayment = cart;
                        Intent intent = new Intent(Cart.this, Address.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("BillAmt", billAmt);
                        intent.putExtras(bundle);

                        final DatabaseReference userweb=database.getReference("User");
                        userweb.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!(dataSnapshot.child(Commondata.currentUID).exists()))
                                {
                                    userweb.child(Commondata.currentUID).setValue(Commondata.currentUser);
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        startActivity(intent);

                    } else {
                        Toast.makeText(Cart.this, "Cart is Empty", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Cart.this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        if (Commondata.isConnectedToInternet(this)) {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    deliverycharge = Integer.parseInt(dataSnapshot.getValue().toString());
                    loadListFood();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Commondata.isConnectedToInternet(this)) {
            loadListFood();
        } else {
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(Cart.this, navigationhome.class);
        if (Commondata.isConnectedToInternet(this)) {
            if (classnumber.equals("2")) {
                intent = new Intent(Cart.this, FoodList.class);
                Bundle bundle = new Bundle();
                bundle.putString("MenuItemsId", MenuItemsId);
                intent.putExtras(bundle);
            }
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }
        return true;
    }


    public void loadListFood() {


        cart = new Database(Cart.this).getCarts(Commondata.currentUID);
        adapter = new CartAdapter(cart, this, deliverycharge);
        recyclerView.setAdapter(adapter);

        if (cart.size() == 0) {
            emptyText.setVisibility(View.VISIBLE);
        }

        total = 0;
        packingcost = 0;
        foodtotal = 0;

        int delivery = 0;

        if (cart.size() != 0)
            delivery = deliverycharge;

        deliverycharges.setText("Rs. " + (delivery) + ".00");
        for (Order order : cart) {
            packingcost += (order.getPackingCharge()) * (Integer.parseInt(order.getQuantity()));
            foodtotal += (order.getPrice()) * (Integer.parseInt(order.getQuantity()));
        }
        total = packingcost + foodtotal + delivery;

        foodprice.setText("Rs. " + (foodtotal + packingcost) + ".00");
        totalprice.setText("Rs. " + (total) + ".00");

        billAmt = "" + total;

    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartViewHolder) {
            String name = ((CartAdapter) recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition()).getProductName();

            final Order deleteitem = ((CartAdapter) recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition());
            final int deleteindex = viewHolder.getAdapterPosition();

            adapter.removeItem(deleteindex);
            new Database(getBaseContext()).removeFromCart(deleteitem.getProductId(), Commondata.currentUID);

            total = 0;
            packingcost = 0;
            foodtotal = 0;

            int delivery = 0;
            List<Order> orders = new Database(getBaseContext()).getCarts(Commondata.currentUID);
            if (orders.size() != 0)
                delivery = deliverycharge;
            deliverycharges.setText("Rs. " + (delivery) + ".00");
            for (Order item : orders) {
                packingcost += (item.getPackingCharge()) * (Integer.parseInt(item.getQuantity()));
                foodtotal += (item.getPrice()) * (Integer.parseInt(item.getQuantity()));
            }
            total = packingcost + foodtotal + delivery;

            billAmt = "" + total;

            foodprice.setText("Rs. " + (foodtotal + packingcost) + ".00");
            totalprice.setText("Rs. " + (total) + ".00");

            Snackbar snackbar = Snackbar.make(rootLayout, name + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.restoreItem(deleteitem, deleteindex);
                    new Database(getBaseContext()).addToCart(deleteitem);

                    emptyText.setVisibility(View.INVISIBLE);


                    total = 0;
                    packingcost = 0;
                    foodtotal = 0;

                    int delivery = 0;
                    List<Order> orders = new Database(getBaseContext()).getCarts(Commondata.currentUID);
                    if (orders.size() != 0) {
                        delivery = deliverycharge;
                    }
                    deliverycharges.setText("Rs. " + (delivery) + ".00");
                    for (Order item : orders) {
                        packingcost += (item.getPackingCharge()) * (Integer.parseInt(item.getQuantity()));
                        foodtotal += (item.getPrice()) * (Integer.parseInt(item.getQuantity()));
                    }
                    total = packingcost + foodtotal + delivery;

                    billAmt = "" + total;

                    foodprice.setText("Rs. " + (foodtotal + packingcost) + ".00");
                    totalprice.setText("Rs. " + (total) + ".00");

                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
            loadListFood();
        }
    }
}
