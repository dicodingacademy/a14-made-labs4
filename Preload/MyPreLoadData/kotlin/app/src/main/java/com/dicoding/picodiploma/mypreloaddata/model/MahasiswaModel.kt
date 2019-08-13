package com.dicoding.picodiploma.mypreloaddata.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by dicoding on 12/6/2016.
 */
@Parcelize
data class MahasiswaModel(
        var id: Int = 0,
        var name: String? = null,
        var nim: String? = null
) : Parcelable {
    constructor(name: String, nim: String) : this() {
        this.name = name
        this.nim = nim
    }
}