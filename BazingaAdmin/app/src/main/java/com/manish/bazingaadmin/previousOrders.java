package com.manish.bazingaadmin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.manish.bazingaadmin.Common.Common;
import com.manish.bazingaadmin.Interface.ItemClickListener;
import com.manish.bazingaadmin.Model.Requests;
import com.manish.bazingaadmin.ViewHolder.OrderViewHolder;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import info.hoang8f.widget.FButton;

public class previousOrders extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    FButton dateChange;
    TextView dateprevOrders;

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    TextView noorders,wait;
    FirebaseRecyclerAdapter<Requests,OrderViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference requests,users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_orders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.previousOrdersToolbar);
        toolbar.setTitle("Orders History");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dateChange=(FButton)findViewById(R.id.changeDate);
        dateprevOrders=findViewById(R.id.datetext);
        noorders=findViewById(R.id.noordersprev);
        wait=findViewById(R.id.waitaminprev);

        database=FirebaseDatabase.getInstance();
        requests=database.getReference("Requests");
        users=database.getReference("User");

        recyclerView=(RecyclerView)findViewById(R.id.previousOrderList);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager( layoutManager);

        final Calendar now=Calendar.getInstance();
        long time=now.getTimeInMillis();
        String datedisplay=Common.getDate(time);
        final int todayday=now.get(Calendar.DAY_OF_MONTH);
        final int todaymonth=now.get(Calendar.MONTH);
        final int todayyear=now.get(Calendar.YEAR);
        dateprevOrders.setText(datedisplay);

        dateChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog=DatePickerDialog.newInstance(previousOrders.this
                ,todayyear
                ,todaymonth
                ,todayday);
                datePickerDialog.setAccentColor(getResources().getColor(R.color.toolbar_end));
                datePickerDialog.show(getFragmentManager(),"DatePicker");
            }
        });

        if (Common.isConnectedToInternet(this)) {
            loadOrders(datedisplay);
        }
        else
        {
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date=DATE(year,(monthOfYear+1),dayOfMonth);
        dateprevOrders.setText(date);
        recyclerView.removeAllViews();
        loadOrders(date);
        if (adapter!=null)
            adapter.startListening();
    }

    private void loadOrders(String datesearch) {

        final Query query=requests.orderByChild("date").equalTo("true "+datesearch);

        FirebaseRecyclerOptions<Requests> options =
                new FirebaseRecyclerOptions.Builder<Requests>()
                        .setQuery(query,Requests.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Requests, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final OrderViewHolder holder, int position, @NonNull Requests model) {
                final String key=adapter.getRef(position).getKey().substring(0,13);
                holder.txtOrderprice.setText("Rs. "+model.getTotal()+".00");
                long maxtime =Long.parseLong("9999999999999");
                long timemillis = maxtime - (Long.parseLong(key));
                String TimeSet = Common.getTime(timemillis);
                String Timef = "AM";
                String hour = (TimeSet.substring(0,2));
                String min = (TimeSet.substring(3,5));
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
                holder.txtOrderAddress.setText(model.getAddress());
                holder.txtOrdercontact.setText(model.getPhone());
                holder.txtOrderDate.setText(Common.getDate(timemillis));

                users.child(model.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists())
                        {
                            String name=dataSnapshot.child("name").getValue().toString();
                            if (name!=null&&(!name.isEmpty()))
                                holder.txtOrderName.setText(name);
                            else
                                holder.txtOrderName.setText(" ");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                if (model.getDelivered()==1)
                {
                    holder.txtOrderStatus.setText("Out for Delivery");
                    holder.txtOrderStatus.setTextColor(getResources().getColor(R.color.yellow));
                }
                else if (model.getDelivered()==0)
                {
                    holder.txtOrderStatus.setText("Order is placed");
                    holder.txtOrderStatus.setTextColor(getResources().getColor(R.color.red));
                }
                else if (model.getDelivered()==2)
                {
                    holder.txtOrderStatus.setText("Order Delivered");
                    holder.txtOrderStatus.setTextColor(getResources().getColor(R.color.darkblue));
                }

                final Requests finalModel = model;

                if (Common.isConnectedToInternet(previousOrders.this)) {
                    holder.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onClick(View view, int position, boolean isLongClick) {
                            if (Common.isConnectedToInternet(previousOrders.this)) {
                                Intent intent = new Intent(previousOrders.this, ordersDetails.class);
                                //Common.currentRequests = finalModel;
                                intent.putExtra("OrderId", key);
                                startActivity(intent);
                            } else {
                                Toast.makeText(previousOrders.this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(previousOrders.this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
                }
            }


            @Override
            public int getItemCount() {
                return super.getItemCount();
            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.orderslistcard, parent, false);

                return new OrderViewHolder(view);
            }

        };

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
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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

    @Override
    public void onBackPressed() {
        if (Common.isConnectedToInternet(previousOrders.this)) {
            Intent intent = new Intent(previousOrders.this, Orders.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(previousOrders.this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (Common.isConnectedToInternet(previousOrders.this)) {
            Intent intent = new Intent(previousOrders.this, Orders.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(previousOrders.this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }
        return true;
    }

    public String capsName(String Name)
    {
        Name=Name.trim();
        String Caps="";
        char first=Character.toUpperCase(Name.charAt(0));
        Caps+=first;
        Caps+=Name.substring(1,Name.length());
        return Caps;
    }

    public String DATE(int year,int month,int day)
    {
        String dateformat="";
        if(day<=9)
        {
            dateformat+="0";
            dateformat+=day;
        }
        else
        {
           dateformat+=day;
        }
        dateformat+="-";
        if (month<=9)
        {
            dateformat+="0";
            dateformat+=month;
        }
        else
        {
            dateformat+=month;
        }
        dateformat+="-";
        dateformat+=year;
        return  dateformat;
    }
}
