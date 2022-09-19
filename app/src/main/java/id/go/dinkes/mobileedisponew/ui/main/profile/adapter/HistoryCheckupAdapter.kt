package id.go.dinkes.mobileedisponew.ui.main.profile.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.go.dinkes.mobileedisponew.databinding.ItemHistoryCheckupBinding
import id.go.dinkes.mobileedisponew.model.akm.DataCheckup
import id.go.dinkes.mobileedisponew.util.GetDate

class HistoryCheckupAdapter(private val dataCheckup: List<DataCheckup>, private val category:String) : RecyclerView.Adapter<HistoryHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHistoryCheckupBinding.inflate(inflater, parent, false)
        return HistoryHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HistoryHolder, position: Int) {
        val historyCheckUp = dataCheckup[position]
        when(category){
            "berat_badan" -> {
                holder.binding.txtNilaiCheckup.text = historyCheckUp.berat+"kg"
                holder.binding.txtTglCheckup.text = "Tanggal :${GetDate.formatDateWithTime(historyCheckUp.tgl_cek_berat)}  WIB"
            }
            "tinggi"->{
                holder.binding.txtNilaiCheckup.text = historyCheckUp.tinggi+"cm"
                holder.binding.txtTglCheckup.text = "Tanggal :${GetDate.formatDateWithTime(historyCheckUp.tgl_cek_tinggi)} WIB"
            }
            "tensi"->{
                holder.binding.txtNilaiCheckup.text = "${historyCheckUp.sistol}/${historyCheckUp.diastol}"
                holder.binding.txtTglCheckup.text = "Tanggal :${GetDate.formatDateWithTime(historyCheckUp.tgl_cek_tensi)}  WIB"
            }
        }


    }

    override fun getItemCount(): Int {
       return dataCheckup.size
    }

}
class HistoryHolder(val binding: ItemHistoryCheckupBinding): RecyclerView.ViewHolder(binding.root)
