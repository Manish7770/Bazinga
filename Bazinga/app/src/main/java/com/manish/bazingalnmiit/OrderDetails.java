package com.manish.bazingalnmiit;

import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.manish.bazingalnmiit.Commondata.Commondata;
import com.manish.bazingalnmiit.Model.Requests;
import com.manish.bazingalnmiit.ViewHolder.DetailsAdapter;
import info.hoang8f.widget.FButton;

public class OrderDetails extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String OrderId = "";
    DetailsAdapter adapter;

    LinearLayout alertlayout;
    FButton confirmButton;

    ProgressBar progress;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        alertlayout = findViewById(R.id.alertlayout);
        confirmButton = findViewById(R.id.confirmbutton);

        Toolbar toolbar=(Toolbar)findViewById(R.id.OrderDetailsToolbar);
        toolbar.setTitle("Order Details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progress=findViewById(R.id.progressorderdetails);
        progress.setVisibility(View.VISIBLE);

        recyclerView = (RecyclerView) findViewById(R.id.listOrderDetails);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (getIntent() != null) {
            OrderId = getIntent().getStringExtra("OrderId");
            if (OrderId != null && !OrderId.isEmpty()) {

                if (Commondata.isConnectedToInternet(this)) {
                    loadOrderDetails(OrderId);
                }
                else
                {
                    Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        }

        database=FirebaseDatabase.getInstance();
        reference=database.getReference("Requests").child(OrderId);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Commondata.currentRequests.getDelivered()==1)
                {
                    reference.child("delivered").setValue(2);
                    confirmButton.setText("ORDER DELIVERED");
                    alertlayout.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp(){
        if (Commondata.isConnectedToInternet(this)) {
            Intent intent = new Intent(this, OrdersList.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (Commondata.isConnectedToInternet(this)) {
            Intent intent = new Intent(this, OrdersList.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }

    }

    private void loadOrderDetails(final String orderId) {

        database=FirebaseDatabase.getInstance();
        reference=database.getReference("Requests");

        reference.child(orderId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    Commondata.currentRequests=dataSnapshot.getValue(Requests.class);

                    if (Commondata.currentRequests.getDelivered()==0)
                    {
                        confirmButton.setText("ORDER IS PLACED");
                        alertlayout.setVisibility(View.INVISIBLE);
                    }
                    else if (Commondata.currentRequests.getDelivered()==1)
                    {
                        confirmButton.setText("OUT FOR DELIVERY");
                        alertlayout.setVisibility(View.VISIBLE);
                    }
                    else if (Commondata.currentRequests.getDelivered()==2)
                    {
                        confirmButton.setText("ORDER DELIVERED");
                        alertlayout.setVisibility(View.INVISIBLE);
                    }

                    if (Commondata.currentRequests.getFoods()!=null) {
                        adapter = new DetailsAdapter(Commondata.currentRequests.getFoods());
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);

                    }
                }
                progress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
