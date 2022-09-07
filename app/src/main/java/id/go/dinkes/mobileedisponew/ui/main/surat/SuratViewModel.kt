package id.go.dinkes.mobileedisponew.ui.main.surat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.go.dinkes.mobileedisponew.model.SuccessMessage
import id.go.dinkes.mobileedisponew.model.SuratResponse
import id.go.dinkes.mobileedisponew.repository.DispoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class SuratViewModel constructor(private val repository: DispoRepository) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    val successMessage = MutableLiveData<SuccessMessage>()
    val surat = MutableLiveData<SuratResponse>()

    val loading = MutableLiveData<Boolean>()

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

    // berdasarkan detail
    fun getDetailSurat(id:String){
        loading.value = true

        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val response = repository.getDetailSurat(id)
                    surat.postValue(response.data!!)
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

    //dapat surat berdasarkan tanggal
    fun getSuratDPbyTgl(jenis:String, rule:String, bidang:String, seksi: String, user_id: String,
                        status1: String, status2: String, tgl: String){
        loading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val response = repository.getSuratDispoByTgl(jenis,rule, bidang, seksi, user_id, status1, status2, tgl)
                    surat.postValue(response.body())

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

    //dapat surat berdasarkan tanggal
    fun getSuratKadinbyTgl(jenis:String, tgl: String){
        loading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val response = repository.getSuratbyKadinByTgl(jenis, tgl)
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

    //rule kadin
    fun getTerimaKadin (idSurat: String, user_id: String, idBidang: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val response = repository.getTerimaKadin(idSurat, user_id, idBidang)
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

    //rule kabid
    fun getTerimaKabid(idSurat: String, user_id: String, idBidang: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val response = repository.getTerimaKabid(idSurat, user_id, idBidang)
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

    //rule kasi
    fun getTerimaKasi(idSurat: String, user_id: String, idBidang: String, seksi:String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val response = repository.getTerimaKasi(idSurat, user_id, idBidang,seksi)
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

    //rule staff
    fun getTerimaStaff(idSurat: String, user_id: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val response = repository.getTerimaStaff(idSurat, user_id)
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