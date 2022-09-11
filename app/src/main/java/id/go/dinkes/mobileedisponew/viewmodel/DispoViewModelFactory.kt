package id.go.dinkes.mobileedisponew.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.go.dinkes.mobileedisponew.repository.DispoRepository
import id.go.dinkes.mobileedisponew.ui.main.agenda.AgendaViewModel
import id.go.dinkes.mobileedisponew.ui.main.home.HomeViewModel
import id.go.dinkes.mobileedisponew.ui.main.login.LoginViewModel
import id.go.dinkes.mobileedisponew.ui.main.notulen.NotulenViewModel
import id.go.dinkes.mobileedisponew.ui.main.profile.ProfileViewModel
import id.go.dinkes.mobileedisponew.ui.main.surat.SuratViewModel
import id.go.dinkes.mobileedisponew.ui.main.surat.menudispo.MenuDispoViewModel

class DispoViewModelFactory constructor(private val repository: DispoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(LoginViewModel::class.java)){
            LoginViewModel(repository) as T
        } else if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            HomeViewModel(repository) as T
        } else if(modelClass.isAssignableFrom(SuratViewModel::class.java)){
            SuratViewModel(repository) as T
        }else if(modelClass.isAssignableFrom(AgendaViewModel::class.java)){
            AgendaViewModel(repository) as T
        }
        else if(modelClass.isAssignableFrom(ProfileViewModel::class.java)){
            ProfileViewModel(repository) as T
        } else if(modelClass.isAssignableFrom(MenuDispoViewModel::class.java)){
            MenuDispoViewModel(repository) as T
        }else if(modelClass.isAssignableFrom(NotulenViewModel::class.java)){
            NotulenViewModel(repository) as T
        }
        else{
            throw IllegalAccessException("View Model Not Found")
        }
    }

}