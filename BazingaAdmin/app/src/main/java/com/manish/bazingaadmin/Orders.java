package com.manish.bazingaadmin;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.manish.bazingaadmin.Common.Common;
import com.manish.bazingaadmin.Interface.ItemClickListener;
import com.manish.bazingaadmin.Model.Order;
import com.manish.bazingaadmin.Model.Requests;
import com.manish.bazingaadmin.Model.Token;
import com.manish.bazingaadmin.ViewHolder.OrderViewHolder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import info.hoang8f.widget.FButton;

public class Orders extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Requests,OrderViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference requests,users;

    SwipeRefreshLayout swipeRefreshLayout;

    int count=0;
    TextView noorders,wait;

    ProgressBar progressBar;

    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Orders");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database=FirebaseDatabase.getInstance();
        requests=database.getReference("Requests");
        users=database.getReference("User");

        progressBar=findViewById(R.id.progressorders);
        progressBar.setVisibility(View.VISIBLE);

        requests.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postsnapshot: dataSnapshot.getChildren())
                {
                   if ((postsnapshot.child("payment").exists())&&(!(postsnapshot.child("date").exists()))) {
                       requests.child(postsnapshot.getKey()).child("date").setValue(postsnapshot.child("payment").getValue().toString() + " " + Common.getDate(Long.parseLong("9999999999999") - Long.parseLong(postsnapshot.getKey())));
                   }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        swipeRefreshLayout=findViewById(R.id.swipelayout);
        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                requests.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapShot: dataSnapshot.getChildren())
                        {

                            if (!(postSnapShot.child("payment").exists())) {
                                String urlorderId = postSnapShot.getKey();
                                String urlChecksum = null;
                                if (postSnapShot.child("CHECKSUMHASH").exists())
                                    urlChecksum = postSnapShot.child("CHECKSUMHASH").getValue().toString();
                                else if (postSnapShot.child("checksumhash").exists())
                                    urlChecksum = postSnapShot.child("checksumhash").getValue().toString();

                                String url = "https://securegw.paytm.in/merchant-status/getTxnStatus?JsonData={%22MID%22:%22KRVHos43349349683725%22,%22ORDERID%22:%22" + urlorderId + "%22,%22CHECKSUMHASH%22:%22" + urlChecksum + "%22}";
                                new JsonTask().execute(url, urlorderId);

                            }
                        }
                        swipeRefreshLayout.setRefreshing(false);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {

                if (id==R.id.orders)
                {

                }
                else if (id==R.id.prevOrders)
                {
                    Intent intent=new Intent(Orders.this,previousOrders.class);
                    startActivity(intent);
                }
                else if (id==R.id.signout)
                {
                    Common.currentAdmin=null;
                    Intent intent=new Intent(Orders.this,SignInActivity.class);
                    startActivity(intent);
                }
                else if (id==R.id.daymenu)
                {
                    Intent intent=new Intent(Orders.this,DayMenu.class);
                    startActivity(intent);
                }
                else if (id==R.id.setmenu)
                {
                    Intent intent=new Intent(Orders.this,SetMenu.class);
                    startActivity(intent);
                }
                else if (id==R.id.nightmenu)
                {
                    Intent intent=new Intent(Orders.this,NightMenu.class);
                    startActivity(intent);
                }
                else if (id==R.id.sale)
                {
                    Intent intent=new Intent(Orders.this,Sale.class);
                    startActivity(intent);
                }
                else if (id==R.id.saleexcel)
                {
                    Intent intent=new Intent(Orders.this,salexcel.class);
                    startActivity(intent);
                }


            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true);

        noorders=findViewById(R.id.noorders);
        wait=findViewById(R.id.waitamin);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerorders);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager( layoutManager);

        if (Common.isConnectedToInternet(this)) {
            loadOrders();
        }
        else
        {
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }

        updateToken(FirebaseInstanceId.getInstance().getToken());


    }

    private void updateToken(String token) {

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference tokens=database.getReference("Tokens");

        Token data=new Token(token,true);
        //false is client side

        tokens.child("ADMIN").setValue(data);

    }

    private void loadOrders() {
        final Query query=requests.orderByChild("payment").equalTo(true).limitToFirst(100);

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

                if (Common.isConnectedToInternet(Orders.this)) {
                    holder.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onClick(View view, int position, boolean isLongClick) {
                            if (Common.isConnectedToInternet(Orders.this)) {
                                Intent intent = new Intent(Orders.this, ordersDetails.class);
                                //Common.currentRequests = finalModel;
                                intent.putExtra("OrderId", key);
                                startActivity(intent);
                            } else {
                                Toast.makeText(Orders.this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(Orders.this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
                }

                progressBar.setVisibility(View.INVISIBLE)   ;
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
        recyclerView.setAdapter(adapter);

        //check for wmpty orders
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


    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                }

                String result=buffer.toString();
                Log.i("result123",params[1]+"  "+result);
                if (result.contains("TXN_SUCCESS"))
                {
                    requests.child(params[1]).child("payment").setValue(true);
                    requests.child(params[1]).child("delivered").setValue(0);
                    requests.child(params[1]).child("date").setValue("true "+Common.getDate(Long.parseLong("9999999999999") - Long.parseLong(params[1])));
                }
                else if (result.contains("TXN_FAILURE"))
                {
                    requests.child(params[1]).removeValue();
                }

                return (buffer.toString());


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent=new Intent(Orders.this,SignInActivity.class);
            startActivity(intent);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    public String capsName(String Name)
    {
        Name=Name.trim();
        String Caps="";
        char first=Character.toUpperCase(Name.charAt(0));
        Caps+=first;
        Caps+=Name.substring(1,Name.length());
        return Caps;
    }
}
