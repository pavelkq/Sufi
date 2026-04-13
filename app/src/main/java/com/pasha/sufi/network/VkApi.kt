package com.pasha.sufi.network

import com.pasha.sufi.models.VkResponse
import com.pasha.sufi.models.VkPost
import retrofit2.http.GET
import retrofit2.http.Query

interface VkApi {
    @GET("wall.get")
    suspend fun getWallPosts(
        @Query("owner_id") ownerId: String = VkApiClient.COMMUNITY_ID,
        @Query("count") count: Int = 20,
        @Query("offset") offset: Int,
        @Query("v") version: String = VkApiClient.API_VERSION,
        @Query("access_token") token: String = VkApiClient.ACCESS_TOKEN
    ): VkResponse<VkPost>
}