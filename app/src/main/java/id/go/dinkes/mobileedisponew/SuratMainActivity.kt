package id.go.dinkes.mobileedisponew

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import id.go.dinkes.mobileedisponew.databinding.ActivitySuratMainBinding
import id.go.dinkes.mobileedisponew.ui.main.ViewPagerAdapter

class SuratMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySuratMainBinding
    private val menuarray = arrayOf(
        "Sudah DiProses",
        "Belum DiProses"
    )

    var jenis_surat: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySuratMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        jenis_surat = intent.getStringExtra("jenis")
        when(jenis_surat){
            "undangan" ->  binding.title.text = "Surat Undangan"
            "umum" -> binding.title.text = "Surat Umum"
            "dispo balik" -> binding.title.text = "Surat Dispo Balik"
        }

        val sectionsPagerAdapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        val viewPager= binding.viewPager
       viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs

        TabLayoutMediator(tabs, viewPager){tab, position ->
            tab.text = menuarray[position]
        }.attach()
    }
}