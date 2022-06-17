package study.heltoe.movieapp.repository

import study.heltoe.movieapp.client.RetrofitClient

class MovieRepository () {
    suspend fun getMovieList(page: Int = 1) = RetrofitClient.api.getMovieList(page.toString())
    suspend fun getMovieInfo(id: Int) = RetrofitClient.api.getMovieInfo(id.toString())
}