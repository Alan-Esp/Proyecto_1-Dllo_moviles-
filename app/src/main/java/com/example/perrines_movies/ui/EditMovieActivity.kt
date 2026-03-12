package com.example.perrines_movies.ui

import android.app.AlertDialog
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

class EditMovieActivity : AppCompatActivity() {

    private var posterUri: String = ""
    private lateinit var repository: MovieRepository
    private lateinit var imgPoster: ImageView
    private var movieId: Int = 0

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
        setContentView(R.layout.activity_edit_movie)

        val dao = MovieDatabase.getDatabase(applicationContext).movieDao()
        repository = MovieRepository(dao)

        imgPoster = findViewById(R.id.imgPoster)
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etDirector = findViewById<EditText>(R.id.etDirector)
        val etDuracion = findViewById<EditText>(R.id.etDuracion)
        val etResena = findViewById<EditText>(R.id.etResena)
        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        val btnActualizar = findViewById<Button>(R.id.btnActualizar)
        val btnEliminar = findViewById<Button>(R.id.btnEliminar)

        movieId = intent.getIntExtra("movie_id", 0)
        posterUri = intent.getStringExtra("movie_poster") ?: ""
        etNombre.setText(intent.getStringExtra("movie_nombre"))
        etDirector.setText(intent.getStringExtra("movie_director"))
        etDuracion.setText(intent.getIntExtra("movie_duracion", 0).toString())
        etResena.setText(intent.getStringExtra("movie_resena"))
        ratingBar.rating = intent.getFloatExtra("movie_calificacion", 0f)

        if (posterUri.isNotEmpty()) {
            imgPoster.setImageURI(Uri.parse(posterUri))
        }

        imgPoster.setOnClickListener { pickImage.launch("image/*") }

        btnActualizar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val director = etDirector.text.toString().trim()
            val duracionStr = etDuracion.text.toString().trim()
            val resena = etResena.text.toString().trim()

            if (nombre.isEmpty() || director.isEmpty() || duracionStr.isEmpty()) {
                Toast.makeText(this, "Completa los campos obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val movie = Movie(
                id = movieId,
                posterUri = posterUri,
                nombre = nombre,
                director = director,
                duracion = duracionStr.toInt(),
                resena = resena,
                calificacion = ratingBar.rating
            )

            lifecycleScope.launch {
                repository.update(movie)
                Toast.makeText(this@EditMovieActivity, "Película actualizada ✓", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        btnEliminar.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Eliminar película")
                .setMessage("¿Estás seguro de que quieres eliminar esta película?")
                .setPositiveButton("Eliminar") { _, _ ->
                    val movie = Movie(
                        id = movieId,
                        posterUri = posterUri,
                        nombre = etNombre.text.toString(),
                        director = etDirector.text.toString(),
                        duracion = etDuracion.text.toString().toIntOrNull() ?: 0,
                        resena = etResena.text.toString(),
                        calificacion = ratingBar.rating
                    )
                    lifecycleScope.launch {
                        repository.delete(movie)
                        Toast.makeText(this@EditMovieActivity, "Película eliminada", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }
}