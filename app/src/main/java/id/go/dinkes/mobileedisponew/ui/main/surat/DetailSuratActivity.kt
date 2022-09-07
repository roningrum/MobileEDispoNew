package id.go.dinkes.mobileedisponew.ui.main.surat

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import id.go.dinkes.mobileedisponew.LihatFileSurat
import id.go.dinkes.mobileedisponew.databinding.ActivityDetailSuratBinding
import id.go.dinkes.mobileedisponew.remote.RetrofitService
import id.go.dinkes.mobileedisponew.repository.DispoRepository
import id.go.dinkes.mobileedisponew.ui.main.surat.menudispo.DialogDispoBalikFragment
import id.go.dinkes.mobileedisponew.ui.main.surat.menudispo.DialogDisposisiFragment
import id.go.dinkes.mobileedisponew.util.DialogFragmentBerhasil
import id.go.dinkes.mobileedisponew.util.GetDate
import id.go.dinkes.mobileedisponew.util.SessionManager
import id.go.dinkes.mobileedisponew.viewmodel.DispoViewModelFactory

class DetailSuratActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailSuratBinding
    private lateinit var suratViewModel: SuratViewModel
    private var idSurat : String?=""
    var isShow = true
    var scrollRange = -1
    private lateinit var  sessionManager: SessionManager

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

        idSurat = intent.extras?.getString("id_surat")

        Log.d("id", "id_surat $idSurat")

        suratViewModel.getDetailSurat(idSurat!!)

        sessionManager = SessionManager(this)
        if(sessionManager.getRule() == "staff"){
            binding.content.btnDisposisi?. visibility = GONE
        }

        binding.content.btnDisposisi?.setOnClickListener {
            val fm = supportFragmentManager
            val args = Bundle()
            args.putString("id_surat", idSurat)
            val dialogFragment = DialogDisposisiFragment()
            dialogFragment.arguments = args
            dialogFragment.show(fm, "Fragment Disposisi")
        }

        binding.content.btnTerimaSurat?.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Terima Surat")
            builder.setMessage("Apakah anda yakin untuk menerima Surat?")
            builder.setPositiveButton("Terima Surat", DialogInterface.OnClickListener { dialog, which ->
                terimaSurat()
                return@OnClickListener
            })
            builder.setNegativeButton("Tidak"){dialog, which ->
                dialog.dismiss()
            }
            val alert = builder.create()
            alert.show()
        }

        binding.content.btnDisposisiBalik?.setOnClickListener {
            val fm = supportFragmentManager
            val args = Bundle()
            args.putString("id_surat", idSurat)
            val dialogFragment = DialogDispoBalikFragment()
            dialogFragment.arguments = args
            dialogFragment.show(fm, "Fragment Disposisi")
        }

        observeViewModel()

    }

    private fun terimaSurat() {
        when (sessionManager.getRule()) {
            "kadin" ->{
                suratViewModel.getTerimaKadin(
                    idSurat!!,
                    sessionManager.getUserId(),
                    sessionManager.getBidang()
                )
            }
            "kabid" -> {
                suratViewModel.getTerimaKabid(
                    idSurat!!,
                    sessionManager.getUserId(),
                    sessionManager.getBidang()
                )
            }
            "kasi" ->{
                suratViewModel.getTerimaKasi(
                    idSurat!!,
                    sessionManager.getUserId(),
                    sessionManager.getBidang(),
                    sessionManager.getSeksi()
                )
            }
            "staff" ->{
                suratViewModel.getTerimaStaff(
                    idSurat!!,
                    sessionManager.getUserId(),
                )
            }
        }
    }

    private fun observeViewModel() {
       suratViewModel.surat.observe(this){ surat->
           //Acara
           if(surat.surat[0].acara.isNullOrEmpty()){
               binding.content.txtAcara?.visibility = GONE
           } else{
               binding.content.txtAcara?.text = surat.surat[0].acara
           }
           //Tempat
           if(surat.surat[0].tempat.isNullOrEmpty()){
               binding.content.layoutTempat?.visibility = GONE
           } else{
               binding.content.txtTempat?.text = surat.surat[0].tempat
           }
           //No Surat
           if(surat.surat[0].no_surat.isNullOrEmpty()){
               binding.content.layoutNoSurat?.visibility = GONE
           } else{
               binding.content.txtNoSurat?.text = surat.surat[0].tempat
           }
           //tanggal
           if(surat.surat[0].tanggal.isNullOrEmpty()){
               binding.content.layoutTanggal?.visibility = GONE
           } else{
               binding.content.txtTanggalJam?.text = GetDate.formatDate(surat.surat[0].tgl_surat)
           }
           //No Agenda
           if(surat.surat[0].no_agenda.isNullOrEmpty()){
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
           if(surat.surat[0].isi_dp.isNullOrEmpty()){
               binding.content.layoutIsiSurat?.visibility = GONE
           } else{
               binding.content.txtIsi?.text = surat.surat[0].isi_dp
           }

           //isi surat
           if(surat.surat[0].isi_surat.isNullOrEmpty()){
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
           if(surat.surat[0].status.isNullOrEmpty()){
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
        suratViewModel.loading.observe(this){
            it?.let {
                binding.content.progressBar?.visibility = if(it) View.VISIBLE else GONE
            }
        }
        suratViewModel.successMessage.observe(this){
            if(it.success == "1"){
                binding.content.progressBar?.visibility = GONE
//                Toast.makeText(this, "Sukses", Toast.LENGTH_SHORT).show()
                showDialogBerhasil()
            } else{
                binding.content.progressBar?.visibility = GONE
                Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun showDialogBerhasil() {
       val fm = supportFragmentManager
        val dialogFragment = DialogFragmentBerhasil()
        dialogFragment.show(fm, "Fragment Berhasil")
    }
}