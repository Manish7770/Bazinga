package com.manish.bazingaadmin;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.manish.bazingaadmin.Common.Common;
import com.manish.bazingaadmin.Model.DataMessage;
import com.manish.bazingaadmin.Model.MyResponse;
import com.manish.bazingaadmin.Model.Requests;
import com.manish.bazingaadmin.Model.Token;
import com.manish.bazingaadmin.Remote.APIService;
import com.manish.bazingaadmin.ViewHolder.DetailsAdapter;

import java.util.HashMap;
import java.util.Map;

import info.hoang8f.widget.FButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ordersDetails extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String OrderId = "";
    String phone="";
    DetailsAdapter adapter;

    String confirmation="";

    FirebaseDatabase database;
    DatabaseReference reference;

    LinearLayout alertlayout;
    TextView orderalert;
    FButton confirmButton,callnow;

    APIService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_details);

        alertlayout=findViewById(R.id.alertlayout);
        orderalert=findViewById(R.id.orderalert);

        mService=Common.getFCMClient();

        Toolbar toolbar=(Toolbar)findViewById(R.id.OrderDetailsToolbar);
        toolbar.setTitle("Order Details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       // confirmation=Common.currentRequests.getStatus();



        recyclerView = (RecyclerView) findViewById(R.id.listOrderDetails);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        confirmButton=(FButton)findViewById(R.id.confirmbutton);
        callnow=findViewById(R.id.callnow);

        if (getIntent() != null) {
            OrderId = getIntent().getStringExtra("OrderId");
            if (!OrderId.isEmpty() && OrderId != null) {

                if (Common.isConnectedToInternet(this)) {
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
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    phone=dataSnapshot.child("phone").getValue().toString();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        callnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Common.isConnectedToInternet(ordersDetails.this))
                {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+Common.currentRequests.getPhone()));
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(ordersDetails.this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Common.isConnectedToInternet(ordersDetails.this))
                {

                    if (Common.currentRequests.getDelivered()==0){
                        reference.child("delivered").setValue(1);
                        confirmButton.setText("OUT FOR DELIVERY");
                        Common.currentRequests.setDelivered(1);
                        sendOrderStatusToUser();
                        orderalert.setText("Press button when order is delivered ");
                        alertlayout.setVisibility(View.VISIBLE);
                        Uri sms_uri = Uri.parse("smsto:"+phone);
                        Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
                        sms_intent.putExtra("sms_body", "Your Order "+OrderId+" is Out for Delivery. Please collect it from Hostel Main door.");
                        startActivity(sms_intent);
                    }
                    else if (Common.currentRequests.getDelivered()==1){
                        reference.child("delivered").setValue(2);
                        confirmButton.setText("ORDER DELIVERED");
                        alertlayout.setVisibility(View.INVISIBLE);
                        Common.currentRequests.setDelivered(2);
                    }
                }
                else
                {
                    Toast.makeText(ordersDetails.this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void sendOrderStatusToUser() {
        DatabaseReference tokens=database.getReference("Tokens");
        tokens.orderByKey().equalTo(Common.currentRequests.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapShot:dataSnapshot.getChildren())
                        {
                            Token token =postSnapShot.getValue(Token.class);

                            Map<String,String> dataSend=new HashMap<>();
                            dataSend.put("title","Your order is out for delivery");
                            dataSend.put("message","Please confirm when your order has been delivered");
                            dataSend.put("orderId",OrderId);

                            DataMessage message=new DataMessage(token.getToken(),dataSend);

                            mService.sendNotification( message)
                                    .enqueue(new Callback<MyResponse>() {
                                        @Override
                                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {

                                            if (response.code() == 200) {
                                                if (response.body().success == 1) {
                                                    Toast.makeText(getApplicationContext(), "Out for Delivery Updated", Toast.LENGTH_SHORT).show();
                                                    //finish();
                                                } else {
                                                   // Toast.makeText(getApplicationContext(), "Failed !!!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<MyResponse> call, Throwable t) {


                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void loadOrderDetails(final String orderId) {
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("Requests");

        reference.child(orderId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    //Log.i("123654","hii");
                    Common.currentRequests=dataSnapshot.getValue(Requests.class);

                    if (Common.currentRequests.getDelivered()==0)
                    {
                        confirmButton.setText("ORDER IS PLACED");
                        orderalert.setText("Press button when order is out for delivery ");
                        alertlayout.setVisibility(View.VISIBLE);
                    }
                    else if (Common.currentRequests.getDelivered()==1)
                    {
                        confirmButton.setText("OUT FOR DELIVERY");
                        orderalert.setText("Press button when order is delivered ");
                        alertlayout.setVisibility(View.VISIBLE);
                    }
                    else if (Common.currentRequests.getDelivered()==2)
                    {
                        confirmButton.setText("ORDER DELIVERED");
                        alertlayout.setVisibility(View.INVISIBLE);
                    }

                    if (Common.currentRequests.getFoods()!=null) {
                        adapter = new DetailsAdapter(Common.currentRequests.getFoods());
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
