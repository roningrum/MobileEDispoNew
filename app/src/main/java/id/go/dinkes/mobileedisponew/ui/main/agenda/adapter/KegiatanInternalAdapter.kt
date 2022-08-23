package id.go.dinkes.mobileedisponew.ui.main.agenda.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.go.dinkes.mobileedisponew.databinding.ItemKegiatanInternalBinding
import id.go.dinkes.mobileedisponew.model.KegiatanInternal
import id.go.dinkes.mobileedisponew.util.GetDate

class KegiatanInternalAdapter(var data : List<KegiatanInternal>) :
    RecyclerView.Adapter<KegiatanInternalVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KegiatanInternalVH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemKegiatanInternalBinding.inflate(inflater, parent, false)
        return KegiatanInternalVH(binding)
    }

    override fun onBindViewHolder(holder: KegiatanInternalVH, position: Int) {
       val kegiatanInternal = data[position]
        holder.binding.txtAcara.text = kegiatanInternal.kegiatan
        holder.binding.txtTempat.text = kegiatanInternal.lokasi
        holder.binding.txtHadir.text = kegiatanInternal.dihadiri
        holder.binding.txtSeksi.text = kegiatanInternal.disposisi
        holder.binding.txtKeterangan.text = kegiatanInternal.deskripsi

        val newDate = GetDate.formatDate(kegiatanInternal.tgl_kegiatan)
        holder.binding.txtTanggalJam.text = newDate
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
class KegiatanInternalVH(val binding: ItemKegiatanInternalBinding): RecyclerView.ViewHolder(binding.root)