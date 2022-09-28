package com.example.yassir.data.model.details

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SpokenLanguage(
    val english_name: String? = null,
    val iso_639_1: String? = null,
    val name: String? = null
) : Parcelable