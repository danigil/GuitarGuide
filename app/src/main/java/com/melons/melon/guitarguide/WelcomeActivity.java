package com.melons.melon.guitarguide;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaDataSource;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.Serializable;


public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    //necesseties
    CoordinatorLayout coordinatorLayout;
    ConnectivityReceiver connectivityReceiver;

    //page components
    Button Login,Register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        coordinatorLayout = findViewById(R.id.colayout);
        connectivityReceiver = new ConnectivityReceiver(coordinatorLayout);

        Login = findViewById(R.id.ButtonWelcomeLogin);
        Register = findViewById(R.id.ButtonWelcomeRegister);

        Login.setOnClickListener(this);
        Register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==findViewById(R.id.ButtonWelcomeLogin).getId()) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
        if(v.getId()==findViewById(R.id.ButtonWelcomeRegister).getId()) {
            startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            finish();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        connectivityReceiver.i = Integer.valueOf(0);
        unregisterReceiver(connectivityReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(connectivityReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

}
