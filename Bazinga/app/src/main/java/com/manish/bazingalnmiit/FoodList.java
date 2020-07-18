package com.manish.bazingalnmiit;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andremion.counterfab.CounterFab;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.manish.bazingalnmiit.Commondata.Commondata;
import com.manish.bazingalnmiit.Database.Database;
import com.manish.bazingalnmiit.Interface.ItemClickListener;
import com.manish.bazingalnmiit.Model.Food;
import com.manish.bazingalnmiit.Model.MenuItems;
import com.manish.bazingalnmiit.Model.Order;
import com.manish.bazingalnmiit.ViewHolder.FoodViewHolder;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList,heading;
    FirebaseRecyclerAdapter adapter;

    MenuItems headerforfoodlist;

    TextView nameoffoodlist;
    ImageView imageinlist;
    ElegantNumberButton quantity;

    List<Order> cart=new ArrayList<>();
    CounterFab fab;
    ImageView foodlistdark,foodlistimage;

    ProgressBar progress;
    String MenuItemsId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        Toolbar toolbar=(Toolbar)findViewById(R.id.FoodListToolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        foodlistdark=findViewById(R.id.foodlistdark);
        foodlistimage=findViewById(R.id.foodlistimage);

        database=FirebaseDatabase.getInstance();
        foodList=database.getReference("Food");
        heading=database.getReference("MenuItems");

        recyclerView=(RecyclerView)findViewById(R.id.recyclerfood);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        if(getIntent()!=null)
        {
            MenuItemsId= getIntent().getStringExtra("MenuItemsId");

            if(!MenuItemsId.isEmpty() && MenuItemsId !=null)
            {
                if (Commondata.isConnectedToInternet(this)) {
                    loadFood(MenuItemsId);
                }
                else
                {
                    Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        }


        fab = (CounterFab) findViewById(R.id.buttonforcart);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartintent=new Intent(FoodList.this,Cart.class);
                Bundle bundle=new Bundle();
                bundle.putString("ClassNo","2");
                bundle.putString("MenuItemsId",MenuItemsId);
                cartintent.putExtras(bundle);
                startActivity(cartintent);
            }
        });
        fab.setCount(new Database(this).getCountCart(Commondata.currentUID));
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
    public boolean onSupportNavigateUp(){
        if (Commondata.isConnectedToInternet(this)) {
            Intent intent = new Intent(FoodList.this, navigationhome.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Commondata.isConnectedToInternet(this)) {
            progress.setVisibility(View.VISIBLE);
            progress.setIndeterminateTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            fab.setCount(new Database(this).getCountCart(Commondata.currentUID));
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
                    if(address == 0)
                        address = getResources().getIdentifier( "blank", "drawable", getPackageName());
                    Picasso.get().load(address).fit().centerCrop().into(imageinlist);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        progress=findViewById(R.id.progressfoodlist);
        progress.setVisibility(View.VISIBLE);

        Query query = foodList.orderByChild("Available").equalTo(MenuItemsId+"1");

        FirebaseRecyclerOptions<Food> options =
                new FirebaseRecyclerOptions.Builder<Food>()
                        .setQuery(query, Food.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {

            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.foodlayout, parent, false);

                return new FoodViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final FoodViewHolder holder, final int position, @NonNull final Food model) {
                holder.nameoffood.setText("" + model.getName());
                holder.priceoffood.setText("Rs. " + model.getPrice()+".00");
                int address = getResources().getIdentifier( "food"+adapter.getRef(position).getKey(), "drawable", getPackageName());
                if(address==0)
                {
                    address=getResources().getIdentifier( "blank", "drawable", getPackageName());
                }
                Picasso.get().load(address).fit().centerCrop().into(holder.foodicon);
                holder.outofstock.setVisibility(View.INVISIBLE);
                if(model.getStock().equals("1")) {
                    cart=new Database(FoodList.this).getCarts(Commondata.currentUID);
                    int count=0;
                    for(int i=0;i<cart.size();i++)
                    {
                        if((holder.nameoffood.getText().toString()).equals(cart.get(i).getProductName()))
                        {
                            holder.quantity.setNumber(cart.get(i).getQuantity());
                            count++;
                        }
                    }

                    if(count==1)
                    {
                        if(holder.quantity.getNumber().equals("0"))
                        {
                            holder.quantity.setVisibility(View.INVISIBLE);
                            holder.addtocart.setVisibility(View.VISIBLE);
                        }
                        else {
                            holder.addtocart.setVisibility(View.INVISIBLE);
                            holder.quantity.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {
                        holder.addtocart.setVisibility(View.VISIBLE);
                        holder.quantity.setVisibility(View.INVISIBLE);
                    }


                    holder.quantity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                        @Override
                        public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {

                            new Database(getBaseContext()).addToCart(new Order(
                                            Long.parseLong(model.getPackingCharge()),
                                            Long.parseLong(model.getPrice()),
                                            adapter.getRef(position).getKey(),
                                            model.getName(),
                                            holder.quantity.getNumber(),
                                            Commondata.currentUID
                                    )
                            );

                            if(newValue==0) {
                                holder.quantity.setVisibility(View.INVISIBLE);
                                holder.addtocart.setVisibility(View.VISIBLE);
                                new Database(getBaseContext()).removeFromCart(adapter.getRef(position).getKey(),Commondata.currentUID);
                                fab.setCount(new Database(FoodList.this).getCountCart(Commondata.currentUID));
                            }

                        }
                    });

                    holder.addtocart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.addtocart.setVisibility(View.INVISIBLE);
                            holder.quantity.setVisibility(View.VISIBLE);
                            holder.quantity.setNumber("1");
                            new Database(getBaseContext()).addToCart(new Order(
                                            Long.parseLong(model.getPackingCharge()),
                                            Long.parseLong(model.getPrice()),
                                            adapter.getRef(position).getKey(),
                                            model.getName(),
                                            holder.quantity.getNumber(),
                                            Commondata.currentUID
                                    )
                            );
                            fab.setCount(new Database(FoodList.this).getCountCart(Commondata.currentUID));
                        }
                    });
                }
                else {

                    holder.quantity.setVisibility(View.INVISIBLE);
                    holder.addtocart.setVisibility(View.INVISIBLE);
                    holder.outofstock.setVisibility(View.VISIBLE);

                    cart=new Database(FoodList.this).getCarts(Commondata.currentUID);
                    int count=0;
                    for(int i=0;i<cart.size();i++)
                    {
                        if((holder.nameoffood.getText().toString()).equals(cart.get(i).getProductName()))
                        {
                            holder.quantity.setNumber(cart.get(i).getQuantity());
                            count++;
                        }
                    }

                    if(count==1)
                    {
                        new Database(getBaseContext()).removeFromCart(adapter.getRef(position).getKey(), Commondata.currentUID);
                        fab.setCount(new Database(FoodList.this).getCountCart(Commondata.currentUID));
                    }
                    fab.setCount(new Database(FoodList.this).getCountCart(Commondata.currentUID));
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
