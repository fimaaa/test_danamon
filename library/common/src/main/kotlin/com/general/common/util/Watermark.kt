package com.general.common.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Paint.DITHER_FLAG
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.Typeface
import androidx.annotation.ColorInt
import com.general.common.R

fun addWatermark(
    context: Context,
    bitmap: Bitmap,
    watermarkText: String,
    options: WatermarkOptions = WatermarkOptions()
): Bitmap {
    try {
        val result = bitmap.copy(bitmap.config, true)
        val canvas = Canvas(result)
        val paint = Paint(ANTI_ALIAS_FLAG or DITHER_FLAG)
        paint.textAlign = when (options.corner) {
            Corner.TOP_LEFT,
            Corner.BOTTOM_LEFT -> Paint.Align.LEFT
            Corner.TOP_RIGHT,
            Corner.BOTTOM_RIGHT -> Paint.Align.RIGHT
        }
        val textSize = result.width * options.textSizeToWidthRatio
        paint.textSize = textSize
        paint.color = options.textColor
        if (options.shadowColor != null) {
            paint.setShadowLayer(textSize / 2, 0f, 0f, options.shadowColor)
        }
        if (options.typeface != null) {
            paint.typeface = options.typeface
        }
        val padding = result.width * options.paddingToWidthRatio
        val coordinates =
            calculateCoordinates(
                watermarkText,
                paint,
                options,
                canvas.width,
                canvas.height,
                padding
            )
        canvas.drawText(watermarkText, coordinates.x, coordinates.y, paint)
        val opt = BitmapFactory.Options()
        opt.inMutable = true
        val logoMaxSize = if (result.width < 300) 80 else 300
        val logo = getResizedBitmap(
            BitmapFactory.decodeResource(
                context.resources,
                R.drawable.logo_app_general,
                opt
            ), logoMaxSize
        )
        val optionsLogo = WatermarkOptions(corner = Corner.TOP_LEFT)
        val paddingLogo = result.width * 0.05f
        val coordinatesLogo =
            calculateCoordinates(
                watermarkText,
                paint,
                optionsLogo,
                canvas.width,
                canvas.height,
                paddingLogo
            )
        canvas.drawBitmap(logo, coordinatesLogo.x, coordinatesLogo.y, paint)
        return result
    } catch (e: Exception) {
        e.printStackTrace()
        return bitmap
    }
}

private fun calculateCoordinates(
    watermarkText: String,
    paint: Paint,
    options: WatermarkOptions,
    width: Int,
    height: Int,
    padding: Float
): PointF {
    val x = when (options.corner) {
        Corner.TOP_LEFT,
        Corner.BOTTOM_LEFT -> {
            padding
        }
        Corner.TOP_RIGHT,
        Corner.BOTTOM_RIGHT -> {
            width - padding
        }
    }
    val y = when (options.corner) {
        Corner.BOTTOM_LEFT,
        Corner.BOTTOM_RIGHT -> {
            height - padding
        }
        Corner.TOP_LEFT,
        Corner.TOP_RIGHT -> {
            val bounds = Rect()
            paint.getTextBounds(watermarkText, 0, watermarkText.length, bounds)
            val textHeight = bounds.height()
            textHeight + padding
        }
    }
    return PointF(x, y)
}

enum class Corner {
    TOP_LEFT,
    TOP_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_RIGHT,
}

data class WatermarkOptions(
    val corner: Corner = Corner.BOTTOM_RIGHT,
    val textSizeToWidthRatio: Float = 0.04f,
    val paddingToWidthRatio: Float = 0.03f,
    @ColorInt val textColor: Int = Color.WHITE,
    @ColorInt val shadowColor: Int? = Color.BLACK,
    val typeface: Typeface? = null
)

fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap {
    var width = image.width
    var height = image.height
    val bitmapRatio = width.toFloat() / height.toFloat()
    if (bitmapRatio > 1) {
        width = maxSize
        height = (width / bitmapRatio).toInt()
    } else {
        height = maxSize
        width = (height * bitmapRatio).toInt()
    }
    return Bitmap.createScaledBitmap(image, width, height, true)
}