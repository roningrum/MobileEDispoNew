package id.go.dinkes.mobileedisponew.ui.main.agenda

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.go.dinkes.mobileedisponew.databinding.ActivityAgendaExternalBinding
import id.go.dinkes.mobileedisponew.remote.RetrofitService
import id.go.dinkes.mobileedisponew.repository.DispoRepository
import id.go.dinkes.mobileedisponew.util.GetDate
import id.go.dinkes.mobileedisponew.viewmodel.DispoViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("SetTextI18n")
class AgendaExternal : AppCompatActivity() {
    lateinit var binding : ActivityAgendaExternalBinding
    lateinit var viewModel: AgendaViewModel


    var bidang= ArrayList<String>()
    var tglUbah: String?=""
    var listIdBidang = ArrayList<String>()
    lateinit var idBidang: String
    lateinit var adapter: KegiatanLuarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgendaExternalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofitService = RetrofitService.getInstance()
        val repo = DispoRepository(retrofitService)
        viewModel = ViewModelProvider(this, DispoViewModelFactory(repo))[AgendaViewModel::class.java]
        viewModel.getBidang()

        observeViewModel()
        initRecyclerView()

        //search based on tgl
        binding.etTglCari.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year =calendar.get(Calendar.YEAR)
            val month =calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                // on below line we are passing context.
                this,
                { _, year, monthOfYear, dayOfMonth ->
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
                tglUbah = binding.etTglCari.text.toString()
                Log.d("tanggal yang dipilih", "tanggal ${binding.etTglCari.text}")
                CoroutineScope(Dispatchers.IO).launch {
                    delay(8000)
                    if(binding.etTglCari.text != binding.etTglCari.text )
                        return@launch
                    binding.progressBar.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })


        binding.spinnerBidang.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
               val idBidangNya = listIdBidang[bidang.indexOf(binding.spinnerBidang.selectedItem.toString())]
                idBidang = idBidangNya
                if(binding.etTglCari.length()<1){
                    viewModel.getKegiatanExternalBidang(idBidang, GetDate.getTodayDate())
                    setJudulPencarianHariIni(binding.spinnerBidang.selectedItem.toString())
                }
                else{
                    viewModel.getKegiatanExternalBidang(idBidang, tglUbah!!)
                    setJudulPencarianTanggal(binding.spinnerBidang.selectedItem.toString(), binding.etTglCari.text.toString())
                }
              Log.d("bidang yang dipilih", "id Bidang $idBidangNya")

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

    }

    private fun observeViewModel() {
        viewModel.loading.observe(this){ isLoading->
            isLoading?.let {
                binding.progressBar.visibility = if(it) View.VISIBLE else View.GONE
            }
        }
        viewModel.bidang.observe(this){ bidangreponse ->
            bidangreponse.bidang.let{
                for(element in it){
                    listIdBidang.add(element.id_bidang)
                    bidang.add(element.alias)
                }
                initSpinner(bidang)
            }
        }
        viewModel.kegiatanExternal.observe(this){ kegiatanResponse->
            kegiatanResponse.kegiatan_luar.let {
                adapter = KegiatanLuarAdapter(it)
                binding.rvAgenda.adapter = adapter
            }

        }
        viewModel.loadZero.observe(this){ isLoading->
            isLoading?.let {
                if(it){
                    tidakAdaSurat()
                }
                else{
                    adaSurat()
                }
            }
        }
    }

    fun initSpinner(bidang: ArrayList<String>){
        val arrayAdapter = ArrayAdapter(this, com.anychart.R.layout.support_simple_spinner_dropdown_item, bidang)
        binding.spinnerBidang.adapter = arrayAdapter
    }

    private fun initRecyclerView() {
        binding.rvAgenda.setHasFixedSize(true)
        val  layoutManager = LinearLayoutManager(this)
        binding.rvAgenda.layoutManager = layoutManager
    }

    private fun tidakAdaSurat(){
        binding.layoutKosong.visibility = View.VISIBLE
        binding.rvAgenda.visibility = View.GONE
    }

    private fun adaSurat(){
        binding.layoutKosong.visibility = View.GONE
        binding.rvAgenda.visibility = View.VISIBLE
    }

    fun setJudulPencarianHariIni(bidang:String){
        binding.txtTglAgenda.text = "Agenda external bidang $bidang hari ini ${GetDate.getTodayDate()}"
    }


    fun setJudulPencarianTanggal(bidang:String, tanggal:String){
        binding.txtTglAgenda.text = "Agenda external bidang $bidang tanggal $tanggal"
    }
}