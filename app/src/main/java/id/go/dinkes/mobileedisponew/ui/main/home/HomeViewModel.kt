package id.go.dinkes.mobileedisponew.ui.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.go.dinkes.mobileedisponew.model.Agenda
import id.go.dinkes.mobileedisponew.model.PenerimaSurat
import id.go.dinkes.mobileedisponew.model.SuratResponse
import id.go.dinkes.mobileedisponew.model.UserDetail
import id.go.dinkes.mobileedisponew.repository.DispoRepository
import kotlinx.coroutines.*

class HomeViewModel constructor(private val repository: DispoRepository) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    val userDetail = MutableLiveData<UserDetail>()
    val agenda = MutableLiveData<Agenda>()
    val surat = MutableLiveData<SuratResponse>()
    val penerima = MutableLiveData<List<PenerimaSurat>>()

    var job: Job? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    val loading = MutableLiveData<Boolean>()
    val loadZero = MutableLiveData<Boolean>()

    fun detailUser(userId: String){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.getUserDetail(userId)
            withContext(Dispatchers.Main){
                if(response.isSuccessful){
                    userDetail.postValue(response.body())
                    loading.value = false
                }
                else{
                    onError("Error: ${response.message()}")
                }
            }
            delay(8000)
        }
    }

    fun getAgendaHariIni(dari:String, sampai:String){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.getAgendaToday(dari, sampai)
            withContext(Dispatchers.Main){
                if(response.isSuccessful){
                    agenda.postValue(response.body())
                    loading.value = false
                }
                else{
                    onError("Error: ${response.message()}")
                    loading.value = false
                }
            }
            delay(8000)
        }
    }

    fun getAgendaSearch(dari: String, sampai: String){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.getAgendaToday(dari, sampai)
            withContext(Dispatchers.Main){
                loading.value = true
                if(response.isSuccessful){
                    if(response.body()?.result?.data.isNullOrEmpty()){
                        loadZero.value = true
                    }
                    else{
                        agenda.postValue(response.body())
                        loadZero.value = false
                    }
                    loading.value = false

                }
                else{
                    onError("Error: ${response.message()}")
                    loadZero.value = true
                    loading.value = false
                }
            }
            delay(8000)
        }
    }

    fun getDetailAgenda(id:String){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.getDetailSurat(id)
            withContext(Dispatchers.Main){
                loading.value = true
                if(response.isSuccessful){
                    if(response.body()?.surat.isNullOrEmpty() && response.body()?.surat?.get(0)?.penerima_surat.isNullOrEmpty()){
                        loadZero.value = true
                    }
                    else{
                        surat.postValue(response.body())
                        penerima.postValue(response.body()?.surat?.get(0)?.penerima_surat as List<PenerimaSurat>?)
                        loadZero.value = false
                    }
                    loading.value = false

                }
                else{
                    onError("Error: ${response.message()}")
                    loadZero.value = true
                    loading.value = false
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