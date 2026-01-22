package org.tues.sponti.data.network

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import org.tues.sponti.BuildConfig
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.tues.sponti.data.auth.SessionManager

object RetrofitClient {
    private lateinit var sessionManager: SessionManager

    fun init(sm: SessionManager) {
        sessionManager = sm
    }

    private val client by lazy {
        OkHttpClient.Builder().addInterceptor(AuthInterceptor(sessionManager))
            .callTimeout(30, TimeUnit.SECONDS).build()
    }

    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    val api: ApiService by lazy {
        Retrofit.Builder().baseUrl(BuildConfig.BASE_URL).client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi)).build()
            .create(ApiService::class.java)
    }

    fun getMoshi() = moshi
}
