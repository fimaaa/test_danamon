package com.general.common.customview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.core.content.ContextCompat
import com.general.common.R
import com.general.common.extension.setBackgroundFromText
import kotlin.math.abs

/**
 * Created by Satya Dwi Anggiawan on 25/03/19.
 */
class SwipeButton : RelativeLayout {

    var view: View
    private lateinit var contentContainer: RelativeLayout
    private lateinit var contentTv: TextView
    private lateinit var arrow1: ImageView
    private lateinit var arrow2: ImageView
    private lateinit var arrowLeft1: ImageView
    private lateinit var arrowLeft2: ImageView
    private lateinit var arrowHintRightContainer: LinearLayout
    private lateinit var arrowHintLeftContainer: LinearLayout

    /*
        User configurable settings
     */
    private var btnText: CharSequence = "BUTTON"

    @ColorInt
    private var textColorInt: Int = 0

    @ColorInt
    private var bgColorInt: Int = 0

    @ColorInt
    private var arrowColorInt: Int = 0

    @ColorInt
    private var arrowBackgroundColorInt: Int = 0

    @Dimension
    private var textSize: Float = 14f
    private var goRight: Boolean = true

    private var swipeListener: OnSwipeListener? = null
    private var swipeDistance: Float = 0.25f
    private var currentPoint: Float = 0f

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        android.R.attr.textViewStyle
    ) {
        setAttrs(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        setAttrs(context, attrs)
    }

    private fun setAttrs(context: Context, attrs: AttributeSet?) {
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SwipeButton,
            0, 0
        )
        try {
            val btnString = a.getString(R.styleable.SwipeButton_btn_text)
            if (btnString != null)
                btnText = btnString
            textColorInt = a.getColor(
                R.styleable.SwipeButton_text_color,
                ContextCompat.getColor(context, android.R.color.white)
            )
            bgColorInt = a.getColor(
                R.styleable.SwipeButton_bg_color,
                ContextCompat.getColor(context, R.color.shamrock)
            )
            arrowColorInt = a.getColor(
                R.styleable.SwipeButton_arrow_color,
                ContextCompat.getColor(context, R.color.whiteTransparent)
            )
            arrowBackgroundColorInt = a.getColor(
                R.styleable.SwipeButton_arrow_background_color,
                ContextCompat.getColor(context, R.color.shamrock)
            )
            goRight = a.getBoolean(R.styleable.SwipeButton_go_right, true)
//            textSize = a.getDimensionPixelSize(R.styleable.SwipeButton_text_size, 14).toFloat()
        } finally {
            a.recycle()
        }
    }

    init {
        val inflater = LayoutInflater.from(context)
        view = inflater.inflate(R.layout.swipe_button, this, true)
    }

