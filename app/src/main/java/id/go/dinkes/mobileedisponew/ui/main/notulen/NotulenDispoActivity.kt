package id.go.dinkes.mobileedisponew.ui.main.notulen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.go.dinkes.mobileedisponew.R
import id.go.dinkes.mobileedisponew.databinding.ActivityLihatNotulenBinding
import id.go.dinkes.mobileedisponew.databinding.ActivityNotulenDispoBinding
import id.go.dinkes.mobileedisponew.remote.RetrofitService
import id.go.dinkes.mobileedisponew.repository.DispoRepository
import id.go.dinkes.mobileedisponew.ui.main.home.HomeViewModel
import id.go.dinkes.mobileedisponew.ui.main.notulen.adapter.NotulenAdapter
import id.go.dinkes.mobileedisponew.util.SessionManager
import id.go.dinkes.mobileedisponew.viewmodel.DispoViewModelFactory

class NotulenDispoActivity : AppCompatActivity() {
    private lateinit var binding : ActivityNotulenDispoBinding
    private lateinit var notulenViewModel : NotulenViewModel
    private lateinit var adapter: NotulenAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotulenDispoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = SessionManager(this).getUserId()

        val retrofitService = RetrofitService.getInstance()
        val repo = DispoRepository(retrofitService)

        notulenViewModel = ViewModelProvider(this,
            DispoViewModelFactory(repo))[NotulenViewModel::class.java]
        notulenViewModel.getNotulenList(userId)

        initRecyclerView()
        observeViewModel()
    }

    private fun observeViewModel() {
        notulenViewModel.loading.observe(this) {
            it.let {
                binding.progressBar.visibility = if (it) VISIBLE else GONE
            }
        }
        notulenViewModel.itemNotulen.observe(this){
            if(it.item_notulen.isNullOrEmpty()){
                binding.layoutKosong.visibility = VISIBLE
            } else{
                adapter = NotulenAdapter(it.item_notulen)
                binding.rvNotulen.adapter = adapter
            }
        }
    }

    private fun initRecyclerView() {
        binding.rvNotulen.layoutManager = LinearLayoutManager(this)
        binding.rvNotulen.setHasFixedSize(true)
    }
}