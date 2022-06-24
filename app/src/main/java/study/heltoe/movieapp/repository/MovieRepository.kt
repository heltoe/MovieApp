package study.heltoe.movieapp.repository

import retrofit2.Response
import study.heltoe.movieapp.models.movieInfo.Film
import study.heltoe.movieapp.models.movieList.FilmSearchByFiltersResponse
import study.heltoe.movieapp.models.movieTop.FilmTopResponse
import study.heltoe.movieapp.models.staffInfo.PersonResponse
import study.heltoe.movieapp.models.staffList.StaffResponse

interface MovieRepository {
    suspend fun getMovieList(page: Int): Response<FilmSearchByFiltersResponse>
    suspend fun getTopMoviesList(page: Int): Response<FilmTopResponse>
    suspend fun getMovieInfo(id: Int): Response<Film>
    //
    suspend fun getMovieInfoStaff(id: Int): Response<List<StaffResponse>>
    suspend fun getPersonInfo(id: String): Response<PersonResponse>
}