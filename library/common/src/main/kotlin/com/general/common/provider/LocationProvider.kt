package com.general.common.provider

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Looper
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.general.common.base.BaseViewModel
import com.general.common.helper.PermissionHelper
import com.general.model.common.DeviceLocation
import com.general.model.common.ViewState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class LocationProvider @Inject constructor(@ApplicationContext private val context: Context) {

    suspend fun safeGetLocation(
        maximumAge: Long = 5000,
        viewModel: BaseViewModel
    ): DeviceLocation? {
        return try {
            getLocation(maximumAge)
        } catch (apiException: ApiException) {
            viewModel.setStatusViewModel(
                ViewState.ERROR(
                    code = apiException.statusCode,
                    err = apiException
                )
            )
            null
        }
    }

    suspend fun getLocation(maximumAge: Long = 5000): DeviceLocation? {
        checkLocationSetting()
        val minTime = System.currentTimeMillis() - maximumAge
        if (
            PermissionHelper.checkPermission(
                context,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        ) {
            val lastLocation = withContext(Dispatchers.Default) { getLastLocation() }
            if (lastLocation != null && lastLocation.time >= minTime) {
                return DeviceLocation(
                    latitude = lastLocation.latitude,
                    longitude = lastLocation.longitude
                )
            }

            val locationMethod1 = getCurrentLocationMethodOne()
            if (locationMethod1 != null) {
                return DeviceLocation(
                    latitude = locationMethod1.latitude,
                    longitude = locationMethod1.longitude
                )
            }

            val locationMethod2 = withContext(Dispatchers.Main) { getCurrentLocationMethod2() }
            if (locationMethod2 != null) return DeviceLocation(
                latitude = locationMethod2.latitude,
                longitude = locationMethod2.longitude
            )

            val currentLocation = withContext(Dispatchers.Default) { getCurrentLocation() }
            if (currentLocation != null) return DeviceLocation(
                latitude = currentLocation.latitude,
                longitude = currentLocation.longitude
            )
        }
        return null
    }

    suspend fun checkLocationSetting() =
        suspendCancellableCoroutine<LocationSettingsResponse?> { cont ->
            val locationRequest = LocationRequest().apply {
                numUpdates = 1
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            val locationSetting: LocationSettingsRequest =
                LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build()
            val task =
                LocationServices.getSettingsClient(context).checkLocationSettings(locationSetting)
            task.addOnCompleteListener {
                try {
                    val response = it.getResult(ApiException::class.java)
                    cont.resume(response)
                } catch (apiException: ApiException) {
                    cont.resumeWithException(apiException)
                }
            }
        }

    @SuppressLint("MissingPermission")
    private suspend fun getLastLocation() = suspendCoroutine<Location?> { cont ->
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { cont.resume(it) }
            .addOnFailureListener { cont.resumeWithException(it) }
    }

    @SuppressLint("MissingPermission")
    private suspend fun getCurrentLocationMethodOne() = suspendCoroutine<Location?> { cont ->
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)
        fusedLocationProviderClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            object : CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken {
                    return CancellationTokenSource().token
                }

                override fun isCancellationRequested(): Boolean = false
            }
        ).addOnSuccessListener { location ->
            cont.resume(location)
        }.addOnFailureListener {
            cont.resumeWithException(it)
        }
    }

    private suspend fun getCurrentLocationMethod2() = suspendCoroutine<Location?> { cont ->
        if (
            !PermissionHelper.checkPermission(
                context,
                arrayOf(
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            )
        ) {
            cont.resume(null)
            return@suspendCoroutine
        }
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var locationGPS: Location? = null
        var locationNetwork: Location? = null

        // step 1
        val hasGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        // step2
        val gpsLocationListener = LocationListener {
            locationGPS = it
        }
        val networkLocationListener = LocationListener {
            locationNetwork = it
        }

        // step3
        if (hasGPS) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5000,
                0F,
                gpsLocationListener
            )
        }
        if (hasNetwork) {
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                5000,
                0F,
                networkLocationListener
            )
        }

        // step4
        val lastLocationByGps =
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        lastLocationByGps?.let {
            locationGPS = it
        }
        val lastLocationNetwork =
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        lastLocationNetwork?.let {
            locationNetwork = it
        }

        // step5
        val accuracyGPS = locationGPS?.accuracy
        val accuracyNetwork = locationNetwork?.accuracy

        if (accuracyGPS != null && accuracyNetwork != null) {
            if (accuracyGPS > accuracyNetwork) cont.resume(locationGPS) else cont.resume(
                locationNetwork
            )
            return@suspendCoroutine
        }
        cont.resume(null)
    }

    @SuppressLint("MissingPermission")
    private suspend fun getCurrentLocation() = withTimeout(1000) {
        suspendCancellableCoroutine<Location?> { cont ->
            val fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(context)
            val locationRequest = LocationRequest().apply {
                numUpdates = 1
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            val listener = object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult) {
                    if (cont.isActive) cont.resume(p0.lastLocation)
                }
            }
            val task = fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                listener,
                Looper.getMainLooper()
            )
            task.addOnFailureListener {
                cont.resumeWithException(it)
            }
            task.addOnCanceledListener {
                cont.resume(null)
            }
        }
    }
}