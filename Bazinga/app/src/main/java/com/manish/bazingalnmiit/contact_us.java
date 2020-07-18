package com.manish.bazingalnmiit;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.manish.bazingalnmiit.Commondata.Commondata;

public class contact_us extends AppCompatActivity {

    Button contactBazinga;
    FirebaseDatabase database;
    DatabaseReference reference;
    ImageView facemanish,githubmanish,faceart,githubart;
    TextView ppolicy,rpolicy,tandc,abtpolicy;
    String bazinga;

    public static String FACEBOOK_URL_MANISH = "https://www.facebook.com/manish7770";
    public static String FACEBOOK_PAGE_ID_MANISH = "manish7770";
    public static String FACEBOOK_URL_ARTHAK = "https://www.facebook.com/arthak.kumar.9";
    public static String FACEBOOK_PAGE_ID_ARTHAK = "arthak.kumar.9";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        Toolbar toolbar=(Toolbar)findViewById(R.id.contacttoolbar);
        toolbar.setTitle("Contact Us");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        contactBazinga=findViewById(R.id.callbazinga);
        facemanish=findViewById(R.id.facemanish);
        faceart=findViewById(R.id.facearthak);
        ppolicy=findViewById(R.id.ppolicy);
        rpolicy=findViewById(R.id.rpolicy);
        tandc=findViewById(R.id.tandc);
        abtpolicy=findViewById(R.id.abtus);
        githubart=findViewById(R.id.githubarthak);
        githubmanish=findViewById(R.id.githubmanish);

        database=FirebaseDatabase.getInstance();
        reference=database.getReference("Contact");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bazinga=dataSnapshot.child("Bazinga").getValue().toString();
                contactBazinga.setText("BAZINGA : "+bazinga);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        contactBazinga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+bazinga));
                startActivity(intent);
            }
        });

        facemanish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                String facebookUrl = getFacebookPageURLmanish(contact_us.this);
                facebookIntent.setData(Uri.parse(facebookUrl));
                startActivity(facebookIntent);

            }
        });

        faceart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                String facebookUrl = getFacebookPageURLarthak(contact_us.this);
                facebookIntent.setData(Uri.parse(facebookUrl));
                startActivity(facebookIntent);
            }
        });

        githubmanish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent githubIntent = new Intent(Intent.ACTION_VIEW);
                String gitUrl = "https://github.com/Manish7707";
                githubIntent.setData(Uri.parse(gitUrl));
                startActivity(githubIntent);
            }
        });

        githubart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent githubIntent = new Intent(Intent.ACTION_VIEW);
                String gitUrl = "https://github.com/art-hack";
                githubIntent.setData(Uri.parse(gitUrl));
                startActivity(githubIntent);
            }
        });

        abtpolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent abt = new Intent(contact_us.this,About_us.class);
                startActivity(abt);
            }
        });

        ppolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent abt = new Intent(contact_us.this,Privacy.class);
                startActivity(abt);
            }
        });

        rpolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent abt = new Intent(contact_us.this,Refund.class);
                startActivity(abt);
            }
        });

        tandc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent abt = new Intent(contact_us.this,TandC.class);
                startActivity(abt);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        if (Commondata.isConnectedToInternet(this)) {
            Intent intent=new Intent(contact_us.this,navigationhome.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (Commondata.isConnectedToInternet(this)) {
            Intent intent=new Intent(contact_us.this,navigationhome.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    public String getFacebookPageURLmanish(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) {
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL_MANISH;
            } else {
                return "fb://page/" + FACEBOOK_PAGE_ID_MANISH;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL_MANISH;
        }
    }

    public String getFacebookPageURLarthak(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) {
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL_ARTHAK;
            } else {
                return "fb://page/" + FACEBOOK_PAGE_ID_ARTHAK;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL_ARTHAK;
        }
    }
}
