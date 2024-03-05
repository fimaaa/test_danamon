package com.general.network

import com.general.local.AppPreference
import com.general.local.AppPreference.Companion.APP_TOKEN
import com.general.local.AppPreference.Companion.APP_USER
import com.general.network.service.AuthService
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val appPreference: AppPreference,
    private val sessionService: AuthService
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        var newToken: String?
        runBlocking {
            newToken = getUpdatedToken()
        }
        if (newToken == null) {
            return null
        }
        val responseRequest = response.request.newBuilder()
        if (!newToken.isNullOrEmpty()) {
            responseRequest.header("Authorization", "Bearer $newToken")
        }
        return responseRequest.build()
    }

    private suspend fun getUpdatedToken(): String? {
        val oldMember = appPreference.readMember()
        return try {
            val newMember = sessionService.refreshToken(
                ("Bearer " + oldMember?.refreshToken)
            )
            appPreference.storeValue(APP_USER, Gson().toJson(newMember.data))
            appPreference.storeValue(APP_TOKEN, newMember.data.apiToken)
            appPreference.readToken() ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}