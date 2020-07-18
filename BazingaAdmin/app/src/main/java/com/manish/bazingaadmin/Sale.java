package com.manish.bazingaadmin;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.manish.bazingaadmin.Common.Common;
import com.manish.bazingaadmin.ViewHolder.SaleAdapter;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import info.hoang8f.widget.FButton;

public class Sale extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    FButton dateChange,totalsalebutton;
    FirebaseDatabase database;
    DatabaseReference requests,menuitems,food;
    TextView saledatetext;

    private List<String> menulist;
    private HashMap<String,List<String>> subfoodlist;
    private long totalsale=0,deliverytot=0;

    int quantity[]=new int[10000];
    int foodmenuId[]=new int[10000];
    String foodname[]=new String[10000];
    long foodtotal[]=new long[10000];

    int i,j;
    int maxfood2;

    private ExpandableListView salelist;
    private ExpandableListAdapter saleadapt;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);

        Toolbar toolbar = (Toolbar) findViewById(R.id.saleToolbar);
        toolbar.setTitle("Sale");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database=FirebaseDatabase.getInstance();
        menuitems=database.getReference("MenuItems");
        requests=database.getReference("Requests");
        food=database.getReference("Food");
        dateChange=(FButton)findViewById(R.id.salechangeDate);
        saledatetext=findViewById(R.id.saledatetext);
        totalsalebutton=findViewById(R.id.totalsale);

        progressBar=(ProgressBar)findViewById(R.id.progressale);
        progressBar.setVisibility(View.VISIBLE);

        menulist=new ArrayList<>();
        subfoodlist=new HashMap<>();

        for(i=0;i<10000;i++)
        {
            quantity[i]=0;
            foodtotal[i]=0;
        }

        final Calendar now=Calendar.getInstance();
        long time=now.getTimeInMillis();
        String datedisplay=Common.getDate(time);
        final int todayday=now.get(Calendar.DAY_OF_MONTH);
        final int todaymonth=now.get(Calendar.MONTH);
        final int todayyear=now.get(Calendar.YEAR);
        saledatetext.setText(datedisplay);

        dateChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog=DatePickerDialog.newInstance(Sale.this
                        ,todayyear
                        ,todaymonth
                        ,todayday);
                datePickerDialog.setAccentColor(getResources().getColor(R.color.toolbar_end));
                datePickerDialog.show(getFragmentManager(),"DatePicker");
            }
        });

        initmenuitems(datedisplay);

        //Log.i("POSITMIN",maxfood2+"");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date=DATE(year,(monthOfYear+1),dayOfMonth);
        saledatetext.setText(date);
        totalsale=0;
        deliverytot=0;

        progressBar.setVisibility(View.VISIBLE);

        menulist=new ArrayList<>();
        subfoodlist=new HashMap<>();

        for(i=0;i<10000;i++)
        {
            quantity[i]=0;
            foodtotal[i]=0;
        }
        if(i==10000)
            initmenuitems(date);
    }

    private void initmenuitems(final String date)
    {
        menuitems.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postsnapshot2 : dataSnapshot.getChildren()) {
                    menulist.add(postsnapshot2.child("Name").getValue().toString());
                }
                initfooditems(date);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initfooditems(final String date)
    {
        food.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int maxfood=1;
                for (DataSnapshot postsnapshot2 : dataSnapshot.getChildren()) {
                    foodname[maxfood]=postsnapshot2.child("Name").getValue().toString();
                    foodmenuId[maxfood]=Integer.parseInt(postsnapshot2.child("MenuId").getValue().toString());
                    maxfood +=1;
                }
                maxfood2=maxfood;

                initrequest(date);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private  void initrequest(final String date2)
    {
        requests.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                deliverytot=0;
                for (DataSnapshot postsnapshot2 : dataSnapshot.getChildren()) {
                    String date="true "+date2;
                    String truepayment="";
                    //&&(postsnapshot2.child("payment").getValue().equals(true))
                    if((postsnapshot2.child("date").getValue().toString().equals(date)))
                    {
                        deliverytot+=10;
                        for (DataSnapshot foodsnapshot : postsnapshot2.child("foods").getChildren())
                        {
                            int productid=Integer.parseInt(foodsnapshot.child("productId").getValue().toString());
                            int quanti=Integer.parseInt(foodsnapshot.child("quantity").getValue().toString());
                            quantity[productid]+=quanti;
                            long price =Long.parseLong(foodsnapshot.child("price").getValue().toString());
                            long packing=Long.parseLong(foodsnapshot.child("packingCharge").getValue().toString());
                            foodtotal[productid]+=(price*quanti)+packing;
                            Log.i("POSITTOT",quantity[productid]+"");
                        }

                    }
                }

                inti();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void inti()
    {
        List<String> fooddetails=new ArrayList<>();
        int tempid=0;
        int pos=0;
        String food3;

        totalsale=0;

        for(i=1;i<maxfood2;i++)
        {
            if(i==1)
            {
                fooddetails=new ArrayList<>();
                tempid=foodmenuId[i];
                food3=quantity[i]+"#"+foodtotal[i]+"#"+foodname[i];
                fooddetails.add(food3);
            }
            else
            {
                if(foodmenuId[i]==tempid)
                {
                    food3=quantity[i]+"#"+foodtotal[i]+"#"+foodname[i];
                    fooddetails.add(food3);
                }
                else
                {
                    subfoodlist.put(menulist.get(pos),fooddetails);
                    pos+=1;
                    tempid=foodmenuId[i];
                    fooddetails=new ArrayList<>();
                    food3=quantity[i]+"#"+foodtotal[i]+"#"+foodname[i];
                    fooddetails.add(food3);
                }
            }
            totalsale+=foodtotal[i];
            //Log.i("POSIT",food3+"");
        }

        totalsale+=deliverytot;

        totalsalebutton.setText("TOTAL SALE OF RS. "+totalsale+".00");
        subfoodlist.put(menulist.get(pos),fooddetails);

        salelist=(ExpandableListView)findViewById(R.id.saleList);
        saleadapt=new SaleAdapter(this,menulist,subfoodlist);
        salelist.setAdapter(saleadapt);

        progressBar.setVisibility(View.INVISIBLE);


    }

    @Override
    public void onBackPressed() {
        if (Common.isConnectedToInternet(Sale.this)) {
            Intent intent = new Intent(Sale.this, Orders.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(Sale.this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (Common.isConnectedToInternet(Sale.this)) {
            Intent intent = new Intent(Sale.this, Orders.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(Sale.this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }
        return true;
    }

    public String capsName(String Name)
    {
        Name=Name.trim();
        String Caps="";
        char first=Character.toUpperCase(Name.charAt(0));
        Caps+=first;
        Caps+=Name.substring(1,Name.length());
        return Caps;
    }

    public String DATE(int year,int month,int day)
    {
        String dateformat="";
        if(day<=9)
        {
            dateformat+="0";
            dateformat+=day;
        }
        else
        {
            dateformat+=day;
        }
        dateformat+="-";
        if (month<=9)
        {
            dateformat+="0";
            dateformat+=month;
        }
        else
        {
            dateformat+=month;
        }
        dateformat+="-";
        dateformat+=year;
        return  dateformat;
    }


}
