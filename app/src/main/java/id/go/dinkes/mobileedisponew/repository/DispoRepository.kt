package id.go.dinkes.mobileedisponew.repository

import id.go.dinkes.mobileedisponew.remote.RetrofitService

class DispoRepository constructor(private val retrofitService: RetrofitService) {
    suspend fun getLogin(username:String, pass:String) = retrofitService.getLogin(username, pass)
    suspend fun getUserDetail (userId:String) = retrofitService.getDetailUser(userId)
    suspend fun getAgendaToday(dari:String, sampai:String) = retrofitService.getAgenda(dari, sampai)
    suspend fun getSuratDispo(jenis:String, rule:String, bidang:String, seksi: String, user_id: String,
                              status1: String, status2: String) = retrofitService.getSuratDp(jenis, rule, bidang, seksi, user_id, status1, status2)
    suspend fun getSuratDispoByTgl(jenis:String, rule:String, bidang:String, seksi: String, user_id: String,
                                   status1: String, status2: String, tgl:String) = retrofitService.getSuratDpbyTgl(jenis,rule, bidang, seksi, user_id, status1, status2,tgl)

    suspend fun getSuratDispoBalik(rule: String, bidang: String, seksi: String, notifDpBalik:String)
    = retrofitService.getListDpBalik(rule, bidang, seksi, notifDpBalik)

    suspend fun getSuratbyKadin(jenis: String) = retrofitService.getSuratDPbyKadin(jenis)
    suspend fun getBidang() = retrofitService.getBidang()
    suspend fun getKegiatanExternalBidang(idBidang:String, tglKegiatan:String) = retrofitService.getKegiatanExternalBidang(idBidang, tglKegiatan)
    suspend fun getKegiatanInternalBidang(idBidang:String, tglKegiatan:String) = retrofitService.getKegiatanInternalBidang(idBidang, tglKegiatan)
    suspend fun getKegiatanPPPK(dari:String, sampai:String) = retrofitService.getPPPK(dari, sampai)
}
