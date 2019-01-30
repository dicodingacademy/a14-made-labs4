package com.dicoding.picodiploma.mypreloaddata.services;

interface LoadDataCallback {
    void onPreLoad();
    void onProgressUpdate(long progress);
    void onLoadSuccess();
    void onLoadFailed();
    void onLoadCancel();
}
