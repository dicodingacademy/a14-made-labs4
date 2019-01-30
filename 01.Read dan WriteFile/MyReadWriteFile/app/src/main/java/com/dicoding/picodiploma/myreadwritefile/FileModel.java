package com.dicoding.picodiploma.myreadwritefile;

public class FileModel {
    private String filename;
    private String data;

    String getFilename() {
        return filename;
    }

    void setFilename(String filename) {
        this.filename = filename;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
