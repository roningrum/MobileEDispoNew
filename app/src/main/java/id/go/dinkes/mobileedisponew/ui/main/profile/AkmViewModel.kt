package id.go.dinkes.mobileedisponew.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.go.dinkes.mobileedisponew.model.akm.DataCheckUpResponse
import id.go.dinkes.mobileedisponew.repository.AKMRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class AkmViewModel constructor(private val repository:AKMRepository) : ViewModel() {
    val dataCheckUp = MutableLiveData<DataCheckUpResponse>()
    val errorMessage = MutableLiveData<String>()

    fun getDataCheckup(nik:String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try{
                    val response = repository.getCheckupHistory(nik)
                    dataCheckUp.postValue(response.data!!)
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
        }
    }
}