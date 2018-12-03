package com.dicoding.picodiploma.mymediaplayer;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = MainActivity.class.getSimpleName();
    private Button btnPlay;
    private Button btnStop;
    private Messenger mService = null;

    private Intent mBoundServiceIntent;
    private boolean mServiceBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlay = findViewById(R.id.btn_play);
        btnStop = findViewById(R.id.btn_stop);
        btnPlay.setOnClickListener(this);
        btnStop.setOnClickListener(this);

         /*
         Start service untuk media player
        */
        mBoundServiceIntent = new Intent(MainActivity.this, MediaService.class);
        mBoundServiceIntent.setAction(MediaService.ACTION_CREATE);

        startService(mBoundServiceIntent);
        bindService(mBoundServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        unbindService(mServiceConnection);
        mBoundServiceIntent.setAction(MediaService.ACTION_DESTROY);

        startService(mBoundServiceIntent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_play:
                /*
                Untuk mengirim perintah play
                 */
                if (!mServiceBound) return;
                try {
                    mService.send(Message.obtain(null, MediaService.PLAY, 0, 0));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.btn_stop:
                /*
                Untuk mengirim perintah stop
                */
                if (!mServiceBound) return;
                try {
                    mService.send(Message.obtain(null, MediaService.STOP, 0, 0));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    /*
    Service Connection adalah interface yang digunakan untuk menghubungkan antara boundservice dengan activity
     */
    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mServiceBound = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            mServiceBound = true;
        }

    };
}
