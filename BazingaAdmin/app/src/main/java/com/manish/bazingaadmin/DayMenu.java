package com.manish.bazingaadmin;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
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
import com.manish.bazingaadmin.Model.MenuItems;
import com.manish.bazingaadmin.ViewHolder.MenuViewHolder;
import com.squareup.picasso.Picasso;

public class DayMenu extends AppCompatActivity
{

    FirebaseDatabase database;
    DatabaseReference menuitems;
    FirebaseRecyclerAdapter adapter;

    ProgressDialog progress;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    ImageView navigationdark,navigationimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daymenu);
        Toolbar toolbar = findViewById(R.id.DayMenuToolbar);
        toolbar.setTitle("Day's Menu");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclermenu);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Context context=recyclerView.getContext();
        LayoutAnimationController controller=null;

        controller= AnimationUtils.loadLayoutAnimation(context,R.anim.layout_fall_down);
        recyclerView.setLayoutAnimation(controller);

        progress=new ProgressDialog(this);
        progress.setMessage("LOADING...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();

        navigationimage=findViewById(R.id.navigationimage);
        navigationdark=findViewById(R.id.navigationdark);

        database = FirebaseDatabase.getInstance();
        menuitems = database.getReference("MenuItems");

        Query query = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("MenuItems");

        FirebaseRecyclerOptions<MenuItems> options =
                new FirebaseRecyclerOptions.Builder<MenuItems>()
                        .setQuery(query, MenuItems.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<MenuItems, MenuViewHolder>(options) {
            @Override
            public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.menulayout, parent, false);

                return new MenuViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MenuViewHolder holder, int position, @NonNull MenuItems model) {
                int address = getResources().getIdentifier( "foodlist"+adapter.getRef(position).getKey(), "drawable", getPackageName());
                Picasso.get().load(address).fit().centerCrop().into(holder.imageView);
                holder.menuitemname.setText(model.getName());
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent=new Intent(DayMenu.this,ChangeDayMenu.class);
                        intent.putExtra("MenuItemsId",adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });
                progress.dismiss();

                navigationdark.setAlpha(0.7f);
                navigationimage.setImageResource(R.drawable.home);
            }
        };

        loadMenu();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter!=null)
            adapter.startListening();
    }

    private void loadMenu() {

        recyclerView.setAdapter(adapter);

        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
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
        if (Common.isConnectedToInternet(this)) {
            Intent intent = new Intent(this,Orders.class);
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
            Intent intent = new Intent(this,Orders.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }
        return true;
    }
}
