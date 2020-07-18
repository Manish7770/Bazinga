package com.manish.bazingalnmiit;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.manish.bazingalnmiit.Commondata.Commondata;
import com.manish.bazingalnmiit.Database.Database;
import com.manish.bazingalnmiit.Model.DataMessage;
import com.manish.bazingalnmiit.Model.MyResponse;
import com.manish.bazingalnmiit.Model.Request2;
import com.manish.bazingalnmiit.Model.Requests;
import com.manish.bazingalnmiit.Model.Token;
import com.manish.bazingalnmiit.Remote.APIService;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import info.hoang8f.widget.FButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Address extends AppCompatActivity {

    public String url = Commondata.url,mid=Commondata.mid,channel_id=Commondata.channel_id,industry_type_id=Commondata.industry_type_id,website=Commondata.website,callback_url=Commondata.callback_url, param = "", orderId = "", custId = Commondata.currentUID, billAmount = "", CHECKSUMHASH = "";
    PaytmPGService Service = null;

    public EditText hostel,room,phonenumber;
    public TextView fullname;
    public FButton proceedpay;

    FirebaseDatabase database;
    DatabaseReference request,users,payment;

    APIService mService;
    ProgressDialog mdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        mService=Commondata.getFCMService();

        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            billAmount = bundle.getString("BillAmt");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.addressToolbar);
        toolbar.setTitle("Address");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database = FirebaseDatabase.getInstance();
        request = database.getReference("Requests");
        users=database.getReference("UserApp");
        payment=database.getReference("PaymentUrl");

        hostel=findViewById(R.id.hostel);
        room=findViewById(R.id.roomnumber);
        phonenumber=findViewById(R.id.contactnumber);
        proceedpay=findViewById(R.id.addproccedtopay);
        fullname=findViewById(R.id.fullname);

        fullname.setText(Commondata.currentUser.getName());

        if(!(Commondata.currentUser.getAddress().isEmpty()))
        {
            String address=Commondata.currentUser.getAddress();
            if (!(Commondata.currentUser.getAddress().equals(("")))) {
                hostel.setText(address.substring(0, address.indexOf(',')));
                room.setText(address.substring(address.indexOf(',') + 2, address.indexOf(", LNMIIT")));
            }
        }

        if(!(Commondata.currentUser.getPhoneNumber().isEmpty()))
        {
            if (!(Commondata.currentUser.getPhoneNumber().equals(""))) {
                phonenumber.setText(Commondata.currentUser.getPhoneNumber());
            }
        }

        mdialog = new ProgressDialog(Address.this);
        mdialog.setMessage("Please Wait...");
        proceedpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdialog.show();
                mdialog.setCanceledOnTouchOutside(false);
                if ((!(hostel.getText().toString().isEmpty())) && (!(hostel.getText().toString().equals("")))) {

                    if ((!(room.getText().toString().isEmpty())) && (!(room.getText().toString().equals("")))) {

                        if ((!(phonenumber.getText().toString().isEmpty())) && (!(phonenumber.getText().toString().equals("")))) {
                            {
                                if (phonenumber.getText().toString().length()==10)
                                {


                                    users.child(Commondata.currentUID).child("address").setValue(hostel.getText().toString()+", "+room.getText().toString()+", LNMIIT, Jaipur");
                                    users.child(Commondata.currentUID).child("phoneNumber").setValue(phonenumber.getText().toString());
                                    Commondata.currentUser.setAddress(hostel.getText().toString()+", "+room.getText().toString()+", LNMIIT, Jaipur");
                                    Commondata.currentUser.setPhoneNumber(phonenumber.getText().toString());
                                    long maxtime=Long.parseLong("9999999999999");
                                    long currentTime=System.currentTimeMillis();
                                    orderId=String.valueOf(maxtime-currentTime);
                                    new Task().execute();

                                }
                                else
                                {
                                    Toast.makeText(Address.this, "Enter 10 digit Phone Number", Toast.LENGTH_SHORT).show();
                                    mdialog.dismiss();
                                }
                            }
                        }
                        else
                        {
                            Toast.makeText(Address.this, "Enter your Phone Number", Toast.LENGTH_SHORT).show();
                            mdialog.dismiss();

                        }
                    }
                    else
                    {
                        Toast.makeText(Address.this, "Enter your Room Number", Toast.LENGTH_SHORT).show();
                        mdialog.dismiss();

                    }
                }
                else
                {
                    Toast.makeText(Address.this, "Enter your Hostel Name", Toast.LENGTH_SHORT).show();
                    mdialog.dismiss();

                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class Task extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            //url = "https://bazingacafe.000webhostapp.com/Paytm/generateChecksum.php";
            JSONParser jsonParser = new JSONParser(Address.this);

                param = "ORDER_ID=" + orderId +
                    "&MID="+mid +
                    "&CUST_ID=" + custId +
                    "&CHANNEL_ID="+channel_id+"&INDUSTRY_TYPE_ID="+industry_type_id+"&WEBSITE="+website+"&TXN_AMOUNT=" + billAmount + "&CALLBACK_URL="+(callback_url+orderId);

            JSONObject jsonObject = jsonParser.makeHttpRequest(url, "POST", param);
            if (jsonObject != null) {
                try {

                    CHECKSUMHASH = jsonObject.has("CHECKSUMHASH") ? jsonObject.getString("CHECKSUMHASH") : "";

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Service = PaytmPGService.getProductionService();
            HashMap<String, String> paramMap = new HashMap<String, String>();

            paramMap.put("ORDER_ID", orderId);
            paramMap.put("MID", mid);
            paramMap.put("CUST_ID", custId);
            paramMap.put("CHANNEL_ID", channel_id);
            paramMap.put("INDUSTRY_TYPE_ID", industry_type_id);
            paramMap.put("WEBSITE", website);
            paramMap.put("TXN_AMOUNT", billAmount);
            paramMap.put("CALLBACK_URL", callback_url+orderId);
            paramMap.put("CHECKSUMHASH", CHECKSUMHASH);
            PaytmOrder Order = new PaytmOrder(paramMap);


            Service.initialize(Order, null);
            Service.startPaymentTransaction(Address.this, true, true, new PaytmPaymentTransactionCallback() {
                @Override
                public void someUIErrorOccurred(String inErrorMessage) {
                    mdialog.dismiss();
                }

                @Override
                public void onTransactionResponse(Bundle inResponse) {
                    String response = inResponse.getString("STATUS");
                    if (response.equals("TXN_SUCCESS")) {
                        Requests requests=new Requests(
                                CHECKSUMHASH,
                                hostel.getText().toString()+", "+room.getText().toString()+", LNMIIT, Jaipur",
                                0,
                                Commondata.cartforpayment,
                                true,
                                phonenumber.getText().toString(),
                                Long.parseLong(billAmount),
                                Commondata.currentUID
                        );
                        request.child(orderId)
                                .setValue(requests);
                        new Database(getBaseContext()).cleanCart(Commondata.currentUID);

                        sendNotificationOrder(orderId);
                        Toast.makeText(getApplicationContext(), "Payment Successfull", Toast.LENGTH_SHORT).show();

                        finish();
                    } else if (response.equals("PENDING")){

                        Request2 requests=new Request2(
                                CHECKSUMHASH,
                                hostel.getText().toString()+", "+room.getText().toString()+", LNMIIT, Jaipur",
                                Commondata.cartforpayment,
                                phonenumber.getText().toString(),
                                Long.parseLong(billAmount),
                                Commondata.currentUID
                        );
                        request.child(orderId)
                                .setValue(requests);
                        new Database(getBaseContext()).cleanCart(Commondata.currentUID);

                        Toast.makeText(getApplicationContext(), "Your payment is pending , it will be confirmed within few minutes", Toast.LENGTH_LONG).show();

                        finish();
                    }
                    else  if (response.equals("TXN_FAILURE")){
                        Toast.makeText(getApplicationContext(), "Payment Failed", Toast.LENGTH_LONG).show();
                    }
                }


                @Override
                public void networkNotAvailable() {
                    mdialog.dismiss();
                    Toast.makeText(Address.this, "Network Not Available", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void clientAuthenticationFailed(String inErrorMessage) {

                    Toast.makeText(Address.this, "Client Authentication Failed", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onErrorLoadingWebPage(int iniErrorCode,
                                                  String inErrorMessage, String inFailingUrl) {

                    Toast.makeText(Address.this, "Error Loading WebPage... TryAgain!!!", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onBackPressedCancelTransaction() {
                    mdialog.dismiss();
                    Toast.makeText(Address.this, "Transaction Canceled", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                    mdialog.dismiss();
                    Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void sendNotificationOrder(final String orderId) {
        DatabaseReference tokens=FirebaseDatabase.getInstance().getReference("Tokens");
        final Query data=tokens.orderByChild("serverToken").equalTo(true);

        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapShot: dataSnapshot.getChildren())
                {
                    Token serverToken=postSnapShot.getValue(Token.class);

                    Map<String,String> dataSend=new HashMap<>();
                    dataSend.put("title","Bazinga");
                    dataSend.put("message","You have a new Order");
                    dataSend.put("orderId",orderId);

                    DataMessage message=new DataMessage(serverToken.getToken(),dataSend);

                    mService.sendNotification( message)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {

                                    if (response.code() == 200) {
                                        if (response.body().success == 1) {
                                            Toast.makeText(getApplicationContext(), "Order is Placed", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                    Toast.makeText(Address.this, "", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
