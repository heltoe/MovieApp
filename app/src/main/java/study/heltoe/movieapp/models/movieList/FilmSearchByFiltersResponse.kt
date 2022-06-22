package study.heltoe.movieapp.models.movieList

data class FilmSearchByFiltersResponse(
    val total: Int,
    val totalPages: Int,
    val items: MutableList<FilmSearchByFiltersResponseItems>
)
