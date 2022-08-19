package id.go.dinkes.mobileedisponew

data class Data(
    val acara: String,
    val dari: String,
    val disposisi1: String,
    val disposisi2: String,
    val disposisi3: String,
    val dp_balik: String,
    val dp_balik_kasi: String,
    val dp_balik_staff: String,
    val file_surat: String,
    val id: String,
    val jam: String,
    val no_agenda: String,
    val no_surat: String,
    val penerima_surat: List<PenerimaSurat>,
    val semua_penerima: String,
    val tanggal: String,
    val tanggal2: String,
    val tempat: String,
    val tgl_terima: String
)