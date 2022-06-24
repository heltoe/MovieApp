package study.heltoe.movieapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import study.heltoe.movieapp.databinding.ActivityMainBinding
import study.heltoe.movieapp.fragments.movies.MoviesFragmentViewModel
import study.heltoe.movieapp.repository.NetworkRepository
import study.heltoe.movieapp.utils.Constants.REPOSITORY

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val mBinding get() = _binding!!
    lateinit var viewModel: MoviesFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initViewModel()
        initNavigation()
    }

    private fun initViewModel() {
        REPOSITORY = NetworkRepository()
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

