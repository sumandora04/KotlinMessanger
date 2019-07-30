package com.notepoint4ugmail.kotlinmessanger.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val uid: String = "",
    val userName: String = "",
    val email: String = "",
    val profileImageUrl: String = ""
) : Parcelable {
}