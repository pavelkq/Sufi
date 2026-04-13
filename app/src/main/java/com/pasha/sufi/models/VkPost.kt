package com.pasha.sufi.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class VkPost(
    val id: Int,
    @SerializedName("from_id")
    val fromId: Int,
    @SerializedName("owner_id")
    val ownerId: Int,
    val date: Long,
    val text: String,
    val likes: Likes?,
    val reposts: Reposts?,
    val views: Views?,
    val attachments: List<Attachment>?
) : Serializable

data class Likes(
    val count: Int
) : Serializable

data class Reposts(
    val count: Int
) : Serializable

data class Views(
    val count: Int
) : Serializable

data class Attachment(
    val type: String,
    val photo: Photo?,
    val link: Link?,
    val video: Video?,
    val audio: Audio?
) : Serializable

data class Photo(
    val id: Int,
    @SerializedName("owner_id")
    val ownerId: Int,
    val sizes: List<PhotoSize>
) : Serializable

data class PhotoSize(
    val url: String,
    val width: Int,
    val height: Int,
    val type: String
) : Serializable

data class Link(
    val url: String,
    val title: String,
    val description: String? = null
) : Serializable

data class Video(
    val id: Int,
    @SerializedName("owner_id")
    val ownerId: Int,
    val title: String,
    val duration: Int
) : Serializable

data class Audio(
    val id: Int,
    @SerializedName("owner_id")
    val ownerId: Int,
    val artist: String,
    val title: String,
    val duration: Int
) : Serializable

data class VkResponse<T>(
    @SerializedName("response")
    val response: VkResponseItems<T>?
) : Serializable

data class VkResponseItems<T>(
    val count: Int,
    val items: List<T>
) : Serializable
