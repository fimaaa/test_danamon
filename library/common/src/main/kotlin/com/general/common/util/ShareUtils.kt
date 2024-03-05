package com.general.common.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.general.common.extension.isPackageInstalled
import java.util.*

object ShareUtils {

    const val TWITTER = "com.twitter.android"
    const val FACEBOOK = "com.facebook.katana"
    const val INSTAGRAM = "com.instagram.android"

    fun shareIntent(
        mContext: Context,
        packageName: String,
        linkUrl: String,
        listenerError: ((Throwable) -> Unit)? = null
    ) {
        with(mContext) {
            try {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                shareIntent.setPackage(packageName.lowercase(Locale.getDefault()))
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Link FK")
                shareIntent.putExtra(Intent.EXTRA_TEXT, linkUrl)
                val resolved = mContext.isPackageInstalled(packageName)
                return if (resolved) {
                    startActivity(shareIntent)
                } else {
                    try {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=$packageName")
                            )
                        )
                    } catch (e: ActivityNotFoundException) {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                            )
                        )
                    }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
                listenerError?.invoke(e)
            }
        }
    }

    fun shareIntentDefault(
        mContext: Context,
        linkUrl: String,
        listenerError: ((Throwable) -> Unit)? = null
    ) {
        with(mContext) {
            try {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Link FK")
                shareIntent.putExtra(Intent.EXTRA_TEXT, linkUrl)
                startActivity(shareIntent)
            } catch (e: Throwable) {
                e.printStackTrace()
                listenerError?.invoke(e)
            }
        }
    }
}
