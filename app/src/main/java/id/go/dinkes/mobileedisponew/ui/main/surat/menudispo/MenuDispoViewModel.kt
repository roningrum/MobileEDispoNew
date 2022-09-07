package id.go.dinkes.mobileedisponew.ui.main.surat.menudispo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.go.dinkes.mobileedisponew.model.ItemDisposisiResponse
import id.go.dinkes.mobileedisponew.model.ItemEditDisposisiResponse
import id.go.dinkes.mobileedisponew.model.SuccessMessage
import id.go.dinkes.mobileedisponew.repository.DispoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class MenuDispoViewModel constructor(private val repository: DispoRepository) : ViewModel() {
    val listItemDisposisi = MutableLiveData<ItemDisposisiResponse>()
    val listItemEditDiposisi = MutableLiveData<ItemEditDisposisiResponse>()
    val errorMessage = MutableLiveData<String>()
    val successMessage = MutableLiveData<SuccessMessage>()
    val loading = MutableLiveData<Boolean>()

    //Item Disposisi
    fun getItemDisposisi(rule: String, bidang: String, seksi:String){
        loading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val response = repository.getItemDispo(rule, bidang, seksi)
                    listItemDisposisi.postValue(response.data!!)
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

    //Item Edit Disposisi
    fun getItemEditDisposisi(idSurat:String, rule: String){
        loading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val response = repository.getItemEditDisposisi(idSurat, rule)
                    listItemEditDiposisi.postValue(response.data!!)
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

    //dispobalik
    fun getDispoBalik(idSurat: String, isiDp:String, rule:String, idBidang:String, idSeksi:String, userId: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val response = repository.getDispoBalik(idSurat, isiDp, rule, idBidang, idSeksi, userId)
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
        }
    }

    //post dispo
    fun getPostDispo(idSurat: String, disposisi:String, isiDp:String, rule:String, idBidang:String, idSeksi:String, userId:String, instalasiFarmasi:String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val response = repository.getPostDisposisi(idSurat, disposisi, isiDp, rule, idBidang, idSeksi, userId, instalasiFarmasi)
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
        }
    }
}