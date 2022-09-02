package id.go.dinkes.mobileedisponew.repository

import id.go.dinkes.mobileedisponew.model.*
import id.go.dinkes.mobileedisponew.remote.Result
import id.go.dinkes.mobileedisponew.remote.RetrofitService

class DispoRepository constructor(private val retrofitService: RetrofitService) : BaseRepo(){
    suspend fun getLogin(username:String, pass:String) : Result<LoginUser> = safeApiCall { retrofitService.getLogin(username, pass) }
    suspend fun getUserDetail (userId:String):Result<UserDetail> = safeApiCall{ retrofitService.getDetailUser(userId)}
    suspend fun getAgendaToday(dari:String, sampai:String): Result<Agenda> = safeApiCall { retrofitService.getAgenda(dari, sampai) }
    suspend fun getSuratDispo(jenis:String, rule:String, bidang:String, seksi: String, user_id: String,
                              status1: String, status2: String) : Result<SuratResponse> =  safeApiCall{retrofitService.getSuratDp(jenis, rule, bidang, seksi, user_id, status1, status2)}
    suspend fun getSuratDispoByTgl(jenis:String, rule:String, bidang:String, seksi: String, user_id: String,
                                   status1: String, status2: String, tgl:String) = retrofitService.getSuratDpbyTgl(jenis,rule, bidang, seksi, user_id, status1, status2,tgl)

    suspend fun getSuratDispoBalik(rule: String, bidang: String, seksi: String, notifDpBalik:String) : Result<SuratResponse>
    = safeApiCall { retrofitService.getListDpBalik(rule, bidang, seksi, notifDpBalik) }

    suspend fun getSuratbyKadin(jenis: String) : Result<SuratResponse> = safeApiCall { retrofitService.getSuratDPbyKadin(jenis) }
    suspend fun getBidang() : Result<BidangResponse> = safeApiCall { retrofitService.getBidang() }
    suspend fun getKegiatanExternalBidang(idBidang:String, tglKegiatan:String) : Result<KegiatanLuarResponse> = safeApiCall { retrofitService.getKegiatanExternalBidang(idBidang, tglKegiatan) }
    suspend fun getKegiatanInternalBidang(idBidang:String, tglKegiatan:String) : Result<KegiatanInternalResponse> = safeApiCall { retrofitService.getKegiatanInternalBidang(idBidang, tglKegiatan) }
    suspend fun getKegiatanPPPK(dari:String, sampai:String) : Result<KegiatanPPPKResponse> = safeApiCall { retrofitService.getPPPK(dari, sampai) }
    suspend fun getDetailSurat(id:String): Result<SuratResponse> = safeApiCall { retrofitService.getSuratId(id) }
}
