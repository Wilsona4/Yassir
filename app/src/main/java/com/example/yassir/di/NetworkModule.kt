package com.example.yassir.di

import com.example.yassir.data.remote.ApiService
import com.example.yassir.util.Constants.API_KEY
import com.example.yassir.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                var request = chain.request()
                val originalHttpUrl: HttpUrl = request.url.newBuilder()
                    .addQueryParameter("api_key", API_KEY)
                    .build()

                val requestBuilder = request.newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .url(
                        originalHttpUrl
                    )

                request = requestBuilder.build()
                chain.proceed(request)
            }
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .addInterceptor(Interceptor { chain ->
                val request = chain.request()
                val response = chain.proceed(request)
                response
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitService(
        client: OkHttpClient,
        converterFactory: MoshiConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(
        retrofit: Retrofit
    ): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}