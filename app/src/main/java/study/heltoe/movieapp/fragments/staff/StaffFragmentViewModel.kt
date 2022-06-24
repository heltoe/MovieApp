package study.heltoe.movieapp.fragments.staff

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import study.heltoe.movieapp.MovieApplication
import study.heltoe.movieapp.models.staffInfo.PersonResponse
import study.heltoe.movieapp.utils.Constants.REPOSITORY
import study.heltoe.movieapp.utils.StateData
import study.heltoe.movieapp.utils.hasInternetConnection
import java.io.IOException

class StaffFragmentViewModel(app: Application) : AndroidViewModel(app) {
    var staffId: Int? = null
    var personInfo: MutableLiveData<StateData<PersonResponse>> = MutableLiveData()

    fun getStaffInfo() = viewModelScope.launch {
        if (staffId != null) {
            fetchStaffInfo()
        }
    }

    private suspend fun fetchStaffInfo() {
        try {
            personInfo.postValue(StateData.Loading())
            if (hasInternetConnection(getApplication<MovieApplication>())) {
                val response = REPOSITORY.getPersonInfo(staffId.toString())
                if (response.isSuccessful) {
                    response.body()?.let {
                        personInfo.postValue(StateData.Success(it))
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
        staffId = null
        personInfo.postValue(null)
    }
}