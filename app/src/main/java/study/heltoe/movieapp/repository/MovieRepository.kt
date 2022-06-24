package study.heltoe.movieapp.repository

import study.heltoe.movieapp.client.RetrofitClient

class MovieRepository () {
    suspend fun getMovieList(page: Int = 1) = RetrofitClient.api.getMovieList(page.toString())
    suspend fun getTopMoviesList(page: Int = 1) = RetrofitClient.api.getTopMovieList(page.toString())
    suspend fun getMovieInfo(id: Int) = RetrofitClient.api.getMovieInfo(id.toString())
    //
    suspend fun getMovieInfoStaff(id: Int) = RetrofitClient.api.getMovieInfoStaff(id.toString())
    suspend fun getPersonInfo(name: String) = RetrofitClient.api.getPersonInfo(name)
}