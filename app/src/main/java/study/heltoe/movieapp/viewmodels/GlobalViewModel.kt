package study.heltoe.movieapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Response
import study.heltoe.movieapp.MovieApplication
import study.heltoe.movieapp.models.movieList.FilmSearchByFiltersResponse
import study.heltoe.movieapp.models.movieTop.FilmTopResponse
import study.heltoe.movieapp.utils.Constants.REPOSITORY
import study.heltoe.movieapp.utils.StateData
import study.heltoe.movieapp.utils.hasInternetConnection
import java.io.IOException

class GlobalViewModel(app: Application): AndroidViewModel(app) {
    val listMovies: MutableLiveData<StateData<FilmSearchByFiltersResponse>> = MutableLiveData()
    private var movieListResponse: FilmSearchByFiltersResponse? = null
    private var page = 1
    var isLastPage = false
    //
    val topListMovies: MutableLiveData<StateData<FilmTopResponse>> = MutableLiveData()
    private var topMovieListResponse: FilmTopResponse? = null
    private var pageTopMovies = 1
    var isLastTopMoviesPage = false
    var isFirstLoading = false
    //

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

    fun getListTopMovies() = viewModelScope.launch {
        fetchListTopMovies()
    }

    private fun handleTopMoviesResponse(response: Response<FilmTopResponse>): StateData<FilmTopResponse> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                if (pageTopMovies == result.pagesCount) {
                    isLastTopMoviesPage = true
                }
                pageTopMovies++
                if (topMovieListResponse == null) {
                    topMovieListResponse = result
                } else {
                    val oldList = topMovieListResponse?.films
                    val newList = result.films
                    oldList?.addAll(newList)
                }
                val finalResult = topMovieListResponse ?: result
                if (finalResult.films.isEmpty()) {
                    return StateData.Default(finalResult)
                }
                return StateData.Success(finalResult)
            }
        }
        return StateData.Error(response.message())
    }

    private suspend fun fetchListTopMovies() {
        try {
            topListMovies.postValue(StateData.Loading())
            if (hasInternetConnection(getApplication<MovieApplication>())) {
                val response = REPOSITORY.getTopMoviesList(pageTopMovies)
                topListMovies.postValue(handleTopMoviesResponse(response))
                isFirstLoading = true
            } else {
                topListMovies.postValue((StateData.Error("Нет интернет соединения")))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> topListMovies.postValue(StateData.Error("Ошибка интернет соединения"))
                else -> topListMovies.postValue(StateData.Error("Ошибка загрузки ${t.message}"))
            }
        }
    }
}