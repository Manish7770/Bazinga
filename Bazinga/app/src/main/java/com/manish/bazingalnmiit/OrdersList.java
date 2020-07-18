package com.manish.bazingalnmiit;

import android.content.Intent;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.manish.bazingalnmiit.Commondata.Commondata;
import com.manish.bazingalnmiit.Interface.ItemClickListener;
import com.manish.bazingalnmiit.Model.Requests;
import com.manish.bazingalnmiit.ViewHolder.OrderViewHolder;

public class

OrdersList extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Requests,OrderViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference requests;

    ProgressBar progress;
    int count=0;
    TextView noorders,wait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_list);

        Toolbar toolbar=(Toolbar)findViewById(R.id.orderListToolbar);
        toolbar.setTitle("Orders");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noorders=findViewById(R.id.noorders);
        wait=findViewById(R.id.waitamin);

        progress=findViewById(R.id.progressorderlist);
        progress.setVisibility(View.VISIBLE);

        database=FirebaseDatabase.getInstance();
        requests=database.getReference("Requests");

        recyclerView=(RecyclerView)findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager( layoutManager);

        if (Commondata.isConnectedToInternet(this)) {
            loadOrders(Commondata.currentUID);
        }
        else
        {
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp(){

        if (Commondata.isConnectedToInternet(this)) {
            Intent intent = new Intent(this, navigationhome.class);
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
            Intent intent = new Intent(this, navigationhome.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Commondata.isConnectedToInternet(this)) {
            progress.setVisibility(View.VISIBLE);
            loadOrders(Commondata.currentUID);
            if (adapter != null)
                adapter.startListening();
        }
        else
        {
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    private void loadOrders(String uid) {

        final Query query = requests.orderByChild("uid").equalTo(uid);

        FirebaseRecyclerOptions<Requests> options =
                new FirebaseRecyclerOptions.Builder<Requests>()
                        .setQuery(query, Requests.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Requests, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull Requests model) {
                holder.txtOrderprice.setText("Rs. "+String.valueOf(model.getTotal())+".00");
                long maxtime = Long.parseLong("9999999999999");
                long timemillis = maxtime - (Long.parseLong(adapter.getRef(position).getKey()));
                String TimeSet = Commondata.getTime(timemillis);
                String Timef = "AM";
                String hour = (TimeSet.substring(0, 2));
                String min = (TimeSet.substring(3, 5));
                int hourint = Integer.parseInt(hour);
                int minint = Integer.parseInt(min);
                if (hourint > 12) {
                    hourint -= 12;
                    Timef = "PM";
                    holder.txtOrderTime.setText(hourint + ":" + min + " " + Timef);
                } else if (hourint == 12 && minint > 0) {
                    hourint -= 12;
                    Timef = "PM";
                    holder.txtOrderTime.setText(hourint + ":" + min + " " + Timef);

                } else {
                    holder.txtOrderTime.setText(hourint + ":" + min + " " + Timef);
                }
                holder.txtOrdercontact.setText(model.getPhone());
                holder.txtOrderDate.setText(Commondata.getDate(timemillis));
                holder.txtOrderAddress.setText(model.getAddress());
                if (model.getDelivered()==0)
                {
                    holder.txtOrderStatus.setText("order is placed");
                }
                else if (model.getDelivered()==1)
                {
                    holder.txtOrderStatus.setText("out for delivery");
                }
                else if (model.getDelivered()==2)
                {
                    holder.txtOrderStatus.setText("order delivered");
                }
                if(model.getDelivered()==0)
                {
                    holder.txtOrderStatus.setTextColor(getResources().getColor(R.color.red));
                }
                else if (model.getDelivered()==1)
                {
                    holder.txtOrderStatus.setTextColor(getResources().getColor(R.color.yellow));
                }
                else if (model.getDelivered()==2)
                {
                    holder.txtOrderStatus.setTextColor(getResources().getColor(R.color.darkblue));
                }

                if (Commondata.isConnectedToInternet(OrdersList.this)) {
                    holder.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onClick(View view, int position, boolean isLongClick) {
                            if (Commondata.isConnectedToInternet(OrdersList.this)) {
                                Intent intent = new Intent(OrdersList.this, OrderDetails.class);
                                intent.putExtra("OrderId", adapter.getRef(position).getKey());
                                startActivity(intent);
                            } else {
                                Toast.makeText(OrdersList.this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(OrdersList.this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
                }

                progress.setVisibility(View.INVISIBLE);
            }


            @Override
            public int getItemCount() {
                return super.getItemCount();
            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.orderlayout, parent, false);

                return new OrderViewHolder(view);
            }

        };
        recyclerView.setAdapter(adapter);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    noorders.setVisibility(View.INVISIBLE);
                    wait.setVisibility(View.INVISIBLE);
                }
                else {
                    noorders.setVisibility(View.VISIBLE);
                    wait.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
