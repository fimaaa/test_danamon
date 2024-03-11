package com.general.repository.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
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
        val gson = Gson()

//        val sortJson = gson.toJsonTree(sortBy).asJsonObject
//        val jsonObject = JsonObject().apply {
//
//            add("sort_by", sortJson)
//
//            val memberJson = gson.toJsonTree(filter).asJsonObject
//            add("value", memberJson)
//        }
        val response = serviceMember.getAllMember(
            limit,
            page
//            MemberFind(
//                filter = filter,
//                sortBy = sortJson.toString()
//            )
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