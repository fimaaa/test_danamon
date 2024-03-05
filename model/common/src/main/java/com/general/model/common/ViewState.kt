package com.general.model.common

import javax.net.ssl.HttpsURLConnection

abstract class ViewState<out T> {
    object LOADING : ViewState<Nothing>()

    data class SUCCESS<out T>(val data: T) : ViewState<T>()

    data class EMPTY(val msg: UIText = UIText.StringResource(R.string.error_search_notfound, "data")) :
        ViewState<Nothing>()

    data class ERROR<out T>(
        val msg: UIText = UIText.StringResource(R.string.error_default),
        val code: Int = HttpsURLConnection.HTTP_INTERNAL_ERROR,
        val err: Throwable? = null,
        val errResponse: ErrorResponse? = null,
        val data: T? = null
    ) : ViewState<T>()
}