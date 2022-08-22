package id.go.dinkes.mobileedisponew.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object GeDate {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getTodayDate(){
        val current = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd-MM-yyyy")
        val today = formatter.format(current)
    }
}