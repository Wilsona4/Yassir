package com.example.yassir.data.local.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.example.yassir.data.model.movie.Movie

@Dao
interface MovieDao {

    /*Add Movie to Database*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMovies(user: List<Movie>)

    /*Get Movie in the Database*/
    @Transaction
    @Query("SELECT * FROM movie_table")
    fun readMovies(): PagingSource<Int, Movie>

    /*Delete Movie in the Database*/
    @Query("DELETE FROM movie_table")
    suspend fun clearMovies()
}