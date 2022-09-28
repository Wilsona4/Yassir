package com.example.yassir.data.model.movie

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "movie_table")
data class Movie(
    val adult: Boolean,
    val backdrop_path: String,
    val genre_ids: List<Int>? = null,
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
) : Parcelable