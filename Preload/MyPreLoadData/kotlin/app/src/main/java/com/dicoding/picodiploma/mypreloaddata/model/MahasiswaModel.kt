package com.dicoding.picodiploma.mypreloaddata.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by dicoding on 12/6/2016.
 */
@Parcelize
data class MahasiswaModel(
        var id: Int = 0,
        var name: String? = null,
        var nim: String? = null
) : Parcelable