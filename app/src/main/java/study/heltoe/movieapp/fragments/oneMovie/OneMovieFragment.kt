package study.heltoe.movieapp.fragments.oneMovie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import study.heltoe.movieapp.R
import study.heltoe.movieapp.adapters.ActorAdapter
import study.heltoe.movieapp.databinding.FragmentOneMovieBinding
import study.heltoe.movieapp.utils.Constants.MOVIE_ID
import study.heltoe.movieapp.utils.Constants.PARENT_FRAGMENT
import study.heltoe.movieapp.utils.Constants.STAFF_ID
import study.heltoe.movieapp.utils.StateData

class OneMovieFragment : Fragment() {
    private var _binding: FragmentOneMovieBinding? = null
    private val mBinding get() = _binding!!
    lateinit var viewModel: OneMovieFragmentViewModel
    lateinit var actorAdapter: ActorAdapter

    //
    private var parentFragmentID: Int? = null
    private var movieId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOneMovieBinding.inflate(inflater, container, false)
        movieId = arguments?.getInt(MOVIE_ID)
        parentFragmentID = arguments?.getInt(PARENT_FRAGMENT)

        val callbackBack = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                leaveFromFragment()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callbackBack)

        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        initRecycler()
        iitViewModel()

        mBinding.backBtn.setOnClickListener {
            leaveFromFragment()
        }
    }

    private fun iitViewModel() {
        viewModel = ViewModelProvider(this)[OneMovieFragmentViewModel::class.java]

        viewModel.movieInfo.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is StateData.Success -> {
                    hideLoader()
                    hideDefaultState()
                    response.data?.let { info ->
                        val name = info.nameRu ?: info.nameOriginal ?: info.nameEn ?: ""
                        val shortDescription = info.shortDescription ?: ""
                        val description = info.description ?: "Нет описания"
                        val rating = info.ratingKinopoisk?.toString() ?: ""
                        val year = info.year?.let { "$it г" } ?: ""
                        val genre = info.genres.joinToString(", ", "", "", 3, "...") { it.genre }
                        val movieLength = info.filmLength?.let { "$it мин" } ?: ""
                        val country =
                            info.countries.joinToString(", ", "", "", 3, "...") { it.country }
                        val ageRating = info.ratingAgeLimits?.let { "${it.drop(3)}+" } ?: ""
                        val stringTemplate = listOf(
                            isExistText(year),
                            isExistText(genre),
                            isExistText(movieLength),
                            isExistText(country),
                            isExistText(ageRating)
                        ).filterNot { it.isEmpty() }.joinToString(", ")
                        //
                        Glide.with(this).load(info.posterUrl).into(mBinding.cardImage)
                        if (name.isNotEmpty()) {
                            mBinding.name.text = "${resources.getString(R.string.movie_info_name)} $name"
                        }
                        if (rating.isNotEmpty()) {
                            mBinding.rating.text = rating
                            if (rating.toDouble() > 7) {
                                mBinding.rating.setTextColor(resources.getColor(R.color.green))
                            }
                            if (rating.toDouble() < 5) {
                                mBinding.rating.setTextColor(resources.getColor(R.color.red))
                            }
                        } else {
                            mBinding.containerRating.visibility = View.GONE
                        }
                        isTextExist(stringTemplate, mBinding.cardYearDuration)
                        isTextExist(shortDescription, mBinding.cardShortDescription)
                        mBinding.cardDescription.text = description
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
        viewModel.staffInfo.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is StateData.Success -> {
                    response.data?.let { responseStaff ->
                        actorAdapter.differ.submitList(responseStaff.toList())
                    }
                }
                is StateData.Error -> {}
                is StateData.Loading -> {}
                is StateData.Default -> {
                    mBinding.actorRecyclerView.visibility = View.GONE
                    mBinding.cardStaffPlaceholder.visibility = View.GONE
                }
            }
        })

        if (movieId != null && movieId != -1) {
            viewModel.movieId = movieId
            viewModel.getMovieInfo()
        } else {
            leaveFromFragment()
        }
    }

    private fun initRecycler() {
        actorAdapter = ActorAdapter()
        mBinding.actorRecyclerView.apply {
            adapter = actorAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        actorAdapter.setOnItemClickListener {
            val bundle = Bundle()
            bundle.putInt(STAFF_ID, it.staffId)
            bundle.putInt(MOVIE_ID, movieId!!)
            bundle.putInt(PARENT_FRAGMENT, parentFragmentID!!)
            findNavController().navigate(R.id.action_oneMovieFragment_to_actorFragment, bundle)
        }
    }

    private fun isExistText(text: String? = null): String {
        return text ?: ""
    }

    private fun isTextExist(text: String, view: TextView) {
        if (text.isEmpty()) {
            view.visibility = View.GONE
        } else {
            view.text = text
        }
    }

    private fun showLoader() {
        mBinding.loader.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        mBinding.loader.visibility = View.INVISIBLE
    }

    private fun showDefaultState() {
        mBinding.defaultPlaceholder.visibility = View.VISIBLE
    }

    private fun hideDefaultState() {
        mBinding.defaultPlaceholder.visibility = View.INVISIBLE
    }

    private fun leaveFromFragment() {
        var idRoute = R.id.action_oneMovieFragment_to_moviesFragment
        if (parentFragmentID != R.id.moviesFragment) {
            idRoute = R.id.action_oneMovieFragment_to_topMoviesListFragment
        }
        findNavController().navigate(idRoute)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.clearData()
    }
}