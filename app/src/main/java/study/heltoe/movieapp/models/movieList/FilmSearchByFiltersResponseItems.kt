package study.heltoe.movieapp.models.movieList

data class FilmSearchByFiltersResponseItems(
    val kinopoiskId: Int,
    val imdbId: String? = null,
    val nameRu: String? = null,
    val nameEn: String? = null,
    val nameOriginal: String? = null,
    val countries: List<Country>,
    val genres: List<Genre>,
    val ratingKinopoisk: Number? = null,
    val ratingImdb: Number? = null,
    val year: Number? = null,
    val type: String,
    val posterUrl: String,
    val posterUrlPreview: String
)
