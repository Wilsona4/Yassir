package com.example.yassir.data.model.details

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductionCompany(
    val id: Int,
    val logo_path: String? = null,
    val name: String? = null,
    val origin_country: String? = null
) : Parcelable