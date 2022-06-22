package study.heltoe.movieapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import study.heltoe.movieapp.models.movieInfo.Film
import study.heltoe.movieapp.utils.StateData

class CardMovieViewModel : ViewModel() {
    var movieId: Int? = null
    var parentFragment: Int? = null
    val movieInfo: MutableLiveData<StateData<Film>> = MutableLiveData()

    fun clearData() {
        movieId = null
        parentFragment = null
        movieInfo.postValue(null)
    }
}