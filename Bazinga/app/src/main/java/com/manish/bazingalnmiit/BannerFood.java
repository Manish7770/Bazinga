package com.manish.bazingalnmiit;

import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.andremion.counterfab.CounterFab;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.manish.bazingalnmiit.Commondata.Commondata;
import com.manish.bazingalnmiit.Database.Database;
import com.manish.bazingalnmiit.Model.Food;
import com.manish.bazingalnmiit.Model.MenuItems;
import com.manish.bazingalnmiit.Model.Order;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import info.hoang8f.widget.FButton;

public class BannerFood extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference food,heading;

    MenuItems headerforbanner;

    TextView bannermenuname;
    ImageView bannerimage;
    ElegantNumberButton quantity;

    CounterFab fab;
    String MenuItemsId="";
    String FoodId="";
    Food bannerfood;

    List<Order> cart=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bannerfood);

        Toolbar toolbar=(Toolbar)findViewById(R.id.BannerToolbar);
        toolbar.setTitle("Food");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database=FirebaseDatabase.getInstance();
        food=database.getReference("Food");
        heading=database.getReference("MenuItems");

        if(getIntent()!=null) {
            FoodId = getIntent().getStringExtra("FoodId");
            if (!FoodId.isEmpty() && FoodId != null) {

                food.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child(FoodId).exists()) {
                            bannerfood = dataSnapshot.child(FoodId).getValue(Food.class);
                            MenuItemsId = bannerfood.getMenuId();

                            if (Commondata.isConnectedToInternet(BannerFood.this)) {
                                loadFood(MenuItemsId);
                            }
                            else
                            {
                                Toast.makeText(BannerFood.this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

        }

        fab = (CounterFab) findViewById(R.id.bannerbuttonforcart);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartintent=new Intent(BannerFood.this,Cart.class);
                Bundle bundle=new Bundle();
                bundle.putString("ClassNo","3");
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
            Intent intent = new Intent(BannerFood.this, navigationhome.class);
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
        fab.setCount(new Database(this).getCountCart(Commondata.currentUID));
    }

    private void loadFood(String menuItemsId) {

        heading.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(MenuItemsId).exists())
                {
                    headerforbanner=dataSnapshot.child(MenuItemsId).getValue(MenuItems.class);
                    bannermenuname=(TextView)findViewById(R.id.bannermenuname);
                    bannerimage=(ImageView)findViewById(R.id.bannermenu);
                    bannermenuname.setText(""+headerforbanner.getName());
                    int address = getResources().getIdentifier( "foodlist"+MenuItemsId, "drawable", getPackageName());
                    if(address==0)
                        address = getResources().getIdentifier( "blank", "drawable", getPackageName());
                    bannerimage.setImageResource(address);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ImageView BannerFoodIcon=(ImageView)findViewById(R.id.bannerfoodicon);
        TextView BannerFoodName=(TextView)findViewById(R.id.bannerfoodname);
        TextView BannerFoodPrice=(TextView)findViewById(R.id.bannerfoodprice);
        final FButton Banneraddcart=(FButton)findViewById(R.id.banneradd_to_cart);
        final FButton outofstock=(FButton)findViewById(R.id.outofstock);
        final ElegantNumberButton bannerquantity=(ElegantNumberButton)findViewById(R.id.bannerelegantnumberbutton);
        BannerFoodName.setText(bannerfood.getName());
        BannerFoodPrice.setText("Rs. " + bannerfood.getPrice()+".00");
        int address = getResources().getIdentifier( "food"+FoodId, "drawable", getPackageName());
        if(address==0)
            address = getResources().getIdentifier( "blank", "drawable", getPackageName());
        Picasso.get().load(address).fit().centerCrop().into(BannerFoodIcon);
        cart=new Database(BannerFood.this).getCarts(Commondata.currentUID);
        outofstock.setVisibility(View.INVISIBLE);
        if(bannerfood.getStock().equals("1"))
        {
            int count=0;
            for(int i=0;i<cart.size();i++) {
                if ((BannerFoodName.getText().toString()).equals(cart.get(i).getProductName())) {
                    bannerquantity.setNumber(cart.get(i).getQuantity());
                    count++;
                }
            }

            if(count==1) {
                if (bannerquantity.getNumber().equals("0")) {
                    bannerquantity.setVisibility(View.INVISIBLE);
                    Banneraddcart.setVisibility(View.VISIBLE);
                } else {
                    Banneraddcart.setVisibility(View.INVISIBLE);
                    bannerquantity.setVisibility(View.VISIBLE);
                }
            }
            else {
                Banneraddcart.setVisibility(View.VISIBLE);
                bannerquantity.setVisibility(View.INVISIBLE);
            }

            bannerquantity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                @Override
                public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {

                    new Database(getBaseContext()).addToCart(new Order(
                                    Long.parseLong(bannerfood.getPackingCharge()),
                                    Long.parseLong(bannerfood.getPrice()),
                                    FoodId,
                                    bannerfood.getName(),
                                    bannerquantity.getNumber(),
                                    Commondata.currentUID
                            )
                    );

                    if (newValue == 0) {
                        bannerquantity.setVisibility(View.INVISIBLE);
                        Banneraddcart.setVisibility(View.VISIBLE);
                        new Database(getBaseContext()).removeFromCart(FoodId,Commondata.currentUID);
                        fab.setCount(new Database(BannerFood.this).getCountCart(Commondata.currentUID));
                    } else {
                       // Toast.makeText(BannerFood.this, "Quantity of " + bannerfood.getName() + " changed to " + bannerquantity.getNumber(), Toast.LENGTH_SHORT).show();
                    }

                }
            });

            Banneraddcart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Banneraddcart.setVisibility(View.INVISIBLE);
                    bannerquantity.setVisibility(View.VISIBLE);
                    bannerquantity.setNumber("1");
                    new Database(getBaseContext()).addToCart(new Order(
                                    Long.parseLong(bannerfood.getPackingCharge()),
                                    Long.parseLong(bannerfood.getPrice()),
                                    FoodId,
                                    bannerfood.getName(),
                                    bannerquantity.getNumber(),
                                    Commondata.currentUID
                            )
                    );
                    //Toast.makeText(BannerFood.this, "" + bannerquantity.getNumber() + " " + bannerfood.getName() + " added To cart", Toast.LENGTH_SHORT).show();
                    fab.setCount(new Database(BannerFood.this).getCountCart(Commondata.currentUID));
                }
            });
        }
        else
        {
            Banneraddcart.setVisibility(View.INVISIBLE);
            bannerquantity.setVisibility(View.INVISIBLE);
            outofstock.setVisibility(View.VISIBLE);
            int count=0;
            for(int i=0;i<cart.size();i++)
            {
                if((BannerFoodName.getText().toString()).equals(cart.get(i).getProductName()))
                {
                    bannerquantity.setNumber(cart.get(i).getQuantity());
                    count++;
                }
            }

            if(count==1)
            {
                new Database(getBaseContext()).removeFromCart(FoodId, Commondata.currentUID);
                fab.setCount(new Database(BannerFood.this).getCountCart(Commondata.currentUID));
            }
            fab.setCount(new Database(BannerFood.this).getCountCart(Commondata.currentUID));

        }

    }
}
