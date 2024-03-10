package com.general.common.firebase

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.general.common.BuildConfig
import com.general.common.util.DialogUtils
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig

class ForceUpdateChecker(
    private val context: Activity
) {

    fun check(
        onSuccess: () -> Unit
    ) {
        val remoteConfig = Firebase.remoteConfig

        val minVersion = remoteConfig.getLong(KEY_MIN_VERSION)
        val thisVersion = BuildConfig.VERSION_CODE

        if (minVersion > thisVersion) {
            val updateUrl = remoteConfig.getString(KEY_UPDATE_URL)
            onUpdateNeeded(updateUrl)
            return
        }

        val maxVersion = remoteConfig.getLong(KEY_MAX_VERSION)
        if (maxVersion > thisVersion) {
            onUpdateAvailable()
        }
        onSuccess.invoke()
    }

    private fun onUpdateAvailable() {
        DialogUtils.showDialogAlert(
            context = context,
            "Attention",
            "There is new App version"
        )
    }

    private fun onUpdateNeeded(updateUrl: String) {
        DialogUtils.showDefaultDialog(
            context = context,
            DialogUtils.DefaultDialogData(
                title = "New version available",
                desc = "Please, update app to new version to continue.",
                txtButton1 = "No, thanks",
                txtButton2 = "Update"
            ),
            {
                context.finish()
            },
            {
                redirectStore(updateUrl)
                context.finish()
            }
        )
    }

    private fun redirectStore(updateUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    companion object {
        const val KEY_MAX_VERSION = "test_danamon_force_update_max_version"
        const val KEY_MIN_VERSION = "test_danamon_force_update_min_version"
        const val KEY_UPDATE_URL = "test_danamon_force_update_store_url"
    }
}