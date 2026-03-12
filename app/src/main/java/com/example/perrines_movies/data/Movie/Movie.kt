package com.example.perrines_movies.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val posterUri: String = "",   // URI de la imagen como String
    val nombre: String,
    val director: String,
    val duracion: Int,            // en minutos
    val resena: String,
    val calificacion: Float       // 1.0 a 5.0
)