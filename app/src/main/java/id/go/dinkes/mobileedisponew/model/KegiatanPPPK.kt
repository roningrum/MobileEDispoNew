package id.go.dinkes.mobileedisponew.model

data class KegiatanPPPK (
    val pppk_id: String,
    val kegiatan: String,
    val lokasi: String,
    val tgl_kegiatan_1: String,
    val tgl_kegiatan_2: String,
    val jam: String,
    val deskripsi: String,
    val pelaksana: String,
    val penanggung_jawab: String,
    val no_telp_pj: String,
    val created_at: String,
    val update_at: String
)