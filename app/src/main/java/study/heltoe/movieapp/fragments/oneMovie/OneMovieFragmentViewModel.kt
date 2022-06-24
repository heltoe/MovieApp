package study.heltoe.movieapp.fragments.oneMovie

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import study.heltoe.movieapp.MovieApplication
import study.heltoe.movieapp.models.movieInfo.Film
import study.heltoe.movieapp.models.staffList.StaffResponse
import study.heltoe.movieapp.utils.Constants.REPOSITORY
import study.heltoe.movieapp.utils.StateData
import study.heltoe.movieapp.utils.hasInternetConnection
import java.io.IOException

class OneMovieFragmentViewModel(app: Application) : AndroidViewModel(app) {
    var movieId: Int? = null
    val movieInfo: MutableLiveData<StateData<Film>> = MutableLiveData()
    val staffInfo: MutableLiveData<StateData<List<StaffResponse>>> = MutableLiveData()

    fun clearData() {
        movieId = null
        movieInfo.postValue(null)
        staffInfo.postValue(null)
    }

    fun getMovieInfo() = viewModelScope.launch {
        if (movieId != null) {
            fetchMovieInfo()
        }
    }

    private suspend fun fetchMovieInfo() {
        try {
            movieInfo.postValue(StateData.Loading())
            if (hasInternetConnection(getApplication<MovieApplication>())) {
                val response = REPOSITORY.getMovieInfo(movieId!!)
                if (response.isSuccessful) {
                    response.body()?.let { result ->
                        movieInfo.postValue(StateData.Success(result))
                        fetchListStuff(result.kinopoiskId)
                    }
                }
            } else {
                movieInfo.postValue((StateData.Error("Нет интернет соединения")))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> movieInfo.postValue(StateData.Error("Ошибка интернет соединения"))
                else -> movieInfo.postValue(StateData.Error("Ошибка загрузки ${t.message}"))
            }
        }
    }

    private suspend fun fetchListStuff(id: Int) {
        try {
            staffInfo.postValue(StateData.Loading())
            if (hasInternetConnection(getApplication<MovieApplication>())) {
                val response = REPOSITORY.getMovieInfoStaff(id)
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.isEmpty()) {
                            staffInfo.postValue(StateData.Default(it))
                        } else {
                            staffInfo.postValue(StateData.Success(it))
                        }
                    }
                }
            } else {
                staffInfo.postValue(StateData.Error("Нет интернет соединения"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> staffInfo.postValue(StateData.Error("Ошибка интернет соединения"))
                else -> staffInfo.postValue(StateData.Error("Ошибка загрузки ${t.message}"))
            }
        }
    }
}