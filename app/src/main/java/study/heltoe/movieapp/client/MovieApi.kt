package study.heltoe.movieapp.client

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import study.heltoe.movieapp.models.staffList.StaffResponse
import study.heltoe.movieapp.models.movieInfo.Film
import study.heltoe.movieapp.models.movieList.FilmSearchByFiltersResponse
import study.heltoe.movieapp.models.movieTop.FilmTopResponse
import study.heltoe.movieapp.models.personsList.PersonByNameResponse

interface MovieApi {
    @GET("v2.2/films")
    suspend fun getMovieList(
        @Query("page") page: String = "1",
        @Query("order") order: String = "YEAR",
        @Query("yearFrom") yearFrom: String = "1990",
    ): Response<FilmSearchByFiltersResponse>
    @GET("v2.2/films/{id}")
    suspend fun getMovieInfo(
        @Path("id") id: String = "",
    ): Response<Film>
    //
    @GET("v2.2/films/top")
    suspend fun getTopMovieList(
        @Query("page") page: String = "1",
    ): Response<FilmTopResponse>
    //
    @GET("v1/staff")
    suspend fun getMovieInfoStaff(
        @Query("filmId") field: String = "",
    ): Response<List<StaffResponse>>
    @GET("v1/persons")
    suspend fun getPersonInfo(
        @Query("name") name: String = "",
        @Query("page") page: String = "1",
    ): Response<PersonByNameResponse>
}