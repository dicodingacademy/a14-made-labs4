package com.dicoding.picodiploma.mypreloaddata.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.dicoding.picodiploma.mypreloaddata.MainActivity;
import com.dicoding.picodiploma.mypreloaddata.database.MahasiswaHelper;
import com.dicoding.picodiploma.mypreloaddata.prefs.AppPreference;

public class DataManagerService extends Service {

    private String TAG = DataManagerService.class.getSimpleName();

    private DataManagerBinder mBinder = new DataManagerBinder();

    private LoadDataAsync loadData;

    private Messenger mActivityMessenger;

    public static final String ACTIVITY_HANDLER = "activity_handler";

    @Override
    public void onCreate() {
        super.onCreate();

        MahasiswaHelper mahasiswaHelper = MahasiswaHelper.getInstance(getApplicationContext());
        AppPreference appPreference = new AppPreference(getApplicationContext());

        loadData = new LoadDataAsync(mahasiswaHelper, appPreference, myCallback, getResources());

        Log.d(TAG, "onCreate: ");
    }

    /*
    Ketika semua ikatan sudah di lepas maka ondestroy akan secara otomatis dipanggil
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    /*
    Method yang akan dipanggil ketika service diikatkan ke activity
    */
    @Override
    public IBinder onBind(Intent intent) {

        mActivityMessenger = intent.getParcelableExtra(ACTIVITY_HANDLER);

        loadData.execute();
        return mBinder;
    }

    /*
    Method yang akan dipanggil ketika service dilepas dari activity
     */
    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: ");
        return super.onUnbind(intent);
    }

    /*
    Method yang akan dipanggil ketika service diikatkan kembali
     */
    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d(TAG, "onRebind: ");
    }

    LoadDataCallback myCallback = new LoadDataCallback() {
        @Override
        public void onPreLoad() {

        }

        @Override
        public void onProgressUpdate(long progress) {
            try {
                Message message = Message.obtain(null, MainActivity.UPDATE_MESSAGE);
                Bundle bundle = new Bundle();
                bundle.putLong("KEY_PROGRESS", progress);
                message.setData(bundle);
                mActivityMessenger.send(message);
            } catch (RemoteException e) {
                //
            }
        }

        @Override
        public void onLoadSuccess() {
            Message message = Message.obtain(null, MainActivity.SUCCESS_MESSAGE);
            try {
                mActivityMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onLoadFailed() {
            Message message = Message.obtain(null, MainActivity.FAILED_MESSAGE);
            try {
                mActivityMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    public class DataManagerBinder extends Binder {
        public DataManagerService getService() {
            return DataManagerService.this;
        }
    }

}
