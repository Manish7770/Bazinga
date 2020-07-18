package com.manish.bazingaadmin;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.manish.bazingaadmin.Common.Common;
import com.manish.bazingaadmin.Model.Order;

import info.hoang8f.widget.FButton;

public class adminlogin extends AppCompatActivity {

    public TextView signinadmin;
    public EditText username,password;
    public FButton signinbutton;

    private FirebaseDatabase database;
    private DatabaseReference admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlogin);

        signinadmin=findViewById(R.id.signinadmin);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        signinbutton=findViewById(R.id.signinbutton);

        database=FirebaseDatabase.getInstance();
        admin=database.getReference("Admin");

        signinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isConnectedToInternet(adminlogin.this)) {
                    if((!(username.getText().toString().isEmpty())) && (!(username.getText().toString().equals("")))) {
                        if((!(password.getText().toString().isEmpty())) && (!(password.getText().toString().equals("")))) {
                            admin.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.child("username").getValue().toString().equals(username.getText().toString()))
                                    {
                                        if(dataSnapshot.child("password").getValue().toString().equals(password.getText().toString()))
                                        {
                                            Common.currentAdmin=username.getText().toString();
                                            Intent intent=new Intent(adminlogin.this, Orders.class);
                                            startActivity(intent);
                                        }
                                        else
                                        {
                                            Toast.makeText(adminlogin.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(adminlogin.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(adminlogin.this, "Enter your Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(adminlogin.this, "Enter your Username", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(adminlogin.this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(adminlogin.this,SignInActivity.class);
        startActivity(intent);
    }
}
