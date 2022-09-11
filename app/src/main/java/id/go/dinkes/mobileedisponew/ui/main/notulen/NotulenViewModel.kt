package id.go.dinkes.mobileedisponew.ui.main.notulen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.go.dinkes.mobileedisponew.model.ItemNotulenResponse
import id.go.dinkes.mobileedisponew.repository.DispoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class NotulenViewModel constructor(private val repository: DispoRepository) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    val itemNotulen = MutableLiveData<ItemNotulenResponse>()
    val loading = MutableLiveData<Boolean>()

    fun getNotulenList(userId:String){
        loading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try{
                    val response = repository.getNotulen(userId)
                    itemNotulen.postValue(response.data!!)
                } catch (throwable: Throwable){
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