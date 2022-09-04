package id.go.dinkes.mobileedisponew.ui.main.surat

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.AppBarLayout
import id.go.dinkes.mobileedisponew.LihatFileSurat
import id.go.dinkes.mobileedisponew.R
import id.go.dinkes.mobileedisponew.databinding.ActivityDetailSuratBinding
import id.go.dinkes.mobileedisponew.remote.RetrofitService
import id.go.dinkes.mobileedisponew.repository.DispoRepository
import id.go.dinkes.mobileedisponew.util.GetDate
import id.go.dinkes.mobileedisponew.util.SessionManager
import id.go.dinkes.mobileedisponew.viewmodel.DispoViewModelFactory

class DetailSuratActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailSuratBinding
    private lateinit var suratViewModel: SuratViewModel
    private var idSurat : String = ""
    var isShow = true
    var scrollRange = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailSuratBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        setSupportActionBar(findViewById(R.id.toolbar))
//        binding.toolbarLayout.title = "Detail Surat"
        binding.appBar.addOnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = barLayout.totalScrollRange
            }
            if (scrollRange + verticalOffset == 0) {
                binding.toolbarLayout.title = "Detail Surat"
                isShow = true
            } else if (isShow) {
                binding.toolbarLayout.title = " "
                isShow = false
            }
        }

        val retrofitService = RetrofitService.getInstance()
        val repo = DispoRepository(retrofitService)
        suratViewModel = ViewModelProvider(this, DispoViewModelFactory(repo))[SuratViewModel::class.java]

        idSurat = intent.getStringExtra("id_surat")!!
        suratViewModel.getDetailSurat(idSurat)

        val session = SessionManager(this)
        if(session.getRule() == "staff"){
            binding.content.btnDisposisi?. visibility = GONE
        }

        observeViewModel()

    }

    private fun observeViewModel() {
       suratViewModel.surat.observe(this){ surat->
           //Acara
           if(surat.surat[0].acara.isEmpty()){
               binding.content.txtAcara?.visibility = GONE
           } else{
               binding.content.txtAcara?.text = surat.surat[0].acara
           }
           //Tempat
           if(surat.surat[0].tempat.isEmpty()){
               binding.content.layoutTempat?.visibility = GONE
           } else{
               binding.content.txtTempat?.text = surat.surat[0].tempat
           }
           //No Surat
           if(surat.surat[0].no_surat.isEmpty()){
               binding.content.layoutNoSurat?.visibility = GONE
           } else{
               binding.content.txtNoSurat?.text = surat.surat[0].tempat
           }
           //tanggal
           if(surat.surat[0].tanggal.isEmpty()){
               binding.content.layoutTanggal?.visibility = GONE
           } else{
               binding.content.txtTanggalJam?.text = GetDate.formatDate(surat.surat[0].tgl_surat)
           }
           //No Agenda
           if(surat.surat[0].no_agenda.isEmpty()){
               binding.content.layoutNoAgenda?.visibility = GONE
           } else{
               binding.content.txtNoAgenda?.text = surat.surat[0].no_agenda
           }
           binding.content.txtPerihal?.text = surat.surat[0].perihal_surat
           binding.content.txtDari?.text = surat.surat[0].dari
           binding.content.txtBidang?.text = surat.surat[0].disposisi1
           binding.content.txtSeksi?.text = surat.surat[0].disposisi2
           binding.content.txtHadir?.text = surat.surat[0].disposisi3

           //isi dp
           if(surat.surat[0].isi_dp.isEmpty()){
               binding.content.layoutIsiSurat?.visibility = GONE
           } else{
               binding.content.txtIsi?.text = surat.surat[0].isi_dp
           }

           //isi surat
           if(surat.surat[0].isi_surat.isEmpty()){
               binding.content.layoutKeterangan?.visibility = GONE
           }
           else{
               binding.content.txtKeterangan?.text = surat.surat[0].isi_surat
           }

           //keterangan Dp balik
           if(surat.surat[0].keterangan_dp_balik.isNullOrEmpty()){
               binding.content.layoutKeteranganDpBalik?.visibility = GONE
           }
           else{
               binding.content.txtKeteranganDpBalik?.text = surat.surat[0].keterangan_dp_balik
           }

           //status
           if(surat.surat[0].status.isEmpty()){
               binding.content.btnDisposisi?.text = "Disposisi"
           }
           else{
               binding.content.btnDisposisi?.text = "Edit Disposisi"
           }

           binding.content.btnLihatFileSurat?.setOnClickListener {
               val intent = Intent(this, LihatFileSurat::class.java)
               intent.putExtra("file_surat", surat.surat[0].file_surat)
               startActivity(intent)
           }

       }
    }
}