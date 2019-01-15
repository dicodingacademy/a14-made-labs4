package com.dicoding.picodiploma.mypreloaddata.services;

import android.content.res.Resources;
import android.os.AsyncTask;
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


public class LoadDataAsync extends AsyncTask<Void, Integer, Boolean> {
    private final String TAG = LoadDataAsync.class.getSimpleName();
    private MahasiswaHelper mahasiswaHelper;
    private AppPreference appPreference;
    private WeakReference<LoadDataCallback> weakCallback;
    private WeakReference<Resources> weakResources;
    double progress;
    double maxprogress = 100;

    LoadDataAsync(MahasiswaHelper mahasiswaHelper, AppPreference preference, LoadDataCallback callback, Resources resources) {
        this.mahasiswaHelper = mahasiswaHelper;
        this.appPreference = preference;
        this.weakCallback = new WeakReference<>(callback);
        this.weakResources = new WeakReference<>(resources);
    }

    /*
    Persiapan sebelum proses dimulai
    Berjalan di Main Thread
     */
    @Override
    protected void onPreExecute() {
        weakCallback.get().onPreLoad();
    }

    /*
    Proses background terjadi di method doInBackground
     */
    @Override
    protected Boolean doInBackground(Void... params) {
        // Panggil preference first run
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

            progress = 30;
            publishProgress((int) progress);
            Double progressMaxInsert = 80.0;
            Double progressDiff = (progressMaxInsert - progress) / mahasiswaModels.size();

            boolean isInsertSuccess;

            /*
             * Gunakan kode ini untuk query insert yang transactional
             * Begin Transaction
             */
            try {

                mahasiswaHelper.beginTransaction();

                for (MahasiswaModel model : mahasiswaModels) {
                    //Jika service atau activity dalam keadaan destroy maka akan menghentikan perulangan
                    if (isCancelled()) {
                        break;
                    } else {
                        mahasiswaHelper.insertTransaction(model);
                        progress += progressDiff;
                        publishProgress((int) progress);
                    }
                }

                //Jika service atau activity dalam keadaan destroy maka data insert tidak di essekusi
                if (isCancelled()) {
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

//            try {
//                for (MahasiswaModel model : mahasiswaModels) {
//                    mahasiswaHelper.insert(model);
//                    progress += progressDiff;
//                    publishProgress((int) progress);
//
//                }
//
//                isInsertSuccess = true;
//
//                appPreference.setFirstRun(false);
//
//            } catch (Exception e) {
//                // Jika gagal maka do nothing
//                Log.e(TAG, "doInBackground: Exception");
//                isInsertSuccess = false;
//            }


            // Close helper ketika proses query sudah selesai
            mahasiswaHelper.close();

            publishProgress((int) maxprogress);

            return isInsertSuccess;

        } else {
            try {
                synchronized (this) {
                    this.wait(2000);

                    publishProgress(50);

                    this.wait(2000);
                    publishProgress((int) maxprogress);

                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }
    }

    //Update prosesnya
    @Override
    protected void onProgressUpdate(Integer... values) {
        weakCallback.get().onProgressUpdate(values[0]);
    }

    /*
    Setelah proses selesai
    Berjalan di Main Thread
    */
    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            weakCallback.get().onLoadSuccess();
        } else {
            weakCallback.get().onLoadFailed();
        }

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
            Resources res = weakResources.get();
            InputStream raw_dict = res.openRawResource(R.raw.data_mahasiswa);

            reader = new BufferedReader(new InputStreamReader(raw_dict));
            do {
                line = reader.readLine();
                String[] splitstr = line.split("\t");

                MahasiswaModel mahasiswaModel;

                mahasiswaModel = new MahasiswaModel(splitstr[0], splitstr[1]);
                mahasiswaModels.add(mahasiswaModel);
            } while (line != null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mahasiswaModels;
    }
}
