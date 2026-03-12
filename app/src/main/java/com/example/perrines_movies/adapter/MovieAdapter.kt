package com.example.perrines_movies.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.perrines_movies.R
import com.example.perrines_movies.data.Movie

class MovieAdapter(
    private val onMovieClick: (Movie) -> Unit
) : ListAdapter<Movie, MovieAdapter.MovieViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val poster: ImageView = itemView.findViewById(R.id.imgPoster)
        val nombre: TextView = itemView.findViewById(R.id.tvNombre)
        val director: TextView = itemView.findViewById(R.id.tvDirector)
        val calificacion: RatingBar = itemView.findViewById(R.id.ratingBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.nombre.text = movie.nombre
        holder.director.text = movie.director
        holder.calificacion.rating = movie.calificacion

        if (movie.posterUri.isNotEmpty()) {
            try {
                holder.poster.setImageURI(Uri.parse(movie.posterUri))
            } catch (e: Exception) {
                holder.poster.setImageResource(R.drawable.ic_movie_placeholder)
            }
        } else {
            holder.poster.setImageResource(R.drawable.ic_movie_placeholder)
        }

        holder.itemView.setOnClickListener { onMovieClick(movie) }
    }
}