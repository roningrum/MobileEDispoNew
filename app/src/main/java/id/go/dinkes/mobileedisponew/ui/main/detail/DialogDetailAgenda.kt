package id.go.dinkes.mobileedisponew.ui.main.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.go.dinkes.mobileedisponew.LihatFileSurat
import id.go.dinkes.mobileedisponew.R
import id.go.dinkes.mobileedisponew.databinding.FragmentDialogDetailAgendaFragementBinding
import id.go.dinkes.mobileedisponew.model.PenerimaSurat
import id.go.dinkes.mobileedisponew.remote.RetrofitService
import id.go.dinkes.mobileedisponew.repository.DispoRepository
import id.go.dinkes.mobileedisponew.ui.main.home.HomeViewModel
import id.go.dinkes.mobileedisponew.ui.main.home.adapter.PenerimaSuratAdapter
import id.go.dinkes.mobileedisponew.util.GetDate
import id.go.dinkes.mobileedisponew.viewmodel.DispoViewModelFactory

class DialogDetailAgenda : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentDialogDetailAgendaFragementBinding
    lateinit var viewModel : HomeViewModel
    var id:String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val retrofitService = RetrofitService.getInstance()
        val repo = DispoRepository(retrofitService)
        viewModel = ViewModelProvider(this@DialogDetailAgenda, DispoViewModelFactory(repo))[HomeViewModel::class.java]
        binding = FragmentDialogDetailAgendaFragementBinding.inflate(inflater, container, false)

        val argument = arguments
        id = argument?.getString("id").toString()
        viewModel.getDetailAgenda(id)
        initRecyclerview()
        return binding.root
    }

    private fun initRecyclerview() {
        val layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        binding.rvPenerimaSurat.layoutManager = layoutManager
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.surat.observe(this){ surat->
            for(detail in surat.surat){
                binding.txtAcara.text = detail.acara
                binding.txtTempat.text = detail.tempat
                binding.txtTanggalJam.text = detail.tgl_surat

                binding.txtNoSurat.text = detail.no_surat
                binding.txtNoAgenda.text = detail.no_agenda
                binding.txtDari.text = detail.dari

                binding.txtBidang.text = detail.disposisi1
                binding.txtSeksi.text = detail.disposisi2
                binding.txtHadir.text = detail.disposisi3

                if(detail.penerima_surat.isEmpty()){
                    binding.textTeleponDihubungi.visibility = GONE
                }
                else{
                    binding.textTeleponDihubungi.visibility = VISIBLE
                }

                binding.btnLihatFileSurat.setOnClickListener {
                    val intent = Intent(this.context, LihatFileSurat::class.java)
                    intent.putExtra("file_surat", detail.file_surat)
                    startActivity(intent)
                }
            }

        }
        viewModel.penerima.observe(this){
            it.let {
                val adapterPenerimaSurat = PenerimaSuratAdapter(it)
                binding.rvPenerimaSurat.adapter = adapterPenerimaSurat
            }
        }
        viewModel.loading.observe(this){ isLoading ->
            isLoading?.let {
                //it = true
                binding.progressBar.visibility = if(it) View.VISIBLE else GONE
            }
        }

        viewModel.loadZero.observe(this) { isNull ->
            isNull?.let {
                //it = true
                if(it){
                    binding.textTeleponDihubungi.visibility = GONE
                }
                else{
                    binding.textTeleponDihubungi.visibility = View.VISIBLE
                }
            }
        }

    }
}