package id.go.dinkes.mobileedisponew.ui.main.home.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import id.go.dinkes.mobileedisponew.databinding.ItemPenerimaSuratBinding
import id.go.dinkes.mobileedisponew.model.PenerimaSurat

@SuppressLint("SetTextI18n")
class PenerimaSuratAdapter (private var data: List<PenerimaSurat>) : RecyclerView.Adapter<PenerimaSuratVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PenerimaSuratVH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPenerimaSuratBinding.inflate(inflater, parent, false)
        return PenerimaSuratVH(binding)
    }
    override fun onBindViewHolder(holder: PenerimaSuratVH, position: Int) {
        val penerima = data[position]
        if(penerima.telp.isNullOrEmpty()){
            holder.binding.txtNama.text = "Tidak dicantumkan nomor telp"
        } else{
            holder.binding.txtNama.text = "${penerima.nama}(${penerima.telp})"
        }

        holder.binding.btnTelepon.setOnClickListener {
            if(penerima.telp.isNullOrEmpty()){
                Toast.makeText(holder.itemView.context, "Nomor Telepon Pengguna Belum Diisi Melalui Profile E-Dispo", Toast.LENGTH_SHORT).show()
            }
            else{
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:"+ penerima.telp)
                holder.itemView.context.startActivity(intent)
            }
        }


    }

    override fun getItemCount(): Int {
       return data.size
    }
}
class PenerimaSuratVH(val binding: ItemPenerimaSuratBinding): RecyclerView.ViewHolder(binding.root){}