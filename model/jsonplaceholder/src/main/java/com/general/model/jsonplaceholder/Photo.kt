package com.general.model.jsonplaceholder

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Photo(
    @Json(name = "albumId") val albumId: Int,
    @Json(name = "id") val id: Int,
    @Json(name = "thumbnailUrl") val thumbnailUrl: String,
    @Json(name = "title") val title: String,
    @Json(name = "url") val url: String
) : Parcelable