package com.example.yassir.data.model.details

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductionCountry(
    val iso_3166_1: String? = null,
    val name: String? = null
) : Parcelable