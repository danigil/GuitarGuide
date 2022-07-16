package com.melons.melon.guitarguide;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.snackbar.Snackbar;

import static java.security.AccessController.getContext;

public class ConnectivityReceiver extends BroadcastReceiver {

    public Integer i;

    public CoordinatorLayout layout;
    public NotificationManager notificationManager;

    public ConnectivityReceiver() {
        this.i = Integer.valueOf(0);
    }

    public ConnectivityReceiver(CoordinatorLayout layout, NotificationManager notificationManager) {
        this.i = Integer.valueOf(0);
        this.layout = layout;
        this.notificationManager = notificationManager;
    }

    public ConnectivityReceiver(CoordinatorLayout layout) {
        this.i = Integer.valueOf(0);
        this.layout = layout;
    }

    //checks if device has internet connection
    public static boolean isOnline(Context context) {
        if(context==null)
            return false;

        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
            if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE"))
                if (!isOnline(context)) {
                    (new AlertDialog.Builder(context)).setNegativeButton("CLOSE APP", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(notificationManager != null)
                                        notificationManager.cancelAll();

                                    ((Activity) context).finishAffinity();
                                }
                            }


                    ).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                            param1DialogInterface.cancel();
                        }
                    }).setTitle("WARNING").setMessage("This app requires internet connection in order to function correctly, please connect to the internet").setCancelable(false).create().show();
                    Snackbar snackbar = Snackbar.make(this.layout, "Offline", Snackbar.LENGTH_INDEFINITE);
                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.RED));
                    snackbar.show();
                    this.i = Integer.valueOf(this.i.intValue() + 1);
                } else {
                    if (this.i.intValue() > 0) {
                        Snackbar snackbar = Snackbar.make(this.layout, "Back Online", Snackbar.LENGTH_SHORT);
                        snackbar.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.GREEN));
                        snackbar.show();
                    }
                    this.i = Integer.valueOf(this.i.intValue() + 1);
        }
    }
}
