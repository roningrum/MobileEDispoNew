package id.go.dinkes.mobileedisponew.ui.main.surat.menudispo

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.go.dinkes.mobileedisponew.R
import id.go.dinkes.mobileedisponew.databinding.FragmentDialogDisposisiBinding
import id.go.dinkes.mobileedisponew.databinding.ItemDisposisiBinding
import id.go.dinkes.mobileedisponew.model.ItemDisposisi
import id.go.dinkes.mobileedisponew.model.ItemEditDisposisi
import id.go.dinkes.mobileedisponew.remote.RetrofitService
import id.go.dinkes.mobileedisponew.repository.DispoRepository
import id.go.dinkes.mobileedisponew.ui.main.surat.DetailSuratActivity
import id.go.dinkes.mobileedisponew.ui.main.surat.adapter.ItemDispoAdapter
import id.go.dinkes.mobileedisponew.util.SessionManager
import id.go.dinkes.mobileedisponew.viewmodel.DispoViewModelFactory

class DialogDisposisiFragment : DialogFragment() {
    private lateinit var binding: FragmentDialogDisposisiBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var itemDispoAdapter: ItemDispoAdapter
    private var listItemEditDispo = ArrayList<ItemEditDisposisi>()
    private var listItemDispo = ArrayList<ItemDisposisi>()
    private var listIdDispo = ArrayList<String>()

    private lateinit var menuViewModel: MenuDispoViewModel

    var idSurat: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDialogDisposisiBinding.inflate(inflater, container, false)
        val retrofitService = RetrofitService.getInstance()
        val repo = DispoRepository(retrofitService)
        menuViewModel = ViewModelProvider(this, DispoViewModelFactory(repo))[MenuDispoViewModel::class.java]
        sessionManager = SessionManager(requireContext())

        val args =arguments
        idSurat = args?.getString("id_surat").toString()

        binding.btnDisposisi.setOnClickListener {
            if(listIdDispo.isNullOrEmpty()){
                Toast.makeText(this.context, "Tujuan disposisi tidak boleh kosong", Toast.LENGTH_LONG).show()
            } else{
                if(sessionManager.getBidang() == "3" && listIdDispo.any{it in "19"}){
                    removeItemDisposisi("19")
                    dispoSurat("19")
                }else{
                    dispoSurat("")
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        menuViewModel.getItemDisposisi(sessionManager.getRule(), sessionManager.getBidang(), sessionManager.getSeksi())
        menuViewModel.getItemEditDisposisi(idSurat, sessionManager.getRule())
        observeViewModel()
        initRecyclerViewModel()

    }

    override fun onResume() {
        menuViewModel.getItemDisposisi(sessionManager.getRule(), sessionManager.getBidang(), sessionManager.getSeksi())
        super.onResume()
    }

    private fun initRecyclerViewModel() {
        binding.rvItemDisposisi.setHasFixedSize(true)
        binding.rvItemDisposisi.layoutManager = LinearLayoutManager(this.context)
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.getWindow()?.setLayout(width, height)
        }
    }

    private fun observeViewModel() {
       menuViewModel.listItemEditDiposisi.observe(this){
           it.let {
               listItemEditDispo = it.item_edit_disposisi as ArrayList<ItemEditDisposisi>
           }

       }
        menuViewModel.listItemDisposisi.observe(this){
            listItemDispo = it.item_disposisi as ArrayList<ItemDisposisi>
            if(sessionManager.getBidang() == "3"){
                val tambahModel = ItemDisposisi( id = "19", nama = "Instalasi Farmasi")
               listItemDispo.add(tambahModel)
            }
            itemDispoAdapter = ItemDispoAdapter(listItemDispo, listItemEditDispo, this)
            binding.rvItemDisposisi.adapter = itemDispoAdapter
        }

        menuViewModel.loading.observe(this){
            it.let {
               if(it) showProgressbar() else hideProgressbar()
            }
        }
        menuViewModel.successMessage.observe(this){
            val success = it.success
            val message = it.message

            if(success == "1"){
                hideProgressbar()
                (activity as DetailSuratActivity).showDialogBerhasil()
            } else{
                hideProgressbar()
                Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun addItemDisposisi(id:String){
        listIdDispo.add(id)
    }

    fun removeItemDisposisi(id:String){
       listIdDispo.remove(id)
    }

    fun showProgressbar(){
        binding.progressBar.visibility = View.VISIBLE
    }

    fun hideProgressbar(){
        binding.progressBar.visibility = View.GONE
    }

    fun dispoSurat(instalasiSurat: String){
        val disposisiString = listIdDispo.joinToString(
            prefix = "[",
            separator = ",",
            postfix = "]",
            transform = {"\"${it}\""})
        menuViewModel.getPostDispo(idSurat, disposisiString, binding.etIsiDisposisi.text.toString(), sessionManager.getRule(), sessionManager.getBidang(),
            sessionManager.getSeksi(), sessionManager.getUserId(), instalasiSurat
        )
    }
}