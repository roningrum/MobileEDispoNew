package id.go.dinkes.mobileedisponew

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.go.dinkes.mobileedisponew.databinding.ActivityDetailSuratBinding

class DetailSuratActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailSuratBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailSuratBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title
//        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
    }
}