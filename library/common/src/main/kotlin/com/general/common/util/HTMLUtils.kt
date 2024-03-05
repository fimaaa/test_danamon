package com.general.common.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LevelListDrawable
import android.os.Build
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.net.URL

object HTMLUtils {
    private fun makeLinkClickable(
        mContext: Context,
        strBuilder: SpannableStringBuilder,
        span: URLSpan
    ) {
        val start = strBuilder.getSpanStart(span)
        val end = strBuilder.getSpanEnd(span)
        val flags = strBuilder.getSpanFlags(span)
        val clickable = object : ClickableSpan() {
            override fun onClick(view: View) {
                val url = URL(span.url)
                Toast.makeText(mContext, "Url = $url", Toast.LENGTH_SHORT).show()
            }
        }
        strBuilder.setSpan(clickable, start, end, flags)
        strBuilder.removeSpan(span)
    }

    fun setTextViewHTML(mContext: Context, text: TextView, html: String?) {
        val sequence =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(
                    html,
                    Html.FROM_HTML_MODE_COMPACT,
                    HtmlGetterImage(mContext, text),
                    null
                )
            } else {
                @Suppress("DEPRECATION")
                Html.fromHtml(html, HtmlGetterImage(mContext, text), null)
            }
        val strBuilder = SpannableStringBuilder(sequence)
        val urls = strBuilder.getSpans(0, sequence.length, URLSpan::class.java)
        for (span in urls) {
            makeLinkClickable(mContext, strBuilder, span)
        }
        text.text = strBuilder
        text.movementMethod = LinkMovementMethod.getInstance()
    }

    class HtmlGetterImage internal constructor(
        private val context: Context,
        private val textView: TextView
    ) : Html.ImageGetter {

        override fun getDrawable(url: String): Drawable {
            val drawable = LevelListDrawable()
            try {
                Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            val bitmapDrawable = BitmapDrawable(context.resources, resource)
                            drawable.run {
                                addLevel(1, 1, bitmapDrawable)
                                setBounds(0, 0, resource.width, resource.height)
                            }
                            drawable.level = 1
                            textView.invalidate()
                            textView.text = textView.text
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                        }
                    })
            } catch (e: Exception) {
            }
            return drawable
        }
    }
}
