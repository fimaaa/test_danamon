package com.general.repository.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.general.local.dao.MemberDao
import com.general.model.common.user.Member
import com.general.model.common.user.toMember

class MemberPagingSource(
    private val sessionRepository: MemberDao,
    private val sortBy: String?,
    private val limit: Int
) : PagingSource<Int, Member>() {
    private val STARTING_PAGE_INDEX = 0
    override fun getRefreshKey(state: PagingState<Int, Member>): Int = STARTING_PAGE_INDEX

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Member> = try {
        val page = params.key ?: STARTING_PAGE_INDEX

        val response = sessionRepository.fetchListMembers(limit, page).map { it.toMember() }

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