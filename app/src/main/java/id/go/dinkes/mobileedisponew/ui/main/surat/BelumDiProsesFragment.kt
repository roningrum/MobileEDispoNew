package id.go.dinkes.mobileedisponew.ui.main.surat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.go.dinkes.mobileedisponew.R
import id.go.dinkes.mobileedisponew.SuratMainActivity
import id.go.dinkes.mobileedisponew.databinding.FragmentBelumDiProsesBinding
import id.go.dinkes.mobileedisponew.remote.RetrofitService
import id.go.dinkes.mobileedisponew.repository.DispoRepository
import id.go.dinkes.mobileedisponew.ui.main.surat.adapter.SuratAdapter
import id.go.dinkes.mobileedisponew.util.SessionManager
import id.go.dinkes.mobileedisponew.viewmodel.DispoViewModelFactory

class BelumDiProsesFragment : Fragment() {
    private lateinit var adapter: SuratAdapter
    lateinit var suratViewModel: SuratViewModel
    lateinit var binding: FragmentBelumDiProsesBinding
    var jenis: String?= ""
    lateinit var sessionManager: SessionManager
    var status1: String? = "proses"
    var status2: String? = "proses"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val retrofitService = RetrofitService.getInstance()
        val repo = DispoRepository(retrofitService)
        suratViewModel = ViewModelProvider(this@BelumDiProsesFragment, DispoViewModelFactory(repo))[SuratViewModel::class.java]
        binding = FragmentBelumDiProsesBinding.inflate(inflater, container, false)

        val activity = activity as SuratMainActivity?
        jenis = activity?.jenis_surat

        sessionManager = SessionManager(requireContext())

        binding.txtJudul.text = "Surat $jenis belum diproses"

        binding.swipeRefresh.setOnRefreshListener {
            suratViewModel.getSuratDispo(jenis!!, sessionManager.getRule(), sessionManager.getBidang(), sessionManager.getSeksi(), sessionManager.getUserId(), status1!!, status2!! )
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        suratViewModel.getSuratDispo(jenis!!, sessionManager.getRule(), sessionManager.getBidang(), sessionManager.getSeksi(), sessionManager.getUserId(), status1!!, status2!! )
        initRecyclerView()
        initSwipeRefresh()
        observeViewModel()
    }

    private fun observeViewModel() {
        suratViewModel.surat.observe(viewLifecycleOwner){
            if(it.surat.isNullOrEmpty()){
                tidakAdaSurat()
            } else{
                adapter = SuratAdapter(it.surat, this.requireActivity())
                binding.rvSurat.adapter = adapter
                adaSurat()
            }
//            it.let {
//
//            }
        }
//        suratViewModel.loadZero.observe(viewLifecycleOwner){ isLoading->
//            isLoading?.let {
//                if(it){
//                    tidakAdaSurat()
//                } else{
//                    adaSurat()
//                }
//            }
//        }
        suratViewModel.loading.observe(viewLifecycleOwner){ isLoading->
            isLoading?.let {
                if(it){
                    binding.progressBar.visibility = View.VISIBLE
                    binding.swipeRefresh.isRefreshing = true
                }
                else{
                    binding.progressBar.visibility = View.GONE
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
        binding.rvSurat.visibility = View.GONE
    }

    private fun adaSurat(){
        binding.layoutKosong.visibility = View.GONE
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