package id.go.dinkes.mobileedisponew.remote

sealed class Result<T>(val data: T? = null, val message: String?=null){
    class Success<T>(dataResult: T): Result<T>(dataResult)
    class Failure<T>(errorMessage: String): Result<T>(message = errorMessage)
}

