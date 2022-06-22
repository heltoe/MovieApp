package study.heltoe.movieapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import study.heltoe.movieapp.databinding.ActivityMainBinding
import study.heltoe.movieapp.repository.MovieRepository
import study.heltoe.movieapp.utils.MovieViewModelProviderFactory
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
        initNavigation()
    }

    private fun initViewModel() {
        val movieRepository = MovieRepository()
        val viewModelProviderFactory = MovieViewModelProviderFactory(application, movieRepository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(MoviesViewModel::class.java)
    }

    private fun initNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentConstraint) as NavHostFragment
        val navController = navHostFragment.navController
        mBinding.navigation.setupWithNavController(navController)
        mBinding.navigation.setOnNavigationItemReselectedListener { /* NO-OP */ }

        navHostFragment.findNavController().addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.moviesFragment, R.id.topMoviesListFragment -> mBinding.navigation.visibility = View.VISIBLE
                else -> mBinding.navigation.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

