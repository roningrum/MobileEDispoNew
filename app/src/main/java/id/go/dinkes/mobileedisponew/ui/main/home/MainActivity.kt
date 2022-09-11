package id.go.dinkes.mobileedisponew.ui.main.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import id.go.dinkes.mobileedisponew.ProfileActivity
import id.go.dinkes.mobileedisponew.R
import id.go.dinkes.mobileedisponew.SuratMainActivity
import id.go.dinkes.mobileedisponew.databinding.ActivityMainBinding
import id.go.dinkes.mobileedisponew.databinding.EDispoMenuBinding
import id.go.dinkes.mobileedisponew.remote.RetrofitService
import id.go.dinkes.mobileedisponew.repository.DispoRepository
import id.go.dinkes.mobileedisponew.ui.main.agenda.AgendaExternal
import id.go.dinkes.mobileedisponew.ui.main.agenda.AgendaInternal
import id.go.dinkes.mobileedisponew.ui.main.agenda.AgendaPPPK
import id.go.dinkes.mobileedisponew.ui.main.home.adapter.TodayAgenda
import id.go.dinkes.mobileedisponew.ui.main.notulen.NotulenDispoActivity
import id.go.dinkes.mobileedisponew.util.GetDate
import id.go.dinkes.mobileedisponew.util.SessionManager
import id.go.dinkes.mobileedisponew.viewmodel.DispoViewModelFactory

class MainActivity : AppCompatActivity() {
    lateinit var homeViewModel: HomeViewModel
    lateinit var binding: ActivityMainBinding
    lateinit var agenda: TodayAgenda
    var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userId = SessionManager(this).getUserId()

        val retrofitService = RetrofitService.getInstance()
        val repo = DispoRepository(retrofitService)
        val todayDate = GetDate.getTodayDate()

        val edispoLayout = binding.eDispoMenuLayout

        homeViewModel = ViewModelProvider(this, DispoViewModelFactory(repo)).get(HomeViewModel::class.java)
        homeViewModel.detailUser(userId)
        homeViewModel.getAgendaHariIni(todayDate,todayDate)

        initRecyclerView()
        initSwipeRefresh()
        accessCariAgenda()
        initMenuHome(edispoLayout)

        binding.agendaHariIniLayout.textAgendaHariIni.text = "Agenda hari ini $todayDate"

        binding.swipeRefresh.setOnRefreshListener {
            homeViewModel.detailUser(userId)
            homeViewModel.getAgendaHariIni(todayDate,todayDate)
            observeViewModel()
        }

        binding.imgProfile.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        observeViewModel()
    }

    private fun observeViewModel(){
        homeViewModel.loading.observe(this){ isLoading ->
            isLoading?.let {
                //it = true
                binding.progressBar.visibility = if(it) View.VISIBLE else View.GONE
            }
            binding.swipeRefresh.isRefreshing = isLoading
        }

        homeViewModel.userDetail.observe(this){
            if(!it.user[0].foto.isEmpty()){
                Picasso.get().load("http://119.2.50.170:9095/e_dispo/assets/temp/foto/" + it.user[0].foto)
                    .placeholder(R.drawable.ic_user)
                    .error(R.drawable.ic_user)
                    .fit()
                    .centerCrop()
                    .into(binding.imgProfile)
            }

        }
        homeViewModel.agenda.observe(this){
            agenda = TodayAgenda(it.result.data)
            binding.agendaHariIniLayout.rvAgendaHariIni.adapter = agenda
//            agenda.notifyDataSetChanged()
            Log.d("agenda", "agenda${it.result.data}")

        }
//        homeViewModel.errorMessage.observe(this){
//            Toast.makeText(this,"Silakan Koneksi Ulang", Toast.LENGTH_SHORT).show()
//        }
    }

    private fun initMenuHome(edispoLayout: EDispoMenuBinding) {
        edispoLayout.btnSuratUndangan.setOnClickListener{
            val intent = Intent(this, SuratMainActivity::class.java)
            intent.putExtra("jenis", "undangan")
            startActivity(intent)

        }
        edispoLayout.btnSuratUmum.setOnClickListener{
            val intent = Intent(this, SuratMainActivity::class.java)
            intent.putExtra("jenis", "umum")
            startActivity(intent)

        }
        edispoLayout.btnDispoBalik.setOnClickListener {
            val intent = Intent(this, SuratMainActivity::class.java)
            intent.putExtra("jenis", "dispo balik")
            startActivity(intent)

        }
        edispoLayout.btnKegiatanInternal.setOnClickListener {
            val intent = Intent(this, AgendaInternal::class.java)
            startActivity(intent)
        }
        edispoLayout.btnKegiatanLuar.setOnClickListener {
            val intent = Intent(this, AgendaExternal::class.java)
            startActivity(intent)
        }
        edispoLayout.btnKegiatanPpk.setOnClickListener {
            val intent = Intent(this, AgendaPPPK::class.java)
            startActivity(intent)
        }
        edispoLayout.btnKegiatanNotulen.setOnClickListener {
            val  intent = Intent(this, NotulenDispoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initSwipeRefresh() {
        binding.swipeRefresh.setColorSchemeColors(
            ContextCompat.getColor(this, android.R.color.holo_red_dark),
            ContextCompat.getColor(this, android.R.color.holo_red_light),
            ContextCompat.getColor(this, R.color.colorMerahMuda),
        )
    }

    private fun initRecyclerView() {
       binding.agendaHariIniLayout.rvAgendaHariIni.layoutManager =  LinearLayoutManager(this)
        binding.agendaHariIniLayout.rvAgendaHariIni.setHasFixedSize(true)

    }

    private fun accessCariAgenda(){
        val intent = Intent(this, CariAgenda::class.java)
        binding.agendaHariIniLayout.btnCariAgenda.setOnClickListener {
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        if(backPressedTime + 2000 > System.currentTimeMillis()){
            super.onBackPressed()
//            val startMain = Intent(Intent.ACTION_MAIN)
//            startMain.addCategory(Intent.CATEGORY_HOME)
//            startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            startActivity(startMain)
            Toast.makeText(this, "Terima kasih, Selamat Beraktivitas", Toast.LENGTH_SHORT).show()
            finish()
        }
        else{
            Toast.makeText(this, "Tekan kembali sebelum keluar dari aplikasi", Toast.LENGTH_SHORT).show()
        }

        backPressedTime = System.currentTimeMillis()

    }
}