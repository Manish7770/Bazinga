package com.manish.bazingaadmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.manish.bazingaadmin.Common.Common;

import info.hoang8f.widget.FButton;

public class SetMenu extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference food,daymenu,nightmenu,currentmenu,delivery,menuitem;
    CheckBox checkday,checknight,checkoff;

    FButton setdaymenu,setnightmenu,dayoff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setmenu);
        Toolbar toolbar = findViewById(R.id.SetMenuToolbar);
        toolbar.setTitle("Set Menu");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setdaymenu = (FButton) findViewById(R.id.setdaymenu);
        setnightmenu = (FButton) findViewById(R.id.setnightmenu);
        dayoff = (FButton) findViewById(R.id.dayoff);

        checkday=(CheckBox)findViewById(R.id.checkdaymenu);
        checknight=(CheckBox)findViewById(R.id.checknightmenu);
        checkoff=(CheckBox)findViewById(R.id.checkdayoff);

        database = FirebaseDatabase.getInstance();
        food = database.getReference("Food");
        delivery=database.getReference("DeliveryCharge");
        daymenu = database.getReference("Day's Menu");
        nightmenu=database.getReference("Night's Menu");
        currentmenu=database.getReference("CurrentMenu");
        menuitem=database.getReference("MenuItems");

        checkday.setChecked(false);
        checknight.setChecked(false);
        checkoff.setChecked(false);

        currentmenu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if((dataSnapshot.child("Checked").getValue().toString()).equals("0"))
                {
                    checkday.setChecked(true);
                    checknight.setChecked(false);
                    checkoff.setChecked(false);
                }
                else
                {
                    if((dataSnapshot.child("Checked").getValue().toString()).equals("1"))
                    {
                        checkday.setChecked(false);
                        checknight.setChecked(true);
                        checkoff.setChecked(false);
                    }
                    else
                    {
                        if((dataSnapshot.child("Checked").getValue().toString()).equals("2"))
                        {
                            checkday.setChecked(false);
                            checknight.setChecked(false);
                            checkoff.setChecked(true);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        /**requests.addValueEventListener(new ValueEventListener() {
        @Override public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {
        if ((postsnapshot.child("payment").exists()) && (!(postsnapshot.child("date").exists()))) {
        requests.child(postsnapshot.getKey()).child("date").setValue(postsnapshot.child("payment").getValue().toString() + " " + Common.getDate(Long.parseLong("9999999999999") - Long.parseLong(postsnapshot.getKey())));
        }
        }
        }
        };**/


        setdaymenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                delivery.setValue("0");

                daymenu.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postsnapshot2 : dataSnapshot.getChildren()) {
                            daymenu.child(postsnapshot2.getKey()).child("PackingCharge").setValue("0");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                currentmenu.child("Checked").setValue("0");

                checkday.setChecked(true);
                checknight.setChecked(false);
                checkoff.setChecked(false);

                final String arrstock[]=new String[1000];
                final String arrpacking[]=new String[1000];
                final int arrmenu[]=new int[100];
                for(int j=0;j<30;j++)
                    arrmenu[j]=0;

                daymenu.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot2) {
                        int i=0;
                        for (DataSnapshot postsnapshot2 : dataSnapshot2.getChildren()) {
                            arrstock[i]=postsnapshot2.child("Stock").getValue().toString();
                            arrpacking[i]=postsnapshot2.child("PackingCharge").getValue().toString();

                            if(arrstock[i].equals("1"))
                            {
                                    arrmenu[Integer.parseInt(postsnapshot2.child("MenuId").getValue().toString())]=1;
                            }

                            i++;
                        }

                        menuitem.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                int i=1;

                                for (final DataSnapshot postsnapshot : dataSnapshot.getChildren()) {

                                    if(arrmenu[i]==0)
                                    {
                                        menuitem.child(postsnapshot.getKey()).child("Available").setValue("0");
                                    }
                                    else
                                    {
                                        menuitem.child(postsnapshot.getKey()).child("Available").setValue("1");
                                    }

                                    i++;
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                food.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i=0;
                        for (final DataSnapshot postsnapshot : dataSnapshot.getChildren()) {
                            food.child(postsnapshot.getKey()).child("Stock").setValue(arrstock[i]);
                            food.child(postsnapshot.getKey()).child("PackingCharge").setValue(arrpacking[i]);

                            String avail=postsnapshot.child("MenuId").getValue().toString();
                            food.child(postsnapshot.getKey()).child("Available").setValue(avail+arrstock[i]);

                            i++;
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        setnightmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                delivery.setValue("10");

                currentmenu.child("Checked").setValue("1");

                nightmenu.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postsnapshot2 : dataSnapshot.getChildren()) {
                            nightmenu.child(postsnapshot2.getKey()).child("PackingCharge").setValue("4");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                checkday.setChecked(false);
                checknight.setChecked(true);
                checkoff.setChecked(false);

                final String arrstock[]=new String[1000];
                final String arrpacking[]=new String[1000];
                final int arrmenu[]=new int[100];
                for(int j=0;j<30;j++)
                    arrmenu[j]=0;

                nightmenu.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot2) {
                        int i=0;
                        for (DataSnapshot postsnapshot2 : dataSnapshot2.getChildren()) {
                            arrstock[i]=postsnapshot2.child("Stock").getValue().toString();
                            arrpacking[i]=postsnapshot2.child("PackingCharge").getValue().toString();

                            if(arrstock[i].equals("1"))
                            {
                                arrmenu[Integer.parseInt(postsnapshot2.child("MenuId").getValue().toString())]=1;
                            }

                            i++;
                        }

                        menuitem.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                int i=1;

                                for (final DataSnapshot postsnapshot : dataSnapshot.getChildren()) {

                                    if(arrmenu[i]==0)
                                    {
                                        menuitem.child(postsnapshot.getKey()).child("Available").setValue("0");
                                    }
                                    else
                                    {
                                        menuitem.child(postsnapshot.getKey()).child("Available").setValue("1");
                                    }

                                    i++;
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                food.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i=0;
                        for (final DataSnapshot postsnapshot : dataSnapshot.getChildren()) {
                            food.child(postsnapshot.getKey()).child("Stock").setValue(arrstock[i]);
                            food.child(postsnapshot.getKey()).child("PackingCharge").setValue(arrpacking[i]);

                            String avail=postsnapshot.child("MenuId").getValue().toString();
                            food.child(postsnapshot.getKey()).child("Available").setValue(avail+arrstock[i]);

                            i++;
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        dayoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentmenu.child("Checked").setValue("2");

                checkday.setChecked(false);
                checknight.setChecked(false);
                checkoff.setChecked(true);

                menuitem.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (final DataSnapshot postsnapshot : dataSnapshot.getChildren()) {

                            menuitem.child(postsnapshot.getKey()).child("Available").setValue("0");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                food.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (final DataSnapshot postsnapshot : dataSnapshot.getChildren()) {
                            food.child(postsnapshot.getKey()).child("Stock").setValue("0");

                            String avail=postsnapshot.child("MenuId").getValue().toString();
                            food.child(postsnapshot.getKey()).child("Available").setValue(avail+"0");

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

        });

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