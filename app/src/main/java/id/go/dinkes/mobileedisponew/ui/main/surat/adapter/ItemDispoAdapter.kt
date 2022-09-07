package id.go.dinkes.mobileedisponew.ui.main.surat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import id.go.dinkes.mobileedisponew.databinding.ItemDisposisiBinding
import id.go.dinkes.mobileedisponew.model.ItemDisposisi
import id.go.dinkes.mobileedisponew.model.ItemEditDisposisi
import id.go.dinkes.mobileedisponew.ui.main.surat.menudispo.DialogDisposisiFragment

class ItemDispoAdapter(var data:List<ItemDisposisi>, var dataEdit: List<ItemEditDisposisi>, var dialog:DialogDisposisiFragment): RecyclerView.Adapter<ItemDispoHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemDispoHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDisposisiBinding.inflate(inflater, parent, false)
        return ItemDispoHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemDispoHolder, position: Int) {
       holder.binding.checkbox.text = data[position].nama
        val arrayListEditString : MutableList<String> = mutableListOf()

        if(dataEdit.isNullOrEmpty()){
            Toast.makeText(holder.itemView.context, "Harus diisi", Toast.LENGTH_SHORT).show()
        } else{
            for(i in dataEdit.indices){
                arrayListEditString.add(dataEdit[i].id_dp)
            }
        }
        if (data[position].id in arrayListEditString){
            holder.binding.checkbox.isChecked = true
            holder.binding.checkbox.isEnabled = false
        }

        holder.binding.checkbox.setOnClickListener {
            if(holder.binding.checkbox.isChecked){
                dialog.addItemDisposisi(data[position].id)
            } else{
                dialog.removeItemDisposisi(data[position].id)
            }
//            if(holder.binding.checkbox.isChecked){
//                if()
//            }
//        }
        }
    }

    override fun getItemCount(): Int {
       return data.size
    }
}
class ItemDispoHolder (val binding: ItemDisposisiBinding): RecyclerView.ViewHolder(binding.root)