package id.go.dinkes.mobileedisponew.ui.main.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.go.dinkes.mobileedisponew.model.SuccessMessage
import id.go.dinkes.mobileedisponew.model.UserDetail
import id.go.dinkes.mobileedisponew.repository.DispoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class ProfileViewModel constructor(private val repository: DispoRepository) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    val userDetail = MutableLiveData<UserDetail>()
    val successMessage = MutableLiveData<SuccessMessage>()
    val loading = MutableLiveData<Boolean>()

    fun detailInfoUser(userId: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val response = repository.getUserDetail(userId)
                    userDetail.postValue(response.data!!)
                } catch (throwable : Throwable){
                    when(throwable){
                        is IOException -> {
                            errorMessage.postValue("Jaringan Error")
//                            loading.value = false
                        }
                        is HttpException -> {
                            errorMessage.postValue("Error")
//                            loading.value = false
                        }
                        else ->{
                            errorMessage.postValue("Unknown Error")
//                            loading.value = false
                        }
                    }
                }
            }
            loading.value = false
        }
    }
    fun logout(userId: String, token:String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val response = repository.getLogout(userId,token)
                    successMessage.postValue(response.data!!)
                } catch (throwable : Throwable){
                    when(throwable){
                        is IOException -> {
                            errorMessage.postValue("Jaringan Error")
//                            loading.value = false
                        }
                        is HttpException -> {
                            errorMessage.postValue("Error")
//                            loading.value = false
                        }
                        else ->{
                            errorMessage.postValue("Unknown Error")
//                            loading.value = false
                        }
                    }
                }
            }
            loading.value = false
        }
    }
}