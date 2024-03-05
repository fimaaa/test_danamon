package com.general.repository.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.general.model.jsonplaceholder.Photo
import com.general.network.service.JsonPlaceHolderService

class JsonPlaceHolderPhotoPagingSource(
    private val jsonPlaceHolderService: JsonPlaceHolderService,
    private val sortBy: String?,
    private val limit: Int
) : PagingSource<Int, Photo>() {
    private val STARTING_PAGE_INDEX = 1
    override fun getRefreshKey(state: PagingState<Int, Photo>): Int = STARTING_PAGE_INDEX

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> = try {
        val page = params.key ?: STARTING_PAGE_INDEX

        val response = jsonPlaceHolderService.photo(page, limit, sortBy ?: "albumId")

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