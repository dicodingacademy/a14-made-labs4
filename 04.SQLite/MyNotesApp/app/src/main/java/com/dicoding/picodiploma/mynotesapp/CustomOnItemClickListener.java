package com.dicoding.picodiploma.mynotesapp;

import android.view.View;

/**
 * Created by sidiqpermana on 10/29/16.
 */

public class CustomOnItemClickListener implements View.OnClickListener {
    private final int position;
    private final OnItemClickCallback onItemClickCallback;
    public CustomOnItemClickListener(int position, OnItemClickCallback onItemClickCallback) {
        this.position = position;
        this.onItemClickCallback = onItemClickCallback;
    }
    @Override
    public void onClick(View view) {
        onItemClickCallback.onItemClicked(view, position);
    }
    public interface OnItemClickCallback {
        void onItemClicked(View view, int position);
    }
}
