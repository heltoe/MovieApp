package study.heltoe.movieapp.client

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import study.heltoe.movieapp.models.Movie

interface RetrofitServices {
    @GET("movie")
    fun getMovieList(@Query("token") key: String): Call<MutableList<Movie>>
}