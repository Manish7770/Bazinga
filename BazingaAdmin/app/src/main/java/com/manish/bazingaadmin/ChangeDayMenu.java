package com.manish.bazingaadmin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.manish.bazingaadmin.Common.Common;
import com.manish.bazingaadmin.Interface.ItemClickListener;
import com.manish.bazingaadmin.Model.Food;
import com.manish.bazingaadmin.Model.MenuItems;
import com.manish.bazingaadmin.ViewHolder.FoodViewHolder;
import com.squareup.picasso.Picasso;


public class ChangeDayMenu extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference heading,daystocks;
    FirebaseRecyclerAdapter adapter;

    MenuItems headerforfoodlist;

    TextView nameoffoodlist;
    ImageView imageinlist;

    ImageView foodlistdark,foodlistimage;

    ProgressBar progress;
    String MenuItemsId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daystock);

        Toolbar toolbar=(Toolbar)findViewById(R.id.FoodListToolbar);
        toolbar.setTitle("STOCK");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        foodlistdark=findViewById(R.id.foodlistdark);
        foodlistimage=findViewById(R.id.foodlistimage);

        database=FirebaseDatabase.getInstance();
        heading=database.getReference("MenuItems");
        daystocks=database.getReference("Day's Menu");

        recyclerView=(RecyclerView)findViewById(R.id.recyclerfood);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        if(getIntent()!=null)
        {
            MenuItemsId= getIntent().getStringExtra("MenuItemsId");

            if(!MenuItemsId.isEmpty() && MenuItemsId !=null)
            {
                if (Common.isConnectedToInternet(this)) {
                    loadFood(MenuItemsId);
                }
                else
                {
                    Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (Common.isConnectedToInternet(this)) {
            Intent intent = new Intent(this, DayMenu.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        if (Common.isConnectedToInternet(this)) {
            Intent intent = new Intent(ChangeDayMenu.this, DayMenu.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
        if (Common.isConnectedToInternet(this)) {
            progress.setVisibility(View.VISIBLE);
            progress.setIndeterminateTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            if (adapter != null)
                adapter.startListening();
        }
        else
        {
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    private void loadFood(String menuItemsId) {

        heading.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(MenuItemsId).exists())
                {
                    headerforfoodlist=dataSnapshot.child(MenuItemsId).getValue(MenuItems.class);
                    nameoffoodlist=(TextView)findViewById(R.id.textviewfoodlist);
                    imageinlist=(ImageView)findViewById(R.id.imageviewfoodlist);
                    nameoffoodlist.setText(""+headerforfoodlist.getName());
                    int address = getResources().getIdentifier( "foodlist"+MenuItemsId, "drawable", getPackageName());
                    Picasso.get().load(address).fit().centerCrop().into(imageinlist);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        progress=findViewById(R.id.progressfoodlist);
        progress.setVisibility(View.VISIBLE);

        Query query = daystocks.orderByChild("MenuId").equalTo(MenuItemsId);

        FirebaseRecyclerOptions<Food> options =
                new FirebaseRecyclerOptions.Builder<Food>()
                        .setQuery(query, Food.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {

            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.daystock, parent, false);

                return new FoodViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final FoodViewHolder holder, final int position, @NonNull final Food model) {
                holder.nameoffood.setText("" + model.getName());
                if(model.getStock().equals("1")) {
                    holder.status.setText("IN STOCK");
                    holder.background.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    holder.addstock.setVisibility(View.INVISIBLE);
                    holder.deletestock.setVisibility(View.VISIBLE);

                    holder.deletestock.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.addstock.setVisibility(View.VISIBLE);
                            holder.deletestock.setVisibility(View.INVISIBLE);
                            daystocks.child(adapter.getRef(position).getKey()).child("Stock").setValue("0");
                        }
                    });
                }
                else {

                    holder.status.setText("OUT OF STOCK");
                    holder.background.setBackgroundColor(getResources().getColor(R.color.bagroungdelete));
                    holder.addstock.setVisibility(View.VISIBLE);
                    holder.deletestock.setVisibility(View.INVISIBLE);

                    holder.addstock.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.addstock.setVisibility(View.INVISIBLE);
                            holder.deletestock.setVisibility(View.VISIBLE);
                            daystocks.child(adapter.getRef(position).getKey()).child("Stock").setValue("1");
                        }
                    });
                }

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int positon, boolean isLongClick) {
                    }
                });
                progress.setVisibility(View.INVISIBLE);

                foodlistdark.setAlpha(0.7f);
                foodlistimage.setAlpha(1f);
            }
        };

        recyclerView.setAdapter(adapter);
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

