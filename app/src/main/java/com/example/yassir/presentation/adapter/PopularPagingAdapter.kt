package com.example.yassir.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.yassir.presentation.ui.MovieDetailsFragment.Companion.imageBaseUrl
import com.example.yassir.R
import com.example.yassir.data.model.movie.Movie
import com.example.yassir.databinding.GalleryRvItemBinding

class PopularPagingAdapter(private val interaction: Interaction? = null) :
    PagingDataAdapter<Movie, PopularPagingAdapter.PagingViewHolder>(diffCallback) {

    private val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagingViewHolder {
        val binding = GalleryRvItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PagingViewHolder(
            binding,
            interaction
        )
    }

    override fun onBindViewHolder(holder: PagingViewHolder, position: Int) {
        val tile = getItem(position)
        if (tile != null) {
            holder.bind(tile)
        }
    }

    class PagingViewHolder constructor(
        private var binding: GalleryRvItemBinding,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onMovieSelected(movie)
            }

            binding.apply {
                name.text = movie.title
                overview.text = movie.overview

                val imageUrl = "$imageBaseUrl${movie.poster_path}"

                image.load(imageUrl) {
                    crossfade(true)
                    placeholder(R.drawable.image_6)
                    error(R.drawable.image_6)
                }

                movie.vote_average.let {
                    favourite.isChecked = it >= 7
                }

                tvRating.text = movie.vote_average.toString()
            }

        }
    }

    interface Interaction {
        fun onMovieSelected(item: Movie)
    }

    companion object {

        val diffCallback = object : DiffUtil.ItemCallback<Movie>() {

            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }
}