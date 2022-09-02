package id.go.dinkes.mobileedisponew.ui.main.agenda

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.go.dinkes.mobileedisponew.model.BidangResponse
import id.go.dinkes.mobileedisponew.model.KegiatanInternalResponse
import id.go.dinkes.mobileedisponew.model.KegiatanLuarResponse
import id.go.dinkes.mobileedisponew.model.KegiatanPPPKResponse
import id.go.dinkes.mobileedisponew.repository.DispoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class AgendaViewModel constructor(private val repository: DispoRepository) : ViewModel() {
    private val errorMessage = MutableLiveData<String>()
    val bidang = MutableLiveData<BidangResponse>()
    val kegiatanExternal = MutableLiveData<KegiatanLuarResponse>()
    val kegiatanInternal = MutableLiveData<KegiatanInternalResponse>()
    val kegiatanPPPK = MutableLiveData<KegiatanPPPKResponse>()

    val loading = MutableLiveData<Boolean>()
    val loadZero = MutableLiveData<Boolean>()

    fun getBidang(){
        loading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val response = repository.getBidang()
                    bidang.postValue(response.data!!)
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

                        }
                    }
                }
            }
            loading.value = false
        }
    }

    //kegiatan External
    fun getKegiatanExternalBidang(idBidang:String, tgl:String){
        loading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val response = repository.getKegiatanExternalBidang(idBidang, tgl)
                    kegiatanExternal.postValue(response.data!!)

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

    //kegiatan internal
    fun getKegiatanInternalBidang(idBidang:String, tgl:String){
        loading.value = true

        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val response = repository.getKegiatanInternalBidang(idBidang, tgl)
                    kegiatanInternal.postValue(response.data!!)

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

    //kegiatan PPPK
    fun getKegiatanPPPK(dari:String, sampai:String){
        loading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val response = repository.getKegiatanPPPK(dari, sampai)
                    kegiatanPPPK.postValue(response.data!!)

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