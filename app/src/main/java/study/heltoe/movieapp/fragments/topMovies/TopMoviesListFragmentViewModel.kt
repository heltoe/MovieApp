package study.heltoe.movieapp.fragments.topMovies

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Response
import study.heltoe.movieapp.MovieApplication
import study.heltoe.movieapp.models.movieTop.FilmTopResponse
import study.heltoe.movieapp.utils.Constants.REPOSITORY
import study.heltoe.movieapp.utils.StateData
import study.heltoe.movieapp.utils.hasInternetConnection
import java.io.IOException

class TopMoviesListFragmentViewModel(app: Application) : AndroidViewModel(app) {
    val topListMovies: MutableLiveData<StateData<FilmTopResponse>> = MutableLiveData()
    private var movieListResponse: FilmTopResponse? = null
    private var page = 1
    var isLastPage = false

    init {
        getListMovies()
    }

    fun getListMovies() = viewModelScope.launch {
        fetchListMovies()
    }

    private fun handleMoviesResponse(response: Response<FilmTopResponse>): StateData<FilmTopResponse> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                if (page == result.pagesCount) {
                    isLastPage = true
                }
                page++
                if (movieListResponse == null) {
                    movieListResponse = result
                } else {
                    val oldList = movieListResponse?.films
                    val newList = result.films
                    oldList?.addAll(newList)
                }
                val finalResult = movieListResponse ?: result
                if (finalResult.films.isEmpty()) {
                    return StateData.Default(finalResult)
                }
                return StateData.Success(finalResult)
            }
        }
        return StateData.Error(response.message())
    }

    private suspend fun fetchListMovies() {
        try {
            topListMovies.postValue(StateData.Loading())
            if (hasInternetConnection(getApplication<MovieApplication>())) {
                val response = REPOSITORY.getTopMoviesList(page)
                topListMovies.postValue(handleMoviesResponse(response))
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