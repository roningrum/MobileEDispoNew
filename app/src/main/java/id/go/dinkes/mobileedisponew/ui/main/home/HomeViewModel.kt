package id.go.dinkes.mobileedisponew.ui.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.go.dinkes.mobileedisponew.model.Agenda
import id.go.dinkes.mobileedisponew.model.PenerimaSurat
import id.go.dinkes.mobileedisponew.model.SuratResponse
import id.go.dinkes.mobileedisponew.model.UserDetail
import id.go.dinkes.mobileedisponew.repository.DispoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class HomeViewModel constructor(private val repository: DispoRepository) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    val userDetail = MutableLiveData<UserDetail>()
    val agenda = MutableLiveData<Agenda>()
    val surat = MutableLiveData<SuratResponse>()
    val penerima = MutableLiveData<List<PenerimaSurat>>()

    val loading = MutableLiveData<Boolean>()
    val loadZero = MutableLiveData<Boolean>()

    fun detailUser(userId: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val response = repository.getUserDetail(userId)
                    userDetail.postValue(response.data!!)
//                    loading.value = false
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

    fun getAgendaHariIni(dari:String, sampai:String){
        loading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val response = repository.getAgendaToday(dari, sampai)
                    agenda.postValue(response.data!!)
                    loading.value = false
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
        }
    }

    fun getAgendaSearch(dari: String, sampai: String){
        loading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val response = repository.getAgendaToday(dari, sampai)
                    agenda.postValue(response.data!!)

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

    fun getDetailAgenda(id:String){
        loading.value = true

        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val response = repository.getDetailSurat(id)
                    surat.postValue(response.data!!)
                    penerima.postValue(response.data.surat[0].penerima_surat)
//                    if(response.data?.surat.isNullOrEmpty()){
//                        loadZero.value = true
//                    }
//                    else{
//                       surat.postValue(response.data!!)
//                        loadZero.value = false
//                    }

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