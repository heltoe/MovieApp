package study.heltoe.movieapp.fragments.movies

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import study.heltoe.movieapp.MainActivity
import study.heltoe.movieapp.R
import study.heltoe.movieapp.adapters.MovieAdapter
import study.heltoe.movieapp.databinding.FragmentMoviesBinding
import study.heltoe.movieapp.utils.Constants.MOVIE_ID
import study.heltoe.movieapp.utils.Constants.PARENT_FRAGMENT
import study.heltoe.movieapp.utils.Constants.TOTAL_QUERY_PAGE_SIZE
import study.heltoe.movieapp.utils.StateData
import study.heltoe.movieapp.viewmodels.GlobalViewModel

class MoviesFragment : Fragment() {
    private var _binding: FragmentMoviesBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var viewModel: GlobalViewModel
    lateinit var movieAdapter: MovieAdapter

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)

        val callbackBack = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                AlertDialog.Builder(requireContext())
                    .setTitle("Внимание!")
                    .setMessage("Вы действительно хотите выйти?")
                    .setNegativeButton(R.string.common_no, null)
                    .setPositiveButton(R.string.common_yes) { p0, p1 -> (activity as MainActivity).finish() }.create().show();
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callbackBack)

        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        initViewModel()
        setupRecyclerView()
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

    private val scrollHandler = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState === AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as GridLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= TOTAL_QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage &&
                    isAtLastItem &&
                    isNotAtBeginning &&
                    isTotalMoreThanVisible &&
                    isScrolling

            if (shouldPaginate) {
                viewModel.getListMovies()
                isScrolling = false
            }
        }
    }

    private fun setupRecyclerView() {
        movieAdapter = MovieAdapter()
        mBinding.recyclerView.apply {
            adapter = movieAdapter
            val gridLayoutManager = GridLayoutManager(requireContext(), 2)
            layoutManager = gridLayoutManager
            addOnScrollListener(this@MoviesFragment.scrollHandler)
        }
        movieAdapter.setOnItemClickListener {
            val bundle = Bundle()
            bundle.putInt(MOVIE_ID, it.kinopoiskId)
            bundle.putInt(PARENT_FRAGMENT, R.id.moviesFragment)
            findNavController().navigate(R.id.action_moviesFragment_to_oneMovieFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}