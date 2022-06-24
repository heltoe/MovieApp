package study.heltoe.movieapp.utils

import android.app.Application
import study.heltoe.movieapp.repository.MovieRepository


object Constants {
    const val BASE_URL = "https://api.kinopoisk.dev/"
    const val BASE_URL_UNOFICIAL = "https://kinopoiskapiunofficial.tech/api/"
    const val TOKEN_UNOFICIAL = "4cd3807b-2c89-496f-a70e-c0318d7fb3e7"
    const val TOTAL_QUERY_PAGE_SIZE = 20
    const val MOVIE_ID = "movieId"
    const val STAFF_ID = "staffId"
    const val PARENT_FRAGMENT = "parentFragment"
    lateinit var REPOSITORY: MovieRepository
    lateinit var APPLICATION: Application
}