package id.go.dinkes.mobileedisponew.remote

import com.google.gson.GsonBuilder
import id.go.dinkes.mobileedisponew.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkRepo {
    var retrofitService: RetrofitService? = null
    var retrofitService2 : RetrofitService? = null


    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    fun getDispo() : RetrofitService{
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

    fun getAKM() : RetrofitService{
        if(retrofitService2 == null){
            val gson = GsonBuilder().setLenient().create()
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BuildConfig.URL_AKM)
                .client(client)
                .build()
          retrofitService = retrofit.create(RetrofitService::class.java)
        }
        return  retrofitService!!
    }
}