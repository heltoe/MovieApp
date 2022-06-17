package study.heltoe.movieapp.viewmodels

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
import study.heltoe.movieapp.models.movieInfo.MovieInfo
import study.heltoe.movieapp.models.movieList.MovieListResponse
import study.heltoe.movieapp.repository.MovieRepository
import study.heltoe.movieapp.utils.StateData
import java.io.IOException

class MoviesViewModel(
    app: Application,
    private val repository: MovieRepository
): AndroidViewModel(app) {
    val listMovies: MutableLiveData<StateData<MovieListResponse>> = MutableLiveData()
    private var movieListResponse: MovieListResponse? = null
    private var page = 1
    var isLastPage = false
    //
    var movieId: Int? = null
    val movieInfo: MutableLiveData<StateData<MovieInfo>> = MutableLiveData()

    init {
        getListMovies()
    }

    fun getListMovies() = viewModelScope.launch {
        fetchListMovies()
    }

    fun getMovieInfo() = viewModelScope.launch {
        if (movieId != null) {
            fetchMovieInfo()
        }
    }

    private fun handleMoviesResponse(response: Response<MovieListResponse>): StateData<MovieListResponse> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                if (page == result.pages) {
                    isLastPage = true
                }
                page++
                if (movieListResponse == null) {
                    movieListResponse = result
                } else {
                    val oldList = movieListResponse?.docs
                    val newList = result.docs
                    oldList?.addAll(newList)
                }
                val finalResult = movieListResponse ?: result
                if (finalResult.docs.isEmpty()) {
                    return StateData.Default(finalResult)
                }
                return StateData.Success(finalResult)
            }
        }
        return StateData.Error(response.message())
    }

    private suspend fun fetchListMovies () {
        listMovies.postValue(StateData.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getMovieList(page)
                listMovies.postValue(handleMoviesResponse(response))
            } else {
                listMovies.postValue((StateData.Error("Нет интернет соединения")))
            }
        } catch (t: Throwable) {
            when(t) {
                is IOException -> listMovies.postValue(StateData.Error("Ошибка интернет соединения"))
                else -> listMovies.postValue(StateData.Error("Ошибка загрузки ${t.message}"))
            }
        }
    }

    private suspend fun fetchMovieInfo() {
        movieInfo.postValue(StateData.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getMovieInfo(movieId!!)
                if (response.isSuccessful) {
                    response.body()?.let { result ->
                        movieInfo.postValue(StateData.Success(result))
                    }
                }
            } else {
                movieInfo.postValue((StateData.Error("Нет интернет соединения")))
            }
        } catch (t: Throwable) {
            when(t) {
                is IOException -> movieInfo.postValue(StateData.Error("Ошибка интернет соединения"))
                else -> movieInfo.postValue(StateData.Error("Ошибка загрузки ${t.message}"))
            }
        }
    }

    fun clearMovieInfo() {
        movieInfo.postValue(null)
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<MovieApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}