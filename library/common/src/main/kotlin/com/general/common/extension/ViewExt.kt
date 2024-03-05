package com.general.common.extension

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.SystemClock
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.snackbar.Snackbar
import com.general.common.util.ColorUtils.safeParseColor
import com.general.model.common.UIText
import kotlinx.coroutines.flow.SharedFlow

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.safeOnClickListener(
    defaultInterval: Int = 1000,
    listener: () -> Unit
) {
    var lastTimeClicked: Long = 0

    this.setOnClickListener {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return@setOnClickListener
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        listener()
    }
}

fun Context?.showKeyboard() {
    (this?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
        InputMethodManager.SHOW_FORCED,
        InputMethodManager.HIDE_IMPLICIT_ONLY
    )
}

fun Context?.hideKeyboard(view: View?) {
    (this?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
        view?.windowToken,
        0
    )
}

fun Context.setBackground(drawable: Int): Drawable? = ContextCompat.getDrawable(this, drawable)

fun View.getParentActivity(): AppCompatActivity? {
    var context = this.context
    while (context is ContextWrapper) {
        if (context is AppCompatActivity) {
            return context
        }
        context = context.baseContext
    }
    return null
}

fun NestedScrollView.onScroll(
    listener: (v: View?, scrollY: Int, alpha: Float) -> Unit
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        setOnScrollChangeListener { v, _, scrollY, _, _ ->
            listener.invoke(v, scrollY, scrollY.toFloat() / 500)
        }
    } else {
        setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener {
                v, _, scrollY, _, _ -> listener.invoke(v, scrollY, scrollY.toFloat() / 500)
            }
        )
    }
}

fun View.animateShowFromBottom(isShowing: Boolean, duration: Long = 400L) {
    if (isShowing) {
        if (isVisible) return
        visible()
        val animation = TranslateAnimation(0f, 0f, height.toFloat(), 0f)
        animation.duration = duration
        startAnimation(animation)
    } else {
        if (isGone) return
        val animation = TranslateAnimation(0f, 0f, 0f, height.toFloat())
        animation.duration = duration
        startAnimation(animation)
        gone()
    }
}

/**
 * Used to scroll to the given view.
 *
 * @param scrollViewParent Parent ScrollView
 * @param view View to which we need to scroll.
 */
fun NestedScrollView.scrollToView(view: View) {
    // Get deepChild Offset
    val childOffset = Point()
    getDeepChildOffset(this, view.parent, view, childOffset)
    // Scroll to child.
    this.smoothScrollTo(0, childOffset.y)
}

/**
 * Used to get deep child offset.
 *
 *
 * 1. We need to scroll to child in scrollview, but the child may not the direct child to scrollview.
 * 2. So to get correct child position to scroll, we need to iterate through all of its parent views till the main parent.
 *
 * @param mainParent Main Top parent.
 * @param parent Parent.
 * @param child Child.
 * @param accumulatedOffset Accumulated Offset.
 */
private fun getDeepChildOffset(
    mainParent: ViewGroup,
    parent: ViewParent,
    child: View,
    accumulatedOffset: Point
) {
    val parentGroup = parent as ViewGroup
    accumulatedOffset.x += child.left
    accumulatedOffset.y += child.top
    if (parentGroup == mainParent) {
        return
    }
    getDeepChildOffset(mainParent, parentGroup.parent, parentGroup, accumulatedOffset)
}

fun ViewGroup.changeSize(width: Int? = null, height: Int? = null) {
    val param = layoutParams
    width?.let { param.width = it }
    height?.let { param.height = it }
    layoutParams = param
}

fun View.safeOnClikListener(
    defaultInterval: Int = 1000,
    listener: () -> Unit
) {
    var lastTimeClicked: Long = 0

    this.setOnClickListener {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return@setOnClickListener
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        listener()
    }
}

fun View.setBackgroundFromText(
    color: String
) {
    val colorDefault: Int = safeParseColor(color)
    this.background = ColorDrawable(colorDefault)
}

fun View.setRoundedBackgroundFromText(
    color: String
) {
    val colorDefault: Int = safeParseColor(color)
    val shape = GradientDrawable()
    shape.shape = GradientDrawable.RECTANGLE
    shape.setColor(colorDefault)
    shape.cornerRadius = 8f
    this.background = shape
}

fun View.setCircleBackgroundFromText(
    color: String
) {
    val colorDefault: Int = safeParseColor(color)
    val shape = GradientDrawable()
    shape.shape = GradientDrawable.OVAL
    shape.setColor(colorDefault)
    this.background = shape
}

fun ViewGroup.ChangeSize(width: Int? = null, heigt: Int? = null) {
    val param = layoutParams
    width?.let { param.width = it }
    heigt?.let { param.height = it }
    layoutParams = param
}

/**
 * Transforms static java function Snackbar.make() to an extension function on View.
 */
fun Fragment.showSnackbar(snackbarText: String, timeLength: Int) {
    activity?.let { Snackbar.make(it.findViewById<View>(android.R.id.content), snackbarText, timeLength).show() }
}

/**
 * Triggers a snackbar message when the value contained by snackbarTaskMessageLiveEvent is modified.
 */
fun Fragment.setupSnackbar(lifecycleOwner: LifecycleOwner, snackbarEvent: SharedFlow<UIText>, timeLength: Int) {
    safeCollect(snackbarEvent) { res ->
            context?.let { showSnackbar(res.asString(it), timeLength) }
    }
}