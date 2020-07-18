package com.manish.bazingalnmiit.Commondata;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.text.format.DateFormat;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.manish.bazingalnmiit.Model.Order;
import com.manish.bazingalnmiit.Model.Requests;
import com.manish.bazingalnmiit.Model.UserApp;
import com.manish.bazingalnmiit.Remote.APIService;
import com.manish.bazingalnmiit.Remote.RetrofitClient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Retrofit;

/**
 * Created by Dell on 31-03-2018.
 */

public class Commondata {

    public static List<Order> cartforpayment=new ArrayList<>();
    public static String currentUID="" ,url = "",mid="",channel_id="",industry_type_id="",website="",callback_url="";
    public static UserApp currentUser=new UserApp();
    public static Requests currentRequests=new Requests();
    public static FirebaseAuth firebaseAuth;
    public static GoogleSignInClient googleSignInClient;

    public static int popup=1;

    private static final String BASE_URL = "https://fcm.googleapis.com/";

    public static APIService getFCMService()
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
