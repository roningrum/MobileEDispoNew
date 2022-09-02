package id.go.dinkes.mobileedisponew.repository

import id.go.dinkes.mobileedisponew.remote.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

abstract class BaseRepo{
    suspend fun <T> safeApiCall(apiCalled: suspend () -> Response<T>): Result<T>{
       return withContext(Dispatchers.IO){
           try{
               val response: Response<T> = apiCalled()
               if(response.isSuccessful){
                   Result.Success(dataResult = response.body()!!)
               } else{
                   Result.Failure(errorMessage = response.message())
               }
           } catch (e: HttpException){
               Result.Failure(errorMessage = "Something went wrong, Try again")
           } catch (e: IOException){
               Result.Failure("Please Check Your Network Connection")
           } catch (e: Exception){
               Result.Failure("Something Wrong")
           }
       }
    }
}