package com.example.yassir.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.yassir.data.local.MovieDatabase
import com.example.yassir.data.local.mediator.MovieRemoteMediator
import com.example.yassir.data.model.details.MovieDetail
import com.example.yassir.data.model.movie.Movie
import com.example.yassir.data.remote.ApiService
import com.example.yassir.di.Dispatcher
import com.example.yassir.di.MyDispatchers
import com.example.yassir.domain.repository.IMovieRepository
import com.example.yassir.util.Resource
import com.example.yassir.util.handleHttpException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val movieDatabase: MovieDatabase,
    @Dispatcher(MyDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : IMovieRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun discoverMovies(): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(NETWORK_PAGE_SIZE),
        remoteMediator = MovieRemoteMediator(apiService, movieDatabase)
    ) {
        movieDatabase.movieDao().readMovies()
    }.flow

    override fun getMovieDetail(movieId: Int): Flow<Resource<MovieDetail>> = flow {

        emit(Resource.Loading())
        val remoteData = try {
            apiService.getMovieDetail(movieId)
        } catch (e: HttpException) {
            val errorResponse = e.handleHttpException()
            emit(Resource.Error(message = errorResponse?.status_message ?: "Something went wrong"))
            null
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message ?: "Something went wrong"))
            null
        }

        remoteData?.let {
            emit(Resource.Success(it))
        }

    }.flowOn(ioDispatcher)


    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }
}