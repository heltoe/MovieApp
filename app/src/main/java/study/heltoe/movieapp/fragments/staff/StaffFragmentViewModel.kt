package study.heltoe.movieapp.fragments.staff

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import study.heltoe.movieapp.MovieApplication
import study.heltoe.movieapp.models.personsList.PersonByNameResponse
import study.heltoe.movieapp.utils.Constants.REPOSITORY
import study.heltoe.movieapp.utils.StateData
import study.heltoe.movieapp.utils.hasInternetConnection
import java.io.IOException

class StaffFragmentViewModel(app: Application) : AndroidViewModel(app) {
    var staffName: String? = null
    var personInfo: MutableLiveData<StateData<PersonByNameResponse>> = MutableLiveData()

    fun getStaffInfo() = viewModelScope.launch {
        if (staffName != null) {
            fetchStaffInfo()
        }
    }

    private suspend fun fetchStaffInfo() {
        try {
            personInfo.postValue(StateData.Loading())
            if (hasInternetConnection(getApplication<MovieApplication>())) {
                val response = REPOSITORY.getPersonInfo(staffName!!)
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.items.isEmpty()) {
                            personInfo.postValue(StateData.Default(it))
                        } else {
                            personInfo.postValue(StateData.Success(it))
                        }
                    }
                }
            } else {
                personInfo.postValue(StateData.Error("Нет интернет соединения"))
            }
        } catch(t: Throwable) {
            when (t) {
                is IOException -> personInfo.postValue(StateData.Error("Ошибка интернет соединения"))
                else -> personInfo.postValue(StateData.Error("Ошибка загрузки ${t.message}"))
            }
        }
    }

    fun clearData() {
        staffName = null
        personInfo.postValue(null)
    }
}