package id.go.dinkes.mobileedisponew.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.go.dinkes.mobileedisponew.repository.AKMRepository
import id.go.dinkes.mobileedisponew.ui.main.profile.AkmViewModel

class AKMViewModelFactory constructor(private val repo: AKMRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(AkmViewModel::class.java)){
            AkmViewModel(repo) as T
        }else{
            throw IllegalAccessException("View Model Not Found")
        }
    }
}