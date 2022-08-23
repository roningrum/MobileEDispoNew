package id.go.dinkes.mobileedisponew.ui.main.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.go.dinkes.mobileedisponew.model.LoginUser
import id.go.dinkes.mobileedisponew.repository.DispoRepository
import kotlinx.coroutines.*

class LoginViewModel constructor(private val repository: DispoRepository) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    val login = MutableLiveData<LoginUser>()
    var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    val loading = MutableLiveData<Boolean>()

    fun loginUser(username: String, pass: String){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.getLogin(username, pass)
            withContext(Dispatchers.Main){
                if(response.isSuccessful){
                    login.postValue(response.body())
                    loading.value = false
//                  Log.d("response", "${response.body()}")
                }
                else{
                    onError("Error : ${response.message()}")
                }
            }
            delay(8000)
        }
    }

    private fun onError(message: String){
        errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}
