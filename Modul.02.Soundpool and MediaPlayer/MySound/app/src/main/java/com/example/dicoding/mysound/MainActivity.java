package com.example.dicoding.mysound;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.net.URISyntaxException;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnSound;
    Button btnMedia;
    Button btnMediaStop;

    // MediaService mediaService = new MediaService();
    Intent it;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSound = (Button)findViewById(R.id.btn_soundpool);
        btnMedia = (Button)findViewById(R.id.btn_mediaplayer);
        btnMediaStop = (Button)findViewById(R.id.btn_mediaplayer_stop);
        btnSound.setOnClickListener(this);
        btnMedia.setOnClickListener(this);
        btnMediaStop.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sp = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .build();
        } else {
            sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
        }

        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            public void onLoadComplete(SoundPool soundPool, int sampleId,int status) {
                //Log.d("Load","true");
                spLoaded = true;
            }
        });

        wav = sp.load(this, R.raw.clinking_glasses, 1); // in 2nd param u have to pass your desire ringtone

        it = new Intent(this, MediaService.class);
        it.setAction(MediaService.ACTION_CREATE);
        it.setPackage(MediaService.ACTION_PACKAGE);
        startService(it);
    }
    SoundPool sp ;
    int wav;
    boolean spLoaded = false;

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_soundpool:
                if (spLoaded == true ){
                    sp.play(wav, 1, 1, 0, 0, 1);
                }
                break;

            case R.id.btn_mediaplayer:
                 it.setAction(MediaService.ACTION_PLAY);
                it.setPackage(MediaService.ACTION_PACKAGE);
                startService(it);

                break;
            case R.id.btn_mediaplayer_stop:
                it = new Intent(this,MediaService.class);
                it.setAction(MediaService.ACTION_STOP);
                it.setPackage(MediaService.ACTION_PACKAGE);
                startService(it);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(it);
    }
}