//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, 56)
//    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        contentContainer = view.findViewById(R.id.relativeLayout_swipeBtn_contentContainer)
        arrowHintRightContainer = view.findViewById(R.id.linearLayout_swipeRightBtn_hintContainer)
        arrowHintLeftContainer = view.findViewById(R.id.linearLayout_swipeLeftBtn_hintContainer)
        contentTv = view.findViewById(R.id.tv_btnText)
        arrow1 = view.findViewById(R.id.iv_arrow1)
        arrow2 = view.findViewById(R.id.iv_arrow2)
        arrowLeft1 = view.findViewById(R.id.iv_arrowLeft1)
        arrowLeft2 = view.findViewById(R.id.iv_arrowLeft2)

        tintArrowHint()
        arrowBackgroundColorChange()
        contentTv.text = btnText
        contentTv.setTextColor(textColorInt)

        if (goRight) {
            arrowHintRightContainer.visibility = View.VISIBLE
            arrowHintLeftContainer.visibility = View.GONE
        } else {
            arrowHintRightContainer.visibility = View.GONE
            arrowHintLeftContainer.visibility = View.VISIBLE
        }

        setBgColor(bgColorInt)
        setupTouchListener()
    }

    private fun setupTouchListener() {
        setOnTouchListener(object : OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        currentPoint = event.x
                        return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        if (goRight) {
                            arrowHintRightContainer.x = event.x - currentPoint

                            if (arrowHintRightContainer.x + arrowHintRightContainer.width > width &&
                                arrowHintRightContainer.x + arrowHintRightContainer.width / 2 < width
                            ) {
                                arrowHintRightContainer.x =
                                    (width - arrowHintRightContainer.width).toFloat()
                            }

                            if (event.x < arrowHintRightContainer.width / 2 &&
                                arrowHintRightContainer.x > 0
                            ) {
                                arrowHintRightContainer.x = 0f
                            }
                        } else {
                            arrowHintLeftContainer.x =
                                event.x - currentPoint + (width - arrowHintLeftContainer.width)

                            if (arrowHintLeftContainer.x + arrowHintLeftContainer.width > width &&
                                arrowHintLeftContainer.x + arrowHintLeftContainer.width / 2 < width
                            ) {
                                arrowHintLeftContainer.x =
                                    (width - arrowHintLeftContainer.width).toFloat()
                            }

                            if (event.x < arrowHintLeftContainer.width / 2 &&
                                arrowHintLeftContainer.x > 0
                            ) {
                                arrowHintLeftContainer.x = 0f
                            }
                        }

                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        if (goRight) {
                            when {
                                event.x > currentPoint && abs(abs(currentPoint) - abs(event.x)) > width * swipeDistance -> performSuccessfulSwipe()
                                arrowHintRightContainer.x <= 0 -> {
                                    arrowHintRightContainer.x = 0f
                                    startHintInitAnim()
                                }
                                else -> animateHintBack()
                            }
                        } else {
                            when {
                                event.x < currentPoint && abs(abs(currentPoint) - abs(event.x)) > width * swipeDistance -> performSuccessfulSwipe()
                                arrowHintLeftContainer.x >= width - arrowHintLeftContainer.width -> {
                                    arrowHintLeftContainer.x =
                                        width - arrowHintLeftContainer.width.toFloat()
                                    startHintInitAnim()
                                }
                                else -> animateHintBack()
                            }
                        }

                        return true
                    }
                }
                return false
            }
        })
    }

    private fun performSuccessfulSwipe() {
        swipeListener?.onSwipeConfirm()
        releaseHintInitAnim()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        startFwdAnim()
    }

    private fun animateHintBack() {
        if (goRight) {
            val positionAnimator = ValueAnimator.ofFloat(arrowHintRightContainer.x, 0f)
            positionAnimator.interpolator = AccelerateDecelerateInterpolator()
            positionAnimator.addUpdateListener {
                val x: Float = positionAnimator.animatedValue as Float
                arrowHintRightContainer.x = x
            }

            positionAnimator.duration = 200
            positionAnimator.start()
        } else {
            val positionAnimator = ValueAnimator.ofFloat(
                arrowHintLeftContainer.x,
                measuredWidth - arrowHintLeftContainer.width.toFloat()
            )
            positionAnimator.interpolator = AccelerateDecelerateInterpolator()
            positionAnimator.addUpdateListener {
                val x: Float = positionAnimator.animatedValue as Float
                arrowHintLeftContainer.x = x
            }

            positionAnimator.duration = 200
            positionAnimator.start()
        }
    }

    private fun startFwdAnim() {
        if (isEnabled) {
            if (goRight) {
                val animation = TranslateAnimation(0f, measuredWidth.toFloat(), 0f, 0f)
                animation.interpolator = AccelerateDecelerateInterpolator()
                animation.duration = 1000
                animation.setAnimationListener(object : Animation.AnimationListener {

                    override fun onAnimationStart(animation: Animation) = Unit

                    override fun onAnimationEnd(animation: Animation) {
                        startHintInitAnim()
                    }

                    override fun onAnimationRepeat(animation: Animation?) = Unit
                })
                arrowHintRightContainer.startAnimation(animation)
            } else {
                val animation = TranslateAnimation(0f, -measuredWidth.toFloat(), 0f, 0f)
                animation.interpolator = AccelerateDecelerateInterpolator()
                animation.duration = 1000
                animation.setAnimationListener(object : Animation.AnimationListener {

                    override fun onAnimationStart(animation: Animation) = Unit

                    override fun onAnimationEnd(animation: Animation) {
                        startHintInitAnim()
                    }

                    override fun onAnimationRepeat(animation: Animation?) = Unit
                })
                arrowHintLeftContainer.startAnimation(animation)
            }
        }
    }

    /**
     * animate entry of hint from the left-most edge
     */
    private fun startHintInitAnim() {
        if (goRight) {
            val anim = TranslateAnimation(-arrowHintRightContainer.width.toFloat(), 0f, 0f, 0f)
            anim.duration = 500
            arrowHintRightContainer.startAnimation(anim)
        } else {
            val anim = TranslateAnimation(arrowHintLeftContainer.width.toFloat(), 0f, 0f, 0f)
            anim.duration = 500
            arrowHintLeftContainer.startAnimation(anim)
        }
    }

    /**
     * animate entry of hint from the current position
     */
    private fun releaseHintInitAnim() {
        if (goRight) {
            val anim = TranslateAnimation(0f, width.toFloat(), 0f, 0f)
            anim.duration = 100
            anim.setAnimationListener(object : Animation.AnimationListener {

                override fun onAnimationStart(animation: Animation) = Unit

                override fun onAnimationEnd(animation: Animation) {
                    arrowHintRightContainer.x = 0f
                    startHintInitAnim()
                }

                override fun onAnimationRepeat(animation: Animation?) = Unit
            })
            arrowHintRightContainer.startAnimation(anim)
        } else {
            val anim = TranslateAnimation(0f, -width.toFloat(), 0f, 0f)
            anim.duration = 100
            anim.setAnimationListener(object : Animation.AnimationListener {

                override fun onAnimationStart(animation: Animation) = Unit

                override fun onAnimationEnd(animation: Animation) {
                    arrowHintLeftContainer.x = width - arrowHintLeftContainer.width.toFloat()
                    startHintInitAnim()
                }

                override fun onAnimationRepeat(animation: Animation?) = Unit
            })
            arrowHintLeftContainer.startAnimation(anim)
        }
    }

    /**
     * Just like performOnClick() in a standard button,
     * this will call the attached OnSwipeListener
     * and morph the btn to a circle
     */
    fun performOnSwipe() {
        performSuccessfulSwipe()
    }

    private fun updateBackground(color: Int? = null) {
        contentContainer.setBackgroundColor(color ?: getBgColor())
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        if (!enabled) {
            updateBackground(R.color.charcoalGrey)
            this.alpha = 0.5f
        } else {
            setBgColor(getBgColor())
            this.alpha = 1f
        }
    }

    private fun tintArrowHint() {
        arrow1.setColorFilter(arrowColorInt, PorterDuff.Mode.MULTIPLY)
        arrow2.setColorFilter(arrowColorInt, PorterDuff.Mode.MULTIPLY)
        arrowLeft1.setColorFilter(arrowColorInt, PorterDuff.Mode.MULTIPLY)
        arrowLeft2.setColorFilter(arrowColorInt, PorterDuff.Mode.MULTIPLY)
    }

    private fun arrowBackgroundColorChange() {
        arrowHintRightContainer.setBackgroundColor(arrowBackgroundColorInt)
        arrowHintLeftContainer.setBackgroundColor(arrowBackgroundColorInt)
    }

    interface OnSwipeListener {
        fun onSwipeConfirm()
    }

    fun setText(text: CharSequence) {
        this.btnText = text
        contentTv.text = text
    }

    fun getText(): CharSequence {
        return this.btnText
    }

    fun setTextColor(@ColorInt textColor: Int) {
        this.textColorInt = textColor
        contentTv.setTextColor(textColor)
    }

    @ColorInt
    fun getTextColor(): Int {
        return this.textColorInt
    }

    private fun setBgColor(@ColorInt bgColor: Int) {
        this.bgColorInt = bgColor
        updateBackground()
    }

    @ColorInt
    fun getBgColor(): Int {
        return this.bgColorInt
    }

    fun updateBackgroundFromText(value: String) {
        contentContainer.setBackgroundFromText(value)
    }

    fun getArrowColorRes(): Int {
        return this.arrowColorInt
    }

    /**
     * Include alpha in arrowColor for transparency (ex: #33FFFFFF)
     */
    fun setArrowColor(arrowColor: Int) {
        this.arrowColorInt = arrowColor
        tintArrowHint()
    }

    fun setArrowBackground(arrowBackground: Int) {
        this.arrowBackgroundColorInt = arrowBackground
        arrowBackgroundColorChange()
    }

    fun setTextSize(@Dimension textSize: Float) {
        this.textSize = textSize
        contentTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
    }

    @Dimension
    fun getTextSize(): Float {
        return this.textSize
    }

    /**
     * How much of the button must the user swipe to trigger the OnSwipeListener successfully
     *
     * @param swipeDistance float from 0.0 to 1.0 where 1.0 means user must swipe the button fully from end to end. Default is 0.85.
     */
    fun setSwipeDistance(@Dimension swipeDistance: Float) {
        if (swipeDistance > 1.0f) {
            this.swipeDistance = 1.0f
        }
        if (swipeDistance < 0.0f) {
            this.swipeDistance = 0.0f
        }
        this.swipeDistance = swipeDistance
    }

    @Dimension
    fun getSwipeDistance(): Float {
        return this.swipeDistance
    }

    fun setOnSwipeListener(customSwipeListener: OnSwipeListener) {
        this.swipeListener = customSwipeListener
    }

    fun setOnSwipeListener(listener: () -> Unit) {
        this.swipeListener = object : OnSwipeListener {
            override fun onSwipeConfirm() {
                listener.invoke()
            }
        }
    }
}