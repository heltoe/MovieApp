package study.heltoe.movieapp.models

data class Rating(
    val _id: String,
    val await: Int? = null,
    val filmCritics: Int? = null,
    val imdb: Int? = null,
    val kp: Int? = null,
    val russianFilmCritics: Int? = null
)