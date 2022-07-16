package com.melons.melon.guitarguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //necesseties
    CoordinatorLayout coordinatorLayout;
    ConnectivityReceiver connectivityReceiver;
    public static String username;

    //page design
    RelativeLayout header,r1,r2,r3,r4;
    int screenwidth;
    int screenheight;
    int remainderheight;

    //animation
    OvershootInterpolator interpolator;

    //song player
    RelativeLayout songplayer;
    Button play, pause, forward, backward;
    TextView songname, songartist, totalsonglength, remainingsonglength;
    SeekBar positionBar;
    Integer totaltime;
    Boolean isShown = false;

    //song player notification
    NotificationManager notificationManager;
    BroadcastReceiver broadcastReceiver;

    //music service
    private boolean mIsBound = false;
    public static MusicService mServ;

    private ServiceConnection Scon = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicService.ServiceBinder) binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationManager = getSystemService(NotificationManager.class);



        coordinatorLayout = findViewById(R.id.colayout);
        connectivityReceiver = new ConnectivityReceiver(coordinatorLayout,notificationManager);

        username = getIntent().getStringExtra("user");

        header = findViewById(R.id.MainHeaderLayout);
        r1 = findViewById(R.id.Mainlayout1);
        r2 = findViewById(R.id.Mainlayout2);
        r3 = findViewById(R.id.Mainlayout3);
        r4 = findViewById(R.id.Mainlayout4);

        //page deisgn
        SetMetrics();
        SetLayouts();

        songplayer = findViewById(R.id.songplayer);
        songplayer.bringToFront();
        coordinatorLayout.bringChildToFront(songplayer);

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(songplayer);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {

            @Override
            public void onStateChanged(@NonNull View view, int i) {
                switch (i){

                    case BottomSheetBehavior.STATE_COLLAPSED:
                        isShown = false;
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        isShown = true;
                        break;
                    default:
                        isShown=false;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        //music player buttons
        play = findViewById(R.id.Buttonplay);
        pause = findViewById(R.id.Buttonpause);
        forward = findViewById(R.id.Buttonskipforward);
        backward = findViewById(R.id.Buttonskipbackward);

        //music player text views
        songname = findViewById(R.id.TVSongname);
        songartist = findViewById(R.id.TVSongartist);
        totalsonglength = findViewById(R.id.TVtotalsonglength);
        remainingsonglength = findViewById(R.id.TVremainingsonglength);

        //music player bar
        positionBar = findViewById(R.id.positionBar);

        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        forward.setOnClickListener(this);
        backward.setOnClickListener(this);

        //initialize song player
        SongplayerInit();

        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        finish();

                    }
                }, new IntentFilter("close")
        );
    }

    //page design
    private void SetLayouts(){
        ViewGroup.LayoutParams dlayoutParams= header.getLayoutParams();
        remainderheight = screenheight-dlayoutParams.height;

        ViewGroup.LayoutParams layoutParams4= r4.getLayoutParams();
        ViewGroup.MarginLayoutParams  marginLayoutParams4= (ViewGroup.MarginLayoutParams) r4.getLayoutParams();
        layoutParams4.height=remainderheight/4;
        r4.setLayoutParams(layoutParams4);

        ViewGroup.LayoutParams layoutParams1= r1.getLayoutParams();
        ViewGroup.MarginLayoutParams  marginLayoutParams1= (ViewGroup.MarginLayoutParams) r1.getLayoutParams();
        layoutParams1.height=remainderheight/4;
        marginLayoutParams1.setMargins(0,0,0,3*remainderheight/4+marginLayoutParams4.bottomMargin);
        r1.setLayoutParams(layoutParams1);
        r1.setLayoutParams(marginLayoutParams1);

        ViewGroup.LayoutParams layoutParams2= r2.getLayoutParams();
        ViewGroup.MarginLayoutParams  marginLayoutParams2= (ViewGroup.MarginLayoutParams) r2.getLayoutParams();
        layoutParams2.height=remainderheight/4;
        marginLayoutParams2.setMargins(0,0,0,2*remainderheight/4+marginLayoutParams4.bottomMargin);
        r2.setLayoutParams(layoutParams2);
        r2.setLayoutParams(marginLayoutParams2);

        ViewGroup.LayoutParams layoutParams3= r3.getLayoutParams();
        ViewGroup.MarginLayoutParams  marginLayoutParams3= (ViewGroup.MarginLayoutParams) r3.getLayoutParams();
        layoutParams3.height=remainderheight/4;
        marginLayoutParams3.setMargins(0,0,0,remainderheight/4+marginLayoutParams4.bottomMargin);
        r3.setLayoutParams(layoutParams3);
        r3.setLayoutParams(marginLayoutParams3);
    }
    private void SetMetrics(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.screenheight= displayMetrics.heightPixels;
        this.screenwidth = displayMetrics.widthPixels;
    }

    //song player & service
    private void SongplayerInit(){

        doBindService();
        final Intent music = new Intent();
        music.setClass(this, MusicService.class);
        startService(music);

         final Handler handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                int CurrentPosition = msg.what;
                positionBar.setProgress(CurrentPosition);

                String Remainingtime = createTimeLabel(totaltime - CurrentPosition);
                remainingsonglength.setText(Remainingtime);
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        songname.setText(intent.getStringExtra("songname"));
                        songartist.setText(intent.getStringExtra("songartist"));
                        totaltime = intent.getIntExtra("songduration", -999);
                        positionBar.setMax(totaltime);
                        totalsonglength.setText(createTimeLabel(totaltime));

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (mIsBound && mServ.mPlayer != null) {
                                    if (!mServ.mPlayer.isPlaying() && ((totaltime - mServ.mPlayer.getCurrentPosition()) / 1000) % 60 < 1)
                                        break;
                                    try {
                                        Message msg = new Message();
                                        msg.what = mServ.mPlayer.getCurrentPosition();
                                        handler.sendMessage(msg);
                                        Thread.sleep(1);
                                    } catch (InterruptedException e) {
                                    }
                                }
                            }
                        }).start();

                    }
                }, new IntentFilter(MusicService.ACTION_SENDSTRING)
        );

        positionBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser && !mServ.firstplay) {

                            if(mServ.isPaused) {
                                mServ.length = progress;
                            }

                            mServ.mPlayer.seekTo(progress);
                            positionBar.setProgress(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CreateNotification.CHANNEL_ID,
                    "Melodev", NotificationManager.IMPORTANCE_LOW);

            if(notificationManager !=null)
                notificationManager.createNotificationChannel(channel);

        }

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getExtras().getString("actionname");

                switch (action){
                    case CreateNotification.ACTION_PREVIOUS:
                        onClick(backward);
                        break;
                    case CreateNotification.ACTION_PLAY:
                        if(mServ.mPlayer.isPlaying())
                            onClick(pause);
                        else
                            onClick(play);
                        break;
                    case CreateNotification.ACTION_NEXT:
                        onClick(forward);
                        break;
                }
            }
        };

        registerReceiver(broadcastReceiver, new IntentFilter("SONGS_SONGS"));
        startService(new Intent(getBaseContext(),OnClearFromRecentService.class));
    }
    public void doBindService() {
        bindService(new Intent(this, MusicService.class),
                Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }
    public void doUnbindService() {
        if (mIsBound) {
            mIsBound = false;
            unbindService(Scon);
        }
    }
    public static String createTimeLabel(int time){
        String TimeLabel = "";
        int min = time/1000/60;
        int sec = time/1000%60;

        TimeLabel = min + ":";
        if(sec<10)
            TimeLabel +="0";

        TimeLabel += sec;

        return TimeLabel;
    }

    //clickable layouts
    public void click(View View) {
        if (View.getId() == findViewById(R.id.Mainlayout1).getId()) {
            animate((Button)findViewById(R.id.MainButtonTech));
            Runnable runnable = new Runnable() {

                public void run() {
                    Intent intent = new Intent(MainActivity.this, TechniqueActivity.class);
                    intent.putExtra("user", MainActivity.this.username);
                    startActivity(intent);
                }
            };
            (new Handler()).postDelayed(runnable, 400L);
        }

        if (View.getId() == findViewById(R.id.Mainlayout2).getId()) {
            animate((Button)findViewById(R.id.MainButtonManu));
            Runnable runnable = new Runnable() {

                public void run() {
                    Intent intent = new Intent(MainActivity.this, ManufacturerActivity.class);
                    intent.putExtra("user", MainActivity.this.username);
                    startActivity(intent);
                }
            };
            (new Handler()).postDelayed(runnable, 400L);
        }

        if (View.getId() == findViewById(R.id.Mainlayout3).getId()) {
            if(!isShown) {
                animate((Button) findViewById(R.id.MainButtonParts));
                Runnable runnable = new Runnable() {

                    public void run() {
                        Intent intent = new Intent(MainActivity.this, GuitarPartsActivity.class);
                        intent.putExtra("user", MainActivity.this.username);
                        startActivity(intent);
                    }
                };
                (new Handler()).postDelayed(runnable, 400L);
            }
            else
                Toast.makeText(this, "Please lower the songplayer in order to access this page!", Toast.LENGTH_SHORT).show();
        }

        if (View.getId() == findViewById(R.id.Mainlayout4).getId()) {
            if(!isShown) {
                animate((Button) findViewById(R.id.MainButtonFame));
                Runnable runnable = new Runnable() {

                    public void run() {
                        Intent intent = new Intent(MainActivity.this, HallOfFameActivity.class);
                        intent.putExtra("user", MainActivity.this.username);
                        startActivity(intent);
                    }
                };
                (new Handler()).postDelayed(runnable, 400L);
            }
        }
    }
    private void animate(Button button) {
        button.animate().setInterpolator(this.interpolator).rotationBy(360.0F).setDuration(300L).start();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==play.getId()){
            CreateNotification.createNotification(getApplicationContext(),mServ.arrayList.get(mServ.getCount()),R.drawable.ic_play_circle_filled,mServ.getCount(),mServ.arrayList.size()-1,true);
            if (mServ != null && mServ.firstplay) {
                mServ.startMusic();
                play.setVisibility(View.INVISIBLE);
                pause.setVisibility(View.VISIBLE);
                mServ.firstplay=false;
            }
            else
            if(mServ !=null){
                mServ.resumeMusic();
                play.setVisibility(View.INVISIBLE);
                pause.setVisibility(View.VISIBLE);
            }
        }

        if(v.getId()==pause.getId()){
            if (mServ != null) {
                mServ.pauseMusic();
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.GONE);
                CreateNotification.createNotification(getApplicationContext(),mServ.arrayList.get(mServ.getCount()),R.drawable.ic_play_circle_filled,mServ.getCount(),mServ.arrayList.size()-1,false);
            }
        }

        if(v.getId()==forward.getId()){
            mServ.skipforward();
            play.setVisibility(View.INVISIBLE);
            pause.setVisibility(View.VISIBLE);
            CreateNotification.createNotification(getApplicationContext(),mServ.arrayList.get(mServ.getCount()),R.drawable.ic_play_circle_filled,mServ.getCount(),mServ.arrayList.size()-1,true);
        }

        if(v.getId()==backward.getId()){
            mServ.skipbackward();
            play.setVisibility(View.INVISIBLE);
            pause.setVisibility(View.VISIBLE);
            CreateNotification.createNotification(getApplicationContext(),mServ.arrayList.get(mServ.getCount()),R.drawable.ic_play_circle_filled,mServ.getCount(),mServ.arrayList.size()-1,true);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        doUnbindService();
        Intent music = new Intent();
        music.setClass(this,MusicService.class);
        stopService(music);

        notificationManager.cancelAll();

        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onBackPressed() {
        (new AlertDialog.Builder((Context)this)).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                param1DialogInterface.cancel();
            }
        }).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        }).setTitle("Log out?").setMessage("Are you sure you want to log out?").setCancelable(true).create().show();
    }
}
