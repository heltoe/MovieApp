package study.heltoe.movieapp.models.movieList

data class Votes(
    val _id: String,
    val await: Int? = null,
    val filmCritics: Int? = null,
    val imdb: Int? = null,
    val kp: Int? = null,
    val russianFilmCritics: Int? = null
)