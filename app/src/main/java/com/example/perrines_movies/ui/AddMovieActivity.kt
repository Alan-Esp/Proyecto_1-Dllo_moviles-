package com.example.perrines_movies.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.perrines_movies.R
import com.example.perrines_movies.data.Movie
import com.example.perrines_movies.data.MovieDatabase
import com.example.perrines_movies.repository.MovieRepository
import kotlinx.coroutines.launch

class AddMovieActivity : AppCompatActivity() {

    private var posterUri: String = ""
    private lateinit var repository: MovieRepository
    private lateinit var imgPoster: ImageView

    private val pickImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            contentResolver.takePersistableUriPermission(
                it, Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            posterUri = it.toString()
            imgPoster.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_movie)

        val dao = MovieDatabase.getDatabase(applicationContext).movieDao()
        repository = MovieRepository(dao)

        imgPoster = findViewById(R.id.imgPoster)
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etDirector = findViewById<EditText>(R.id.etDirector)
        val etDuracion = findViewById<EditText>(R.id.etDuracion)
        val etResena = findViewById<EditText>(R.id.etResena)
        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)

        imgPoster.setOnClickListener {
            pickImage.launch("image/*")
        }

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val director = etDirector.text.toString().trim()
            val duracionStr = etDuracion.text.toString().trim()
            val resena = etResena.text.toString().trim()
            val calificacion = ratingBar.rating

            if (nombre.isEmpty() || director.isEmpty() || duracionStr.isEmpty()) {
                Toast.makeText(this, "Por favor completa los campos obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val movie = Movie(
                posterUri = posterUri,
                nombre = nombre,
                director = director,
                duracion = duracionStr.toInt(),
                resena = resena,
                calificacion = calificacion
            )

            lifecycleScope.launch {
                repository.insert(movie)
                Toast.makeText(this@AddMovieActivity, "Película guardada ✓", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}