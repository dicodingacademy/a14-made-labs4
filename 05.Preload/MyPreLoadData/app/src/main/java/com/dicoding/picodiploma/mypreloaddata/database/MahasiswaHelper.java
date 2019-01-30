package com.dicoding.picodiploma.mypreloaddata.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.dicoding.picodiploma.mypreloaddata.model.MahasiswaModel;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.dicoding.picodiploma.mypreloaddata.database.DatabaseContract.MahasiswaColumns.NAMA;
import static com.dicoding.picodiploma.mypreloaddata.database.DatabaseContract.MahasiswaColumns.NIM;
import static com.dicoding.picodiploma.mypreloaddata.database.DatabaseContract.TABLE_NAME;

/**
 * Created by dicoding on 12/1/2016.
 */

public class MahasiswaHelper {

    private DatabaseHelper dataBaseHelper;
    private static MahasiswaHelper INSTANCE;

    private SQLiteDatabase database;

    public MahasiswaHelper(Context context) {
        dataBaseHelper = new DatabaseHelper(context);
    }

    public static MahasiswaHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MahasiswaHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }

    public void close() {
        dataBaseHelper.close();

        if (database.isOpen())
            database.close();
    }

    /**
     * Gunakan method ini untuk cari NIM berdasarkan nama mahasiswa
     *
     * @param nama nama yang dicari
     * @return NIM dari mahasiswa
     */
    public ArrayList<MahasiswaModel> getDataByName(String nama) {
        Cursor cursor = database.query(TABLE_NAME, null, NAMA + " LIKE ?", new String[]{nama}, null, null, _ID + " ASC", null);
        cursor.moveToFirst();
        ArrayList<MahasiswaModel> arrayList = new ArrayList<>();
        MahasiswaModel mahasiswaModel;
        if (cursor.getCount() > 0) {
            do {
                mahasiswaModel = new MahasiswaModel();
                mahasiswaModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                mahasiswaModel.setName(cursor.getString(cursor.getColumnIndexOrThrow(NAMA)));
                mahasiswaModel.setNim(cursor.getString(cursor.getColumnIndexOrThrow(NIM)));

                arrayList.add(mahasiswaModel);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    /**
     * Gunakan method ini untuk mendapatkan semua data mahasiswa
     *
     * @return hasil query mahasiswa model di dalam arraylist
     */
    public ArrayList<MahasiswaModel> getAllData() {
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, _ID + " ASC", null);
        cursor.moveToFirst();
        ArrayList<MahasiswaModel> arrayList = new ArrayList<>();
        MahasiswaModel mahasiswaModel;
        if (cursor.getCount() > 0) {
            do {
                mahasiswaModel = new MahasiswaModel();
                mahasiswaModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                mahasiswaModel.setName(cursor.getString(cursor.getColumnIndexOrThrow(NAMA)));
                mahasiswaModel.setNim(cursor.getString(cursor.getColumnIndexOrThrow(NIM)));


                arrayList.add(mahasiswaModel);
                cursor.moveToNext();


            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    /**
     * Gunakan method ini untuk query insert
     *
     * @param mahasiswaModel inputan model mahasiswa
     * @return id dari data mahasiswa yang baru saja dimasukkan
     */
    public long insert(MahasiswaModel mahasiswaModel) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(NAMA, mahasiswaModel.getName());
        initialValues.put(NIM, mahasiswaModel.getNim());
        return database.insert(TABLE_NAME, null, initialValues);
    }

    /**
     * Gunakan method ini untuk memulai sesi query transaction
     */
    public void beginTransaction() {
        database.beginTransaction();
    }

    /**
     * Gunakan method ini jika query transaction berhasil, jika error jangan panggil method ini
     */
    public void setTransactionSuccess() {
        database.setTransactionSuccessful();
    }

    /**
     * Gunakan method ini untuk mengakhiri sesi query transaction
     */
    public void endTransaction() {
        database.endTransaction();
    }

    /**
     * Gunakan method ini untuk query insert di dalam transaction
     *
     * @param mahasiswaModel inputan model mahasiswa
     */
    public void insertTransaction(MahasiswaModel mahasiswaModel) {
        String sql = "INSERT INTO " + TABLE_NAME + " (" + NAMA + ", " + NIM
                + ") VALUES (?, ?)";
        SQLiteStatement stmt = database.compileStatement(sql);
        stmt.bindString(1, mahasiswaModel.getName());
        stmt.bindString(2, mahasiswaModel.getNim());
        stmt.execute();
        stmt.clearBindings();

    }

    /**
     * Gunakan method ini untuk update data mahasiswa yang ada di dalam database
     *
     * @param mahasiswaModel inputan model mahasiswa
     * @return jumlah mahasiswa yang ter-update
     */
    public int update(MahasiswaModel mahasiswaModel) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(NAMA, mahasiswaModel.getName());
        initialValues.put(NIM, mahasiswaModel.getNim());
        return database.update(TABLE_NAME, initialValues, _ID + "= '" + mahasiswaModel.getId() + "'", null);
    }

    /**
     * Gunakan method ini untuk menghapus data mahasiswa yang ada di dalam database
     *
     * @param id id mahasiswa yang akan di hapus
     * @return jumlah mahasiswa yang di-delete
     */
    public int delete(int id) {
        return database.delete(TABLE_NAME, _ID + " = '" + id + "'", null);
    }
}
