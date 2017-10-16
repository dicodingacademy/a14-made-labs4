package com.example.dicoding.mypreloaddata.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;


import com.example.dicoding.mypreloaddata.Model.MahasiswaModel;

import java.util.ArrayList;

/**
 * Created by dicoding on 12/1/2016.
 */

public class MahasiswaHelper {
    private static String DATABASE_TABLE = DatabaseHelper.TABLE_NAME;
    private Context context;
    private DatabaseHelper dataBaseHelper;

    private SQLiteDatabase database;


    public MahasiswaHelper(Context context){
        this.context = context;
    }


    public MahasiswaHelper open() throws SQLException {
        dataBaseHelper = new DatabaseHelper(context);
        database = dataBaseHelper.getWritableDatabase();
        return this;
    }


    public void close(){
        dataBaseHelper.close();
    }


    public Cursor searchQueryByName(String query){
        return database.rawQuery("SELECT * FROM "+DATABASE_TABLE+" WHERE "+DatabaseHelper.FIELD_NAMA+" LIKE '%"+query+"%'", null);
    }

    public String getData(String kata){
        String result = "";
        Cursor cursor = searchQueryByName(kata);
        cursor.moveToFirst();
        if (cursor.getCount()>0) {
            result = cursor.getString(2);
            for (; !cursor.isAfterLast(); cursor.moveToNext()) {
                result = cursor.getString(2);
            }
        }
        cursor.close();
        return result;
    }


    public Cursor queryAllData(){
        return database.rawQuery("SELECT * FROM "+DATABASE_TABLE+" ORDER BY "+DatabaseHelper.FIELD_ID+" ASC", null);
    }


    public ArrayList<MahasiswaModel> getAllData(){
        ArrayList<MahasiswaModel> arrayList = new ArrayList<>();
        Cursor cursor = queryAllData();
        cursor.moveToFirst();
        MahasiswaModel mahasiswaModel;
        if (cursor.getCount()>0) {
            do {
                mahasiswaModel = new MahasiswaModel();
                mahasiswaModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_ID)));
                mahasiswaModel.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_NAMA)));
                mahasiswaModel.setNim(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_NIM)));


                arrayList.add(mahasiswaModel);
                cursor.moveToNext();


            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }


    public long insert(MahasiswaModel mahasiswaModel){
        ContentValues initialValues =  new ContentValues();
        initialValues.put(DatabaseHelper.FIELD_NAMA, mahasiswaModel.getName());
        initialValues.put(DatabaseHelper.FIELD_NIM, mahasiswaModel.getNim());
        return database.insert(DATABASE_TABLE, null, initialValues);
    }



    public void beginTransaction(){
        database.beginTransaction();
    }

    public void setTransactionSuccess(){
        database.setTransactionSuccessful();
    }

    public void endTransaction(){
        database.endTransaction();
    }

    public void insertTransaction2(MahasiswaModel mahasiswaModel){

        String sql = "INSERT INTO "+DATABASE_TABLE+" ("+DatabaseHelper.FIELD_NAMA+", "+DatabaseHelper.FIELD_NIM
                +") VALUES (?, ?)";
        SQLiteStatement stmt = database.compileStatement(sql);
        stmt.bindString(1, mahasiswaModel.getName());
        stmt.bindString(2, mahasiswaModel.getNim());
        stmt.execute();
        stmt.clearBindings();

    }

    public void insertTransaction(ArrayList<MahasiswaModel> mahasiswaModels) {
        String sql = "INSERT INTO "+DATABASE_TABLE+" ("+DatabaseHelper.FIELD_NAMA+", "+DatabaseHelper.FIELD_NIM
                +") VALUES (?, ?)";
        database.beginTransaction();

        SQLiteStatement stmt = database.compileStatement(sql);
        for (int i = 0; i < mahasiswaModels.size(); i++) {
            stmt.bindString(1, mahasiswaModels.get(i).getName());
            stmt.bindString(2, mahasiswaModels.get(i).getNim());
            stmt.execute();
            stmt.clearBindings();
        }

        database.setTransactionSuccessful();
        database.endTransaction();

    }

    public void update(MahasiswaModel mahasiswaModel){
        ContentValues args = new ContentValues();
        args.put(DatabaseHelper.FIELD_NAMA, mahasiswaModel.getName());
        args.put(DatabaseHelper.FIELD_NIM, mahasiswaModel.getNim());
        database.update(DATABASE_TABLE, args, DatabaseHelper.FIELD_ID + "= '" + mahasiswaModel.getId() + "'", null);
    }


    public void delete(int id){
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.FIELD_ID + " = '"+id+"'", null);
    }
}
