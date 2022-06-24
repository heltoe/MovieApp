package study.heltoe.movieapp.models.movieTop

import study.heltoe.movieapp.models.movieList.Country
import study.heltoe.movieapp.models.movieList.Genre

data class FilmTopResponseFilms(
    val filmId: Int,
    val nameRu: String? = null,
    val nameEn: String? = null,
    val year: String? = null,
    val filmLength: String? = null,
    val countries: List<Country>,
    val genres: List<Genre>,
    val rating: Number? = null,
    val ratingVoteCount: Number? = null,
    val posterUrl: String,
    val posterUrlPreview: String
)
