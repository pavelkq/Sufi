package com.pasha.sufi.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object VkApiClient {
    private const val BASE_URL = "https://api.vk.com/method/"

    // ID сообщества (с минусом)
    const val COMMUNITY_ID = "-28644050"
    const val API_VERSION = "5.131"

    // Делаем токен публичным (убираем private)
    const val ACCESS_TOKEN = "6d036e886d036e886d036e88ac6e3cf43b66d036d036e8804c341653d60b321414ea6cc"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}