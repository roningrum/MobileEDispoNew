package id.go.dinkes.mobileedisponew.ui.main.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.go.dinkes.mobileedisponew.model.UserDetail
import id.go.dinkes.mobileedisponew.repository.DispoRepository
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException

class ProfileViewModel constructor(private val repository: DispoRepository) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    val userDetail = MutableLiveData<UserDetail>()
    val loading = MutableLiveData<Boolean>()
    val loadZero = MutableLiveData<Boolean>()

    fun detailInfoUser(userId: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val response = repository.getUserDetail(userId)
                    if(response.data?.user.isNullOrEmpty()){
                        loadZero.value = true
                    }
                    else{
                        userDetail.postValue(response.data!!)
                        loadZero.value = false
                    }
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

    private fun onError(message: String){
        errorMessage.value = message
        loading.value = false
    }
}