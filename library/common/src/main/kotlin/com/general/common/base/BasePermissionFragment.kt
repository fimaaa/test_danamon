package com.general.common.base

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

abstract class BasePermissionFragment() :
    BaseFragment() {

    val reqPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            // Good pass
            onPermissionsGranted(0)
        } else {
            // Failed pass
            AlertDialog.Builder(requireActivity())
                .setTitle("Notice")
                .setMessage("Permission access needed")
                .setPositiveButton(
                    "Allow"
                ) { _, _ ->
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    intent.addCategory(Intent.CATEGORY_DEFAULT)
                    intent.data = Uri.parse("package:${requireActivity().packageName}")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                    startActivity(intent)
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            DEFAULT_ACCEPT_REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onPermissionsGranted(requestCode)
            } else {
                AlertDialog.Builder(requireActivity())
                    .setTitle("Notice")
                    .setMessage("Permission access needed")
                    .setPositiveButton(
                        "Allow"
                    ) { _, _ ->
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        intent.addCategory(Intent.CATEGORY_DEFAULT)
                        intent.data = Uri.parse("package:${requireActivity().packageName}")
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                        startActivity(intent)
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }

    fun requestAppPermissions(
        requestedPermissions: Array<String>,
        stringId: Int,
        requestCode: Int
    ) {
        var permissionCheck = PackageManager.PERMISSION_GRANTED
        var shouldShowRequestPermissionRationale = false
        for (permission in requestedPermissions) {
            permissionCheck += ContextCompat.checkSelfPermission(requireActivity(), permission)
            shouldShowRequestPermissionRationale =
                shouldShowRequestPermissionRationale || ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    permission
                )
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale) {
                AlertDialog.Builder(requireActivity())
                    .setTitle("Notice")
                    .setMessage(stringId)
                    .setPositiveButton(
                        "Allow"
                    ) { _, _ ->
                        requestPermissions(requestedPermissions, requestCode)
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            } else {
                requestPermissions(requestedPermissions, requestCode)
            }
        } else {
            onPermissionsGranted(requestCode)
        }
    }

    abstract fun onPermissionsGranted(requestCode: Int)

    companion object {
        const val DEFAULT_ACCEPT_REQUEST_CODE = 20
    }
}