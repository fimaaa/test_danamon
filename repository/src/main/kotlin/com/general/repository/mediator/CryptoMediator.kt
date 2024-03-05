package com.general.repository.mediator

// import androidx.paging.ExperimentalPagingApi
// import androidx.paging.LoadType
// import androidx.paging.PagingState
// import androidx.paging.RemoteMediator
// import kotlinx.coroutines.CoroutineScope
// import kotlinx.coroutines.Dispatchers
// import kotlinx.coroutines.launch
// import org.java_websocket.client.WebSocketClient
//
// @OptIn(ExperimentalPagingApi::class)
// class CryptoMediator(
//    private val service: TopListDataSource,
//    private val dao: CryptoDao,
//    private val remoteKeyDao: RemoteKeysDao,
//    private val webSocketClient: WebSocketClient
// ) : RemoteMediator<Int, ResponseListCryptoInfo>() {
//
//    override suspend fun initialize(): InitializeAction {
//        return InitializeAction.LAUNCH_INITIAL_REFRESH
//    }
//
//    override suspend fun load(
//        loadType: LoadType,
//        state: PagingState<Int, ResponseListCryptoInfo>
//    ): MediatorResult {
//        val pageKeyData = getKeyPageData(loadType, state)
//        val page = when (pageKeyData) {
//            is MediatorResult.Success -> {
//                return pageKeyData
//            }
//            else -> {
//                (pageKeyData as Int?) ?: 0
//            }
//        }
//
//        try {
//            val apiResponse = service.fetchTopUsersAsync(page)
//            println("TAG apiResponse = $apiResponse")
//            val repos = apiResponse.data
//            repos.map {
//                it.id = it.coinInfo.id
//                it.page = page
//            }
//            repos.forEach {
//                webSocketClient.send(
//                    "{\n" +
//                            "    \"action\": \"SubRemove\",\n" +
//                            "    \"subs\": [\"${"21~" + it.coinInfo.name}\"]" +
//                            "}"
//                )
//                webSocketClient.send(
//                    "{\n" +
//                            "    \"action\": \"SubAdd\",\n" +
//                            "    \"subs\": [\"${"21~" + it.coinInfo.name}\"]" +
//                            "}"
//                )
//            }
//            val isEndOfList =
//                repos.isEmpty() || (apiResponse.pagination?.totalPage ?: 0) <= page * 50
//
//            val listCrypto = arrayListOf<ResponseListCryptoInfo>()
//            // clear all tables in the database
//            if (loadType == LoadType.REFRESH) {
//                listCrypto.addAll(dao.getAllListCrypto())
//                remoteKeyDao.clearRemoteKeys()
//                dao.deleteAll()
//            }
//
//            val prevKey = if (page <= 0) null else page - 1
//            val nextKey = if (isEndOfList) null else page + 1
//            CoroutineScope(Dispatchers.IO).launch {
//                val keys = repos.map {
//                    RemoteKeys(repoId = it.id.toString(), prevKey = prevKey, nextKey = nextKey)
//                }
//                remoteKeyDao.insertAll(keys)
//                dao.save(repos)
//            }
//            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
//        } catch (exception: Exception) {
//            println("TAG EXCEPTION $exception")
//            return MediatorResult.Error(exception)
//        }
//    }
//
//    /**
//     * this returns the page key or the final end of list success result
//     */
//    suspend fun getKeyPageData(
//        loadType: LoadType,
//        state: PagingState<Int, ResponseListCryptoInfo>
//    ): Any? {
//        return when (loadType) {
//            LoadType.REFRESH -> {
//                val remoteKeys = getClosestRemoteKey(state)
//                remoteKeys?.nextKey?.minus(1) ?: 0
//            }
//            LoadType.APPEND -> {
//                val remoteKeys = getLastRemoteKey(state)
// //                    ?: throw InvalidObjectException("Remote key should not be null for $loadType")
//                remoteKeys?.nextKey
//            }
//            LoadType.PREPEND -> {
//                val remoteKeys = getFirstRemoteKey(state)
// //                    ?: throw InvalidObjectException("Invalid state, key should not be null")
//                // end of list condition reached
//                remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
//                remoteKeys.prevKey
//            }
//        }
//    }
//
//    /**
//     * get the last remote key inserted which had the data
//     */
//    private suspend fun getLastRemoteKey(state: PagingState<Int, ResponseListCryptoInfo>): RemoteKeys? {
//        return state.pages
//            .lastOrNull { it.data.isNotEmpty() }
//            ?.data?.lastOrNull()
//            ?.let { doggo -> remoteKeyDao.remoteKeysDoggoId(doggo.id.toString()) }
//    }
//
//    /**
//     * get the first remote key inserted which had the data
//     */
//    private suspend fun getFirstRemoteKey(state: PagingState<Int, ResponseListCryptoInfo>): RemoteKeys? {
//        return state.pages
//            .firstOrNull() { it.data.isNotEmpty() }
//            ?.data?.firstOrNull()
//            ?.let { doggo -> remoteKeyDao.remoteKeysDoggoId(doggo.id.toString()) }
//    }
//
//    /**
//     * get the closest remote key inserted which had the data
//     */
//    private suspend fun getClosestRemoteKey(state: PagingState<Int, ResponseListCryptoInfo>): RemoteKeys? {
//        return state.anchorPosition?.let { position ->
//            state.closestItemToPosition(position)?.id?.let { repoId ->
//                remoteKeyDao.remoteKeysDoggoId(repoId.toString())
//            }
//        }
//    }
// }