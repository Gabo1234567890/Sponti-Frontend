package org.tues.sponti.data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import org.tues.sponti.BuildConfig
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitClient {
    private val client = OkHttpClient.Builder().callTimeout(30, TimeUnit.SECONDS).build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
