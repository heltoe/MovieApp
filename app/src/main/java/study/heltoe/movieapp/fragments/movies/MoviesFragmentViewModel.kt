package study.heltoe.movieapp.fragments.movies

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Response
import study.heltoe.movieapp.MovieApplication
import study.heltoe.movieapp.models.movieInfo.Film
import study.heltoe.movieapp.models.movieList.FilmSearchByFiltersResponse
import study.heltoe.movieapp.models.staffList.StaffResponse
import study.heltoe.movieapp.repository.MovieRepository
import study.heltoe.movieapp.utils.Constants.REPOSITORY
import study.heltoe.movieapp.utils.StateData
import study.heltoe.movieapp.utils.hasInternetConnection
import java.io.IOException

class MoviesFragmentViewModel(app: Application) : AndroidViewModel(app) {
    val listMovies: MutableLiveData<StateData<FilmSearchByFiltersResponse>> = MutableLiveData()
    private var movieListResponse: FilmSearchByFiltersResponse? = null
    private var page = 1
    var isLastPage = false

    init {
        getListMovies()
    }

    fun getListMovies() = viewModelScope.launch {
        fetchListMovies()
    }

    private fun handleMoviesResponse(response: Response<FilmSearchByFiltersResponse>): StateData<FilmSearchByFiltersResponse> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                if (page == result.totalPages) {
                    isLastPage = true
                }
                page++
                if (movieListResponse == null) {
                    movieListResponse = result
                } else {
                    val oldList = movieListResponse?.items
                    val newList = result.items
                    oldList?.addAll(newList)
                }
                val finalResult = movieListResponse ?: result
                if (finalResult.items.isEmpty()) {
                    return StateData.Default(finalResult)
                }
                return StateData.Success(finalResult)
            }
        }
        return StateData.Error(response.message())
    }

    private suspend fun fetchListMovies() {
        try {
            listMovies.postValue(StateData.Loading())
            if (hasInternetConnection(getApplication<MovieApplication>())) {
                val response = REPOSITORY.getMovieList(page)
                listMovies.postValue(handleMoviesResponse(response))
            } else {
                listMovies.postValue((StateData.Error("Нет интернет соединения")))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> listMovies.postValue(StateData.Error("Ошибка интернет соединения"))
                else -> listMovies.postValue(StateData.Error("Ошибка загрузки ${t.message}"))
            }
        }
    }
}