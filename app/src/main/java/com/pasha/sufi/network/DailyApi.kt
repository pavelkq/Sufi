package com.pasha.sufi.network

import com.pasha.sufi.models.DailyText
import retrofit2.http.GET
import retrofit2.http.Query

interface DailyApi {
    @GET("daily")
    suspend fun getDailyText(
        @Query("date") date: String
    ): DailyText
}
