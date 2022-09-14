package id.go.dinkes.mobileedisponew.ui.main.home

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.go.dinkes.mobileedisponew.databinding.ActivityCariAgendaBinding
import id.go.dinkes.mobileedisponew.remote.NetworkRepo
import id.go.dinkes.mobileedisponew.remote.RetrofitService
import id.go.dinkes.mobileedisponew.repository.DispoRepository
import id.go.dinkes.mobileedisponew.ui.main.home.adapter.TodayAgenda
import id.go.dinkes.mobileedisponew.viewmodel.DispoViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CariAgenda : AppCompatActivity() {
    lateinit var binding: ActivityCariAgendaBinding
    lateinit var homeViewModel: HomeViewModel
    lateinit var todayAgenda: TodayAgenda

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCariAgendaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofitService = NetworkRepo.getDispo()
        val repo = DispoRepository(retrofitService)
        val search =  binding.etTglCari
        homeViewModel = ViewModelProvider(this, DispoViewModelFactory(repo)).get(HomeViewModel::class.java)

        initRecyclerView()
        observeViewModel()
       search.setOnClickListener{
            val calendar = Calendar.getInstance()
            val year =calendar.get(Calendar.YEAR)
            val month =calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                // on below line we are passing context.
                this,
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

        search.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.layoutInisial.visibility = GONE
               homeViewModel.getAgendaSearch(search.text.toString(), search.text.toString())
                var newDate: String?=""
                val strDate = search.text.toString()
                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val convertedDate: Date
                try{
                    convertedDate = dateFormat.parse(strDate)
                    val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                    newDate = sdf.format(convertedDate)
                }catch (e: Exception){
                    e.printStackTrace()
                }
                binding.progressBar.visibility = VISIBLE

                CoroutineScope(IO).launch {
                    delay(8000)
                    if(search.text.toString() != search.text.toString() )
                        return@launch
                    binding.progressBar.visibility = GONE
                }
                binding.txtTglAgenda.text = "Hasil pencarian Agenda Tangga $newDate"

            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    private fun observeViewModel(){
        homeViewModel.loading.observe(this){ isLoading ->
            isLoading?.let {
                binding.progressBar.visibility = if(it) VISIBLE else GONE
            }

        }
        homeViewModel.agenda.observe(this){
            if(it.result.data.isNullOrEmpty()){
                tidakAdaSurat()
            }
            else{
                todayAgenda = TodayAgenda(it.result.data)
                binding.rvAgenda.adapter = todayAgenda
                adaSurat()
            }

        }
    }

    private fun initRecyclerView() {
        binding.rvAgenda.setHasFixedSize(true)
        val  layoutManager = LinearLayoutManager(this)
        binding.rvAgenda.layoutManager = layoutManager
    }

    private fun tidakAdaSurat(){
        binding.layoutKosong.visibility = VISIBLE
        binding.rvAgenda.visibility = View.GONE
    }

    private fun adaSurat(){
        binding.layoutKosong.visibility = View.GONE
        binding.rvAgenda.visibility = VISIBLE
    }


}