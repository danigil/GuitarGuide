package com.melons.melon.guitarguide;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener {

    //binder and player necesseties
    private final IBinder mBinder = new ServiceBinder();
    MediaPlayer mPlayer;
    public  int length = 0;
    private int count = 0;

    public boolean firstplay = true;
    public Boolean isPaused = false;


    public static final String ACTION_SENDSTRING = MusicService.class.getName();
    public static final String ACTION_SEEKPAUSED = "SEEKPAUSED";

    //local song list
    public ArrayList<Song> arrayList;
    String[] songname = {"Nineteen Eighty", "Guitar Solo", "Purple Haze"};
    String[] songartist = {"Joe Satriani", "Paul Gilbert", "Jimi Hendrix"};
    int[] address = {R.raw.joe, R.raw.gilbert, R.raw.jimi};

    public MusicService() {
    }

    public class ServiceBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        arrayList = new ArrayList<>();
        for(int i = 0; i< songname.length; i++)
            arrayList.add(new Song(songname[i],songartist[i],address[i]));

        mPlayer = MediaPlayer.create(this, arrayList.get(0).getSongAddress());
        if (mPlayer != null) {
            mPlayer.setLooping(true);
            mPlayer.setVolume(50, 50);
        }

        mPlayer.setOnCompletionListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    public int getCount() {
        return count;
    }

    public void startMusic() {
        mPlayer = MediaPlayer.create(this, arrayList.get(count).getSongAddress());

        if (mPlayer != null) {
            mPlayer.setLooping(false);
            mPlayer.setVolume(50, 50);
            mPlayer.start();

            isPaused = false;

            sendBroadcastMessage(arrayList.get(count).getSongname(),arrayList.get(count).getSongartist(),mPlayer.getDuration());
            mPlayer.setOnCompletionListener(this);
        }

    }

    public void pauseMusic() {
        if (mPlayer != null) {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                isPaused = true;
                length = mPlayer.getCurrentPosition();
            }
        }
    }

    public void resumeMusic() {
        if (mPlayer != null) {
            if (!mPlayer.isPlaying()) {
                mPlayer.seekTo(length);
                isPaused = false;
                mPlayer.start();
            }
        }
    }

    public void stopMusic() {
        if (mPlayer != null) {
            mPlayer.stop();
        }
    }

    public void skipforward(){
        stopMusic();

        if(count+1==arrayList.size())
            count=0;
        else
            count++;

        mPlayer = MediaPlayer.create(this, arrayList.get(count).getSongAddress());

        if (mPlayer != null) {
            mPlayer.setLooping(false);
            mPlayer.setVolume(50, 50);
            mPlayer.start();

            isPaused = false;

            sendBroadcastMessage(arrayList.get(count).getSongname(),arrayList.get(count).getSongartist(),mPlayer.getDuration());

            mPlayer.setOnCompletionListener(this);
        }
    }

    public void skipbackward(){
        stopMusic();

        if(count-1<0) {
            count = arrayList.size()-1;
        }
            else
                count--;

        mPlayer = MediaPlayer.create(this, arrayList.get(count).getSongAddress());

        if (mPlayer != null) {
            mPlayer.setLooping(false);
            mPlayer.setVolume(50, 50);
            mPlayer.start();

            isPaused = false;

            sendBroadcastMessage(arrayList.get(count).getSongname(),arrayList.get(count).getSongartist(),mPlayer.getDuration());

            mPlayer.setOnCompletionListener(this);
        }
    }

    //send message for player to change textviews according to song
    private void sendBroadcastMessage(String songname,String songartist,int length) {
        if (songname != null) {
            Intent intent = new Intent(ACTION_SENDSTRING);
            intent.putExtra("songname", songname);
            intent.putExtra("songartist",songartist);
            intent.putExtra("songduration",length);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

        if(count==arrayList.size()-1)
            count=0;
        else
            count++;

        mp.release();


        mPlayer = MediaPlayer.create(getApplicationContext(), arrayList.get(count).getSongAddress());
        mPlayer.seekTo(0);
        mPlayer.start();

        sendBroadcastMessage(arrayList.get(count).getSongname(),arrayList.get(count).getSongartist(),mPlayer.getDuration());

        mPlayer.setOnCompletionListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            try {
                mPlayer.stop();
                mPlayer.release();
            } finally {
                mPlayer = null;
            }
        }
    }
}
