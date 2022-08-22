package id.go.dinkes.mobileedisponew.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import id.go.dinkes.mobileedisponew.R
import id.go.dinkes.mobileedisponew.databinding.ItemAgendaBinding
import id.go.dinkes.mobileedisponew.model.Data
import id.go.dinkes.mobileedisponew.util.GetDate

class TodayAgenda(var agendaToday: List<Data>): RecyclerView.Adapter<TodayAgendaVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayAgendaVH {
       val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAgendaBinding.inflate(inflater, parent, false)
        return TodayAgendaVH(binding)
    }

    override fun onBindViewHolder(holder: TodayAgendaVH, position: Int) {
        val agenda = agendaToday[position]
        holder.binding.txtAcara.text = agenda.acara
        holder.binding.txtTempat.text = agenda.tempat
        holder.binding.txtBidang.text = agenda.disposisi1
        holder.binding.txtSeksi.text = agenda.disposisi2
        holder.binding.txtHadir.text = agenda.disposisi3
        holder.binding.txtSemuaHadir.text= agenda.semua_penerima

        val tglLaksana1 = GetDate.formatDate(agenda.tanggal)
        val tglLaksana2 = GetDate.formatDate(agenda.tanggal2)

        if(agenda.tanggal == agenda.tanggal2){
            (tglLaksana1+" ("+agenda.jam+")").also { holder.binding.txtTanggalJam.text = it }
        }
        else{
            (tglLaksana1+"s/d"+tglLaksana2+" ("+agenda.jam+")").also { holder.binding.txtTanggalJam.text = it }
        }

        if(agenda.tanggal < GetDate.getCurrentDate() ){
            holder.binding.cardAgenda.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.colorAbuMuda))
        }
        else{
            holder.binding.cardAgenda.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
        }

    }

    override fun getItemCount(): Int {
        return agendaToday.size
    }
}
class TodayAgendaVH(val binding: ItemAgendaBinding): RecyclerView.ViewHolder(binding.root){

}