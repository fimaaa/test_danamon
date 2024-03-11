package com.general.repository.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.general.common.GeneralIODispatcher
import com.general.common.extension.getString
import com.general.common.extension.safeGetResponse
import com.general.common.provider.LocationProvider
import com.general.local.AppPreference
import com.general.local.AppPreference.Companion.APP_DEFAULT_LOCATION
import com.general.local.AppPreference.Companion.APP_TOKEN
import com.general.local.AppPreference.Companion.APP_USER
import com.general.local.dao.MemberDao
import com.general.model.common.BaseResponse
import com.general.model.common.DeviceLocation
import com.general.model.common.ViewState
import com.general.model.common.user.CreateMemberData
import com.general.model.common.user.Member
import com.general.network.service.AuthService
import com.general.network.service.MemberService
import com.general.repository.common.Repository
import com.general.repository.datasource.MemberPagingSource
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

interface SessionRepository : Repository {
    @WorkerThread
    suspend fun login(
        phoneNumber: String,
        password: String,
        deviceId: String
    ): Flow<ViewState<Member>>

    @WorkerThread
    suspend fun registerMember(member: CreateMemberData): Flow<ViewState<Member>>

    @WorkerThread
    suspend fun isLoggedIn(): Boolean

    @WorkerThread
    suspend fun setToken(token: String)

    @WorkerThread
    suspend fun setUser(member: Member)

    @WorkerThread
    suspend fun getTokenSession(): String

    @WorkerThread
    suspend fun getUserSession(): Member?

    @WorkerThread
    suspend fun logout()

//    @WorkerThread
//    suspend fun changePassword(
//        phoneNumber: String,
//        currentPassword: String,
//        password: String,
//        confirmedPassword: String
//    ): Flow<ViewState<Member>>

    @WorkerThread
    suspend fun getListMember(
        pageSize: Int,
        page: Int,
        filter: Member,
        sortBy: Map<String, Any>
    ): List<Member>?

    @WorkerThread
    fun showListMemberPaging(
        pageSize: Int = 5,
        filter: Member = Member(),
        sortBy: Map<String, Any> = mapOf()
    ): LiveData<PagingData<Member>>
}

@Singleton
class SessionRepositoryImpl @Inject constructor(
    private val dataStore: AppPreference,
    private val serviceAuth: AuthService,
    private val serviceMember: MemberService,
    private val localDao: MemberDao,
    private val locationProvider: LocationProvider,
    @GeneralIODispatcher private val ioDispatcher: CoroutineDispatcher
) : SessionRepository {
    init {
        Timber.i("Injection SessionRepository")
    }

    @WorkerThread
    override suspend fun login(
        phoneNumber: String,
        password: String,
        deviceId: String
    ): Flow<ViewState<Member>> =
        safeGetResponse(ioDispatcher) {
            val res = serviceAuth.postLogin(phoneNumber, password, "", deviceId)

            setUser(res.data)
            setToken(res.data.apiToken)

            BaseResponse(
                message = "success login",
                data = res.data
            )
        }

    override suspend fun registerMember(
        member: CreateMemberData
    ): Flow<ViewState<Member>> = safeGetResponse(ioDispatcher) {
        serviceMember.createMember(member)
    }

    @WorkerThread
    override suspend fun isLoggedIn(): Boolean {
        return !dataStore.readToken().isNullOrEmpty()
    }

    @WorkerThread
    override suspend fun setToken(token: String) {
        try {
            dataStore.storeValue(APP_TOKEN, token)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @WorkerThread
    override suspend fun setUser(member: Member) {
        withContext(ioDispatcher) {
            try {
                println("TAG SETUSER DATAJSON MEMBER $member")
                val dataJson = Gson().toJson(member)
                dataStore.storeValue(APP_USER, dataJson)
            } catch (e: Exception) {
                println("TAG ERROR SETUSER $e")
                e.printStackTrace()
            }
        }
    }

    @WorkerThread
    private suspend fun setDefaultLocation(deviceLocation: DeviceLocation) {
        withContext(ioDispatcher) {
            try {
                val dataJson = Gson().toJson(deviceLocation)
                dataStore.storeValue(APP_DEFAULT_LOCATION, dataJson)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @WorkerThread
    override suspend fun getTokenSession(): String {
        return dataStore.readToken().orEmpty()
    }

    @WorkerThread
    override suspend fun getUserSession(): Member? = dataStore.readMember()

    @WorkerThread
    override suspend fun logout() {
        dataStore.clear()
    }

//    @WorkerThread
//    override suspend fun changePassword(
//        phoneNumber: String,
//        currentPassword: String,
//        password: String,
//        confirmedPassword: String
//    ): Flow<ViewState<Member>> =
//        safeGetResponse(ioDispatcher) {
// //            val request = mapOf(
// //                "current_password" to currentPassword,
// //                "password" to password,
// //                "confirmed_password" to confirmedPassword
// //            )
// //            val res = service.changePasswordProfile(request)
//            changePassword(phoneNumber, password)
//        }

//    private suspend fun changePassword(
//        phoneNumber: String,
//        newPassword: String
//    ): BaseResponse<String, Member> {
//        val user = service.getMemberByPhoneNumber(phoneNumber)
//            ?: throw CustomException("Username not Found")
//
//        val newUser = user.copy(
//            password = newPassword
//        )
//        val res = service.updateUser(newUser)
//
//        return BaseResponse(
//            message = if (res > 0) "success update Data" else "update failed",
//            data = if (res > 0) newUser.toMember() else user.toMember()
//        )
//    }

    override suspend fun getListMember(
        pageSize: Int,
        page: Int,
        filter: Member,
        sortBy: Map<String, Any>
    ): List<Member> {
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
        return serviceMember.getAllMember(
            pageSize,
            page,
            option
        ).data.listData
    }

    override fun showListMemberPaging(
        pageSize: Int,
        filter: Member,
        sortBy: Map<String, Any>
    ): LiveData<PagingData<Member>> = Pager(
        config = PagingConfig(
            pageSize = 30,
            maxSize = 100,
            enablePlaceholders = false,
            prefetchDistance = 10
        ),
        pagingSourceFactory = {
            MemberPagingSource(
                serviceMember = serviceMember,
                filter = filter,
                limit = pageSize,
                sortBy = sortBy
            )
        }
    ).liveData
}