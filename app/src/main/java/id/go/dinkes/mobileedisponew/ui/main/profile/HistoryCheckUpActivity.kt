package id.go.dinkes.mobileedisponew.ui.main.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.data.Set
import com.anychart.enums.Anchor
import com.anychart.enums.MarkerType
import com.anychart.enums.TooltipPositionMode
import com.anychart.graphics.vector.Stroke
import id.go.dinkes.mobileedisponew.R
import id.go.dinkes.mobileedisponew.databinding.ActivityHistoryCheckUpBinding
import id.go.dinkes.mobileedisponew.databinding.ActivityProfileBinding
import id.go.dinkes.mobileedisponew.model.akm.DataCheckup
import id.go.dinkes.mobileedisponew.remote.NetworkRepo
import id.go.dinkes.mobileedisponew.repository.AKMRepository
import id.go.dinkes.mobileedisponew.repository.DispoRepository
import id.go.dinkes.mobileedisponew.ui.main.profile.adapter.HistoryCheckupAdapter
import id.go.dinkes.mobileedisponew.util.SessionManager
import id.go.dinkes.mobileedisponew.viewmodel.AKMViewModelFactory
import java.util.ArrayList

class HistoryCheckUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryCheckUpBinding
    private lateinit var kategori: String
    private lateinit var nik: String
    private lateinit var akmViewModel: AkmViewModel
    private lateinit var adapter: HistoryCheckupAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryCheckUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val akmService = NetworkRepo.getAKM()
        val repoAkm = AKMRepository(akmService)
        val sessionManager = SessionManager(this)

        kategori = intent.getStringExtra("kategori")!!
        nik = sessionManager.getNIK()
        binding.anyChartView.setProgressBar(binding.progressBar)


        akmViewModel = ViewModelProvider(this, AKMViewModelFactory(repoAkm))[AkmViewModel::class.java]
        when(kategori){
            "berat_badan"->{
                akmViewModel.getHistoryBeratBadan(nik)
            }
            "tinggi"->{
                akmViewModel.getHistoryTinggi(nik)
            }
            "tensi"->{
                akmViewModel.getHistoryTensi(nik)
            }
        }
        observeViewModel()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.RvHistory.layoutManager = LinearLayoutManager(this)
        binding.RvHistory.setHasFixedSize(true)
    }

    private fun observeViewModel() {
        akmViewModel.dataCheckUp.observe(this){
            when(kategori){
                "berat_badan" ->{
                    adapter = HistoryCheckupAdapter(it.data_checkup,  "berat_badan")
                    grafikBeratBadan(it.data_checkup)
                }
                "tinggi" -> {
                    adapter = HistoryCheckupAdapter(it.data_checkup, "tinggi")
                    grafikTinggi(it.data_checkup)
                }
                "tensi"->{
                    adapter = HistoryCheckupAdapter(it.data_checkup, "tensi")
                    grafikTensi(it.data_checkup)
                }
            }
            binding.RvHistory.adapter = adapter

        }
    }



    inner class CustomDataEntry(x:String?, value:Number?, value2:Number?):ValueDataEntry(x, value){
        init {
            setValue("value2", value2)
        }
    }
    inner class CustomDataEntry2(x:String?, value:Number?):ValueDataEntry(x, value)

    private fun grafikBeratBadan(dataCheckup: List<DataCheckup>) {
        val cartesian = AnyChart.line()
        cartesian.animation(true)
        cartesian.padding(10.0, 20.0, 5.0, 20.0)
        cartesian.crosshair().enabled(true)
        cartesian.crosshair()
            .yLabel(true)
            .yStroke(null as Stroke?,null, null, null as String?, null as String?)
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.title("Grafik Berat Badan")
        cartesian.yAxis(0).title("Angka Berat Badan")
        cartesian.xAxis(0).labels().padding(5.0, 5.0, 5.0, 5.0)

        val seriesData = ArrayList<DataEntry>()
        for(i in dataCheckup.indices){
            val berat = dataCheckup[i].berat.toDouble()
            val tgl_berat = dataCheckup[i].tgl_cek_berat
            seriesData.add(CustomDataEntry2(tgl_berat, berat))
        }

        val set = Set.instantiate()
        set.data(seriesData)
        val series1Mapping = set.mapAs("{ x: 'x', value: 'value' }")

        val series1 = cartesian.line(series1Mapping)
        series1.name("Berat Badan")
        series1.hovered().markers().enabled(true)
        series1.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(4.0)
        series1.tooltip()
            .position("right")
            .anchor(Anchor.LEFT_CENTER)
            .offsetX(5.0)
            .offsetY(5.0)

        cartesian.legend().enabled(true)
        cartesian.legend().fontSize(13.0)
        cartesian.legend().padding(0.0, 0.0, 10.0, 0.0)

        binding.anyChartView.setChart(cartesian)

    }
    private fun grafikTinggi(dataCheckup: List<DataCheckup>) {
        val cartesian = AnyChart.line()
        cartesian.animation(true)
        cartesian.padding(10.0, 20.0, 5.0, 20.0)
        cartesian.crosshair().enabled(true)
        cartesian.crosshair()
            .yLabel(true)
            .yStroke(null as Stroke?,null, null, null as String?, null as String?)
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.title("Grafik Tinggi Badan")
        cartesian.yAxis(0).title("Angka Tinggi Badan")
        cartesian.xAxis(0).labels().padding(5.0, 5.0, 5.0, 5.0)

        val seriesData = ArrayList<DataEntry>()
        for(i in dataCheckup.indices){
            val tinggi= dataCheckup[i].tinggi.toDouble()
            val tgl_tinggi = dataCheckup[i].tgl_cek_tinggi
            seriesData.add(CustomDataEntry2(tgl_tinggi, tinggi))
        }

        val set = Set.instantiate()
        set.data(seriesData)
        val series1Mapping = set.mapAs("{ x: 'x', value: 'value' }")

        val series1 = cartesian.line(series1Mapping)
        series1.name("Tinggi Badan")
        series1.hovered().markers().enabled(true)
        series1.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(4.0)
        series1.tooltip()
            .position("right")
            .anchor(Anchor.LEFT_CENTER)
            .offsetX(5.0)
            .offsetY(5.0)

        cartesian.legend().enabled(true)
        cartesian.legend().fontSize(13.0)
        cartesian.legend().padding(0.0, 0.0, 10.0, 0.0)

        binding.anyChartView.setChart(cartesian)

    }

    private fun grafikTensi(dataCheckup: List<DataCheckup>) {
        val cartesian = AnyChart.line()
        cartesian.animation(true)
        cartesian.padding(10.0, 20.0, 5.0, 20.0)
        cartesian.crosshair().enabled(true)
        cartesian.crosshair()
            .yLabel(true)
            .yStroke(null as Stroke?, null, null, null as String?, null as String?)
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.title("Grafik Tensi")
        cartesian.yAxis(0).title("Angka Tekanan Darah")
        cartesian.xAxis(0).labels().padding(5.0, 5.0, 5.0, 5.0)

        val seriesData = ArrayList<DataEntry>()
        for (i in dataCheckup.indices) {
            val sistol = dataCheckup[i].sistol.toDouble()
            val diastol = dataCheckup[i].diastol.toDouble()
            val tgl_tens= dataCheckup[i].tgl_cek_tensi
            seriesData.add(CustomDataEntry(tgl_tens, sistol, diastol))
        }

        val set = Set.instantiate()
        set.data(seriesData)
        val series1Mapping = set.mapAs("{ x: 'x', value: 'value' }")
        val series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }")

        val series1 = cartesian.line(series1Mapping)
        series1.name("Sistol")
        series1.hovered().markers().enabled(true)
        series1.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(4.0)
        series1.tooltip()
            .position("right")
            .anchor(Anchor.LEFT_CENTER)
            .offsetX(5.0)
            .offsetY(5.0)

        val series2 = cartesian.line(series2Mapping)
        series2.name("Diastol")
        series2.hovered().markers().enabled(true)
        series2.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(4.0)
        series2.tooltip()
            .position("right")
            .anchor(Anchor.LEFT_CENTER)
            .offsetX(5.0)
            .offsetY(5.0)

        cartesian.legend().enabled(true)
        cartesian.legend().fontSize(13.0)
        cartesian.legend().padding(0.0, 0.0, 10.0, 0.0)

        binding.anyChartView.setChart(cartesian)

    }

}

