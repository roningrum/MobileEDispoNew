package id.go.dinkes.mobileedisponew.model

import com.google.gson.annotations.SerializedName

data class LoginUser(
    @SerializedName("login")
    val login: List<Login>
)