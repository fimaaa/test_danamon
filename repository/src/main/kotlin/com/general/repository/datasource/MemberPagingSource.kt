package com.general.repository.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.general.common.extension.getString
import com.general.model.common.user.Member
import com.general.network.service.MemberService
import com.google.gson.Gson

class MemberPagingSource(
    private val limit: Int,
    private val filter: Member,
    private val sortBy: Map<String, Any>,
    private val serviceMember: MemberService
) : PagingSource<Int, Member>() {
    private val STARTING_PAGE_INDEX = 1
    override fun getRefreshKey(state: PagingState<Int, Member>): Int = STARTING_PAGE_INDEX

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Member> = try {
        val page = params.key ?: STARTING_PAGE_INDEX

        val option = hashMapOf<String, String>()
        sortBy.entries.forEach {
            when {
                (it.value is String) && (it.value as String).isNotEmpty() -> {
                    option[it.key] = it.value as String
                }

                it.value is Number -> {
                    option[it.key] = it.value.toString()
                }
            }
        }

        Gson().toJsonTree(filter).asJsonObject.entrySet().forEach {
            when {
                it.value.getString().isNotEmpty() -> {
                    option[it.key] = it.value.getString()
                }

                it.value.isJsonPrimitive && it.value.asJsonPrimitive.isNumber -> {
                    option[it.key] = it.value.toString()
                }
            }
        }

        val response = serviceMember.getAllMember(
            limit,
            page,
            option
        ).data.listData

        println("TAG RESPONSE $response $page")

        LoadResult.Page(
            data = response,
            prevKey = if (page <= STARTING_PAGE_INDEX) null else page - 1,
            nextKey = if (response.isEmpty()) null else page + 1
        )
    } catch (e: Exception) {
        e.printStackTrace()
        LoadResult.Error(e)
    }
}