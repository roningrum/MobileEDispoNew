package id.go.dinkes.mobileedisponew.model

data class Result(
    val count: Int,
    val dari: String,
    val data : List<Data>,
    val limit: Int,
    val max_page: Int,
    val page: Int,
    val sampai: String
)