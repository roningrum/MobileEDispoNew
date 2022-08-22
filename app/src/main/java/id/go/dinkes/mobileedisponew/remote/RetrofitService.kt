package id.go.dinkes.mobileedisponew.remote

import com.google.gson.GsonBuilder
import id.go.dinkes.mobileedisponew.BuildConfig
import id.go.dinkes.mobileedisponew.model.Agenda
import id.go.dinkes.mobileedisponew.model.LoginUser
import id.go.dinkes.mobileedisponew.model.SuratResponse
import id.go.dinkes.mobileedisponew.model.UserDetail
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface RetrofitService {
    //Login
    @GET("login")
    suspend fun getLogin(
        @Query("username") username: String,
        @Query("password") password: String
    ): Response<LoginUser>

    //user detail
    @GET("get_detail_userbyid")
    suspend fun getDetailUser(
        @Query("user_id") user_id: String
    ): Response<UserDetail>

    //dapat agenda rentang hari ini
    @GET("agenda")
    suspend fun getAgenda(
        @Query("dari") dari:String?,
        @Query("sampai") sampai:String?,
    ): Response<Agenda>

    //dapat Segala Jenis Surat
    @GET("surat_dp")
    suspend fun getSuratDp(
        @Query("jenis") jenis:String?,
        @Query("rule") rule: String?,
        @Query("bidang") bidang: String?,
        @Query("seksi") seksi: String,
        @Query("user_id") user_id: String?,
        @Query("status1") status1: String?,
        @Query("status2") status2: String?
    ): Response<SuratResponse>

    //dapat surat berdasarkan tanggal
    @GET("surat_dp_by_tgl")
    suspend fun getSuratDpbyTgl(
        @Query("jenis") jenis:String?,
        @Query("rule") rule: String?,
        @Query("bidang") bidang: String?,
        @Query("seksi") seksi: String,
        @Query("user_id") user_id: String?,
        @Query("status1") status1: String?,
        @Query("status2") status2: String?,
        @Query("tgl_agenda") tgl_agenda: String?
    ): Response<SuratResponse>

    //dispo balik
    @GET("list_dp_balik")
    suspend fun getListDpBalik(
        @Query("rule") rule: String?,
        @Query("bidang") bidang: String?,
        @Query("seksi") seksi: String,
        @Query("notif_dp_balik") notif_dp_balik: String
    ): Response<SuratResponse>

    companion object{
        var retrofitService: RetrofitService? = null
        var client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
        //retrofit instance service
        fun getInstance():RetrofitService{
            if(retrofitService == null){
                val gson = GsonBuilder().setLenient().create()
                val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(BuildConfig.URL_DISPO)
                    .client(client)
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return  retrofitService!!
        }
    }
}