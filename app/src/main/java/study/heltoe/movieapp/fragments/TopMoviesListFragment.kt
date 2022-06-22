package study.heltoe.movieapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import study.heltoe.movieapp.MainActivity
import study.heltoe.movieapp.R
import study.heltoe.movieapp.adapters.MovieAdapter
import study.heltoe.movieapp.databinding.FragmentTopMoviesListBinding
import study.heltoe.movieapp.utils.StateData
import study.heltoe.movieapp.viewmodels.MoviesViewModel

class TopMoviesListFragment : Fragment() {
    private var _binding: FragmentTopMoviesListBinding? = null
    private val mBinding get() = _binding!!
    lateinit var viewModel: MoviesViewModel
    lateinit var movieAdapter: MovieAdapter

    var isLoading = false
    var isLastPage = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTopMoviesListBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        initViewModel()
        initAdapter()
    }

    private fun initViewModel() {
        viewModel = (activity as MainActivity).viewModel

        viewModel.listMovies.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is StateData.Success -> {
                    hideLoader()
                    hideDefaultState()
                    response.data?.let { movieResponse ->
                        movieAdapter.differ.submitList(movieResponse.items.toList())
                        if (viewModel.isLastPage) {
                            isLastPage = viewModel.isLastPage
                            mBinding.recyclerView.setPadding(10, 0, 0, 0)
                        }
                    }
                }
                is StateData.Error -> {
                    hideLoader()
                    hideDefaultState()
                    response.message?.let { message ->
                        Toast.makeText(activity, "Ошибка: $message", Toast.LENGTH_LONG).show()
                    }
                }
                is StateData.Loading -> {
                    showLoader()
                }
                is StateData.Default -> {
                    hideLoader()
                    showDefaultState()
                }
            }
        })
    }

    private fun initAdapter() {
        movieAdapter = MovieAdapter()
        mBinding.recyclerView.apply {
            adapter = movieAdapter
            val gridLayoutManager = GridLayoutManager(requireContext(), 2)
            layoutManager = gridLayoutManager
        }
        movieAdapter.setOnItemClickListener {
            viewModel.movieId
            findNavController().navigate(R.id.action_topMoviesListFragment_to_oneMovieFragment)
        }
    }

    private fun showLoader() {
        mBinding.loader.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideLoader() {
        mBinding.loader.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showDefaultState() {
        mBinding.defaultPlaceholder.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideDefaultState() {
        mBinding.defaultPlaceholder.visibility = View.INVISIBLE
        isLoading = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}