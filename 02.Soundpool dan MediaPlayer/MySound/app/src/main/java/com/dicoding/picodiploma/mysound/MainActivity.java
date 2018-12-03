package com.dicoding.picodiploma.mysound;

import android.content.Intent;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSound;

    SoundPool sp;
    int soundId;
    boolean spLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSound = findViewById(R.id.btn_soundpool);
        btnSound.setOnClickListener(this);

        sp = new SoundPool.Builder()
                .setMaxStreams(10)
                .build();

        /*
        Tambahkan listener ke soundpool jika proses load sudah selesai
         */
        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (status == 0) {
                    spLoaded = true;
                } else {
                    Toast.makeText(MainActivity.this, "Gagal load", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*
        Load raw clinking_glasses ke soundpool, jika selesai maka id nya dimasukkan ke variable soundId
         */
        soundId = sp.load(this, R.raw.clinking_glasses, 1); // in 2nd param u have to pass your desire ringtone
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_soundpool:
                if (spLoaded) {
                    sp.play(soundId, 1, 1, 0, 0, 1);
                }
                break;

        }
    }


}
