package com.manish.bazingalnmiit;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.manish.bazingalnmiit.Commondata.Commondata;

public class About_us extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);

        Toolbar toolbar = (Toolbar) findViewById(R.id.abouttoolbar);
        toolbar.setTitle("About Us");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textView=(TextView)findViewById(R.id.contacttext);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml("<h2><b>BAZINGA</h2></b><br><p>Bazinga specializes in various forms of cuisines ranging from quick bites to savoring dishes. Situated in The LNM Institute of Information Technology, Jaipur, Bazinga is a part of the food outlet known as Dabbu G's Kitchen in Raja Park, Jaipur. With a wide assortment of Veg as well as Non-Veg dishes, Bazinga values the taste and satisfaction of taste buds as it's utmost priority.</p>", Html.FROM_HTML_MODE_COMPACT));
        } else {
            textView.setText(Html.fromHtml("<h2><b>BAZINGA</h2></b><br><p>Bazinga specializes in various forms of cuisines ranging from quick bites to savoring dishes. Situated in The LNM Institute of Information Technology, Jaipur, Bazinga is a part of the food outlet known as Dabbu G's Kitchen in Raja Park, Jaipur. With a wide assortment of Veg as well as Non-Veg dishes, Bazinga values the taste and satisfaction of taste buds as it's utmost priority.</p>"));
        }


    }

    @Override
    public void onBackPressed() {
        if (Commondata.isConnectedToInternet(this)) {
            Intent intent = new Intent(this, contact_us.class);
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
            Intent intent = new Intent(About_us.this, contact_us.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }
        return true;
    }
}
