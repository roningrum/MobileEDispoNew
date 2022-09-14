package id.go.dinkes.mobileedisponew

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import id.go.dinkes.mobileedisponew.databinding.ActivityProfileBinding
import id.go.dinkes.mobileedisponew.remote.NetworkRepo
import id.go.dinkes.mobileedisponew.remote.RetrofitService
import id.go.dinkes.mobileedisponew.repository.AKMRepository
import id.go.dinkes.mobileedisponew.repository.DispoRepository
import id.go.dinkes.mobileedisponew.ui.main.login.LoginActivity
import id.go.dinkes.mobileedisponew.ui.main.profile.AkmViewModel
import id.go.dinkes.mobileedisponew.ui.main.profile.ProfileViewModel
import id.go.dinkes.mobileedisponew.util.GetDate
import id.go.dinkes.mobileedisponew.util.SessionManager
import id.go.dinkes.mobileedisponew.viewmodel.AKMViewModelFactory
import id.go.dinkes.mobileedisponew.viewmodel.DispoViewModelFactory

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var viewModel : ProfileViewModel
    private lateinit var akmViewModel: AkmViewModel

    var urlPhoto : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofitService = NetworkRepo.getDispo()
        val akmService = NetworkRepo.getAKM()

        val repo = DispoRepository(retrofitService)
        val repoAkm = AKMRepository(akmService)
        val sessionManager = SessionManager(this)

        viewModel = ViewModelProvider(this, DispoViewModelFactory(repo))[ProfileViewModel::class.java]
        akmViewModel = ViewModelProvider(this, AKMViewModelFactory(repoAkm))[AkmViewModel::class.java]

        viewModel.detailInfoUser(sessionManager.getUserId())
        akmViewModel.getDataCheckup(sessionManager.getNIK())

        Log.d("test", "${akmViewModel.getDataCheckup(sessionManager.getNIK())}")

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

        binding.btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            sessionManager.logout()
            finish()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeViewModel() {
        viewModel.loading.observe(this){ isLoading->
            isLoading.let {
                binding.progressBar.visibility = if(it) VISIBLE else GONE
            }
            binding.swpRefresh.isRefreshing = isLoading
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

        akmViewModel.dataCheckUp.observe(this){ checkUp->
            if(checkUp != null){
                binding.txtBeratBadanAkhir.text = checkUp.data_checkup[0].berat
                binding.txtTinggiBadanAkhir.text = checkUp.data_checkup[0].tinggi
                binding.txtTensiAkhir.text = "${checkUp.data_checkup[0].sistol} / ${checkUp.data_checkup[0].diastol} "

                binding.txtTglAkhirBeratBadan.text = "${GetDate.formatDateWithTime(checkUp.data_checkup[0].tgl_cek_berat)} WIB"
                binding.txtTglAkhirTinggiBadan.text = "${GetDate.formatDateWithTime(checkUp.data_checkup[0].tgl_cek_tinggi)} WIB"
                binding.txtTglAkhirTensi.text = "${GetDate.formatDateWithTime(checkUp.data_checkup[0].tgl_cek_tensi)} WIB"

                hitungBMI(checkUp.data_checkup[0].berat, checkUp.data_checkup[0].tinggi)
            }
        }
    }

    private fun hitungBMI(berat: String, tinggi: String) {
        val bb = berat.toDouble()
        val tall = tinggi.toDouble()/100

        val hitungBMI = bb/(tall*tall)
        val hasilHitungBMI = String.format("%.2f", hitungBMI)

        if(hitungBMI != null){
            if(17<=hitungBMI && hitungBMI<18){
                binding.txtKesimpulanCatatan.text = "-> Berat badan Anda Terlalu Kurus (BMI: $hasilHitungBMI)"
                binding.txtKesimpulanCatatan.setTextColor(ContextCompat.getColor(this, android.R.color.holo_orange_dark))
            } else if(18 <=hitungBMI && hitungBMI< 25){
                binding.txtKesimpulanCatatan.text = "-> Berat badan Anda Normal (BMI: $hasilHitungBMI)"
                binding.txtKesimpulanCatatan.setTextColor(ContextCompat.getColor(this, R.color.colorHijau))
            }else if(25 <=hitungBMI && hitungBMI< 27){
                binding.txtKesimpulanCatatan.text = "-> Kelebihan Berat Badan / Gemuk (BMI: $hasilHitungBMI)"
                binding.txtKesimpulanCatatan.setTextColor(ContextCompat.getColor(this, android.R.color.holo_orange_light))
            }else if(hitungBMI > 27){
                binding.txtKesimpulanCatatan.text = "-> Berbahaya! Anda Termasuk Obesitas (BMI: $hasilHitungBMI)"
                binding.txtKesimpulanCatatan.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_light))
            }
        }

    }
}