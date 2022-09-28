package com.example.yassir.data.local.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.yassir.data.local.MovieDatabase
import com.example.yassir.data.local.model.RemoteKeys
import com.example.yassir.data.model.movie.Movie
import com.example.yassir.data.remote.ApiService
import retrofit2.HttpException

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val apiService: ApiService,
    private val movieDatabase: MovieDatabase
) : RemoteMediator<Int, Movie>() {

    override suspend fun initialize(): InitializeAction {
        // Require that remote REFRESH is launched on initial load and succeeds before launching
        // remote PREPEND / APPEND.
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, Movie>
    ): MediatorResult {
        try {
            // Get the closest item from PagingState that we want to load data around.
            val pageKey = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: MOVIES_STARTING_PAGE_INDEX
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    // If remoteKeys is null, that means the refresh result is not in the database yet.
                    // We can return Success with `endOfPaginationReached = false` because Paging
                    // will call this method again if RemoteKeys becomes non-null.

                    // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                    // the end of pagination for prepend.
                    val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    prevKey
                }

                LoadType.APPEND -> {

                    val remoteKeys = getRemoteKeyForLastItem(state)

                    // If remoteKeys is NOT NULL but its nextKey is null, that means we've reached
                    // the end of pagination for append.
                    val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )

                    nextKey
                }
            }

            val apiResponse = apiService.discoverMovies(page = pageKey)

            val movies = apiResponse.results

            val endOfPaginationReached = movies.isEmpty()

            movieDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    movieDatabase.remoteKeysDao().clearRemoteKeys()
                    movieDatabase.movieDao().clearMovies()
                }
                val prevKey = if (pageKey == MOVIES_STARTING_PAGE_INDEX) null else pageKey.minus(1)
                val nextKey = if (endOfPaginationReached) null else pageKey.plus(1)

                val keys = movies.map {
                    RemoteKeys(movieId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                movieDatabase.remoteKeysDao().insertAll(keys)
                movieDatabase.movieDao().insertAllMovies(movies)
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Movie>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { user ->
            // Get the remote keys of the last item retrieved
            movieDatabase.remoteKeysDao().remoteKeysRepoId(user.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Movie>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { user ->
            // Get the remote keys of the first items retrieved
            movieDatabase.remoteKeysDao().remoteKeysRepoId(user.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Movie>
    ): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { userId ->
                movieDatabase.remoteKeysDao().remoteKeysRepoId(userId)
            }
        }
    }

    companion object {
        private const val MOVIES_STARTING_PAGE_INDEX = 1
    }
}