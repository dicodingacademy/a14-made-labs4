package com.dicoding.picodiploma.mysharedpreference

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class UserModel (
        var name: String? = null,
        var email: String? = null,
        var age: Int = 0,
        var phoneNumber: String? = null,
        var isLove: Boolean = false
) : Parcelable