package com.dicoding.picodiploma.mypreloaddata.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.dicoding.picodiploma.mypreloaddata.R;
import com.dicoding.picodiploma.mypreloaddata.database.MahasiswaHelper;
import com.dicoding.picodiploma.mypreloaddata.model.MahasiswaModel;
import com.dicoding.picodiploma.mypreloaddata.prefs.AppPreference;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataManagerService extends Service {
    private final String TAG = DataManagerService.class.getSimpleName();
    private Messenger mActivityMessenger;
    private LoadDataAsync loadData;

    public static final int PREPARATION_MESSAGE = 0;
    public static final int UPDATE_MESSAGE = 1;
    public static final int SUCCESS_MESSAGE = 2;
    public static final int FAILED_MESSAGE = 3;
    public static final int CANCEL_MESSAGE = 4;
    public static final String ACTIVITY_HANDLER = "activity_handler";

    @Override
    public void onCreate() {
        super.onCreate();

        loadData = new LoadDataAsync(this, myCallback);

        Log.d(TAG, "onCreate: ");
    }

    /*
    Ketika semua ikatan sudah di lepas maka ondestroy akan secara otomatis dipanggil
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        loadData.cancel();
        Log.d(TAG, "onDestroy: ");
    }

    /*
    Method yang akan dipanggil ketika service diikatkan ke activity
    */
    @Override
    public IBinder onBind(Intent intent) {

        mActivityMessenger = intent.getParcelableExtra(ACTIVITY_HANDLER);

        loadData.execute();
        return mActivityMessenger.getBinder();
    }

    /*
    Method yang akan dipanggil ketika service dilepas dari activity
     */
    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: ");
        loadData.cancel();
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

    private final LoadDataCallback myCallback = new LoadDataCallback() {
        @Override
        public void onPreLoad() {
            sendMessage(PREPARATION_MESSAGE);
        }

        @Override
        public void onLoadCancel() {
            sendMessage(CANCEL_MESSAGE);
        }

        @Override
        public void onProgressUpdate(long progress) {
            try {
                Message message = Message.obtain(null, UPDATE_MESSAGE);
                Bundle bundle = new Bundle();
                bundle.putLong("KEY_PROGRESS", progress);
                message.setData(bundle);
                mActivityMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onLoadSuccess() {
            sendMessage(SUCCESS_MESSAGE);
        }

        @Override
        public void onLoadFailed() {
            sendMessage(FAILED_MESSAGE);
        }
    };

    public void sendMessage(int messageStatus) {
        Message message = Message.obtain(null, messageStatus);
        try {
            mActivityMessenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static class LoadDataAsync {
        private final String TAG = LoadDataAsync.class.getSimpleName();
        private final WeakReference<Context> context;
        private final WeakReference<LoadDataCallback> weakCallback;
        private static final double MAX_PROGRESS = 100;
        private boolean isCancelled = false;
        private boolean isInsertSuccess = false;

        LoadDataAsync(Context context, LoadDataCallback callback) {
            this.context = new WeakReference<>(context);
            this.weakCallback = new WeakReference<>(callback);
        }

        void execute() {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());

            /*
            Persiapan sebelum proses dimulai
            Berjalan di Main Thread
            */
            weakCallback.get().onPreLoad();

            /*
            Proses background terjadi di method doInBackground
            */
            executor.execute(() -> {
                // Panggil preference first run
                MahasiswaHelper mahasiswaHelper = MahasiswaHelper.getInstance(context.get());
                AppPreference appPreference = new AppPreference(context.get());
                Boolean firstRun = appPreference.getFirstRun();
                /*
                 * Jika first run true maka melakukan proses pre load,
                 * Jika first run false maka akan langsung menuju home
                 */
                if (firstRun) {
            /*
            Load raw data dari file txt ke dalam array model mahasiswa
            */
                    ArrayList<MahasiswaModel> mahasiswaModels = preLoadRaw();

                    mahasiswaHelper.open();

                    double progress = 30;
                    weakCallback.get().onProgressUpdate((int) progress);
                    double progressMaxInsert = 80.0;
                    double progressDiff = (progressMaxInsert - progress) / mahasiswaModels.size();

                    /*
                     * Gunakan kode ini untuk query insert yang transactional
                     * Begin Transaction
                     */
                    try {

                        mahasiswaHelper.beginTransaction();

                        for (MahasiswaModel model : mahasiswaModels) {
                            //Jika service atau activity dalam keadaan destroy maka akan menghentikan perulangan
                            if (isCancelled) {
                                break;
                            } else {
                                mahasiswaHelper.insertTransaction(model);
                                progress += progressDiff;
                                weakCallback.get().onProgressUpdate((int) progress);
                            }
                        }

                        //Jika service atau activity dalam keadaan destroy maka data insert tidak di essekusi
                        if (isCancelled) {
                            isInsertSuccess = false;
                            appPreference.setFirstRun(true);
                            weakCallback.get().onLoadCancel();
                        } else {
                            // Jika semua proses telah di set success maka akan di commit ke database
                            mahasiswaHelper.setTransactionSuccess();
                            isInsertSuccess = true;

                    /*
                     Set preference first run ke false
                     Agar proses preload tidak dijalankan untuk kedua kalinya
                     */
                            appPreference.setFirstRun(false);
                        }
                    } catch (Exception e) {
                        // Jika gagal maka do nothing
                        Log.e(TAG, "doInBackground: Exception");
                        isInsertSuccess = false;

                    } finally {
                        // Transaction
                        mahasiswaHelper.endTransaction();
                    }

                    /*
                     * ==============================================================
                     * End Transaction
                     * ==============================================================
                     */



                /*
                Gunakan ini untuk insert query dengan menggunakan standar query
                 */

//                try {
//                    for (MahasiswaModel model : mahasiswaModels) {
//                        mahasiswaHelper.insert(model);
//                        progress += progressDiff;
//                        publishProgress((int) progress);
//
//                    }
//
//                    isInsertSuccess = true;
//
//                    appPreference.setFirstRun(false);
//
//                } catch (Exception e) {
//                    // Jika gagal maka do nothing
//                    Log.e(TAG, "doInBackground: Exception");
//                    isInsertSuccess = false;
//                }


                    // Close helper ketika proses query sudah selesai
                    mahasiswaHelper.close();
                    weakCallback.get().onProgressUpdate((int) MAX_PROGRESS);
                } else {
                    try {
                        synchronized (this) {
                            this.wait(2000);

                            weakCallback.get().onProgressUpdate(50);

                            this.wait(2000);
                            weakCallback.get().onProgressUpdate((int) MAX_PROGRESS);
                            isInsertSuccess = true;
                        }
                    } catch (Exception e) {
                        isInsertSuccess = false;
                    }
                }

                handler.post(() -> {
                    /*
                    Setelah proses selesai
                    Berjalan di Main Thread
                    */

                    if (isInsertSuccess) {
                        weakCallback.get().onLoadSuccess();
                    } else {
                        weakCallback.get().onLoadFailed();
                    }
                });
            });
        }

        void cancel() {
            isCancelled = true;
        }

        /**
         * Parsing raw data text berupa data menjadi array mahasiswa
         *
         * @return array model dari semua mahasiswa
         */
        private ArrayList<MahasiswaModel> preLoadRaw() {
            ArrayList<MahasiswaModel> mahasiswaModels = new ArrayList<>();
            String line;
            BufferedReader reader;
            try {
                InputStream raw_dict = context.get().getResources().openRawResource(R.raw.data_mahasiswa);

                reader = new BufferedReader(new InputStreamReader(raw_dict));
                do {
                    line = reader.readLine();
                    String[] splitstr = line.split("\t");

                    MahasiswaModel mahasiswaModel = new MahasiswaModel();
                    mahasiswaModel.setName(splitstr[0]);
                    mahasiswaModel.setNim(splitstr[1]);
                    mahasiswaModels.add(mahasiswaModel);
                } while (line != null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mahasiswaModels;
        }


    }
}

interface LoadDataCallback {
    void onPreLoad();

    void onProgressUpdate(long progress);

    void onLoadSuccess();

    void onLoadFailed();

    void onLoadCancel();
}