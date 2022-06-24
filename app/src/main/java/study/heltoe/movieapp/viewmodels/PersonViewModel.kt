package study.heltoe.movieapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import study.heltoe.movieapp.models.personsList.PersonByNameResponse
import study.heltoe.movieapp.utils.StateData

class PersonViewModel: ViewModel() {
    var personInfo: MutableLiveData<StateData<PersonByNameResponse>> = MutableLiveData()

    fun clearData () {
        personInfo.postValue(null)
    }
}