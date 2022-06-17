package study.heltoe.movieapp.client

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import study.heltoe.movieapp.models.movieInfo.MovieInfo
import study.heltoe.movieapp.models.movieList.MovieListResponse
import study.heltoe.movieapp.utils.Constants.TOKEN
import study.heltoe.movieapp.utils.Constants.TOTAL_QUERY_PAGE_SIZE

interface MovieApi {
    @GET("movie")
    suspend fun getMovieList(
        @Query("page") page: String = "1",
        @Query("limit") limit: String = TOTAL_QUERY_PAGE_SIZE.toString(),
        @Query("token") key: String = TOKEN,
        @Query("search") search: String = "1990-2021",
        @Query("field") field: String = "year",
        @Query("sortField") sortField: String = "year",
        @Query("sortType") sortType: String = "-1",
    ): Response<MovieListResponse>
    @GET("movie")
    suspend fun getMovieInfo(
        @Query("search") search: String = "",
        @Query("field") field: String = "id",
        @Query("token") key: String = TOKEN
    ): Response<MovieInfo>
}