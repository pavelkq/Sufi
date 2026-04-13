package com.pasha.sufi.network

import com.pasha.sufi.models.Article
import com.pasha.sufi.models.ArticlesResponse
import com.pasha.sufi.models.Category
import com.pasha.sufi.models.Tag
import retrofit2.Response
import retrofit2.http.*

interface ArticleApi {
    @GET("api/admin-backend/articles")
    suspend fun getArticles(
        @Query("page") page: Int = 1,
        @Query("perPage") perPage: Int = 20,
        @Query("sortField") sortField: String = "publish_date",
        @Query("sortOrder") sortOrder: String = "DESC"
    ): Response<ArticlesResponse>

    // Новый метод для получения статей по категории
    @GET("api/admin-backend/articles")
    suspend fun getArticlesByCategory(
        @Query("page") page: Int = 1,
        @Query("perPage") perPage: Int = 20,
        @Query("sortField") sortField: String = "publish_date",
        @Query("sortOrder") sortOrder: String = "DESC",
        @Query("filter") filter: String // JSON вида {"category_id": 1}
    ): Response<ArticlesResponse>

    @GET("api/admin-backend/articles/{id}")
    suspend fun getArticleById(@Path("id") id: Int): Response<Article>

    @GET("api/admin-backend/articles/categories")
    suspend fun getCategories(): Response<List<Category>>

    @GET("api/admin-backend/articles/{id}/tags")
    suspend fun getArticleTags(@Path("id") id: Int): Response<List<Tag>>
}