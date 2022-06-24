package study.heltoe.movieapp.fragments.staff

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import study.heltoe.movieapp.R
import study.heltoe.movieapp.adapters.StaffMovieAdapter
import study.heltoe.movieapp.databinding.FragmentActorBinding
import study.heltoe.movieapp.utils.Constants.MOVIE_ID
import study.heltoe.movieapp.utils.Constants.PARENT_FRAGMENT
import study.heltoe.movieapp.utils.Constants.STAFF_ID
import study.heltoe.movieapp.utils.StateData

class StaffFragment : Fragment() {
    private var _binding: FragmentActorBinding? = null
    private val mBinding get() = _binding!!
    lateinit var movieAdapter: StaffMovieAdapter
    lateinit var viewModel: StaffFragmentViewModel
    //
    private var movieId: Int? = null
    private var parentFragment: Int? = null
    private var staffId: Int? = null

    var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentActorBinding.inflate(inflater, container, false)
        movieId = arguments?.getInt(MOVIE_ID)
        parentFragment = arguments?.getInt(PARENT_FRAGMENT)
        staffId = arguments?.getInt(STAFF_ID)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        initAdapter()
        initViewModel()
        mBinding.backBtn.setOnClickListener {
            leaveFromFragment()
        }
    }

    private fun initAdapter() {
        movieAdapter = StaffMovieAdapter()
        mBinding.filmsRecyclerView.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        movieAdapter.setOnItemClickListener {
            leaveFromFragment(it.filmId)
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[StaffFragmentViewModel::class.java]

        viewModel.personInfo.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is StateData.Success -> {
                    hideLoader()
                    hideDefaultState()
                    response.data?.let { personResponse ->
                        Glide.with(this).load(personResponse.posterUrl).into(mBinding.cardImage)
                        //
                        val name = personResponse.nameRu ?: personResponse.nameEn ?: ""
                        isTextExist(name, mBinding.name)
                        //
                        val gender = personResponse.sex ?: ""
                        isTextExist(gender, mBinding.gender, mBinding.containerGender)
                        //
                        val profession = personResponse.profession ?: ""
                        isTextExist(profession, mBinding.profession, mBinding.containerProfession)
                        //
                        val birthPlace = personResponse.birthplace ?: ""
                        isTextExist(birthPlace, mBinding.birthPlace, mBinding.containerBirthPlace)
                        //
                        val deathPlace = personResponse.deathplace ?: ""
                        isTextExist(deathPlace, mBinding.deathPlace, mBinding.containerDeathPlace)
                        //
                        if (personResponse.films.isEmpty()) {
                            mBinding.mainContentCard.visibility = View.GONE
                        } else {
                            movieAdapter.differ.submitList(personResponse.films.toList())
                        }
                        //
                        mBinding.link.text = personResponse.webUrl
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
        if (staffId != null) {
            viewModel.staffId = staffId
            viewModel.getStaffInfo()
        } else {
            leaveFromFragment()
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

    private fun isTextExist(text: String, view: TextView, parentView: LinearLayout? = null) {
        if (text.isEmpty()) {
            view.visibility = View.GONE
            if (parentView != null) {
                parentView.visibility = View.GONE
            }
        } else {
            view.text = text
        }
    }

    private fun leaveFromFragment (id: Int? = null) {
        val bundle = Bundle()
        bundle.putInt(MOVIE_ID, id ?: movieId ?: -1)
        bundle.putInt(PARENT_FRAGMENT, parentFragment ?: R.id.action_oneMovieFragment_to_moviesFragment)
        findNavController().navigate(R.id.action_actorFragment_to_oneMovieFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.clearData()
    }
}