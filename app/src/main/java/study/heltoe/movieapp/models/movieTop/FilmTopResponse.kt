package study.heltoe.movieapp.models.movieTop

data class FilmTopResponse(
    val pagesCount: Int,
    val films: MutableList<FilmTopResponseFilms>
)
