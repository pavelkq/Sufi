package com.pasha.sufi.models

import com.google.gson.annotations.SerializedName

data class DailyText(
    val id: Int,
    val date: String,
    val content: String,
    @SerializedName("audio_url")
    val audioUrl: String? = null
)

data class DailyResponse(
    val data: DailyText?,
    val message: String? = null
)