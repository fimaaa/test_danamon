package com.general.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.general.model.common.user.Member
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreference @Inject constructor(@ApplicationContext private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("preference_store")
    var companion = Companion

    private suspend fun <T> DataStore<Preferences>.getFromLocalStorage(preferencesKey: Preferences.Key<T>): Result<String> {
        return Result.runCatching {
            val flow = data.catch {
                if (it is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.map {
                it[preferencesKey] as String
            }
            val value = flow.firstOrNull() ?: ""
            value
        }
    }

    suspend fun clear() {
        val sharedPreferences =
            context.getSharedPreferences("preference_store", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
        context.dataStore.edit {
            it.clear()
        }
    }

    suspend fun <T> storeValue(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit {
            it[key] = value
        }
    }

    suspend fun <T> readValue(key: Preferences.Key<T>): String? =
        context.dataStore.getFromLocalStorage(key).getOrElse { null }

    suspend fun <T> readNonStringValue(key: Preferences.Key<T>): T? =
        context.dataStore.data.first()[key]

    suspend fun readToken(): String? =
        context.dataStore.getFromLocalStorage(APP_TOKEN).getOrElse { null }

    suspend fun readMember(): Member? =
        try {
            var member: Member? = null
            val data = context.dataStore.getFromLocalStorage(APP_USER).getOrElse { null }
            if (!data.isNullOrEmpty()) {
                member = Gson().fromJson(data, Member::class.java)
            }
            println("TAG READ DATAJSON MEMBER $member")
            member
        } catch (e: Exception) {
            null
        }

    companion object {
        val APP_TOKEN = stringPreferencesKey("com.general.testdanamon.session.token")
        val APP_USER = stringPreferencesKey("com.general.testdanamon.session.user")
        val APP_DEFAULT_LOCATION = stringPreferencesKey("com.general.testdanamon.session.location")
    }
}