package id.go.dinkes.mobileedisponew.ui.main.agenda.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.go.dinkes.mobileedisponew.databinding.ItemKegiatanExternalBinding
import id.go.dinkes.mobileedisponew.model.KegiatanLuar
import id.go.dinkes.mobileedisponew.util.GetDate

class KegiatanLuarAdapter(var data : List<KegiatanLuar>): RecyclerView.Adapter<KegiatanLuarVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KegiatanLuarVH {
       val inflater = LayoutInflater.from(parent.context)
        val binding = ItemKegiatanExternalBinding.inflate(inflater, parent, false)
        return KegiatanLuarVH(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: KegiatanLuarVH, position: Int) {
        val kegiatan = data[position]
        holder.binding.txtAcara.text = kegiatan.kegiatan
        holder.binding.txtTempat.text = kegiatan.lokasi
        holder.binding.txtAsal.text = kegiatan.asal
        holder.binding.txtHadir.text = kegiatan.disposisi
        holder.binding.txtPetugas.text = kegiatan.petugas
        holder.binding.txtKeterangan.text = kegiatan.deskripsi

        val tglPelaksanaan = GetDate.formatDate(kegiatan.tgl_kegiatan)
        holder.binding.txtTanggalJam.text = "${kegiatan.hari}, $tglPelaksanaan ${kegiatan.jam}"
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
class KegiatanLuarVH(val binding: ItemKegiatanExternalBinding) : RecyclerView.ViewHolder(binding.root)