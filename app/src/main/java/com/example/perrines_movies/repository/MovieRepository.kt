package com.example.perrines_movies.repository

import com.example.perrines_movies.data.Movie
import com.example.perrines_movies.data.MovieDao
import kotlinx.coroutines.flow.Flow

class MovieRepository(private val movieDao: MovieDao) {

    fun getAllMovies(): Flow<List<Movie>> = movieDao.getAllMovies()

    fun searchMovies(query: String): Flow<List<Movie>> = movieDao.searchMovies(query)

    suspend fun insert(movie: Movie) = movieDao.insert(movie)

    suspend fun update(movie: Movie) = movieDao.update(movie)

    suspend fun delete(movie: Movie) = movieDao.delete(movie)
}