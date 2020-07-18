package com.manish.bazingalnmiit;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andremion.counterfab.CounterFab;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.manish.bazingalnmiit.Commondata.Commondata;
import com.manish.bazingalnmiit.Database.Database;
import com.manish.bazingalnmiit.Interface.ItemClickListener;
import com.manish.bazingalnmiit.Model.Banner;
import com.manish.bazingalnmiit.Model.Food;
import com.manish.bazingalnmiit.Model.MenuItems;
import com.manish.bazingalnmiit.Model.Token;
import com.manish.bazingalnmiit.ViewHolder.MenuViewHolder;
import com.manish.bazingalnmiit.ViewHolder.ViewPagerAdapter;
import com.squareup.picasso.Picasso;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class navigationhome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference menuitems,currentmenu,payment;
    FirebaseRecyclerAdapter adapter;

    TextView name,email;
    CounterFab fab;

    ProgressBar progress;
    int length;

    public Timer timer;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    ViewPagerCustomDuration viewPager;

    ImageView navigationdark,navigationimage,navigationimage2,tint;
    TextView closed;

    int previousitem=0;

    Dialog myoffer;

    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigationhome);
        Toolbar toolbar = findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout=findViewById(R.id.collapsetoolbar);
        collapsingToolbarLayout.setTitleEnabled(false);
        toolbar.setTitle("Bazinga");
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database = FirebaseDatabase.getInstance();
        menuitems = database.getReference("MenuItems");

        final DatabaseReference offer = database.getReference("Offers");

        payment=database.getReference("PaymentUrl");

        myoffer=new Dialog(this);
        myoffer.setContentView(R.layout.offerpopup);
        TextView txtclose =myoffer.findViewById(R.id.close);

        offer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Available").getValue().toString().equals("1"))
                {
                    TextView txtclose =myoffer.findViewById(R.id.close);
                    final TextView offertext=myoffer.findViewById(R.id.text1);
                    offertext.setText(dataSnapshot.child("Text").getValue().toString());

                    txtclose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myoffer.dismiss();
                        }
                    });

                    if(Commondata.popup==1) {
                        myoffer.show();
                        Commondata.popup=0;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        payment.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Commondata.url=dataSnapshot.child("url").getValue().toString();
                Commondata.mid=dataSnapshot.child("mid").getValue().toString();
                Commondata.channel_id=dataSnapshot.child("channel_id").getValue().toString();
                Commondata.industry_type_id=dataSnapshot.child("industry_type_id").getValue().toString();
                Commondata.website=dataSnapshot.child("website").getValue().toString();
                Commondata.callback_url=dataSnapshot.child("callback_url").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        navigationimage=findViewById(R.id.navigationimage);
        navigationdark=findViewById(R.id.navigationdark);
        navigationimage2=findViewById(R.id.navigationimage2);
        tint=findViewById(R.id.imageViewtint);
        closed=findViewById(R.id.closed);

        progress=findViewById(R.id.progressBar2);
        progress.setVisibility(View.VISIBLE);

        currentmenu=database.getReference("CurrentMenu");
        currentmenu.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String current=dataSnapshot.child("Checked").getValue().toString();
                if(current.equals("2"))
                {
                    navigationimage2.setVisibility(View.VISIBLE);
                    closed.setVisibility(View.VISIBLE);
                    tint.setVisibility(View.VISIBLE);
                    //closed.setTypeface(typeface);
                    progress.setVisibility(View.INVISIBLE);
                }
                else
                {
                    navigationimage2.setVisibility(View.INVISIBLE);
                    closed.setVisibility(View.INVISIBLE);
                    tint.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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



        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true);

        View header = navigationView.getHeaderView(0);

        name = (TextView) header.findViewById(R.id.nametextview);
        email = (TextView) header.findViewById(R.id.emailtext);
        name.setText(Commondata.currentUser.getName());
        email.setText(Commondata.currentUser.getEmail());



        fab = (CounterFab) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartintent = new Intent(navigationhome.this, Cart.class);
                Bundle bundle = new Bundle();
                bundle.putString("ClassNo", "1");
                cartintent.putExtras(bundle);
                startActivity(cartintent);
            }
        });

            Query query = FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child("MenuItems").orderByChild("Available").equalTo("1");

            FirebaseRecyclerOptions<MenuItems> options =
                    new FirebaseRecyclerOptions.Builder<MenuItems>()
                            .setQuery(query, MenuItems.class)
                            .build();

        if (Commondata.isConnectedToInternet(this)) {
            loadBanner();
        }
        else
        {
            progress.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }

            adapter = new FirebaseRecyclerAdapter<MenuItems, MenuViewHolder>(options) {
                @Override
                public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.menulayout, parent, false);

                    return new MenuViewHolder(view);
                }

                @Override
                protected void onBindViewHolder(@NonNull MenuViewHolder holder, int position, @NonNull MenuItems model) {
                    int address = getResources().getIdentifier("foodlist" + adapter.getRef(position).getKey(), "drawable", getPackageName());
                    if(address==0)
                        address = getResources().getIdentifier("blank", "drawable", getPackageName());
                    Picasso.get().load(address).fit().centerCrop().into(holder.imageView);
                    holder.menuitemname.setText(model.getName());
                    holder.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onClick(View view, int position, boolean isLongClick) {
                            if (Commondata.isConnectedToInternet(navigationhome.this)) {
                                Intent intent = new Intent(navigationhome.this, FoodList.class);
                                intent.putExtra("MenuItemsId", adapter.getRef(position).getKey());
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(navigationhome.this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    progress.setVisibility(View.INVISIBLE);

                    navigationdark.setAlpha(0.7f);
                    navigationimage.setImageResource(R.drawable.home);
                }
            };

        if (Commondata.isConnectedToInternet(this)) {
            loadMenu();
        }
        else
        {
            progress.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }

        if (Commondata.isConnectedToInternet(this)) {
            fab.setCount(new Database(this).getCountCart(Commondata.currentUID));
        }
        else
        {
            progress.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        {
            @Override
            public void onDrawerClosed(View drawerView) {
                if(id==R.id.nav_menu)
                {

                }
                else if(id==R.id.signout)
                {
                    if (Commondata.isConnectedToInternet(navigationhome.this)) {
                        MainActivity logout = new MainActivity();
                        logout.signOut();
                        Intent intent = new Intent(navigationhome.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(navigationhome.this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }
                else if(id==R.id.nav_orders)
                {
                    if (Commondata.isConnectedToInternet(navigationhome.this)) {
                        Intent intent = new Intent(navigationhome.this, OrdersList.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(navigationhome.this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }
                else if(id==R.id.nav_cart)
                {
                    Intent intent=new Intent(navigationhome.this,Cart.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("ClassNo","1");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else if (id==R.id.nav_contact_us)
                {
                    Intent intent=new Intent(navigationhome.this,contact_us.class);
                    startActivity(intent);
                }
                else if (id==R.id.offers)
                {
                    offer.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child("Available").getValue().toString().equals("1")) {
                                TextView txtclose =myoffer.findViewById(R.id.close);
                                final TextView offertext=myoffer.findViewById(R.id.text1);
                                offertext.setText(dataSnapshot.child("Text").getValue().toString());

                                txtclose.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        myoffer.dismiss();
                                    }
                                });
                                myoffer.show();
                            }
                            else
                            {
                                Toast.makeText(navigationhome.this, "NO OFFERS", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    navigationView.getMenu().getItem(0).setChecked(true);
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        updateToken(FirebaseInstanceId.getInstance().getToken());

    }

    private void updateToken(String token) {

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference tokens=database.getReference("Tokens");

        Token data=new Token(token,false);

        tokens.child(Commondata.currentUID).setValue(data);
    }

    private void loadBanner () {

        if (Commondata.isConnectedToInternet(this)) {

            DatabaseReference banners = database.getReference("Banner");


            banners.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    length = (int) dataSnapshot.getChildrenCount();
                    final Integer images[] = new Integer[length];
                    String bannername[] = new String[length];
                    String foodid[] = new String[length];

                    int count = 0;
                    for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

                        Banner banner = postSnapShot.getValue(Banner.class);
                        int address = getResources().getIdentifier("food" + banner.getFoodId(), "drawable", getPackageName());
                        if(address==0)
                            address = getResources().getIdentifier("blank", "drawable", getPackageName());
                        images[count] = address;
                        bannername[count] = banner.getName();
                        foodid[count] = banner.getFoodId();
                        count++;
                    }

                    viewPager =  findViewById(R.id.viewpager);
                    viewPager.setScrollDurationFactor(2);
                    ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(navigationhome.this, images, bannername, foodid);

                    viewPager.setAdapter(viewPagerAdapter);
                    viewPager.setCurrentItem(length*100);



                    timer = new Timer();
                    timer.scheduleAtFixedRate(new BannerTimer(), 2500, 2500);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else
        {
            progress.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    public void reScheduleTimer(int delay,int period) {
        timer = new Timer();
        timer.scheduleAtFixedRate(new BannerTimer(), delay, period);
    }

    public class BannerTimer extends TimerTask {

        @Override
        public void run() {
            navigationhome.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    for(int i=0;i<length*1000;i++)
                    {
                        if(viewPager.getCurrentItem()==i) {
                            viewPager.setCurrentItem( (i + 1));
                            break;
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Commondata.isConnectedToInternet(this)) {


            progress.setVisibility(View.VISIBLE);

            currentmenu.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String current=dataSnapshot.child("Checked").getValue().toString();
                    if(current.equals("2"))
                    {
                        navigationimage2.setVisibility(View.VISIBLE);
                        closed.setVisibility(View.VISIBLE);
                        tint.setVisibility(View.VISIBLE);
                        //closed.setTypeface(typeface);
                        progress.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        navigationimage2.setVisibility(View.INVISIBLE);
                        closed.setVisibility(View.INVISIBLE);
                        tint.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            fab.setCount(new Database(this).getCountCart(Commondata.currentUID));
            progress.setIndeterminateTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            if (adapter != null)
                adapter.startListening();
        }
        else
        {
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
            progress.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (Commondata.isConnectedToInternet(this)) {

            currentmenu.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String current=dataSnapshot.child("Checked").getValue().toString();
                    if(current.equals("2"))
                    {
                        navigationimage2.setVisibility(View.VISIBLE);
                        closed.setVisibility(View.VISIBLE);
                        tint.setVisibility(View.VISIBLE);
                        //closed.setTypeface(typeface);
                        progress.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        navigationimage2.setVisibility(View.INVISIBLE);
                        closed.setVisibility(View.INVISIBLE);
                        tint.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            fab.setCount(new Database(this).getCountCart(Commondata.currentUID));
            if (adapter != null)
                adapter.startListening();
        }
        else
        {
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
            progress.setVisibility(View.INVISIBLE);
        }

    }

    private void loadMenu() {

        if (Commondata.isConnectedToInternet(this)) {

            recyclerView.setAdapter(adapter);

            recyclerView.getAdapter().notifyDataSetChanged();
            recyclerView.scheduleLayoutAnimation();
        }
        else
        {
            progress.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        id = item.getItemId();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
