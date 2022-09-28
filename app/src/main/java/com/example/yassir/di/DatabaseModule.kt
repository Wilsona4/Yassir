package com.example.yassir.di

import android.content.Context
import androidx.room.Room
import com.example.yassir.data.local.MovieDatabase
import com.example.yassir.data.local.MovieDatabase.Companion.MOVIE_DATABASE
import com.example.yassir.data.local.converter.Converter
import com.example.yassir.data.local.dao.MovieDao
import com.example.yassir.data.local.dao.RemoteKeysDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providesMovieDatabase(
        @ApplicationContext context: Context,
        converter: Converter
    ): MovieDatabase = Room.databaseBuilder(
        context,
        MovieDatabase::class.java,
        MOVIE_DATABASE
    )
        .addTypeConverter(converter)
        .build()

    @Singleton
    @Provides
    fun providesMovieDAO(database: MovieDatabase): MovieDao = database.movieDao()

    @Singleton
    @Provides
    fun providesRemoteKeysDAO(database: MovieDatabase): RemoteKeysDao = database.remoteKeysDao()
}
