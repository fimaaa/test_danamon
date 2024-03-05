package com.general.network.service

import com.general.model.common.BaseResponse
import com.general.model.common.user.Member
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {
    @POST("couriers/login")
    suspend fun postLogin(@Body body: Map<String, String>): BaseResponse<String, Member>

    // TODO IF API HAVE REFRESH TOKEN
    @POST("couriers/refresh-token")
    suspend fun refreshToken(
        @Header("Authorization") refreshToken: String
    ): BaseResponse<String, Member>
}