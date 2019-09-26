package com.dicoding.picodiploma.consumerapp.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by dicoding on 12/13/2016.
 */

@Parcelize
class NoteItem (
        var id: Int = 0,
        var title: String? = null,
        var description: String? = null,
        var date: String? = null
): Parcelable