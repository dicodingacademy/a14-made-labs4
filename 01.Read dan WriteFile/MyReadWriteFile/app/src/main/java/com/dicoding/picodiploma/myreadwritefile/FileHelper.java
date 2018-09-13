package com.dicoding.picodiploma.myreadwritefile;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by dicoding on 11/23/2016.
 */

public class FileHelper {

    private static final String TAG = FileHelper.class.getName();

    /**
     * Method yang digunakan untuk menuliskan data berupa string menjadi file
     *
     * @param filename nama file
     * @param data     data dalam bentuk file
     * @param context  context aplikasi
     */
    static void writeToFile(String filename, String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e(TAG, "File write failed :", e);
        }
    }

    /**
     * Method yang digunakan untuk membaca data dari file
     *
     * @param context  context aplikasi
     * @param filename nama file
     * @return data berupa string
     */
    static String readFromFile(Context context, String filename) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(filename);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found :", e);
        } catch (IOException e) {
            Log.e(TAG, "Can not read file :", e);
        }

        return ret;
    }
}
