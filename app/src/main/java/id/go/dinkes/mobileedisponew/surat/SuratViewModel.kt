package id.go.dinkes.mobileedisponew.surat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.go.dinkes.mobileedisponew.model.Agenda
import id.go.dinkes.mobileedisponew.model.SuratResponse
import id.go.dinkes.mobileedisponew.model.UserDetail
import id.go.dinkes.mobileedisponew.repository.DispoRepository
import kotlinx.coroutines.*

class SuratViewModel constructor(private val repository: DispoRepository) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    val surat = MutableLiveData<SuratResponse>()
    var job: Job? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    val loading = MutableLiveData<Boolean>()
    val loadZero = MutableLiveData<Boolean>()

    fun getSuratDispo(jenis:String, rule:String, bidang:String, seksi: String, user_id: String,
                      status1: String, status2: String){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.getSuratDispo(jenis, rule, bidang, seksi, user_id, status1, status2)
            withContext(Dispatchers.Main){
                loading.value = true
                if(response.isSuccessful){
                    if(response.body()?.surat.isNullOrEmpty()){
                        loadZero.value = true
                    }
                    else{
                        surat.postValue(response.body())
                        loadZero.value = false
                    }
                    loading.value = false
                }
                else{
                    onError("Error : ${response.message()}")
                    loading.value = false
                    loadZero.value = true
                }
            }
        }
    }

    // Dispo Balik
    fun getSuratDispoBalik(rule: String, bidang: String, seksi: String, notifDpBalik: String){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.getSuratDispoBalik(rule, bidang, seksi, notifDpBalik)
            withContext(Dispatchers.Main){
                if(response.isSuccessful){
                    if(response.body()?.surat.isNullOrEmpty()){
                        loadZero.value = true
                    }
                    else{
                        surat.postValue(response.body())
                        loadZero.value = false
                    }
                    loading.value = false
                }
                else{
                    onError("Error : ${response.message()}")
                    loading.value = false
                    loadZero.value = true
                }
            }
        }
    }

    //khusus kadin
    fun getSuratDpbyKadin(jenis: String){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.getSuratbyKadin(jenis)
            withContext(Dispatchers.Main){
                if(response.isSuccessful){
                    if(response.body()?.surat.isNullOrEmpty()){
                        loadZero.value = true
                    }
                    else{
                        surat.postValue(response.body())
                        loadZero.value = false
                    }
                    loading.value = false
                }
                else{
                    onError("Error : ${response.message()}")
                    loading.value = false
                    loadZero.value = true
                }
            }
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