package id.go.dinkes.mobileedisponew.remote

import com.google.gson.GsonBuilder
import id.go.dinkes.mobileedisponew.BuildConfig
import id.go.dinkes.mobileedisponew.model.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
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

    //detail Agenda
    @GET("get_surat_byid")
    suspend fun getSuratId(
        @Query("id") id:String
    ): Response<SuratResponse>

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


    //dispo khusus rule Kadin
    @GET("get_surat_dp_kadin_sudah_diproses")
    suspend fun getSuratDPbyKadin(
        @Query("jenis") jenis: String?
    ):Response<SuratResponse>

    //search Surat Kadin by tanggal
    @GET("get_surat_dp_kadin_sudah_diproses_by_tgl")
    suspend fun getSuratDPKadinbyTgl(
        @Query("jenis") jenis: String?,
        @Query("tgl_terima") tgl_terima: String?
    ):Response<SuratResponse>

    //bidang
    @GET("get_bidang")
    suspend fun getBidang():Response<BidangResponse>

    //kegiatan eksternal
    @GET("get_kegiatan_luar_bybidang")
    suspend fun getKegiatanExternalBidang(
        @Query("id_bidang") id_bidang: String?,
        @Query("tgl_kegiatan") tgl_kegiatan: String?
    ): Response<KegiatanLuarResponse>


    //kegiatan internal
    @GET("get_kegiatan_internal_bybidang")
    suspend fun getKegiatanInternalBidang(
        @Query("id_bidang") id_bidang: String?,
        @Query("tgl_kegiatan") tgl_kegiatan: String?
    ): Response<KegiatanInternalResponse>

    //kegiatan PPPK
    @GET("get_pppk")
    suspend fun getPPPK(
        @Query("dari") dari: String,
        @Query("sampai") sampai: String
    ): Response<KegiatanPPPKResponse>

    //get Item Disposisi Rule Kadin
    @GET("get_item_disposisi")
    suspend fun getItemDisposisi(
        @Query("rule") rule: String?,
        @Query("bidang") bidang: String?,
        @Query("seksi") seksi: String
    ): Response<ItemDisposisiResponse>

    //get Item Edit Disposisi
    @GET("get_item_edit_disposisi")
    suspend fun getItemEditDisposisi(
        @Query("id_surat") idSurat: String?,
        @Query("rule") rule: String?
    ): Response<ItemEditDisposisiResponse>

    //form terima Surat Disposisi

    //mendispo
    @FormUrlEncoded
    @POST("post_disposisi")
    suspend fun postDisposisi(
        @Field("id_surat") id_surat: String?,
        @Field("disposisi") disposisi: String?,
        @Field("isi_dp") isi_dp: String?,
        @Field("rule") rule: String?,
        @Field("id_bidang") id_bidang: String?,
        @Field("id_seksi") id_seksi: String?,
        @Field("user_id") user_id: String?,
        @Field("if") instalasi_farmasi: String?
    ): Response<SuccessMessage>

    //oleh Kadin
    @FormUrlEncoded
    @POST("terima_kadin")
    suspend fun terimaKadin(
        @Field("id_surat") id_surat: String?,
        @Field("user_id") user_id: String?,
        @Field("id_bidang") id_bidang: String?
    ): Response<SuccessMessage>

    //oleh Kabid
    @FormUrlEncoded
    @POST("terima_kabid")
    suspend fun terimaKabid(
        @Field("id_surat") id_surat: String?,
        @Field("user_id") user_id: String?,
        @Field("id_bidang") id_bidang: String?
    ): Response<SuccessMessage>

    //oleh Kasi
    @FormUrlEncoded
    @POST("terima_kasi")
    fun terimaKasi(
        @Field("id_surat") id_surat: String?,
        @Field("user_id") user_id: String?,
        @Field("id_bidang") id_bidang: String?,
        @Field("id_seksi") id_seksi: String?
    ): Response<SuccessMessage>

    //oleh Staff
    @FormUrlEncoded
    @POST("terima_staff")
    suspend fun terimaStaff(
        @Field("id_surat") idSurat: String?,
        @Field("user_id") user_id: String?
    ): Response<SuccessMessage>

    // Dispo Balik
    @FormUrlEncoded
    @POST("dispo_balik")
    suspend fun dispoBalik(
        @Field("id_surat") id_surat: String?,
        @Field("isi_dp") isi_dp: String?,
        @Field("rule") rule: String?,
        @Field("id_bidang") id_bidang: String?,
        @Field("id_seksi") id_seksi: String?,
        @Field("user_id") user_id: String?
    ): Response<SuccessMessage>

    //Edit Profile
    @FormUrlEncoded
    @POST("edit_profile")
    suspend fun editProfile(
        @Field("user_id") user_id: String,
        @Field("nama") nama: String,
        @Field("nik") nik: String,
        @Field("telp") telp: String,
        @Field("tgl_lahir") tgl_lahir: String,
        @Field("jenis_kelamin") jenis_kelamin: String
    ): Response<SuccessMessage>

    //insert register
    @FormUrlEncoded
    @POST("edit_foto")
    suspend fun edit_foto(
        @Field("user_id") user_id: String,
        @Field("foto") foto: String
    ): Response<SuccessMessage>

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