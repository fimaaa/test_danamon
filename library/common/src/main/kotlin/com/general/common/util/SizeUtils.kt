package com.general.common.util

import android.content.Context

/**
 * Created by Satya Dwi Anggiawan on 27/03/19.
 */
object SizeUtils {

    fun setPxToDp(context: Context?, pixel: Int): Int {
        val resources = context?.resources
        val scale = resources?.displayMetrics?.density ?: 0f
        return (pixel * scale + 0.5f).toInt()
    }
}