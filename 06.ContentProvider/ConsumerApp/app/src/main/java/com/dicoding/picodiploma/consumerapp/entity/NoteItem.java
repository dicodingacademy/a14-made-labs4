package com.dicoding.picodiploma.consumerapp.entity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.dicoding.picodiploma.consumerapp.db.DatabaseContract;

import static com.dicoding.picodiploma.consumerapp.db.DatabaseContract.getColumnInt;
import static com.dicoding.picodiploma.consumerapp.db.DatabaseContract.getColumnString;

/**
 * Created by dicoding on 12/13/2016.
 */

public class NoteItem implements Parcelable {
    private int id;
    private String title, description, date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.date);
    }

    public NoteItem() {
    }

    public NoteItem(int id, String title, String description, String date) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public NoteItem(Cursor cursor) {
        this.id = getColumnInt(cursor, DatabaseContract.NoteColumns._ID);
        this.title = getColumnString(cursor, DatabaseContract.NoteColumns.TITLE);
        this.description = getColumnString(cursor, DatabaseContract.NoteColumns.DESCRIPTION);
        this.date = getColumnString(cursor, DatabaseContract.NoteColumns.DATE);
    }

    private NoteItem(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
        this.date = in.readString();
    }

    public static final Creator<NoteItem> CREATOR = new Creator<NoteItem>() {
        @Override
        public NoteItem createFromParcel(Parcel source) {
            return new NoteItem(source);
        }


        @Override
        public NoteItem[] newArray(int size) {
            return new NoteItem[size];
        }
    };
}