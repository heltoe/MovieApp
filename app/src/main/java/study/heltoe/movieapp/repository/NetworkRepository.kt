package study.heltoe.movieapp.repository

import study.heltoe.movieapp.client.RetrofitClient

class NetworkRepository: MovieRepository {
    override suspend fun getMovieList(page: Int) = RetrofitClient.api.getMovieList(page.toString())

    override suspend fun getTopMoviesList(page: Int) = RetrofitClient.api.getTopMovieList(page.toString())

    override suspend fun getMovieInfo(id: Int) = RetrofitClient.api.getMovieInfo(id.toString())

    override suspend fun getMovieInfoStaff(id: Int) = RetrofitClient.api.getMovieInfoStaff(id.toString())

    override suspend fun getPersonInfo(id: String) = RetrofitClient.api.getPersonInfo(id)
}