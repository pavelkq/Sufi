package com.pasha.sufi.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import com.pasha.sufi.models.DailyText

interface DailyApi {
    @GET("daily/{date}")
    suspend fun getDailyText(@Path("date") date: String): Response<DailyText>
}
