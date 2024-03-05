package com.general.common.extension

import com.general.common.R
import com.general.model.common.ErrorResponse
import com.general.model.common.UIText
import com.general.model.common.ViewState
import com.google.android.gms.common.api.ApiException
import com.google.gson.Gson
import com.google.gson.JsonParser
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.HttpsURLConnection

fun Throwable.toThrowableMessage(content: String = "Data"): UIText = when (this) {
    is HttpException ->
        try {
            val errorJsonString = response()?.errorBody()?.string()
//            val messageString = JsonParser.parseString(errorJsonString)
//                .asJsonObject["message"]
//                .asString
            val errorResponse: ErrorResponse? =
                Gson().fromJson(errorJsonString, ErrorResponse::class.java)
            errorResponse?.message?.let {
                UIText.DynamicString(it)
            } ?: errorResponse?.codeMessage?.let {
                UIText.DynamicString(it)
            } ?: code().toMessageCode()
        } catch (e: Exception) {
            code().toMessageCode()
//            when (code()) {
//                HttpsURLConnection.HTTP_UNAUTHORIZED -> UIText.DynamicString("Tidak dapat mengakses data")
//                HttpsURLConnection.HTTP_NOT_FOUND, HttpsURLConnection.HTTP_NO_CONTENT -> UIText.DynamicString(content)
//                HttpsURLConnection.HTTP_INTERNAL_ERROR -> UIText.DynamicString("Terjadi gangguan pada server")
//                HttpsURLConnection.HTTP_BAD_REQUEST -> UIText.DynamicString("Data tidak sesuai")
//                HttpsURLConnection.HTTP_FORBIDDEN -> UIText.DynamicString("Sesi telah berakhir")
//                429 -> UIText.DynamicString("Terlalu Banyak Request")
//                else -> UIText.DynamicString("Oops, Terjadi gangguan, coba lagi beberapa saat")
//            }
        }

    is UnknownHostException -> UIText.DynamicString("Tidak ada koneksi internet")
    is ConnectException, is SocketTimeoutException, is Errors.OfflineException -> UIText.DynamicString(
        "No internet connected"
    )

    is Errors.FetchException -> UIText.DynamicString("Fetch exception")
    is CustomException -> message?.let { UIText.DynamicString(it) } ?: UIText.StringResource(R.string.error_default)
    else -> UIText.StringResource(R.string.error_default)
}

fun Throwable.toThrowableCode(): Int = when (this) {
    is HttpException ->
        try {
            val errorJsonString = response()?.errorBody()?.string()
            JsonParser.parseString(errorJsonString)
                .asJsonObject["statusCode"]
                .asInt
        } catch (e: Exception) {
            when (code()) {
                HttpsURLConnection.HTTP_UNAUTHORIZED -> 401
                HttpsURLConnection.HTTP_NOT_FOUND -> 404
                HttpsURLConnection.HTTP_INTERNAL_ERROR -> 500
                HttpsURLConnection.HTTP_BAD_REQUEST -> 400
                HttpsURLConnection.HTTP_FORBIDDEN -> 403
                HttpsURLConnection.HTTP_CONFLICT -> 409
                else -> code()
            }
        }

    is ApiException -> {
        this.statusCode
    }

    else -> 502
}

fun Int.toMessageCode(errorResponse: ErrorResponse? = null): UIText = when (this) {
    HttpsURLConnection.HTTP_NO_CONTENT -> {
        errorResponse?.codeMessage?.let {
            UIText.DynamicString(it)
        } ?: UIText.DynamicString("Data tidak ditemukan")
    }

    HttpsURLConnection.HTTP_INTERNAL_ERROR -> {
        UIText.DynamicString("Terjadi gangguan pada server")
    }

    HttpsURLConnection.HTTP_BAD_REQUEST -> {
        errorResponse?.codeMessage?.let {
            UIText.DynamicString(it)
        } ?: UIText.DynamicString("Data tidak sesuai")
    }

    HttpsURLConnection.HTTP_FORBIDDEN -> {
        errorResponse?.codeMessage?.let {
            UIText.DynamicString(it)
        } ?: UIText.DynamicString("Sesi telah berakhir")
    }

    429 -> {
        UIText.DynamicString("Terlalu Banyak Request")
    }

    HttpsURLConnection.HTTP_UNAUTHORIZED -> {
        UIText.DynamicString("Anda telah masuk di device lain")
    }

    HttpsURLConnection.HTTP_NOT_FOUND -> {
        UIText.DynamicString("Terjadi kesalahan")
    }

    405, 408 -> {
        errorResponse?.codeMessage?.let {
            UIText.DynamicString(it)
        } ?: UIText.DynamicString("Network Error")
    }

    else -> {
        UIText.DynamicString("Oops, Terjadi gangguan, coba lagi beberapa saat")
    }
}

fun Throwable.ErrorResponseUrl(): String {
    val httpError = this as? HttpException ?: return "unable to parse"
    return httpError.response()?.raw()?.request()?.url().toString()
}

fun Throwable.toViewState(): ViewState.ERROR<Nothing> =
    ViewState.ERROR(msg = toThrowableMessage(), code = toThrowableCode(), err = this)

sealed class Errors
    (msg: String) : Exception(msg) {
    class OfflineException(msg: String = "Not Connected to Internet") : Errors(msg)
    class FetchException(msg: String) : Errors(msg)
}

fun Throwable.toData(): Any? {
    if (this is HttpException) {
        try {
            val errorJsonString = response()?.errorBody()?.string()
            val errorResponse: ErrorResponse? =
                Gson().fromJson(errorJsonString, ErrorResponse::class.java)
            return errorResponse?.data
        } catch (_: Exception) {
        }
    }
    return null
}

class CustomException(message: String?) : RuntimeException(message)