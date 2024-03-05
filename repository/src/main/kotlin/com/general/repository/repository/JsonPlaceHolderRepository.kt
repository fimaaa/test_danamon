package com.general.repository.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.general.common.GeneralIODispatcher
import com.general.common.extension.safeGetResponse
import com.general.model.common.BaseResponse
import com.general.model.common.ViewState
import com.general.model.jsonplaceholder.Photo
import com.general.network.service.JsonPlaceHolderService
import com.general.repository.common.Repository
import com.general.repository.datasource.JsonPlaceHolderPhotoPagingSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

interface JsonPlaceHolderRepository : Repository {

    fun showListPhotoPaging(
        sortBy: String? = null,
        limit: Int = 50,
        page: Int = 1
    ): LiveData<PagingData<Photo>>

    @WorkerThread
    suspend fun showListPhoto(
        sortBy: String? = null,
        limit: Int = 50,
        page: Int = 1
    ): Flow<ViewState<List<Photo>>>
}

@Singleton
class JsonPlaceHolderRepositoryImpl @Inject constructor(
    private val jsonPlaceHolderService: JsonPlaceHolderService,
    @GeneralIODispatcher private val ioDispatcher: CoroutineDispatcher
) : JsonPlaceHolderRepository {
    init {
        Timber.i("Injection SessionRepository")
    }

    override fun showListPhotoPaging(
        sortBy: String?,
        limit: Int,
        page: Int
    ): LiveData<PagingData<Photo>> = Pager(
        config = PagingConfig(
            pageSize = 30,
            maxSize = 100,
            enablePlaceholders = false,
            prefetchDistance = 10
        ),
        pagingSourceFactory = {
            JsonPlaceHolderPhotoPagingSource(
                jsonPlaceHolderService = jsonPlaceHolderService,
                limit = limit,
                sortBy = sortBy
            )
        }
    ).liveData

    override suspend fun showListPhoto(
        sortBy: String?,
        limit: Int,
        page: Int
    ): Flow<ViewState<List<Photo>>> = safeGetResponse(ioDispatcher) {
        val res = jsonPlaceHolderService.photo(page, limit, sortBy ?: "albumId")
        BaseResponse(
            message = "Success",
            data = res
        )
    }
}