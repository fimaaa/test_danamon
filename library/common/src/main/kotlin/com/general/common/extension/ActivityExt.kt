package com.general.common.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import com.general.common.R
import com.general.common.base.BaseActivity
import com.general.common.util.ColorUtils
import com.general.common.util.DialogUtils.showAlertDialog

fun Activity.navigateTo(to: Class<*>) {
    startActivity(Intent(this, to))
    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
}

fun Activity.navigateTo(intent: Intent) {
    startActivity(intent)
    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
}

fun Activity.navigateForResultTo(requestCode: Int, to: Class<*>) {
    startActivityForResult(Intent(this, to), requestCode)
    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
}

fun Activity.navigateForResultTo(requestCode: Int, intent: Intent) {
    startActivityForResult(intent, requestCode)
    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
}

fun Activity.newNavigateForResultTo(launcher: ActivityResultLauncher<Intent>, to: Class<*>) {
    launcher.launch(Intent(this, to))
    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
}

fun Activity.newNavigateForResultTo(launcher: ActivityResultLauncher<Intent>, intent: Intent) {
    launcher.launch(intent)
    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
}

fun Activity.isGPSReady(onReady: () -> Unit) {
    val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    if (statusOfGPS) {
        onReady.invoke()
    } else {
        showAlertDialog(
            this,
            "Silahkan hidupkan GPS untuk melanjutkan.",
            "Perhatian"
        ) {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }
}

fun Activity.isGPSReady(
    onReady: () -> Unit,
    onNotReady: () -> Unit
) {
    val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    if (statusOfGPS) {
        onReady.invoke()
    } else {
        onNotReady.invoke()
    }
}

fun Activity.hideStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.insetsController?.hide(WindowInsets.Type.statusBars())
    } else {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }
}

fun Activity.showStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.insetsController?.show(WindowInsets.Type.statusBars())
    } else {
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}

fun Activity.getScreenDevice(): DisplayMetrics {
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics
}

fun Activity.getWidthAds(): Int {
    val display = windowManager.defaultDisplay
    val outMetrics = DisplayMetrics()
    display.getMetrics(outMetrics)

    val density = outMetrics.density

    var adWidthPixels = getScreenDevice().widthPixels.toFloat()
    if (adWidthPixels == 0f) {
        adWidthPixels = outMetrics.widthPixels.toFloat()
    }

    return (adWidthPixels / density).toInt()
}

fun Activity.changeStatusBarColor(color: Int) {
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = ContextCompat.getColor(
        this, color
    )
}

fun Activity.changeStatusBarColor(color: String) {
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = ColorUtils.safeParseColor(color)
}

fun Activity.makeStatusBarTransparent() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
            statusBarColor = Color.TRANSPARENT
        }
    }
}

fun Activity.scrollBehaviour(scroll: NestedScrollView) {
    scroll.setOnScrollChangeListener { _, _, scrollY, _, _ ->
        if (scrollY != 0) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(
                this@scrollBehaviour, android.R.color.white
            )
        } else if (scrollY == 0) makeStatusBarTransparent()
    }
}

fun Activity.makeDefaultStatusBar() {
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = ContextCompat.getColor(
        this, android.R.color.white
    )
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
}

fun BaseActivity.isGPSReady(onReady: () -> Unit) {
    val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val statusOfGPS =
        manager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    if (statusOfGPS) {
        onReady.invoke()
    } else {
        showAlertDialog(
            this,
            "Perhatian",
            "Silahkan hidupkan GPS untuk melanjutkan.",
            false
        ) {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }
}