package com.example.yassir.di

import com.example.yassir.data.repository.MovieRepositoryImpl
import com.example.yassir.domain.repository.IMovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsUserRepository(
        movieRepository: MovieRepositoryImpl
    ): IMovieRepository
}