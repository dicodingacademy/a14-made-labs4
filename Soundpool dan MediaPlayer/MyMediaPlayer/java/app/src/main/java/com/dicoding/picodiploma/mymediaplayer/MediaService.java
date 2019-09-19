package com.dicoding.picodiploma.mymediaplayer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by dicoding on 11/21/2016.
 */

public class MediaService extends Service implements MediaPlayerCallback {
    private final String TAG = MediaService.class.getSimpleName();
    private boolean isReady;
    private MediaPlayer mMediaPlayer = null;
    public final static String ACTION_CREATE = "com.dicoding.picodiploma.mysound.mediaservice.create";
    public final static String ACTION_DESTROY = "com.dicoding.picodiploma.mysound.mediaservice.destroy";
    public final static int PLAY = 0;
    public final static int STOP = 1;

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

        if (action != null) {
            switch (action) {
                case ACTION_CREATE:
                    if (mMediaPlayer == null) {
                        init();
                    }
                    break;
                case ACTION_DESTROY:
                    if (!mMediaPlayer.isPlaying()) {
                        stopSelf();
                    }
                    break;
                default:
                    break;
            }
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes attribute = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            mMediaPlayer.setAudioAttributes(attribute);
        } else {
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }

        AssetFileDescriptor afd = getApplicationContext().getResources().openRawResourceFd(R.raw.guitar_background);
        try {
            mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            /**
             * Called when MediaPlayer is ready
             */
            @Override
            public void onPrepared(MediaPlayer mp) {
                isReady = true;
                mMediaPlayer.start();
                showNotif();
            }
        });

        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            /**
             * Called when MediaPlayer is error
             */
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
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
                showNotif();
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
            stopNotif();
        }
    }

    /**
     * Digunakan ketika media service berjalan, maka akan muncul notif
     */
    private void showNotif() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);


        String CHANNEL_DEFAULT_IMPORTANCE = "Channel_Test";
        int ONGOING_NOTIFICATION_ID = 1;

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_DEFAULT_IMPORTANCE)
                .setContentTitle("TES1")
                .setContentText("TES2")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .setTicker("TES3")
                .build();

        createChannel(CHANNEL_DEFAULT_IMPORTANCE);

        startForeground(ONGOING_NOTIFICATION_ID, notification);
    }

    private void createChannel(String CHANNEL_ID) {

        NotificationManager mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Battery",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setShowBadge(false);
            channel.setSound(null, null);
            mNotificationManager.createNotificationChannel(channel);
        }
    }

    private void stopNotif() {
        stopForeground(false);
    }

    /**
     * Method incomingHandler sebagai handler untuk aksi dari onklik button di MainActivity
     */
    private final Messenger mMessenger = new Messenger(new IncomingHandler(this));

    static class IncomingHandler extends Handler {

        private final WeakReference<MediaPlayerCallback> mediaPlayerCallbackWeakReference;

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
