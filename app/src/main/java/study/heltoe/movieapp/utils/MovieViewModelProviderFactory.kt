package study.heltoe.movieapp.utils

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import study.heltoe.movieapp.repository.MovieRepository
import study.heltoe.movieapp.viewmodels.MoviesViewModel

class MovieViewModelProviderFactory(
    private val app: Application,
    private val repository: MovieRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MoviesViewModel(app, repository) as T
    }
}