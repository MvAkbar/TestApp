package com.example.testapp.api

import android.content.Context
import android.util.Log
import com.example.testapp.utils.TokenManager
import com.squareup.picasso.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val BASE_URL = "https://valixon.bexatobot.uz/"

    private val client = buildClient()

    private val retrofit = buildRetrofit(client)


    private fun buildRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    private fun buildClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val builder = OkHttpClient.Builder()
            .callTimeout(1, TimeUnit.MINUTES)
            .addNetworkInterceptor(object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                    var request = chain.request()
                    val builder = request.newBuilder()
                    builder.addHeader("Accept", "application/json")
                    builder.header("Content-Type", "application/json")
                    builder.header("Accept", "application/json")
                    builder.header("x-device-type","android")
                    builder.header("x-device-app-version","1.0.0")
                    builder.header("x-device-lang","uz")

                    builder.header("x-device-os-version",android.os.Build.VERSION.RELEASE)
                    builder.header("x-device-uid","u21us")
                    builder.header("x-device-push-token","sa2sdsa2dsa2d")
                    request = builder.build()
                    return chain.proceed(request)
                }
            })
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(interceptor)
        }
        return builder.build()
    }

    @JvmStatic
    fun <T> createService(service: Class<T>?): T {
        return retrofit.create(service)
    }


    fun <T> createServiceWithAuth(service: Class<T>?, context: Context): T {
        val pref = TokenManager.getInstance(context.getSharedPreferences("prefs", Context.MODE_PRIVATE))!!
        Log.d("token-=","-*-*-*")
        Log.d("token-=",pref.token)
        val newClient =
            client.newBuilder().addInterceptor(object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                    var request = chain.request()
                    val builder = request.newBuilder()
                    builder.addHeader("Authorization", "Token " + pref.token)
                    request = builder.build()
                    return chain.proceed(request)
                }
            }).build()

        val newRetrofit = retrofit.newBuilder().client(newClient).build()
        return newRetrofit.create(service)
    }

}