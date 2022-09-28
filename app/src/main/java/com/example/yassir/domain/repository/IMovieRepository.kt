package com.example.yassir.domain.repository

import androidx.paging.PagingData
import com.example.yassir.data.model.details.MovieDetail
import com.example.yassir.data.model.movie.Movie
import com.example.yassir.util.Resource
import kotlinx.coroutines.flow.Flow

interface IMovieRepository {
    fun discoverMovies(): Flow<PagingData<Movie>>
    fun getMovieDetail(movieId: Int): Flow<Resource<MovieDetail>>
}