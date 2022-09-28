package com.example.yassir.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.yassir.data.model.details.MovieDetail
import com.example.yassir.data.model.movie.Movie
import com.example.yassir.domain.repository.IMovieRepository
import com.example.yassir.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val movieRepository: IMovieRepository
) : ViewModel() {

    // UI state exposed to the UI
    private val _uiState = MutableStateFlow(MovieDetailUiState())
    val uiState: StateFlow<MovieDetailUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()


    /*Get Movie List*/
    val discoverMovies: Flow<PagingData<Movie>> =
        movieRepository.discoverMovies().cachedIn(viewModelScope)


    /*Get Movie Detail*/
    fun getMovieDetail(movieId: Int) {

        _uiState.update { it.copy(loading = true) }

        viewModelScope.launch {
            movieRepository.getMovieDetail(movieId).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                loading = false,
                                moviesDetail = result.data,
                            )
                        }
                        _uiEvent.send(UiEvent.NavigateToDetails)
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                loading = false, moviesDetail = null
                            )
                        }
                        _uiEvent.send(UiEvent.ShowMessage(result.message))

                    }
                    is Resource.Loading -> {
                        _uiState.update { it.copy(loading = true) }
                    }
                }
            }
        }
    }
}

data class MovieDetailUiState(
    val moviesDetail: MovieDetail? = null,
    val loading: Boolean = false,
)

sealed class UiEvent {
    object NavigateToDetails : UiEvent()
    data class ShowMessage(val text: String) : UiEvent()
}