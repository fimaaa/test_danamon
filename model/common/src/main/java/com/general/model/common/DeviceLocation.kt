package com.general.model.common

import android.location.Location
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeviceLocation(val latitude: Double = 0.0, val longitude: Double = 0.0) : Parcelable {
    fun toLocation(): Location {
        val location = Location("")
        location.latitude = latitude
        location.longitude = longitude
        return location
    }
}