package com.general.repository.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.general.common.GeneralIODispatcher
import com.general.common.extension.CustomException
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
import com.general.model.common.user.Member
import com.general.model.common.user.MemberLocal
import com.general.model.common.user.toMember
import com.general.repository.common.Repository
import com.general.repository.datasource.MemberPagingSource
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
    suspend fun registerMember(
        userName: String,
        name: String,
        userType: String,
        phone: String,
        password: String,
        deviceId: String
    ): Flow<ViewState<Member>>

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

    @WorkerThread
    suspend fun changePassword(
        phoneNumber: String,
        currentPassword: String,
        password: String,
        confirmedPassword: String
    ): Flow<ViewState<Member>>

    @WorkerThread
    suspend fun getListMember(pageSize: Int, page: Int): List<Member>?

    @WorkerThread
    fun showListMemberPaging(
        sortBy: String? = null,
        limit: Int = 50,
        page: Int = 1
    ): LiveData<PagingData<Member>>
}

@Singleton
class SessionRepositoryImpl @Inject constructor(
    private val dataStore: AppPreference,
//    private val service: AuthService,
    private val service: MemberDao,
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
//            val request = mapOf(
//                "username" to phoneNumber,
//                "password" to password,
//                "id_device" to deviceId
//            )

            val res = service.postLogin(phoneNumber, password)?.copy(
                apiToken = "DUMMY TOKEN",
                refreshToken = "DUMMY REFRESH TOKEN"
            ) ?: throw CustomException("Username and Password not found")

            setUser(res.toMember())
            setToken(res.apiToken)

            BaseResponse(
                message = "success login",
                data = res.toMember()
            )
        }

    override suspend fun registerMember(
        userName: String,
        name: String,
        userType: String,
        phone: String,
        password: String,
        deviceId: String
    ): Flow<ViewState<Member>> {
        val registeredMember = MemberLocal(
            userName = userName,
            name = name,
            userType = userType,
            phone = phone,
            password = password,
            apiToken = "DUMMY TOKEN",
            refreshToken = "REFRESH TOKEN"
        )
        val isExist = service.checkPhoneNumberExist(registeredMember.phone)
        if (isExist > 0) {
            throw CustomException("Register Member Already Exist")
        }
        val res = service.registerMember(registeredMember)
        if (res < 0) {
            throw CustomException("Error Register Member")
        }
        return flow {
            emit(ViewState.SUCCESS(data = registeredMember.toMember()))
        }
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
                val dataJson = Gson().toJson(member)
                dataStore.storeValue(APP_USER, dataJson)
            } catch (e: Exception) {
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

    @WorkerThread
    override suspend fun changePassword(
        phoneNumber: String,
        currentPassword: String,
        password: String,
        confirmedPassword: String
    ): Flow<ViewState<Member>> =
        safeGetResponse(ioDispatcher) {
//            val request = mapOf(
//                "current_password" to currentPassword,
//                "password" to password,
//                "confirmed_password" to confirmedPassword
//            )
//            val res = service.changePasswordProfile(request)
            changePassword(phoneNumber, password)
        }

    private suspend fun changePassword(
        phoneNumber: String,
        newPassword: String
    ): BaseResponse<String, Member> {
        val user = service.getMemberByPhoneNumber(phoneNumber)
            ?: throw CustomException("Username not Found")

        val newUser = user.copy(
            password = newPassword
        )
        val res = service.updateUser(newUser)

        return BaseResponse(
            message = if (res > 0) "success update Data" else "update failed",
            data = if (res > 0) newUser.toMember() else user.toMember()
        )
    }

    override suspend fun getListMember(pageSize: Int, page: Int): List<Member> {
        val res = service.fetchListMembers(pageSize, page)
        println("TAG GETLISTMEMBER $res")
        return res.map { it.toMember() }
    }

    override fun showListMemberPaging(
        sortBy: String?,
        limit: Int,
        page: Int
    ): LiveData<PagingData<Member>> = Pager(
        config = PagingConfig(
            pageSize = 30,
            maxSize = 100,
            enablePlaceholders = false,
            prefetchDistance = 10
        ),
        pagingSourceFactory = {
            MemberPagingSource(
                sessionRepository = service,
                limit = limit,
                sortBy = sortBy
            )
        }
    ).liveData
}