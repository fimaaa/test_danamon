package com.general.common.extension

import android.content.Context
import android.content.res.Resources
import com.general.common.R
import java.text.DecimalFormat
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.roundToInt

fun String.convertPrice(): String {
    val formatter = DecimalFormat("#,###")
    val m = this
    val formattedNumber = "Rp${formatter.format(m.toDouble())}"

    return formattedNumber.replace(",", ".")
}

fun Double.convertPrice(): String {
    val formatter = DecimalFormat("#,###")
    val m = abs(this)
    val formattedNumber = "Rp${formatter.format(m)}"

    return formattedNumber.replace(",", ".")
}

fun Int.pxToDp(): Int {
    return (this / Resources.getSystem().displayMetrics.density).toInt()
}

fun Int.dpToPx(): Int {
    return (this.toFloat() * Resources.getSystem().displayMetrics.density).roundToInt()
}

fun Float.toDistanceString(mContext: Context): String = if (this >= 1000f) {
    mContext.getString(
        R.string.distance_km,
        ceil(this / 1000f).roundToInt()
    )
} else {
    mContext.getString(
        R.string.distance_m,
        ceil(this).roundToInt()
    )
}