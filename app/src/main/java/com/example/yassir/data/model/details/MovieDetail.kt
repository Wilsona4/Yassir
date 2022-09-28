package com.example.yassir.data.model.details

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieDetail(
    val adult: Boolean,
    val backdrop_path: String? = null,
    val belongs_to_collection: BelongsToCollection? = null,
    val budget: Int,
    val genres: List<Genre>? = null,
    val homepage: String? = null,
    val id: Int,
    val imdb_id: String? = null,
    val original_language: String? = null,
    val original_title: String? = null,
    val overview: String? = null,
    val popularity: Double? = null,
    val poster_path: String? = null,
    val production_companies: List<ProductionCompany>? = null,
    val production_countries: List<ProductionCountry>? = null,
    val release_date: String? = null,
    val revenue: Long? = null,
    val runtime: Int? = null,
    val spoken_languages: List<SpokenLanguage>? = null,
    val status: String? = null,
    val tagline: String? = null,
    val title: String? = null,
    val video: Boolean,
    val vote_average: Double? = null,
    val vote_count: Int? = null
) : Parcelable