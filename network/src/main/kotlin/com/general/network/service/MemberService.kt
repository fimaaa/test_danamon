package com.general.network.service

import com.general.model.common.BaseResponse
import com.general.model.common.BaseResponsePagination
import com.general.model.common.user.CreateMemberData
import com.general.model.common.user.Member
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface MemberService {
    @POST("api/v1/member/create")
    suspend fun createMember(@Body member: CreateMemberData): BaseResponse<String, Member>

    @GET("api/v1/member")
    suspend fun getAllMember(
        @Query("size") pageSize: Int,
        @Query("page") page: Int,
        @QueryMap options: Map<String, String> = mapOf()
    ): BaseResponse<String, BaseResponsePagination<Member>>

    @GET("api/v1/member/{id}")
    suspend fun getMember(@Body body: Map<String, String>): BaseResponse<String, Member>
}