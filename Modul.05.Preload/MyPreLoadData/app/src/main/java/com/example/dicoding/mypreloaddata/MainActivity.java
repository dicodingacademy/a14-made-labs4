package com.example.dicoding.mypreloaddata;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.example.dicoding.mypreloaddata.Database.MahasiswaHelper;
import com.example.dicoding.mypreloaddata.Model.MahasiswaModel;
import com.example.dicoding.mypreloaddata.Prefs.AppPreference;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);

        new LoadData().execute();
    }

    private class LoadData extends AsyncTask<Void, Integer, Void>
    {

        MahasiswaHelper mahasiswaHelper;
        AppPreference appPreference;
        double progress;
        double maxprogress = 100;
        //Before running code in separate thread
        @Override
        protected void onPreExecute()
        {

            mahasiswaHelper = new MahasiswaHelper(MainActivity.this);
            appPreference = new AppPreference(MainActivity.this);
        }

        //The code to be executed in a background thread.
        @Override
        protected Void doInBackground(Void... params)
        {

            Boolean firstRun = appPreference.getFirstRun();
            Log.d("First run",":"+firstRun);
            if (firstRun) {
                ArrayList<MahasiswaModel> mahasiswaModels = preLoadRaw();

                Log.d("size"," "+mahasiswaModels.size());
                progress = 30;

                publishProgress((int)progress);

                mahasiswaHelper.open();

                Double progressMaxInsert = 80.0;
                Double progressDiff = (progressMaxInsert - progress) / mahasiswaModels.size();

                //mahasiswaHelper.insertTransaction(mahasiswaModels);

                for (MahasiswaModel model : mahasiswaModels) {

                    mahasiswaHelper.insert(model);

                    progress += progressDiff;

                    publishProgress((int)progress);

                }



                mahasiswaHelper.close();

                appPreference.setFirstRun(false);

                publishProgress((int)maxprogress);
            }
            else {

                try {
                    synchronized (this) {
                        this.wait(2000);

                        publishProgress(50);

                        this.wait(2000);
                        publishProgress((int)maxprogress);
                    }
                }catch (Exception e){

                }


            }
            return null;
        }

        //Update the progress
        @Override
        protected void onProgressUpdate(Integer... values)
        {
            progressBar.setProgress(values[0]);
        }

        //after executing the code in the thread
        @Override
        protected void onPostExecute(Void result)
        {

            Intent i = new Intent(MainActivity.this,MahasiswaActivity.class);
            startActivity(i);
            finish();
        }
    }

    public ArrayList<MahasiswaModel> preLoadRaw(){

        ArrayList<MahasiswaModel> mahasiswaModels = new ArrayList<>();
        String line = null;
        BufferedReader reader;
        try
        {

            Resources res = getResources();
            InputStream raw_dict = res.openRawResource(R.raw.data_mahasiswa);

            reader = new BufferedReader(new InputStreamReader(raw_dict));
            int count = 0;
            do {
                //Log.d("Raw"+count, line);
                line = reader.readLine();
                String[] splitstr = line.split("\t");


                MahasiswaModel mahasiswaModel;

                mahasiswaModel = new MahasiswaModel(splitstr[0],splitstr[1]);
                mahasiswaModels.add(mahasiswaModel);
                count++;

            }while (line != null);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        return mahasiswaModels;

    }
}
