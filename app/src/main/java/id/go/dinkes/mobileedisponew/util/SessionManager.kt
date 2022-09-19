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

    //cek Login

    fun createLoginSession(){
        editor.putBoolean("login_status", true)
        editor.commit()
    }
    fun isLogin(): Boolean{
        return preferences.getBoolean("login_status", false)
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

    fun setNIK(nik:String){
        editor.putString("nik", nik)
        editor.commit()
    }

    fun getNIK(): String{
        return preferences.getString("nik", "")!!
    }

    fun setToken(token:String){
        editor.putString("token", token)
        editor.commit()
    }

    fun getToken(): String{
        return preferences.getString("token", "")!!
    }

    fun logout(){
        editor.clear()
        editor.commit()
    }

}