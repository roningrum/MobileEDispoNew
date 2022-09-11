package id.go.dinkes.mobileedisponew.ui.main.surat.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import id.go.dinkes.mobileedisponew.databinding.ItemSuratBinding
import id.go.dinkes.mobileedisponew.model.Surat
import id.go.dinkes.mobileedisponew.ui.main.surat.DetailSuratActivity
import id.go.dinkes.mobileedisponew.util.GetDate

class SuratAdapter (var data: List<Surat>, var context: FragmentActivity) : RecyclerView.Adapter<SuratAdapterVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuratAdapterVH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSuratBinding.inflate(inflater, parent, false)
        return SuratAdapterVH(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SuratAdapterVH, position: Int) {
       val surat = data[position]
        if(surat.tgl_terima.isNullOrEmpty()){
            holder.binding.txtTglSuratMasuk.text = " "
        }
        else{
            val newDate = GetDate.formatDate(surat.tgl_terima)
            holder.binding.txtTglSuratMasuk.text = "Terima : $newDate"

        }
        if(surat.tgl_dp.isNullOrEmpty()){
            holder.binding.txtTglDisposisi.text = " "

        }
        else{
            val newDate = GetDate.formatDate(surat.tgl_dp)
            holder.binding.txtTglDisposisi.text = "Disposisi : $newDate"
        }

        holder.binding.txtDari.text = surat.dari
        holder.binding.txtAcara.text = surat.acara
        holder.binding.txtTempat.text = surat.tempat

        if(surat.tgl_surat.isNullOrEmpty()){
            holder.binding.txtTanggalJam.text = ""
        }
        else{
            val tglSurat = GetDate.formatDate(surat.tgl_surat)
            holder.binding.txtTanggalJam.text = tglSurat
        }

        holder.binding.txtPerihal.text = surat.perihal_surat
        holder.binding.txtBidang.text = surat.disposisi1
        holder.binding.txtSeksi.text = surat.disposisi2
        holder.binding.txtHadir.text = surat.disposisi3
        holder.binding.txtKeterangan.text = surat.isi_dp
        holder.binding.txtIsiSurat.text = surat.isi_surat
        holder.binding.btnDetail.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailSuratActivity::class.java)
            intent.putExtra("id_surat", surat.id)
            holder.itemView.context.startActivity(intent)
            Log.d("id", "id_surat1 ${surat.id_surat}")
        }
        holder.binding.txtKeteranganDpBalik.text = surat.keterangan_dp_balik

    }

    override fun getItemCount(): Int {
        return data.size
    }
}

class SuratAdapterVH (val binding: ItemSuratBinding) : RecyclerView.ViewHolder(binding.root)
