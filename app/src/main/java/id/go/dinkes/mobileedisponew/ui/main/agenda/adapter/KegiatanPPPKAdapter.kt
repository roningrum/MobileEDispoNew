package id.go.dinkes.mobileedisponew.ui.main.agenda.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import id.go.dinkes.mobileedisponew.databinding.ItemAgendaPppkBinding
import id.go.dinkes.mobileedisponew.model.KegiatanPPPK
import id.go.dinkes.mobileedisponew.util.GetDate
import kotlinx.coroutines.withContext

@SuppressLint("SetTextI18n")
class KegiatanPPPKAdapter(var kegiatanPPPK: List<KegiatanPPPK>): RecyclerView.Adapter<KegiatanPPPKViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KegiatanPPPKViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAgendaPppkBinding.inflate(inflater, parent, false)
        return KegiatanPPPKViewHolder(binding)
    }
    override fun onBindViewHolder(holder: KegiatanPPPKViewHolder, position: Int) {
       val pppk = kegiatanPPPK[position]
        val context = holder.itemView.context
        holder.binding.txtAcara.text = pppk.kegiatan
        holder.binding.txtTempat.text = pppk.lokasi
        holder.binding.txtPelaksana.text = pppk.pelaksana
        holder.binding.txtPj.text = "${pppk.penanggung_jawab} ${pppk.no_telp_pj}"
        holder.binding.txtKeterangan.text = pppk.deskripsi

        val newDate1 = GetDate.formatDate(pppk.tgl_kegiatan_1)
        val newDate2 = GetDate.formatDate(pppk.tgl_kegiatan_2)

        if(pppk.tgl_kegiatan_1 == pppk.tgl_kegiatan_2){
            holder.binding.txtTanggalJam.text = "$newDate1 (${pppk.jam})"
        }
        else{
            holder.binding.txtTanggalJam.text = "$newDate1 s/d $newDate2 (${pppk.jam})"
        }

        holder.binding.btnTelepon.setOnClickListener {
            if(pppk.no_telp_pj.isNullOrEmpty()){
                Toast.makeText(context,"Nomor Telepon penanggung jawab tidak tercantum", Toast.LENGTH_LONG).show()
            }
            else{
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:" + pppk.no_telp_pj)
                context.startActivity(intent)
            }
        }
        holder.binding.btnSms.setOnClickListener {
            if (pppk.no_telp_pj.isNullOrEmpty()){
                Toast.makeText(context, "Nomor telepon penanggung jawab tidak tercantum", Toast.LENGTH_LONG).show()
            }else{
                val smsIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+pppk.no_telp_pj))
                context.startActivity(smsIntent)
            }
        }

    }

    override fun getItemCount(): Int {
        return kegiatanPPPK.size
    }

}
class KegiatanPPPKViewHolder(val binding: ItemAgendaPppkBinding): RecyclerView.ViewHolder(binding.root)