package id.go.dinkes.mobileedisponew.ui.main.notulen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.webkit.WebView
import android.webkit.WebViewClient
import id.go.dinkes.mobileedisponew.R
import id.go.dinkes.mobileedisponew.databinding.ActivityLihatNotulenBinding

class LihatNotulenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLihatNotulenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLihatNotulenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val fileNotulen = intent.getStringExtra("notulen")
        val url = "http://119.2.50.170:9095/e_dispo/assets/temp/notulen/$fileNotulen"

        binding.imageview.visibility = GONE
        binding.webview.settings.javaScriptEnabled = true
        binding.webview.webViewClient = object : WebViewClient(){
            //                    override fun shouldOverrideUrlLoading(
//                        view: WebView?,
//                        request: WebResourceRequest?
//                    ): Boolean {
//                        view?.loadUrl("http://docs.google.com/gview?embedded=true&url=$urlFileSurat")
//                        return false
//                    }
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.progressbar.visibility = GONE
            }
        }
        binding.webview.loadUrl("http://docs.google.com/gview?embedded=true&url=$url")
    }
}