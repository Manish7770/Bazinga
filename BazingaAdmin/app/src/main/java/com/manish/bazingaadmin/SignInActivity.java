package com.manish.bazingaadmin;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.manish.bazingaadmin.Common.Common;
import com.manish.bazingaadmin.Model.Order;

import info.hoang8f.widget.FButton;

public class SignInActivity extends AppCompatActivity {

    public TextView slogan,home2;
    public FButton signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        home2=findViewById(R.id.home);
        slogan=findViewById(R.id.slogan);
        signin=findViewById(R.id.signIn);

        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/NABILA.TTF");
        home2.setTypeface(typeface);
        slogan.setTypeface(typeface);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isConnectedToInternet(SignInActivity.this)) {
                    Intent intent;
                    if (Common.currentAdmin==null) {
                        intent = new Intent(SignInActivity.this, adminlogin.class);
                    }
                    else
                    {
                        intent=new Intent(SignInActivity.this, Orders.class);
                    }
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(SignInActivity.this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);

    }
}
