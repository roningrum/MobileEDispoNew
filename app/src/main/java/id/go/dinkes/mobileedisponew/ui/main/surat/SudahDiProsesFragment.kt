package id.go.dinkes.mobileedisponew.ui.main.surat

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import id.go.dinkes.mobileedisponew.ui.main.surat.adapter.SuratAdapter
import id.go.dinkes.mobileedisponew.util.SessionManager
import id.go.dinkes.mobileedisponew.viewmodel.DispoViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

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

        binding.etTglCari.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year =calendar.get(Calendar.YEAR)
            val month =calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                // on below line we are passing context.
                this.requireContext(),
                { view, year, monthOfYear, dayOfMonth ->
                    // on below line we are setting
                    // date to our edit text.
                    val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    binding.etTglCari.setText(dat)
                },
                // on below line we are passing year, month
                // and day for the selected date in our date picker.
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        binding.etTglCari.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var newDate: String?=""
                val strDate = binding.etTglCari.text.toString()
                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val convertedDate: Date
                try{
                    convertedDate = dateFormat.parse(strDate)
                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    newDate = sdf.format(convertedDate)
                    if(sessionManager.getRule()=="kadin"){
                        suratViewModel.getSuratKadinbyTgl(jenis!!, newDate!!)
                    }
                    else{
                        suratViewModel.getSuratDPbyTgl(jenis!!,sessionManager.getRule(), sessionManager.getBidang(), sessionManager.getSeksi(), sessionManager.getUserId(), status1!!, status2!!, newDate!!)
                    }

                }catch (e: Exception){
                    e.printStackTrace()
                }
                binding.txtJudul.text = "Surat $jenis telah diproses pada tanggal $newDate"
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

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
            if(it.surat.isNullOrEmpty()){
                tidakAdaSurat()
            } else{
                adapterSurat = SuratAdapter(it.surat, this.requireActivity())
                binding.rvSurat.adapter = adapterSurat
                adaSurat()
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