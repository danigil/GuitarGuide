package com.melons.melon.guitarguide;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class CreateNotification {
    public static final String CHANNEL_ID = "channel";

    public static final String ACTION_PREVIOUS = "actionprevious";
    public static final String ACTION_NEXT = "actionnext";
    public static final String ACTION_PLAY = "actionplay";

    public static Notification notification;

    public static void createNotification(Context context, Song song, int playbutton, int pos, int size, boolean isplaying){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(context,"tag");

            PendingIntent pendingIntentprevious,pendingIntentplay,pendingIntentnext;
            int drw_previous,drw_play,drw_next;

             Intent intentPrevious = new Intent(context, NotificationActionReceiver.class)
                     .setAction(ACTION_PREVIOUS);
             pendingIntentprevious = PendingIntent.getBroadcast(context,0,intentPrevious,PendingIntent.FLAG_UPDATE_CURRENT);
             drw_previous = R.drawable.ic_skip_previous;


            Intent intentPlay = new Intent(context, NotificationActionReceiver.class)
                    .setAction(ACTION_PLAY);
            pendingIntentplay = PendingIntent.getBroadcast(context,0,intentPlay,PendingIntent.FLAG_UPDATE_CURRENT);
            if(isplaying)
                drw_play = R.drawable.ic_pause_circle_filled;
            else
                drw_play = R.drawable.ic_play_circle_filled;


            Intent intentNext = new Intent(context, NotificationActionReceiver.class)
                    .setAction(ACTION_NEXT);
            pendingIntentnext = PendingIntent.getBroadcast(context,0,intentNext,PendingIntent.FLAG_UPDATE_CURRENT);
            drw_next = R.drawable.ic_skip_next;


            notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle(song.getSongname())
                    .setContentText(song.getSongartist())
                    .setSmallIcon(R.drawable.ic_guitar)
                    .addAction(drw_previous,"Previous",pendingIntentprevious)
                    .addAction(drw_play,"Play",pendingIntentplay)
                    .addAction(drw_next,"Next",pendingIntentnext)
                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0,1,2)
                        .setMediaSession(mediaSessionCompat.getSessionToken()))
                    .setOnlyAlertOnce(false)
                    .setShowWhen(false)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .build();

            notificationManagerCompat.notify(1,notification);
        }
    }
}
