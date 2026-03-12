package com.example.perrines_movies.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: Movie)

    @Update
    suspend fun update(movie: Movie)

    @Delete
    suspend fun delete(movie: Movie)

    @Query("SELECT * FROM movies ORDER BY nombre ASC")
    fun getAllMovies(): Flow<List<Movie>>

    @Query("""
        SELECT * FROM movies 
        WHERE nombre LIKE '%' || :query || '%' 
        OR director LIKE '%' || :query || '%'
        ORDER BY nombre ASC
    """)
    fun searchMovies(query: String): Flow<List<Movie>>
}