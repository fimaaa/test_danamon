package com.general.network.service

import com.general.model.common.BaseResponse
import com.general.model.common.user.Member
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {
    @FormUrlEncoded
    @POST("api/v1/auth/login")
    suspend fun postLogin(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("token_broadcast") tokenBroadcast: String,
        @Field("id_device") idDevice: String
    ): BaseResponse<String, Member>

    @POST("api/v1/auth/refresh-token")
    suspend fun refreshToken(
        @Header("Authorization") refreshToken: String
    ): BaseResponse<String, Member>
}