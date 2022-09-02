package id.go.dinkes.mobileedisponew

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import id.go.dinkes.mobileedisponew.databinding.ActivityLihatFileSuratBinding

class LihatFileSurat : AppCompatActivity() {
    lateinit var binding: ActivityLihatFileSuratBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLihatFileSuratBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fileSurat = intent.getStringExtra("file_surat")
        val urlFileSurat = "http://119.2.50.170:9095/e_dispo/assets/temp/file_surat/$fileSurat"

        if(fileSurat != null){
            if((fileSurat.takeLast(3) == "png") or (fileSurat.takeLast(3) == "jpg")){
                Picasso.get()
                    .load(urlFileSurat)
                    .into(binding.imageview, object : Callback{
                        override fun onSuccess() {
                            binding.progressbar.visibility = GONE
                            binding.webview.visibility = GONE
                        }

                        override fun onError(e: Exception?) {
//                        Toast.makeText(applicationContext, "Gagal Memuat", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
            else if(fileSurat.takeLast(3) == "pdf"){
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
                binding.webview.loadUrl("http://docs.google.com/gview?embedded=true&url=$urlFileSurat")

            }


        }
        else{
            binding.progressbar.visibility = GONE
            binding.layoutKosong.visibility = VISIBLE
        }


    }
}