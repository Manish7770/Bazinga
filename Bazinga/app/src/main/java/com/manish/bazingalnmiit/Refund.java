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

public class Refund extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refund_policy);

        Toolbar toolbar = (Toolbar) findViewById(R.id.abouttoolbar);
        toolbar.setTitle("Refund Policy");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textView=(TextView)findViewById(R.id.contacttext);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml("<h2><b>Refund and Cancellation Policy</h2></b><br><br><p>Our focus is complete customer satisfaction. In the event, if you are displeased with the services\n" +
                    "provided, we will refund back the money, provided the reasons are genuine and proved after\n" +
                    "investigation. Please read the fine prints of each deal before buying it, it provides all the details about the\n" +
                    "services or the product you purchase.<br><br>\n" +
                    "In case of dissatisfaction from our services, clients have the liberty to cancel their projects and request a\n" +
                    "refund from us. Our Policy for the cancellation and refund will be as follows:<br><br>\n" +
                    "<b>Cancellation Policy</b><br><br>\n" +
                    "<b>No</b> cancelalations will be entertained.<br><br> \n" +
                    "Requests received later than 1 business days prior to the end of the current service period will be treated\n" +
                    "as cancellation of services for the next service period.<br><br>\n" +
                    "<b>Refund Policy</b><br><br>\n" +
                    "We will try our best to create the suitable design concepts for our clients.<br><br>\n" +
                    "In case any client is not completely satisfied with our products we can provide a refund.<br><br> \n" +
                    "If paid by credit card, refunds will be issued to the original credit card provided at the time of purchase\n" +
                    "and in case of payment gateway name payments refund will be made to the same account.<br><br></p>", Html.FROM_HTML_MODE_COMPACT));
        } else {
            textView.setText(Html.fromHtml("<h2><b>Refund and Cancellation Policy</h2></b><br><br><p>Our focus is complete customer satisfaction. In the event, if you are displeased with the services\\n\" +\n" +
                    "                    \"provided, we will refund back the money, provided the reasons are genuine and proved after\\n\" +\n" +
                    "                    \"investigation. Please read the fine prints of each deal before buying it, it provides all the details about the\\n\" +\n" +
                    "                    \"services or the product you purchase.<br><br>\\n\" +\n" +
                    "                    \"In case of dissatisfaction from our services, clients have the liberty to cancel their projects and request a\\n\" +\n" +
                    "                    \"refund from us. Our Policy for the cancellation and refund will be as follows:<br><br>\\n\" +\n" +
                    "                    \"<b>Cancellation Policy</b><br><br>\\n\" +\n" +
                    "                    \"<b>No</b> cancelalations will be entertained.<br><br> \\n\" +\n" +
                    "                    \"Requests received later than 1 business days prior to the end of the current service period will be treated\\n\" +\n" +
                    "                    \"as cancellation of services for the next service period.<br><br>\\n\" +\n" +
                    "                    \"<b>Refund Policy</b><br><br>\\n\" +\n" +
                    "                    \"We will try our best to create the suitable design concepts for our clients.<br><br>\\n\" +\n" +
                    "                    \"In case any client is not completely satisfied with our products we can provide a refund.<br><br> \\n\" +\n" +
                    "                    \"If paid by credit card, refunds will be issued to the original credit card provided at the time of purchase\\n\" +\n" +
                    "                    \"and in case of payment gateway name payments refund will be made to the same account.<br><br></p>"));
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
            Intent intent = new Intent(Refund.this, contact_us.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }
        return true;
    }
}
