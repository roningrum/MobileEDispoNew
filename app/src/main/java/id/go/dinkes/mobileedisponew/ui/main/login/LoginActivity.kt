package id.go.dinkes.mobileedisponew.ui.main.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import id.go.dinkes.mobileedisponew.databinding.ActivityLoginBinding
import id.go.dinkes.mobileedisponew.remote.RetrofitService
import id.go.dinkes.mobileedisponew.repository.DispoRepository
import id.go.dinkes.mobileedisponew.ui.main.home.MainActivity
import id.go.dinkes.mobileedisponew.util.SessionManager
import id.go.dinkes.mobileedisponew.viewmodel.DispoViewModelFactory

class LoginActivity : AppCompatActivity() {
    lateinit var viewModel: LoginViewModel
    lateinit var binding: ActivityLoginBinding
    lateinit var username:String
    lateinit var pass:String
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofitService = RetrofitService.getInstance()
        val repo = DispoRepository(retrofitService)

        sessionManager = SessionManager(this)
        viewModel = ViewModelProvider(this,DispoViewModelFactory(repo)).get(LoginViewModel::class.java)

        binding.btnMasuk.setOnClickListener {
            username = binding.etUsername.text.toString()
            pass = binding.etPassword.text.toString()
            viewModel.loginUser(username, pass)

            binding.progressBar.visibility = View.VISIBLE
            binding.btnMasuk.visibility = View.GONE

            Log.d("Login", "username $username")
        }

        observeViewModel()

        if(sessionManager.isLogin()){
            val intent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        viewModel.login.observe(this) {
            if(it.login.isEmpty()){
                Toast.makeText(this, "Periksa Username dan Password", Toast.LENGTH_SHORT).show()
            }
            else{
                sessionManager.createLoginSession()
                sessionManager.setUserId(it.login[0].user_id)
                sessionManager.setBidang(it.login[0].bidang)
                sessionManager.setRule(it.login[0].rule)
                sessionManager.setSeksi(it.login[0].seksi)
//                Log.d("userid", "id ${SessionManager(this).getUserId()}")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show()
            }

            Log.d("Login Masuk", "User ${it.login}")
            Log.d("Login User id", "User Id ${sessionManager.getUserId()}")
        }

        viewModel.errorMessage.observe(this){
            Toast.makeText(this, it,  Toast.LENGTH_SHORT).show()
        }

        viewModel.loading.observe(this){
            if(it){
                binding.progressBar.visibility = View.VISIBLE
                binding.btnMasuk.visibility = View.GONE
            } else{
                binding.progressBar.visibility = View.GONE
                binding.btnMasuk.visibility = View.VISIBLE
            }
        }
    }

    override fun onBackPressed() {
        val startMain = Intent(Intent.ACTION_MAIN)
        startMain.addCategory(Intent.CATEGORY_HOME)
        startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(startMain)
    }
}