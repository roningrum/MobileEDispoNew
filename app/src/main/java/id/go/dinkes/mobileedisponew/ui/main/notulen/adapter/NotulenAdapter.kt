package id.go.dinkes.mobileedisponew.ui.main.notulen.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.go.dinkes.mobileedisponew.databinding.ItemNotulenBinding
import id.go.dinkes.mobileedisponew.model.ItemNotulen
import id.go.dinkes.mobileedisponew.ui.main.notulen.LihatNotulenActivity
import id.go.dinkes.mobileedisponew.util.GetDate

class NotulenAdapter(var data: List<ItemNotulen>): RecyclerView.Adapter<NotulenHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotulenHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNotulenBinding.inflate(inflater, parent, false)
        return NotulenHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: NotulenHolder, position: Int) {
        val notulen = data[position]
        if(notulen.tgl_terima.isNullOrEmpty()){
            holder.binding.txtTglSuratMasuk.text = ""
        } else{
            val newDate = GetDate.formatDate(notulen.tgl_terima)
            holder.binding.txtTglSuratMasuk.text = "Terima :$newDate"
        }

        if(notulen.tgl_surat.isNullOrEmpty()){
            holder.binding.txtTanggalJam.text = ""
        }
        else{
            holder.binding.txtTanggalJam.text = GetDate.formatDate(notulen.tgl_surat)
        }
        holder.binding.txtTglDisposisi.text = ""
        holder.binding.txtDari.text = notulen.dari
        holder.binding.txtAcara.text = notulen.acara
        holder.binding.txtTempat.text = notulen.tempat
        holder.binding.txtPerihal.text = notulen.perihal_surat
        holder.binding.txtBidang.text = notulen.disposisi1
        holder.binding.txtSeksi.text = notulen.disposisi2
        holder.binding.txtHadir.text = notulen.disposisi3
        holder.binding.txtKeterangan.text = notulen.keterangan
        holder.binding.txtIsiSurat.text = notulen.isi_surat
        holder.binding.btnLihatNotulen.setOnClickListener {
            val intent = Intent(holder.itemView.context, LihatNotulenActivity::class.java)
            intent.putExtra("notulen", notulen.notulen)
            holder.itemView.context.startActivity(intent)
        }
        holder.binding.txtKeteranganDpBalik.text = ""
    }

    override fun getItemCount(): Int {
        return data.size
    }

}

class NotulenHolder(val binding: ItemNotulenBinding): RecyclerView.ViewHolder(binding.root)
