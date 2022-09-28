package com.example.yassir.data.model.details

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BelongsToCollection(
    val backdrop_path: String? = null,
    val id: Int,
    val name: String? = null,
    val poster_path: String? = null
) : Parcelable