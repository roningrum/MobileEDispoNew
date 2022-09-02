package id.go.dinkes.mobileedisponew

import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import id.go.dinkes.mobileedisponew.databinding.ActivityProfileBinding
import id.go.dinkes.mobileedisponew.remote.RetrofitService
import id.go.dinkes.mobileedisponew.repository.DispoRepository
import id.go.dinkes.mobileedisponew.ui.main.profile.ProfileViewModel
import id.go.dinkes.mobileedisponew.util.GetDate
import id.go.dinkes.mobileedisponew.util.SessionManager
import id.go.dinkes.mobileedisponew.viewmodel.DispoViewModelFactory

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var viewModel : ProfileViewModel

    var urlPhoto : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofitService = RetrofitService.getInstance()
        val repo = DispoRepository(retrofitService)
        val sessionManager = SessionManager(this)

        viewModel = ViewModelProvider(this, DispoViewModelFactory(repo))[ProfileViewModel::class.java]
        viewModel.detailInfoUser(sessionManager.getUserId())
        observeViewModel()

        binding.swpRefresh.setOnRefreshListener {
            viewModel.detailInfoUser(sessionManager.getUserId())
            observeViewModel()
        }
        binding.swpRefresh.setColorSchemeColors(
            getColor(android.R.color.holo_blue_bright),
            getColor(android.R.color.holo_green_light),
            getColor(android.R.color.holo_orange_light),
            getColor(android.R.color.holo_red_light),
        )
    }

    private fun observeViewModel() {
        viewModel.loading.observe(this){ isLoading->
            isLoading.let {
                binding.progressBar.visibility = if(it) VISIBLE else GONE
                if(it){
                    binding.swpRefresh.isRefreshing = false
                }
            }
        }
        viewModel.userDetail.observe(this){ userDetail->
            if(userDetail!=null){
                binding.txtUsername.text = userDetail.user[0].username
                binding.txtNama.text = userDetail.user[0].nama
                binding.txtNik.text = userDetail.user[0].nik
                binding.txtRule.text = userDetail.user[0].rule
                binding.txtJenisKelamin.text = userDetail.user[0].jenis_kelamin
                binding.txtTelepon.text = userDetail.user[0].telp

                if( userDetail.user[0].tgl_lahir.isNullOrEmpty()){
                    binding.txtTglLahir.text = ""
                }
                else{
                    binding.txtTglLahir.text = userDetail.user[0].tgl_lahir
                    Log.d("tglLahir", "tglLahir ${userDetail.user[0].tgl_lahir} ")
                }


                urlPhoto = if(userDetail.user[0].foto.isNullOrEmpty()){
                    binding.image.setImageResource(R.drawable.ic_user)
                    ""
                } else{
                    Picasso.get().load("http://119.2.50.170:9095/e_dispo/assets/temp/foto/${userDetail.user[0].foto}")
                        .placeholder(R.drawable.ic_user)
                        .error(R.drawable.ic_user)
                        .into(binding.image)
                    userDetail.user[0].foto
                }
            }
        }
    }
}