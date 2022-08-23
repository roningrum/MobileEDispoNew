package id.go.dinkes.mobileedisponew.surat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.go.dinkes.mobileedisponew.R
import id.go.dinkes.mobileedisponew.SuratMainActivity
import id.go.dinkes.mobileedisponew.databinding.FragmentSudahDiProsesBinding
import id.go.dinkes.mobileedisponew.remote.RetrofitService
import id.go.dinkes.mobileedisponew.repository.DispoRepository
import id.go.dinkes.mobileedisponew.surat.adapter.SuratAdapter
import id.go.dinkes.mobileedisponew.util.SessionManager
import id.go.dinkes.mobileedisponew.viewmodel.DispoViewModelFactory

class SudahDiProsesFragment : Fragment() {

    private lateinit var adapterSurat: SuratAdapter
    lateinit var suratViewModel: SuratViewModel
    lateinit var binding: FragmentSudahDiProsesBinding
    var jenis: String?= ""
    var status1: String? = "disposisi"
    var status2: String? = "diterima"
    var status_dispo_balik_sudah : String? = "0"

    lateinit var sessionManager: SessionManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSudahDiProsesBinding.inflate(inflater, container, false)
        val retrofitService = RetrofitService.getInstance()
        val repo = DispoRepository(retrofitService)
        suratViewModel = ViewModelProvider(this@SudahDiProsesFragment, DispoViewModelFactory(repo))[SuratViewModel::class.java]

        val activity = activity as SuratMainActivity?
        jenis = activity?.jenis_surat

        sessionManager = SessionManager(requireContext())

        binding.txtJudul.text = "Surat $jenis telah diproses"

        if(jenis == "dispo balik"){
            binding.etTglCari.visibility = GONE
        }
        binding.swipeRefresh.setOnRefreshListener {
            if(sessionManager.getRule() == "kadin"){
                //khusus kadin
                suratViewModel.getSuratDpbyKadin(jenis!!)
            }
            else if(jenis == "dispo balik"){
                suratViewModel.getSuratDispoBalik(sessionManager.getRule(), sessionManager.getBidang(), sessionManager.getSeksi(), status_dispo_balik_sudah!!)
            }
            else{
                suratViewModel.getSuratDispo(jenis!!, sessionManager.getRule(), sessionManager.getBidang(), sessionManager.getSeksi(), sessionManager.getUserId(), status1!!, status2!! )
            }
      }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(sessionManager.getRule() == "kadin"){
            suratViewModel.getSuratDpbyKadin(jenis!!)
        }
        else if(jenis == "dispo balik"){
            suratViewModel.getSuratDispoBalik(sessionManager.getRule(), sessionManager.getBidang(), sessionManager.getSeksi(), status_dispo_balik_sudah!!)
        }
        else{
            suratViewModel.getSuratDispo(jenis!!, sessionManager.getRule(), sessionManager.getBidang(), sessionManager.getSeksi(), sessionManager.getUserId(), status1!!, status2!! )
        }
        initRecyclerView()
        observeViewModel()
        initSwipeRefresh()
    }

    private fun observeViewModel() {

        suratViewModel.surat.observe(viewLifecycleOwner){
            it.let {
                adapterSurat = SuratAdapter(it.surat, this.requireActivity())
                binding.rvSurat.adapter = adapterSurat
            }
        }
        suratViewModel.loadZero.observe(viewLifecycleOwner){ isLoading->
            isLoading?.let {
                if(it){
                    tidakAdaSurat()
                } else{
                    adaSurat()
                }
            }
        }
        suratViewModel.loading.observe(viewLifecycleOwner){ isLoading->
            isLoading?.let {
                binding.progressBar.visibility = if(it) View.VISIBLE else GONE
                if(it) {
                    binding.swipeRefresh.isRefreshing = false
                }
            }
        }
    }

    private fun initRecyclerView() {
        binding.rvSurat.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        binding.rvSurat.layoutManager = layoutManager
    }

    private fun tidakAdaSurat(){
        binding.layoutKosong.visibility = View.VISIBLE
        binding.rvSurat.visibility = GONE
    }

    private fun adaSurat(){
        binding.layoutKosong.visibility = GONE
        binding.rvSurat.visibility = View.VISIBLE
    }

    private fun initSwipeRefresh() {
        binding.swipeRefresh.setColorSchemeColors(
            ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark),
            ContextCompat.getColor(requireContext(), android.R.color.holo_red_light),
            ContextCompat.getColor(requireContext(), R.color.colorMerahMuda),
        )
    }
}