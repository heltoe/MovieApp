package study.heltoe.movieapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import study.heltoe.movieapp.R
import study.heltoe.movieapp.databinding.FragmentActorBinding
import study.heltoe.movieapp.utils.StateData
import study.heltoe.movieapp.viewmodels.PersonViewModel

class ActorFragment : Fragment() {
    private var _binding: FragmentActorBinding? = null
    val mBinding get() = _binding!!
    lateinit var viewModel: PersonViewModel

    var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentActorBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        initViewModel()
        mBinding.backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_actorFragment_to_oneMovieFragment)
        }
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
    }

    private fun initViewModel() {
        viewModel = PersonViewModel()
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