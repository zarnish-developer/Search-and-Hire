package com.example.splash_activity.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.splash_activity.R;
import com.example.splash_activity.recycle.myOwnAdapter;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    NotificationManager notificationManager;
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public  void onMessageReceived(RemoteMessage remoteMessage){
        super.onMessageReceived(remoteMessage);
        //playing audio when send a notification
        Uri notification= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r=RingtoneManager.getRingtone(getApplicationContext(),notification);
        r.play();;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            r.setLooping(false);

        }
        //Vibration
        Vibrator v =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
         long[] pattern={100, 300,300,300};
        v.vibrate(pattern, -1);


        int resourceImage=getResources().getIdentifier(remoteMessage.getNotification().getIcon(), "drawable",getOpPackageName());
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this, "CHANNEL_ID");
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
            builder.setSmallIcon(R.drawable.icon);

        }else{
            builder.setSmallIcon(R.drawable.icon);
        }
        Intent resultIntent=new Intent(this, myOwnAdapter.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,1,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentTitle(remoteMessage.getNotification().getTitle());
        builder.setContentText(remoteMessage.getNotification().getBody());
        builder.setContentIntent(pendingIntent);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getNotification().getBody()));
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_MAX);
        notificationManager=
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);



        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            String channelId= "your_channel_id";
            NotificationChannel channel= new NotificationChannel(channelId,
                    "channel human readable title", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);

            //notification is a unique int for each notification that you must define
            notificationManager.notify(100, builder.build());


        }
    }

}
