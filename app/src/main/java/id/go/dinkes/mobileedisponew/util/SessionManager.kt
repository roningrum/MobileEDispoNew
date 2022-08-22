package id.go.dinkes.mobileedisponew.util

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    var preferences: SharedPreferences
    var editor: SharedPreferences.Editor
    private val PREFS_NAME = "SharedPreferenceDemo"

    init {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        editor = preferences.edit()
    }

    fun setUserId(userId:String){
        editor.putString("id_user", userId)
        editor.commit()
    }

    fun getUserId(): String{
        return preferences.getString("id_user", "")!!
    }

    fun setRule(rule:String){
        editor.putString("rule", rule)
        editor.commit()
    }

    fun getRule():String{
        return preferences.getString("rule", "")!!
    }

    fun setBidang(bidang:String){
        editor.putString("bidang", bidang)
        editor.commit()
    }

    fun getBidang():String{
        return preferences.getString("bidang", "")!!
    }

    fun setSeksi(seksi:String){
        editor.putString("seksi", seksi)
        editor.commit()
    }

    fun getSeksi(): String{
        return preferences.getString("seksi", "")!!
    }



}