package com.github.popalay.tutors

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.RequiresApi
import android.support.annotation.StyleRes
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView

class TutorialLayout : FrameLayout {

    private var textColor: Int = 0
    private var shadowColor: Int = 0
    private var textSize: Float = 0f
    private lateinit var completeIcon: Drawable
    private lateinit var nextButtonText: String
    private lateinit var completeButtonText: String
    private var spacing: Int = 0
    private var lineWidth: Float = 0f

    private var isLast: Boolean = false

    private var x: Int = 0
    private var y: Int = 0

    private var tutorialListener: TutorialListener? = null
    private lateinit var buttonNext: Button
    private lateinit var text: TextView
    private var bitmap: Bitmap? = null
    private var lastTutorialView: View? = null
    private var paint: Paint? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, builder: TutorsBuilder) : super(context) {
        init(context, builder)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context,
                attrs: AttributeSet?,
                @AttrRes defStyleAttr: Int,
                @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context)
    }

    fun setTutorialListener(tutorialListener: TutorialListener?) {
        this.tutorialListener = tutorialListener
    }

    fun showTutorial(view: View, text: CharSequence, isLast: Boolean = false) {
        this.isLast = isLast

        bitmap?.recycle()
        lastTutorialView?.isDrawingCacheEnabled = false

        val location = IntArray(2)
        lastTutorialView = view
        view.getLocationInWindow(location)
        view.isDrawingCacheEnabled = true

        this.text.text = text
        this.buttonNext.text = if (isLast) this.completeButtonText else this.nextButtonText
        this.bitmap = view.drawingCache
        this.x = location[0]
        this.y = location[1] - getStatusBarHeight()

        this.visibility = View.VISIBLE
        moveText(!inTop())
        invalidate()
    }

    fun closeTutorial() {
        this.visibility = View.GONE
        this.bitmap?.recycle()
        this.bitmap = null
        this.lastTutorialView?.isDrawingCacheEnabled = false
        this.lastTutorialView = null
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        recycleResources()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        bitmap?.apply {
            canvas.drawBitmap(this, x.toFloat(), y.toFloat(), paint)
            val inTop = inTop()

            val lineX = x + this.width / 2
            val lineY = y + if (inTop) this.height + spacing else -spacing
            val lineYEnd = if (inTop) text.top - spacing else text.bottom + spacing

            canvas.drawLine(lineX.toFloat(), lineY.toFloat(), lineX.toFloat(), lineYEnd.toFloat(), paint)
        }
    }

    private fun inTop(): Boolean {
        bitmap?.apply {
            val viewCenter = this.height / 2 + y
            val parentCenter = this@TutorialLayout.height / 2
            return viewCenter < parentCenter || parentCenter == 0
        }
        return true
    }

    private fun init(context: Context, builder: TutorsBuilder? = null) {
        visibility = View.GONE

        if (isInEditMode) {
            return
        }

        setWillNotDraw(false)

        applyAttrs(context, builder)

        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint?.color = textColor
        paint?.strokeWidth = lineWidth

        setPadding(spacing, spacing, spacing, spacing)
        setBackgroundColor(shadowColor)
        isClickable = true
        isFocusable = true

        initButton(context)
        initText(context)
        initCross(context)
    }

    private fun applyAttrs(context: Context, builder: TutorsBuilder?) {
        this.textColor = Color.WHITE
        this.textSize = resources.getDimension(R.dimen.textNormal)
        this.shadowColor = ContextCompat.getColor(context, R.color.shadow)
        this.completeIcon = ContextCompat.getDrawable(context, R.drawable.ic_cross_24_white)
        this.nextButtonText = resources.getString(R.string.action_next)
        this.completeButtonText = resources.getString(R.string.action_got_it)
        this.spacing = resources.getDimension(R.dimen.spacingNormal).toInt()
        this.lineWidth = resources.getDimension(R.dimen.lineWidth)

        builder?.let {
            this.textColor = builder.textColorRes.ifNotZero(this.textColor) {
                ContextCompat.getColor(context, it)
            }
            this.textSize = builder.textSizeRes.ifNotZero(this.textSize) {
                resources.getDimension(it)
            }
            this.shadowColor = builder.shadowColorRes.ifNotZero(this.shadowColor) {
                ContextCompat.getColor(context, it)
            }
            this.completeIcon = builder.completeIconRes.ifNotZero(this.completeIcon) {
                ContextCompat.getDrawable(context, it)
            }
            this.nextButtonText = builder.nextButtonTextRes.ifNotZero(this.nextButtonText) {
                resources.getString(it)
            }
            this.completeButtonText = builder.completeButtonTextRes.ifNotZero(this.completeButtonText) {
                resources.getString(it)
            }
            this.spacing = builder.spacingRes.ifNotZero(this.spacing) {
                resources.getDimension(it).toInt()
            }
            this.lineWidth = builder.lineWidthRes.ifNotZero(this.lineWidth) {
                resources.getDimension(it)
            }
        }
    }

    private fun initButton(context: Context) {
        this.buttonNext = Button(context)
        val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layoutParams.gravity = Gravity.BOTTOM or Gravity.END
        this.buttonNext.layoutParams = layoutParams

        val outValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.buttonNext.foreground = resources.getDrawable(outValue.resourceId, context.theme)
        }

        addView(buttonNext)

        this.buttonNext.setOnClickListener {
            tutorialListener?.apply { if (isLast) onComplete() else onNext() }
        }
    }

    private fun initCross(context: Context) {
        val cross = ImageView(context)
        val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layoutParams.gravity = Gravity.TOP or Gravity.END
        cross.layoutParams = layoutParams
        cross.setImageDrawable(completeIcon)

        addView(cross)

        cross.setOnClickListener {
            tutorialListener?.onCompleteAll()
        }
    }

    private fun initText(context: Context) {
        this.text = TextView(context)
        this.text.setTextColor(textColor)
        this.text.gravity = Gravity.CENTER
        this.text.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        addView(this.text)
    }

    private fun moveText(top: Boolean) {
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        layoutParams.gravity = Gravity.CENTER or if (top) Gravity.TOP else Gravity.BOTTOM
        val margin = spacing * 3
        if (top) {
            layoutParams.setMargins(0, margin, 0, 0)
        } else {
            layoutParams.setMargins(0, 0, 0, margin)
        }
        this.text.layoutParams = layoutParams
    }

    private fun recycleResources() {
        this.bitmap?.recycle()
        this.bitmap = null
        this.lastTutorialView?.isDrawingCacheEnabled = false
        this.lastTutorialView = null
        this.paint = null
    }

    private fun getStatusBarHeight(): Int {
        var height = 0
        val resId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resId > 0) {
            height = context.resources.getDimensionPixelSize(resId)
        }
        return height
    }
}