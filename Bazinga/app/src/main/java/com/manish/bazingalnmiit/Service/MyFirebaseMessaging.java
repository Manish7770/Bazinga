package com.manish.bazingalnmiit.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.manish.bazingalnmiit.Commondata.Commondata;
import com.manish.bazingalnmiit.Helper.NotificationHelper;
import com.manish.bazingalnmiit.MainActivity;
import com.manish.bazingalnmiit.Model.Token;
import com.manish.bazingalnmiit.OrderDetails;
import com.manish.bazingalnmiit.OrdersList;
import com.manish.bazingalnmiit.R;

import java.util.Map;
import java.util.Random;

import javax.sql.CommonDataSource;

public class MyFirebaseMessaging extends FirebaseMessagingService{

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getData() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                sendNotificationAPI26(remoteMessage);
            else
                sendNotification(remoteMessage);
        }
        
    }

    @Override
    public void onNewToken(String tokenRefreshed) {
        super.onNewToken(tokenRefreshed);
        if(Commondata.currentUser.getName()!=null) {
            updateTokenToFirebase(tokenRefreshed);
        }
    }

    private void updateTokenToFirebase(String tokenRefreshed) {

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference tokens=database.getReference("Tokens");

        Token token=new Token(tokenRefreshed,false);
        //false is client side

        tokens.child(Commondata.currentUID).setValue(token);
    }

    private void sendNotificationAPI26(RemoteMessage remoteMessage) {

        Map<String,String> data= remoteMessage.getData();
        String title=data.get("title");
        String message=data.get("message");
        String orderId=data.get("orderId");

        PendingIntent pendingIntent;
        NotificationHelper helper;
        Notification.Builder builder;

        //Log.i("curr123",Commondata.currentUser+"123");

        if (Commondata.currentUser.getName()!=null) {

            Intent intent = new Intent(this, OrderDetails.class);
            intent.putExtra("OrderId", orderId);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            helper = new NotificationHelper(this);
            builder = helper.getBazingaChannelNotification(title, message, pendingIntent, defaultSoundUri);

            helper.getManager().notify(new Random().nextInt(), builder.build());
        }
        else
        {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            helper = new NotificationHelper(this);
            builder = helper.getBazingaChannelNotification(title, message, pendingIntent, defaultSoundUri);

            helper.getManager().notify(new Random().nextInt(), builder.build());
        }
    }

    private void sendNotification(RemoteMessage remoteMessage) {

        Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        String message = data.get("message");
        String orderId=data.get("orderId");

        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;

        Intent intent;
        if (Commondata.currentUser.getName()!=null) {

            intent = new Intent(this, OrderDetails.class);
            intent.putExtra("OrderId", orderId);
        }
        else
        {
            intent = new Intent(this, MainActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_bazingaround)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        noti.notify(0, builder.build());


    }
}
