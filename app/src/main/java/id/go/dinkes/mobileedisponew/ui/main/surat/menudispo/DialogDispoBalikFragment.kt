package id.go.dinkes.mobileedisponew.ui.main.surat.menudispo

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import id.go.dinkes.mobileedisponew.R
import id.go.dinkes.mobileedisponew.databinding.ActivityProfileBinding
import id.go.dinkes.mobileedisponew.databinding.FragmentDialogDispoBalikBinding
import id.go.dinkes.mobileedisponew.remote.NetworkRepo
import id.go.dinkes.mobileedisponew.remote.RetrofitService
import id.go.dinkes.mobileedisponew.repository.DispoRepository
import id.go.dinkes.mobileedisponew.ui.main.surat.DetailSuratActivity
import id.go.dinkes.mobileedisponew.ui.main.surat.SuratViewModel
import id.go.dinkes.mobileedisponew.util.SessionManager
import id.go.dinkes.mobileedisponew.viewmodel.DispoViewModelFactory
import java.util.zip.Inflater

class DialogDispoBalikFragment : DialogFragment() {
    private lateinit var session: SessionManager
    private lateinit var binding: FragmentDialogDispoBalikBinding
    var id_Surat : String = ""
    private lateinit var menuViewModel: MenuDispoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        binding = FragmentDialogDispoBalikBinding.inflate(inflater, container, false)
        val retrofitService = NetworkRepo.getDispo()
        val repo = DispoRepository(retrofitService)
        menuViewModel = ViewModelProvider(this@DialogDispoBalikFragment, DispoViewModelFactory(repo))[MenuDispoViewModel::class.java]
        session = SessionManager(requireContext())

        val args =arguments
        id_Surat = args?.getString("id_surat").toString()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnDispoBalik.setOnClickListener {
            if(binding.etIsiDisposisi.length()<1){
                Toast.makeText(requireContext(), "Alasan dispo balik tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
            else{
                menuViewModel.getDispoBalik(id_Surat, binding.etIsiDisposisi.text.toString(), session.getRule(), session.getBidang(), session.getSeksi(), session.getUserId())
            }
        }
        observeViewModel()
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)
        }
    }

    private fun observeViewModel() {
        menuViewModel.successMessage.observe(this){
            val success = it.success
            val message = it.message

            if(success == "1"){
//                binding.progressBar.visibility = GONE
                dismiss()
                (activity as DetailSuratActivity).showDialogBerhasil()
            }
            else{
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
        menuViewModel.loading.observe(this){
            it.let {
                binding.progressBar.visibility = if(it) View.VISIBLE else GONE
            }
        }
    }

}