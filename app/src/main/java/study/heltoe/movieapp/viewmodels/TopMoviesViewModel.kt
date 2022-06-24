package study.heltoe.movieapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import study.heltoe.movieapp.models.movieTop.FilmTopResponse
import study.heltoe.movieapp.utils.StateData

class TopMoviesViewModel: ViewModel() {
    val topListMovies: MutableLiveData<StateData<FilmTopResponse>> = MutableLiveData()
    private var movieListResponse: FilmTopResponse? = null
    private var page = 1
    var isLastPage = false
}