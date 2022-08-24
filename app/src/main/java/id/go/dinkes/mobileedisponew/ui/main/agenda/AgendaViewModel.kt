package id.go.dinkes.mobileedisponew.ui.main.agenda

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.go.dinkes.mobileedisponew.model.*
import id.go.dinkes.mobileedisponew.repository.DispoRepository
import kotlinx.coroutines.*

class AgendaViewModel constructor(private val repository: DispoRepository) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    val bidang = MutableLiveData<BidangResponse>()
    val kegiatanExternal = MutableLiveData<KegiatanLuarResponse>()
    val kegiatanInternal = MutableLiveData<KegiatanInternalResponse>()
    val kegiatanPPPK = MutableLiveData<KegiatanPPPKResponse>()
    var job: Job? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    val loading = MutableLiveData<Boolean>()
    val loadZero = MutableLiveData<Boolean>()

    fun getBidang(){
        loading.value = true
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.getBidang()
            withContext(Dispatchers.Main){
                if(response.isSuccessful){
                    bidang.postValue(response.body())
                    loading.value = false
                }
                else{
                    onError("Error : ${response.message()}")
                }
            }
        }
    }

    //kegiatan External
    fun getKegiatanExternalBidang(idBidang:String, tgl:String){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.getKegiatanExternalBidang(idBidang, tgl)
            withContext(Dispatchers.Main){
               if(response.isSuccessful){
                   if(response.body()?.kegiatan_luar.isNullOrEmpty()){
                       loadZero.value = true
                   }
                   else{
                     kegiatanExternal.postValue(response.body())
                       loadZero.value = false
                       Log.d("Kegiatan Luar", "kegiatan ${response.body()}")
                   }
               }
                else{
                    onError("Error : ${response.message()}")
                   loading.value = false
                   loadZero.value = true
                }
            }
        }
    }

    //kegiatan internal
    fun getKegiatanInternalBidang(idBidang:String, tgl:String){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.getKegiatanInternalBidang(idBidang, tgl)
            withContext(Dispatchers.Main){
                if(response.isSuccessful){
                    if(response.body()?.kegiatan_internal.isNullOrEmpty()){
                        loadZero.value = true
                    }
                    else{
                        kegiatanInternal.postValue(response.body())
                        loadZero.value = false
                        Log.d("Kegiatan Internal", "kegiatan ${response.body()}")
                    }
                }
                else{
                    onError("Error : ${response.message()}")
                    loading.value = false
                    loadZero.value = true
                }
            }
        }
    }

    //kegiatan PPPK
    fun getKegiatanPPPK(dari:String, sampai:String){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.getKegiatanPPPK(dari, sampai)
            withContext(Dispatchers.Main){
                if(response.isSuccessful){
                    if(response.body()?.kegiatanPPPK.isNullOrEmpty()){
                        loadZero.value = true
                    }
                    else{
                        kegiatanPPPK.postValue(response.body())
                        loadZero.value = false
                        Log.d("Kegiatan PPPK", "kegiatan ${response.body()}")
                    }
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