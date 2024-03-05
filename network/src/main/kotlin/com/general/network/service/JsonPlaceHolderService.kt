package com.general.network.service

import com.general.model.jsonplaceholder.Photo
import retrofit2.http.GET
import retrofit2.http.Query

interface JsonPlaceHolderService {
    @GET("photos")
    suspend fun photo(
        @Query("_page") page: Int = 1,
        @Query("_limit") limit: Int = 10,
        @Query("_sort") sortBy: String = "albumId"
    ): List<Photo>
}