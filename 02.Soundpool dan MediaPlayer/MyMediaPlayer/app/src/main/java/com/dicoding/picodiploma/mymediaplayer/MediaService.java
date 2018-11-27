package com.dicoding.picodiploma.mymediaplayer;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by dicoding on 11/21/2016.
 */

public class MediaService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayerCallback {
    final String TAG = MediaService.class.getSimpleName();
    public final static int PLAY = 0;
    public final static int STOP = 1;
    public final static String ACTION_CREATE = "com.dicoding.picodiploma.mysound.mediaservice.create";
    public final static String ACTION_DESTROY = "com.dicoding.picodiploma.mysound.mediaservice.destroy";
    private boolean isReady;
    private MediaPlayer mMediaPlayer = null;

    /*
    Ketika kelas service terbentuk, secara otomatis akan memanggil method init()
    */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
    }

    /*
   Method yang akan dipanggil ketika service dimulai
   */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();

        assert action != null;
        switch (action) {
            case ACTION_CREATE:
                if (mMediaPlayer == null) {
                    init();
                }
                break;
            case ACTION_DESTROY:
                if (!mMediaPlayer.isPlaying()){
                    stopSelf();
                }
                break;
            default:
                break;
        }
        Log.d(TAG, "onStartCommand: ");
        return flags;
    }

    /*
        Method yang akan dipanggil ketika service di ikatkan ke activity
        */
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return mMessenger.getBinder();
    }

    /*
        Method yang akan dipanggil ketika service di lepas dari activity
        */
    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: ");
        return super.onUnbind(intent);
    }

    /*
    Method yang akan dipanggil service terlepas dari memory
    */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        if (mMediaPlayer != null)
            mMediaPlayer.release();
    }

    /*
    Method ini berfungsi untuk menginisialisasi mediaplayer
    */
    private void init() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        AssetFileDescriptor afd = getApplicationContext().getResources().openRawResourceFd(R.raw.guitar_background);
        try {
            mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnErrorListener(this);

    }

    /**
     * Called when MediaPlayer is ready
     */
    @Override
    public void onPrepared(MediaPlayer player) {
        isReady = true;
        mMediaPlayer.start();
    }

    /**
     * Called when MediaPlayer is error
     */
    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    /**
     * Callback ketika button play di klik
     */
    @Override
    public void onPlay() {
        if (!isReady) {
            mMediaPlayer.prepareAsync();
        } else {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            } else {
                mMediaPlayer.start();
            }
        }
    }

    /**
     * Callback ketika button stop di klik
     */
    @Override
    public void onStop() {
        if (mMediaPlayer.isPlaying() || isReady) {
            mMediaPlayer.stop();
            isReady = false;
        }
    }

    /**
     * Method incomingHandler sebagai handler untuk aksi dari onklik button di MainActivity
     */
    private final Messenger mMessenger = new Messenger(new IncomingHandler(this));

    static class IncomingHandler extends Handler {

        private WeakReference<MediaPlayerCallback> mediaPlayerCallbackWeakReference;

        IncomingHandler(MediaPlayerCallback playerCallback) {
            this.mediaPlayerCallbackWeakReference = new WeakReference<>(playerCallback);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PLAY:
                    mediaPlayerCallbackWeakReference.get().onPlay();
                    break;

                case STOP:
                    mediaPlayerCallbackWeakReference.get().onStop();
                    break;

                default:
                    super.handleMessage(msg);
            }
        }
    }
}
