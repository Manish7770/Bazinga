package com.manish.bazingaadmin.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateFormat;

import com.manish.bazingaadmin.Model.Requests;
import com.manish.bazingaadmin.Remote.APIService;
import com.manish.bazingaadmin.Remote.RetrofitClient;

import java.util.Calendar;
import java.util.Locale;

import retrofit2.Retrofit;

public class Common {

    public static String currentAdmin;

    public static Requests currentRequests=new Requests();

    private static final String BASE_URL = "https://fcm.googleapis.com/";

    public static APIService getFCMClient()
    {
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }

    public static String getDate(long time)
    {
        Calendar calendar=Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        StringBuilder date=new StringBuilder(DateFormat.format("dd-MM-yyyy",calendar).toString());
        return date.toString();
    }
    public static String getTime(long time)
    {
        Calendar calendar=Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        StringBuilder time2=new StringBuilder(DateFormat.format("HH:mm",calendar).toString());
        return time2.toString();
    }

    public static boolean isConnectedToInternet(Context context)
    {
        ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager!=null)
        {
            NetworkInfo[] info=connectivityManager.getAllNetworkInfo();
            if (info!=null)
            {
                for (int i=0;i<info.length;i++)
                {
                    if (info[i].getState()==NetworkInfo.State.CONNECTED)
                        return  true;
                }
            }
        }
        return false;
    }
}
