package com.dicoding.picodiploma.mynotesapp

import android.view.View

/**
 * Created by sidiqpermana on 10/29/16.
 */

class CustomOnItemClickListener(private val position: Int, private val onItemClickCallback: OnItemClickCallback) : View.OnClickListener {
    override fun onClick(view: View) {
        onItemClickCallback.onItemClicked(view, position)
    }

    interface OnItemClickCallback {
        fun onItemClicked(view: View, position: Int)
    }
}
