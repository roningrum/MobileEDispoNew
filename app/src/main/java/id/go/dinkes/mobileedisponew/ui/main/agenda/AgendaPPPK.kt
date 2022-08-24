package id.go.dinkes.mobileedisponew.ui.main.agenda

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.go.dinkes.mobileedisponew.R
import id.go.dinkes.mobileedisponew.databinding.ActivityAgendaInternalBinding
import id.go.dinkes.mobileedisponew.databinding.ActivityAgendaPppkBinding
import id.go.dinkes.mobileedisponew.remote.RetrofitService
import id.go.dinkes.mobileedisponew.repository.DispoRepository
import id.go.dinkes.mobileedisponew.ui.main.agenda.adapter.KegiatanPPPKAdapter
import id.go.dinkes.mobileedisponew.util.GetDate
import id.go.dinkes.mobileedisponew.viewmodel.DispoViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AgendaPPPK : AppCompatActivity() {
    lateinit var binding: ActivityAgendaPppkBinding
    lateinit var viewModel: AgendaViewModel
    lateinit var adapter: KegiatanPPPKAdapter

    var tglUbah: String?=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgendaPppkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofitService = RetrofitService.getInstance()
        val repo = DispoRepository(retrofitService)
        viewModel = ViewModelProvider(this, DispoViewModelFactory(repo))[AgendaViewModel::class.java]

        viewModel.getKegiatanPPPK(GetDate.getTodayDate(), GetDate.getTodayDate())
        observeViewModel()
        initRecyclerView()

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


        binding.etTglCari.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tglUbah = binding.etTglCari.text.toString()
                viewModel.getKegiatanPPPK(tglUbah!!, tglUbah!!)
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

    }

    private fun initRecyclerView() {
        binding.rvAgenda.setHasFixedSize(true)
        val  layoutManager = LinearLayoutManager(this)
        binding.rvAgenda.layoutManager = layoutManager
    }

    private fun observeViewModel() {
        viewModel.loadZero.observe(this){
            if(it){
                tidakAdaSurat()
            }else{
                adaSurat()
            }
        }
        viewModel.kegiatanPPPK.observe(this)
        {
            it.let{
                adapter = KegiatanPPPKAdapter(it.kegiatanPPPK)
                binding.rvAgenda.adapter = adapter
            }
        }
        viewModel.loading.observe(this){ isLoading->
            isLoading?.let {
                binding.progressBar.visibility = if(it) View.VISIBLE else View.GONE
            }

        }
    }

    private fun tidakAdaSurat(){
        binding.layoutKosong.visibility = View.VISIBLE
        binding.rvAgenda.visibility = View.GONE
    }

    private fun adaSurat(){
        binding.layoutKosong.visibility = View.GONE
        binding.rvAgenda.visibility = View.VISIBLE
    }
}