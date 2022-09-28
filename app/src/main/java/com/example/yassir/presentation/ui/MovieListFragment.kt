package com.example.yassir.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.yassir.R
import com.example.yassir.data.model.movie.Movie
import com.example.yassir.databinding.FragmentMovieListBinding
import com.example.yassir.presentation.MainActivityViewModel
import com.example.yassir.presentation.UiEvent
import com.example.yassir.presentation.adapter.PopularPagingAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieListFragment : Fragment(), PopularPagingAdapter.Interaction {

    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainActivityViewModel by activityViewModels()

    private lateinit var popularPagingAdapter: PopularPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.list.apply {
            popularPagingAdapter = PopularPagingAdapter(this@MovieListFragment)
            adapter = popularPagingAdapter
        }

        binding.retryButton.setOnClickListener {
            popularPagingAdapter.retry()
        }


        // Collect from the PagingData Flow in the ViewModel, and submit it to the
        // PagingDataAdapter.
        viewLifecycleOwner.lifecycleScope.launch {
            // We repeat on the STARTED lifecycle because an Activity may be PAUSED
            // but still visible on the screen, for example in a multi window app
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                /*Submit Movie List to Recycler View Adapter*/
                launch {
                    viewModel.discoverMovies.collectLatest { pagingData ->
                        popularPagingAdapter.submitData(pagingData)
                    }
                }

                /*Observe changes in paging library load states*/
                launch {
                    popularPagingAdapter.loadStateFlow.collectLatest { loadStates ->
                        // Show loading spinner during initial load or refresh.
                        binding.progressBar.isVisible = loadStates.refresh is LoadState.Loading

                        // Show the retry state if initial load or refresh fails.
                        binding.retryButton.isVisible =
                            loadStates.refresh is LoadState.Error && popularPagingAdapter.itemCount == 0

                        binding.tvEmptyList.isVisible =
                            loadStates.refresh is LoadState.Error && popularPagingAdapter.itemCount == 0
                    }
                }

                launch {
                    viewModel.uiState.collect { uiState ->
                        binding.progressBar.isVisible = uiState.loading
                    }
                }

                /*Collect One time  Events*/
                launch {
                    viewModel.uiEvent.collect { event ->
                        when (event) {
                            is UiEvent.ShowMessage -> {
                                Snackbar.make(binding.list, event.text, Snackbar.LENGTH_LONG).show()
                            }
                            UiEvent.NavigateToDetails -> {
                                if (findNavController().currentDestination?.id == R.id.movieListFragment) {
                                    val action =
                                        MovieListFragmentDirections.actionMovieListFragmentToMovieDetailsFragment()
                                    findNavController().navigate(action)
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*Get Movie Details*/
    override fun onMovieSelected(item: Movie) {
        viewModel.getMovieDetail(item.id)
    }
}