package id.go.dinkes.mobileedisponew.surat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.go.dinkes.mobileedisponew.model.Agenda
import id.go.dinkes.mobileedisponew.model.SuratResponse
import id.go.dinkes.mobileedisponew.model.UserDetail
import id.go.dinkes.mobileedisponew.repository.DispoRepository
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException

class SuratViewModel constructor(private val repository: DispoRepository) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    val surat = MutableLiveData<SuratResponse>()

    val loading = MutableLiveData<Boolean>()
    val loadZero = MutableLiveData<Boolean>()

    fun getSuratDispo(jenis:String, rule:String, bidang:String, seksi: String, user_id: String,
                      status1: String, status2: String){
        loading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val response = repository.getSuratDispo(jenis, rule, bidang, seksi, user_id, status1, status2)
                    surat.postValue(response.data!!)

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

    // Dispo Balik
    fun getSuratDispoBalik(rule: String, bidang: String, seksi: String, notifDpBalik: String){
        loading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val response = repository.getSuratDispoBalik(rule, bidang, seksi, notifDpBalik)
                    surat.postValue(response.data!!)

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

    //khusus kadin
    fun getSuratDpbyKadin(jenis: String){
        loading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val response = repository.getSuratbyKadin(jenis)
                    surat.postValue(response.data!!)

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