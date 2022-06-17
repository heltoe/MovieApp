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
//
        initNavigation()
        initViewModel()
    }

    private fun initNavigation() {
        val navigation = mBinding.navigation
        navigation.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.movies_list -> {
                        loadFragment(MoviesFragment())
                        return true
                    }
                    R.id.vish_list -> {
                        loadFragment(VishListFragment())
                        return true
                    }
                }
                return false
            }
        })
    }

    private fun initViewModel() {
        val movieRepository = MovieRepository()
        val viewModelProviderFactory = MovieViewModelProviderFactory(application, movieRepository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(MoviesViewModel::class.java)
    }

    private fun loadFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager.beginTransaction()
        fragmentManager.replace(R.id.fragmentConstraint, fragment)
        fragmentManager.addToBackStack(null)
        fragmentManager.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

