package com.manish.bazingaadmin;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.manish.bazingaadmin.Common.Common;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import info.hoang8f.widget.FButton;

public class salexcel extends AppCompatActivity {

    //TextView tv;
    FButton fromdatebut, todatebut, getsale;
    TextView tvfromdate, tvtodate;

    FirebaseDatabase database;
    DatabaseReference requests, menuitems, food;

    int quantity[] = new int[10000];
    int foodmenuId[] = new int[10000];
    String foodname[] = new String[10000];
    long foodtotal[] = new long[10000];

    int i, j;
    int maxfood2;

    String from,to;
    static final int DATE_DIALOG_ID = 1;
    static final int DATE_DIALOG_ID2 = 2;
    int cur = 0;

    private int year;
    private int month;
    private int day;

    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.salexcel);

        Toolbar toolbar = (Toolbar) findViewById(R.id.salexlToolbar);
        toolbar.setTitle("SaleExcel ");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fromdatebut = (FButton) findViewById(R.id.fromDatebutt);
        todatebut = (FButton) findViewById(R.id.toDatebutt);
        tvfromdate = (TextView) findViewById(R.id.tvfromdate);
        tvtodate = (TextView) findViewById(R.id.tvtodate);
        database = FirebaseDatabase.getInstance();
        menuitems = database.getReference("MenuItems");
        requests = database.getReference("Requests");
        food = database.getReference("Food");
        getsale=(FButton)findViewById(R.id.salegetxl);

        progressBar=findViewById(R.id.progressxl);
        progressBar.setVisibility(View.INVISIBLE);

        for (i = 0; i < 10000; i++) {
            quantity[i] = 0;
            foodtotal[i] = 0;
        }

        final Calendar now = Calendar.getInstance();
        long time = now.getTimeInMillis();
        String datedisplay = Common.getDate(time);
        final int todayday = now.get(Calendar.DAY_OF_MONTH);
        final int todaymonth = now.get(Calendar.MONTH);
        final int todayyear = now.get(Calendar.YEAR);
        tvtodate.setText(datedisplay);
        tvfromdate.setText(datedisplay);

        year=todayyear;
        month=todaymonth;
        day=todayday;


        fromdatebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        todatebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID2);
            }
        });

        getsale.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                from=tvfromdate.getText().toString();
                to=tvtodate.getText().toString();
                for (i = 0; i < 10000; i++) {
                    quantity[i] = 0;
                    foodtotal[i] = 0;
                }
                if(i==10000)
                    initfooditems();
            }
        });

        checkExternalMedia();





    }

    private void initfooditems()
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

                initrequest();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private  void initrequest() {
        requests.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postsnapshot2 : dataSnapshot.getChildren()) {
                    String date = postsnapshot2.child("date").getValue().toString();
                    int space = date.indexOf(' ');
                    String truedate = date.substring(space + 1);

                    if (checkdate(truedate)) {
                        for (DataSnapshot foodsnapshot : postsnapshot2.child("foods").getChildren()) {
                            int productid = Integer.parseInt(foodsnapshot.child("productId").getValue().toString());
                            int quanti = Integer.parseInt(foodsnapshot.child("quantity").getValue().toString());
                            quantity[productid] += quanti;
                            long price = Long.parseLong(foodsnapshot.child("price").getValue().toString());
                            long packing = Long.parseLong(foodsnapshot.child("packingCharge").getValue().toString());
                            foodtotal[productid] += (price * quanti) + packing;
                        }

                    }
                }

                if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
                    Log.e("Storagexxx", "Storage not available or read only");
                }

                boolean success = false;

                Workbook workbook = new HSSFWorkbook();
                Sheet firstSheet = workbook.createSheet("Total Sale");

                firstSheet.setColumnWidth(0,(15 * 100));
                firstSheet.setColumnWidth(1,(15 * 500));
                firstSheet.setColumnWidth(2,(15 * 500));
                firstSheet.setColumnWidth(3,(15 * 500));

                Row row2 = firstSheet.createRow(0);

                Cell serialno2 = row2.createCell(0);
                serialno2.setCellValue(new HSSFRichTextString("Serial No."));

                Cell food2 = row2.createCell(1);
                food2.setCellValue(new HSSFRichTextString("Food Name"));

                Cell quantit2 = row2.createCell(2);
                quantit2.setCellValue(new HSSFRichTextString("Quantity"));

                Cell totalvalue2 = row2.createCell(3);
                totalvalue2.setCellValue(new HSSFRichTextString("Food Total"));

                for(i=1;i<maxfood2;i++)
                {
                    Row row = firstSheet.createRow(i);

                    Cell serialno = row.createCell(0);
                    serialno.setCellValue(new HSSFRichTextString(i+""));

                    Cell food = row.createCell(1);
                    food.setCellValue(new HSSFRichTextString(foodname[i]));

                    Cell quantit = row.createCell(2);
                    quantit.setCellValue(new HSSFRichTextString(quantity[i]+""));

                    Cell totalvalue = row.createCell(3);
                    totalvalue.setCellValue(new HSSFRichTextString(foodtotal[i]+""));
                }

                File file = new File(salexcel.this.getExternalFilesDir(null), "BazingaSale.xls");
                FileOutputStream os = null;

                //writeToSDFile(workbook);

                /**for(i=1;i<maxfood2;i++)
                {
                    Log.i("POSITTOT", foodname[i]+"   "+quantity[i] + "   "+foodtotal[i]);
                }**/

                try {
                    os = new FileOutputStream(file);
                    workbook.write(os);
                    Log.w("FileUtils", "Writing file" + file);
                    success = true;
                } catch (IOException e) {
                    Log.w("FileUtils", "Error writing " + file, e);
                } catch (Exception e) {
                    Log.w("FileUtils", "Failed to save file", e);
                } finally {
                    try {
                        if (null != os)
                            os.close();
                    } catch (Exception ex) {
                    }
                }
                Log.w("Successxx", "Writing file Success" + success);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private boolean checkdate(String date)
    {
        int firstdash=date.indexOf('-');
        int seconddash=date.lastIndexOf('-');
        int day=Integer.parseInt(date.substring(0,firstdash));
        int month=Integer.parseInt(date.substring(firstdash+1,seconddash));
        int year=Integer.parseInt(date.substring(seconddash+1));

        firstdash=from.indexOf('-');
        seconddash=from.lastIndexOf('-');
        int fromday=Integer.parseInt(from.substring(0,firstdash));
        int frommonth=Integer.parseInt(from.substring(firstdash+1,seconddash));
        int fromyear=Integer.parseInt(from.substring(seconddash+1));

        firstdash=to.indexOf('-');
        seconddash=to.lastIndexOf('-');
        int today=Integer.parseInt(to.substring(0,firstdash));
        int tomonth=Integer.parseInt(to.substring(firstdash+1,seconddash));
        int toyear=Integer.parseInt(to.substring(seconddash+1));

        if((year<toyear)&&(year>fromyear))
        {
            return true;
        }
        else
        {
            if((year==toyear)&&(year!=fromyear))
            {
                if(month<tomonth)
                {
                    return true;
                }
                else if(month==tomonth)
                {
                    if(day<=today)
                    {
                        return true;
                    }
                }
            }
            else if((year==fromyear)&&(year!=toyear))
            {
                if(month>frommonth)
                {
                    return true;
                }
                else if(month==frommonth)
                {
                    if(day>=fromday) {
                        return true;
                    }
                }
            }
            else if((year==fromyear)&&(year==toyear))
            {
                if((month<tomonth)&&(month>frommonth))
                {
                    return true;
                }
                else if((month==tomonth)&&(month!=frommonth))
                {
                    if(day<=today)
                        return true;
                }
                else if((month!=tomonth)&&(month==frommonth))
                {
                    if(day>=fromday)
                        return true;
                }
                else if((month==tomonth)&&(month==frommonth))
                {
                    if((day<=today)&&(day>=fromday))
                        return true;
                }
            }
        }
        return false;
    }
        @Override
        protected Dialog onCreateDialog(int id) {
        switch (id) {

            case DATE_DIALOG_ID:

                return new DatePickerDialog(this, from_dateListener, year, month,
                        day);

            case DATE_DIALOG_ID2:

                return new DatePickerDialog(this, to_dateListener, year, month,
                        day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener from_dateListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will bealled.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            String date=DATE(year,(selectedMonth+1),selectedDay);


            tvfromdate.setText(date);


        }
    };

    private DatePickerDialog.OnDateSetListener to_dateListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            String date=DATE(year,(selectedMonth+1),selectedDay);


            tvtodate.setText(date);


        }
    };

    private void checkExternalMedia() {
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // Can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // Can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Can't read or write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
        //tv.append("\n\nExternal Media: readable="
        //      +mExternalStorageAvailable+" writable="+mExternalStorageWriteable);
    }


    private void writeToSDFile(HSSFWorkbook workbook) {

        File root = android.os.Environment.getExternalStorageDirectory();
        //tv.append("\nExternal file system root: "+root);

        // See http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder

        File dir = new File(root.getAbsolutePath() + "/download");
        dir.mkdirs();
        File file = new File(dir, "bazSale.xls");

        progressBar.setVisibility(View.INVISIBLE);

        try {
            FileOutputStream f = new FileOutputStream(file);
           // FileOutputStream f=openFileOutput("bazSale.xls", Context.MODE_PRIVATE);
            workbook.write(f);

            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"17ucs087@lnmiit.ac.in","shalabh1221@gmail.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "Bazinga Sale");
            Uri uri = Uri.fromFile(file);
            i.putExtra(Intent.EXTRA_STREAM, uri);
            try {
                startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(salexcel.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }

            f.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("MEDIA", "******* File not found. Did you" +
                    " add a WRITE_EXTERNAL_STORAGE permission to the   manifest?");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //tv.append("\n\nFile written to "+file);
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

    @Override
    public void onBackPressed() {
        if (Common.isConnectedToInternet(salexcel.this)) {
            Intent intent = new Intent(salexcel.this, Orders.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(salexcel.this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (Common.isConnectedToInternet(salexcel.this)) {
            Intent intent = new Intent(salexcel.this, Orders.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(salexcel.this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }
        return true;
    }


}
