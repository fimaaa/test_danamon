package com.general.common.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import com.general.common.extension.toast
import com.general.model.common.DeviceLocation

object ThirdPartyHelper {
    fun navigateGoogleMaps(
        mContext: Context,
        location: DeviceLocation
    ) {
        val intentUri = Uri.parse("google.navigation:q=${location.latitude},${location.longitude}")
        val navigationIntent = Intent(Intent.ACTION_VIEW, intentUri)
        navigationIntent.setPackage("com.google.android.apps.maps") // Specify Google Maps package
        mContext.startActivity(navigationIntent)
    }
    fun call(activity: Activity, contact: String) {
        try {
            val callIntent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$contact"))
            callIntent.setPackage(isWhatsappInstalled(activity))

            activity.startActivity(callIntent)
        } catch (e: Exception) {
            try {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$contact"))
                activity.startActivity(intent)
            } catch (e: Exception) {
                activity.applicationContext?.toast("Phone Apps not found")
            }
        }
    }

    fun sendMessage(activity: Activity, contact: String, message: String = "") {
        try {
            val waApi = "https://api.whatsapp.com/send"
            val url =
                if (message.isNotEmpty()) "$waApi?phone=$contact&text=$message" else "$waApi?phone=$contact"
            val installedWa = isWhatsappInstalled(activity)
            val sendIntent = Intent().apply {
                `package` = installedWa
                data = Uri.parse(url)
                action = Intent.ACTION_VIEW
            }
            activity.startActivity(sendIntent)
        } catch (_: Exception) {
            try {
                val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$contact"))
                intent.putExtra("sms_body", message)
                activity.startActivity(intent)
            } catch (e: Exception) {
//                Firebase.crashlytics.recordException(e)
                activity.applicationContext?.toast("Message Apps not found")
            }
        }
    }

    private fun isWhatsappInstalled(activity: Activity): String {
        val pm = activity.packageManager
        return try {
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            "com.whatsapp"
        } catch (e: PackageManager.NameNotFoundException) {
            return try {
                pm.getPackageInfo("com.whatsapp.w4b", PackageManager.GET_ACTIVITIES)
                "com.whatsapp.w4b"
            } catch (e: PackageManager.NameNotFoundException) {
                throw e
            }
        }
    }
}