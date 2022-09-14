package id.go.dinkes.mobileedisponew.util

import java.text.SimpleDateFormat
import java.util.*

object GetDate {
    fun getTodayDate(): String{
        val current = Date()
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return formatter.format(current)
    }

    fun getCurrentDate(): String{
        val current = Date()
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return formatter.format(current)
    }

    fun formatDate(tgl:String): String? {

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateCurrent = dateFormat.parse(tgl)
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return dateCurrent?.let { formatter.format(it) }    }

    fun formatDateWithTime(tgl: String): String?{
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val dateCurrent = dateFormat.parse(tgl)
        val formatter = SimpleDateFormat("dd-MM-yyyy \n HH.mm", Locale.getDefault())
        return dateCurrent?.let { formatter.format(it) }
    }

}