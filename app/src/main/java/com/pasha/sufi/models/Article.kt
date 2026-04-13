package com.pasha.sufi.models

import com.google.gson.annotations.SerializedName

data class ArticlesResponse(
    val data: List<Article>,
    val total: Int
)

data class Article(
    val id: Int,
    val title: String,
    @SerializedName("category_id")
    val categoryId: Int,
    @SerializedName("group_id")
    val groupId: Int,
    @SerializedName("publish_date")
    val publishDate: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("markdown_text")
    val content: String // Содержание статьи в HTML
)

data class Category(
    val id: Int,
    val name: String
)

data class Tag(
    val id: Int,
    val name: String
)

