package com.example.yassir.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.yassir.data.local.dao.MovieDao
import com.example.yassir.data.local.dao.RemoteKeysDao
import com.example.yassir.data.local.model.RemoteKeys
import com.example.yassir.data.local.converter.Converter
import com.example.yassir.data.model.details.MovieDetail
import com.example.yassir.data.model.movie.Movie

@Database(
    entities = [Movie::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        var MOVIE_DATABASE = "movie_database"
    }
}