package com.ponykamni.astronomy.di.module;

import com.ponykamni.astronomy.data.network.AstronomyApi
import com.ponykamni.astronomy.data.network.AuthInterceptor
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


@Module
internal object NetworkModule {

    @Provides
    fun provideAstronomyApi(): AstronomyApi {
        val loggingInterceptor = HttpLoggingInterceptor()
            .apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .addInterceptor(loggingInterceptor)
            .build()

        val gson = Gson()

        return Retrofit.Builder()
            .baseUrl(ASTRONOMY_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(client)
            .build()
            .create(AstronomyApi::class.java)
    }
}

private const val ASTRONOMY_API_BASE_URL = "https://api.astronomyapi.com/"