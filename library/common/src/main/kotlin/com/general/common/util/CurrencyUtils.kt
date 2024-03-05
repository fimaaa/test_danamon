package com.general.common.util

import android.content.Context
import com.general.common.R
import java.text.NumberFormat
import java.util.*

object CurrencyUtils {
    private fun convertDoubleToFormatNumber(number: String?): String {
        val format = NumberFormat.getNumberInstance(Locale.GERMANY)
        return try {
            format.format(number?.toDouble())
        } catch (e: Exception) {
            "0"
        }
    }

    fun simpleCovertCurrency(mContext: Context?, amount: String?): String {
        return mContext?.getString(R.string.format_price, convertDoubleToFormatNumber(amount))
            ?: "Rp 0"
    }
}