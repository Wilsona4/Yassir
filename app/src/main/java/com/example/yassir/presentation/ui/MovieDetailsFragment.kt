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
import coil.load
import com.example.yassir.databinding.FragmentMovieDetailsBinding
import com.example.yassir.presentation.MainActivityViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            // We repeat on the STARTED lifecycle because an Activity may be PAUSED
            // but still visible on the screen, for example in a multi window app
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collectLatest { uiState ->
                        binding.progressBar.isVisible = uiState.loading

                        uiState.moviesDetail?.let { moviesDetail ->
                            binding.apply {
                                tvTitle.text = moviesDetail.title
                                tvRating.text = moviesDetail.vote_average.toString()
                                tvReleaseDate.text = moviesDetail.release_date
                                tvPopularity.text = moviesDetail.popularity.toString()
                                tvVoteCount.text = moviesDetail.vote_count.toString()
                                tvOverview.text = moviesDetail.overview
                                ivBackdrop.load("$imageBaseUrl${moviesDetail.backdrop_path}")
                                ivPoster.load("$imageBaseUrl${moviesDetail.poster_path}")
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


    companion object {
        const val imageBaseUrl = "https://image.tmdb.org/t/p/w780"
    }
}