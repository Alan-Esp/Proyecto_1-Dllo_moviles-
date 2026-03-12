package com.example.perrines_movies.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.perrines_movies.R
import com.example.perrines_movies.adapter.MovieAdapter
import com.example.perrines_movies.data.Movie
import com.example.perrines_movies.data.MovieDatabase
import com.example.perrines_movies.repository.MovieRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: MovieAdapter
    private lateinit var repository: MovieRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dao = MovieDatabase.getDatabase(applicationContext).movieDao()
        repository = MovieRepository(dao)

        adapter = MovieAdapter { movie -> openEditActivity(movie) }
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            repository.getAllMovies().collectLatest { movies ->
                adapter.submitList(movies)
            }
        }

        findViewById<FloatingActionButton>(R.id.fabAdd).setOnClickListener {
            startActivity(Intent(this, AddMovieActivity::class.java))
        }

        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                val query = newText.orEmpty()
                lifecycleScope.launch {
                    if (query.isEmpty()) {
                        repository.getAllMovies().collectLatest { adapter.submitList(it) }
                    } else {
                        repository.searchMovies(query).collectLatest { adapter.submitList(it) }
                    }
                }
                return true
            }
        })
    }

    private fun openEditActivity(movie: Movie) {
        val intent = Intent(this, EditMovieActivity::class.java).apply {
            putExtra("movie_id", movie.id)
            putExtra("movie_nombre", movie.nombre)
            putExtra("movie_director", movie.director)
            putExtra("movie_duracion", movie.duracion)
            putExtra("movie_resena", movie.resena)
            putExtra("movie_calificacion", movie.calificacion)
            putExtra("movie_poster", movie.posterUri)
        }
        startActivity(intent)
    }
}