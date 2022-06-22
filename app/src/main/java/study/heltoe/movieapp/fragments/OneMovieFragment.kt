package study.heltoe.movieapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import study.heltoe.movieapp.MainActivity
import study.heltoe.movieapp.R
import study.heltoe.movieapp.adapters.ActorAdapter
import study.heltoe.movieapp.databinding.FragmentOneMovieBinding
import study.heltoe.movieapp.utils.StateData
import study.heltoe.movieapp.viewmodels.MoviesViewModel

class OneMovieFragment : Fragment() {
    private var _binding: FragmentOneMovieBinding? = null
    private val mBinding get() = _binding!!
    lateinit var viewModel: MoviesViewModel
    lateinit var actorAdapter: ActorAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOneMovieBinding.inflate(inflater, container, false)
        return mBinding.root
    }
    override fun onStart() {
        super.onStart()
        initRecycler()
        iitViewModel()

        mBinding.backBtn.setOnClickListener {
            var idRoute = R.id.action_oneMovieFragment_to_moviesFragment
            if (viewModel.parentFragment != R.id.moviesFragment) {
                idRoute = R.id.action_oneMovieFragment_to_topMoviesListFragment
            }
            findNavController().navigate(idRoute)
        }
    }

    private fun iitViewModel() {
        viewModel = (activity as MainActivity).viewModel

        viewModel.getMovieInfo()
        viewModel.movieInfo.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
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
                        val country = info.countries.joinToString(", ", "", "", 3, "...") { it.country }
                        val ageRating = info.ratingAgeLimits?.let { "$it+" } ?: ""
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
                            mBinding.name.text = name
                        } else {
                            mBinding.containerName.visibility = View.GONE
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
            when(response) {
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
    }

    private fun initRecycler() {
        actorAdapter = ActorAdapter()
        mBinding.actorRecyclerView.apply {
            adapter = actorAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        actorAdapter.setOnItemClickListener {
            val name = it.nameRu ?: it.nameEn ?: ""
            if (name.isNotEmpty()) {
                findNavController().navigate(R.id.action_oneMovieFragment_to_actorFragment)
            }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.clearMovieInfo()
    }
}