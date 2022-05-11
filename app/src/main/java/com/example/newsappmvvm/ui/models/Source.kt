package com.example.newsappmvvm.ui.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.io.Serializable

@Parcelize
data class Source(

    val id: String?,
    val name: String?
) : Parcelable