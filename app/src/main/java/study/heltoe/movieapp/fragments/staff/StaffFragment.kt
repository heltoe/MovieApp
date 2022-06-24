package study.heltoe.movieapp.fragments.staff

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import study.heltoe.movieapp.R
import study.heltoe.movieapp.databinding.FragmentActorBinding
import study.heltoe.movieapp.utils.Constants.MOVIE_ID
import study.heltoe.movieapp.utils.Constants.PARENT_FRAGMENT
import study.heltoe.movieapp.utils.Constants.STAFF_NAME
import study.heltoe.movieapp.utils.StateData

class StaffFragment : Fragment() {
    private var _binding: FragmentActorBinding? = null
    private val mBinding get() = _binding!!
    lateinit var viewModel: StaffFragmentViewModel
    //
    private var movieId: Int? = null
    private var parentFragment: Int? = null
    private var staffName: String? = null

    var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentActorBinding.inflate(inflater, container, false)
        movieId = arguments?.getInt(MOVIE_ID)
        parentFragment = arguments?.getInt(PARENT_FRAGMENT)
        staffName = arguments?.getString(STAFF_NAME)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        initViewModel()
        mBinding.backBtn.setOnClickListener {
            leaveFromFragment()
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
                        if (personResponse.items.isNotEmpty()) {
                            val person = personResponse.items[0]
                            Glide.with(this).load(person.posterUrl).into(mBinding.cardImage)
                            //
                            val name = person.nameRu ?: person.nameEn ?: ""
                            if (name.isNotEmpty()) {
                                mBinding.name.text = person.nameRu ?: person.nameEn
                            } else {
                                mBinding.name.visibility = View.GONE
                            }
                            //
                            val gender = person.sex ?: ""
                            if (gender.isNotEmpty()) {
                                mBinding.gender.text = person.sex
                            } else {
                                mBinding.containerGender.visibility = View.GONE
                            }
                            //
                            mBinding.link.text = person.webUrl
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
        if (staffName != null) {
            viewModel.staffName = staffName
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

    private fun leaveFromFragment () {
        val bundle = Bundle()
        bundle.putInt(MOVIE_ID, movieId ?: -1)
        bundle.putInt(PARENT_FRAGMENT, parentFragment ?: R.id.action_oneMovieFragment_to_moviesFragment)
        findNavController().navigate(R.id.action_actorFragment_to_oneMovieFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.clearData()
    }
}