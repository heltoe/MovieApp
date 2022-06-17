package study.heltoe.movieapp.models.movieList

import study.heltoe.movieapp.models.movieList.Movie

data class MovieListResponse(
    val docs: MutableList<Movie>,
    val total: Number,
    val limit: Number,
    val page: Number,
    val pages: Number
)
