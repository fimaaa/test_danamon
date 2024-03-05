package com.general.common.helper

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiManager
import java.net.NetworkInterface
import java.util.*

object MacAddressHelper {
    @SuppressLint("HardwareIds")
    fun getMacAddress(context: Context): String {
        val manager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager?
        val info = manager?.connectionInfo
        var macAddress = info?.macAddress ?: ""
        try {
            val all: List<NetworkInterface> =
                Collections.list(NetworkInterface.getNetworkInterfaces())
            for (nif in all) {
                if (nif.name.lowercase(Locale.getDefault()) != "wlan0") continue
                val macBytes: ByteArray = nif.hardwareAddress
                macAddress = run {
                    val res1 = StringBuilder()
                    for (b in macBytes) {
                        res1.append(String.format("%02X:", b))
                    }
                    if (res1.isNotEmpty()) {
                        res1.deleteCharAt(res1.length - 1)
                    }
                    res1.toString()
                }
            }
        } catch (_: Exception) { }
        return macAddress
    }
}