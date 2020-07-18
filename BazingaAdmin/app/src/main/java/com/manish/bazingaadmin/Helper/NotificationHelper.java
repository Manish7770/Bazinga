package com.manish.bazingaadmin.Helper;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import com.manish.bazingaadmin.R;

public class NotificationHelper extends ContextWrapper{

    private static final String BAZINGA_CHANEL_ID="com.manish.bazinga.BAZINGA";
    private static final String BAZINGA_CHANEL_NAME="BAZINGA";

    private NotificationManager manager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createChannel();

    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel bazingaChannel= new NotificationChannel(BAZINGA_CHANEL_ID,
                BAZINGA_CHANEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT);
        bazingaChannel.enableLights(false);
        bazingaChannel.enableVibration(true);
        bazingaChannel.setLockscreenVisibility(android.app.Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(bazingaChannel);
    }

    public NotificationManager getManager() {

        if (manager == null)
            manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        return manager;

    }

    @TargetApi(Build.VERSION_CODES.O)
    public android.app.Notification.Builder getBazingaChannelNotification(String title, String body, PendingIntent contentIntent,
                                                                          Uri SoundUri)
    {
        return new android.app.Notification.Builder(getApplicationContext(),BAZINGA_CHANEL_ID)
                .setContentIntent(contentIntent)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.mipmap.ic_launcher_bazingaround)
                .setSound(SoundUri)
                .setAutoCancel(true);
    }

    /**@TargetApi(Build.VERSION_CODES.O)
    public android.app.Notification.Builder getBazingaChannelNotification(String title, String body,
                                                                          Uri SoundUri)
    {
        return new android.app.Notification.Builder(getApplicationContext(),BAZINGA_CHANEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(SoundUri)
                .setAutoCancel(true);
    }**/
}
