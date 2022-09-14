package id.go.dinkes.mobileedisponew.repository

import id.go.dinkes.mobileedisponew.model.akm.DataCheckUpResponse
import id.go.dinkes.mobileedisponew.remote.Result
import id.go.dinkes.mobileedisponew.remote.RetrofitService

class AKMRepository constructor(private val retrofitService: RetrofitService) : BaseRepo() {
    suspend fun getCheckupHistory(nik: String): Result<DataCheckUpResponse> =
        safeApiCall { retrofitService.getDataCheckUpAkhir(nik) }

    suspend fun getCheckupBB(nik: String): Result<DataCheckUpResponse> =
        safeApiCall { retrofitService.getHistoryBerat(nik) }

    suspend fun getCheckupTinggi(nik: String): Result<DataCheckUpResponse> =
        safeApiCall { retrofitService.getHistoryTinggi(nik) }

    suspend fun getCheckupTensi(nik: String): Result<DataCheckUpResponse> =
        safeApiCall { retrofitService.getHistoryTensi(nik) }

}
