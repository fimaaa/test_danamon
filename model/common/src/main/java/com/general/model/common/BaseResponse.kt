package com.general.model.common

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

/**
 * Generic data class for parsing response
 * M for message type string, D for data type any
 */

data class BaseResponse<M, D>(
    @Json(name = "code")
    @SerializedName(value = "code")
    var code: Int? = null,
    @Json(name = "code_type")
    @SerializedName(value = "code_type")
    val codeType: String? = null,
    @Json(name = "code_message")
    @SerializedName(value = "code_message")
    var codeMessage: String? = null,
    @Json(name = "message")
    @SerializedName(value = "message")
    val message: M? = null,
    @Json(name = "data")
    @SerializedName(value = "data")
    val data: D,
    @Json(name = "trace_id")
    @SerializedName(value = "trace_id")
    var traceId: String? = null
)

data class BaseResponsePagination<D>(
    @Json(name = "list")
    @SerializedName(value = "list")
    var listData: List<D> = listOf(),
    @Json(name = "metadata")
    @SerializedName(value = "metadata")
    val pagination: BasePagination? = null
)