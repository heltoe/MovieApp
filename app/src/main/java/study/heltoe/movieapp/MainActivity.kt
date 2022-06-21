package study.heltoe.movieapp

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationBarView
import study.heltoe.movieapp.databinding.ActivityMainBinding
import study.heltoe.movieapp.fragments.MoviesFragment
import study.heltoe.movieapp.fragments.VishListFragment
import study.heltoe.movieapp.repository.MovieRepository
import study.heltoe.movieapp.viewmodels.MovieViewModelProviderFactory
import study.heltoe.movieapp.viewmodels.MoviesViewModel

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val mBinding get() = _binding!!
    lateinit var viewModel: MoviesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initViewModel()
    }

    private fun initViewModel() {
        val movieRepository = MovieRepository()
        val viewModelProviderFactory = MovieViewModelProviderFactory(application, movieRepository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(MoviesViewModel::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

