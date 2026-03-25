#  Perrines Movies

Aplicación Android para gestionar una base de datos de películas, desarrollada en Kotlin con Room (SQLite). Proyecto para la materia de **Base de Desarollo de aplicaciones moviles**.

---

##  Capturas de pantalla
<img width="702" height="1600" alt="image" src="https://github.com/user-attachments/assets/25d8e505-a4ab-484d-8b5b-333c8b229c7b" />



---



##  Descripción

Perrines Movies es una app móvil que permite realizar operaciones CRUD completas sobre una base de datos local de películas usando **SQLite a través de Room**. Cada película incluye poster, nombre, director, duración, reseña y calificación.

---

##  Funcionalidades

-  **Agregar** películas con imagen desde la galería
-  **Listar** todas las películas en un RecyclerView
-  **Editar** cualquier campo de una película
-  **Eliminar** películas con confirmación
-  **Buscar** por nombre o director en tiempo real

---

##  Base de datos

La base de datos se implementa con **Room (SQLite)**. La tabla principal es `movies`:

| Columna | Tipo | Descripción |
|---|---|---|
| `id` | INTEGER PK AUTOINCREMENT | Identificador único |
| `posterUri` | TEXT | URI de la imagen del poster |
| `nombre` | TEXT | Nombre de la película |
| `director` | TEXT | Director de la película |
| `duracion` | INTEGER | Duración en minutos |
| `resena` | TEXT | Reseña de la película |
| `calificacion` | REAL | Calificación del 1 al 5 |

### Consultas SQL implementadas

```sql
-- Obtener todas las películas
SELECT * FROM movies ORDER BY nombre ASC

-- Buscar por nombre o director
SELECT * FROM movies 
WHERE nombre LIKE '%query%' OR director LIKE '%query%'
ORDER BY nombre ASC

-- Insert, Update y Delete generados por Room automáticamente
```

---

##  Arquitectura

```
UI (Activities)
      ↓
Repository          ← único punto de acceso a datos
      ↓
DAO                 ← consultas SQL con Room
      ↓
SQLite              ← base de datos en el dispositivo
```

### Estructura de archivos

```
com.example.perrines_movies/
├── data/
│   ├── Movie.kt            → Entidad / tabla SQLite
│   ├── MovieDao.kt         → Consultas SQL (CRUD)
│   └── MovieDatabase.kt    → Conexión a la base de datos
├── repository/
│   └── MovieRepository.kt  → Capa de acceso a datos
├── adapter/
│   └── MovieAdapter.kt     → RecyclerView adapter
├── ui/
│   ├── AddMovieActivity.kt → Pantalla agregar película
│   └── EditMovieActivity.kt→ Pantalla editar / eliminar
└── MainActivity.kt         → Pantalla principal con lista
```

---

##  Tecnologías usadas

- **Kotlin**
- **Android Room** (SQLite)
- **KSP** (Kotlin Symbol Processing)
- **Kotlin Coroutines + Flow**
- **RecyclerView + ListAdapter**
- **Material Design**
- **ActivityResultContracts** (selección de imágenes)

---

##  Configuración del proyecto

**`build.gradle.kts (Module :app)`**
```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

dependencies {
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
}
```

---

##  Cómo ejecutar

1. Clona el repositorio
```bash
git clone https://github.com/tu-usuario/perrines-movies.git
```
2. Abre el proyecto en **Android Studio**
3. Sincroniza Gradle (`Sync Now`)
4. Ejecuta en un emulador o dispositivo físico con **Android 12+**

---

##  Dónde se guarda la base de datos

La base de datos SQLite se almacena localmente en el dispositivo:
```
/data/data/com.example.perrines_movies/databases/movies_database
```

Las imágenes **no se guardan en la BD**. Solo se guarda el URI como texto y se carga con `setImageURI()`.

---

##  Desarrollado por

**Alane** — Materia Desarrollo de aplicaciones moviles  
 · 
