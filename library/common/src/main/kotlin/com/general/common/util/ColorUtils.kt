package com.general.common.util

import android.graphics.Color
import androidx.annotation.ColorInt

object ColorUtils {
    @ColorInt
    fun safeParseColor(colorTarget: String, colorOptional: String = "#5E50A1"): Int = try {
        Color.parseColor(colorTarget.replace(" ", ""))
    } catch (e: IllegalArgumentException) {
        try {
            Color.parseColor(colorOptional.replace(" ", ""))
        } catch (e: IllegalArgumentException) {
            Color.parseColor("#5E50A1")
        }
    }
}