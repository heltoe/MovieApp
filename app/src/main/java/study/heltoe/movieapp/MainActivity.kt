package study.heltoe.movieapp

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView
import study.heltoe.movieapp.databinding.ActivityMainBinding
import study.heltoe.movieapp.fragments.MoviesFragment
import study.heltoe.movieapp.fragments.VishListFragment


class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val navigation = mBinding.navigation
        navigation.setOnItemSelectedListener(object: NavigationBarView.OnItemSelectedListener {
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

