package app.uraniumcode.radarview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.View

class RadarView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
   private var TAG = this.javaClass.simpleName
    private var centerX: Int = 0
    private var centerY: Int = 0
    private var maxRadius: Int = 0
    private var circleCount: Int = 4
    private var circleRadii: IntArray? = null
    private var radarPaint: Paint
    private var scanPaint: Paint
    private var handler: Handler? = null
    private var isAnimating: Boolean = false
    private var sweepAngle: Int = 0
    private var sweepSpeed: Int = 5
    private var matrix: Matrix
    private var circleColor: Int = 0
    private var sweepColor: Int = 0
    private var alpha = 1.0f
    private var isDisappearing = false
    private var radarIcons: List<Bitmap>? = null
    private var iconPositions: List<Pair<Float, Float>>? = null
    private var iconClickListener: OnRadarIconClickListener? = null

    init {
        setAttr(context, attrs!!)
        radarPaint = Paint()
        radarPaint.color = circleColor
        radarPaint.style = Paint.Style.STROKE
        radarPaint.strokeWidth = 4f

        scanPaint = Paint()
        scanPaint.color = sweepColor
        scanPaint.style = Paint.Style.STROKE
        scanPaint.strokeWidth = 4f

        matrix = Matrix()
        startAnimations()
        handler = Handler()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (i in 0 until circleCount) {
            canvas.drawCircle(
                centerX.toFloat(),
                centerY.toFloat(),
                circleRadii!![i].toFloat(),
                radarPaint
            )
        }

        val sweepX =
            (centerX + maxRadius * Math.cos(Math.toRadians(sweepAngle.toDouble()))).toFloat()
        val sweepY =
            (centerY + maxRadius * Math.sin(Math.toRadians(sweepAngle.toDouble()))).toFloat()
        canvas.drawLine(centerX.toFloat(), centerY.toFloat(), sweepX, sweepY, scanPaint)

        try {
            var count = 0
            for (e in radarIcons!!) {
                matrix.reset()
                matrix.postTranslate(
                    iconPositions!![count].component1(),
                    iconPositions!![count].component2()
                )
                val paint = Paint()
                paint.alpha = (alpha * 255).toInt()
                canvas.drawBitmap(e, matrix, paint)
                count++
            }
        }catch (e:Exception){
            Log.e(TAG, e.message.toString() )
        }
    }

    fun startAnimation() {
        if (!isAnimating) {
            isAnimating = true
            handler!!.post(scanRunnable)
        }
    }

    fun stopAnimation() {
        isAnimating = false
        handler!!.removeCallbacks(scanRunnable)
    }

    fun addImagesAndPositions(radarIcons: List<Bitmap>, positions: List<Pair<Float, Float>>) {
        this.radarIcons = radarIcons
        iconPositions = positions
        invalidate()
    }

    private fun startAnimations() {
        val handler = Handler()
        val runnable: Runnable = object : Runnable {
            override fun run() {
                if (isDisappearing) {
                    alpha -= 0.05f
                    if (alpha <= 0) {
                        alpha = 0f
                        isDisappearing = false
                    }
                } else {
                    alpha += 0.05f
                    if (alpha >= 1) {
                        alpha = 1f
                        isDisappearing = true
                    }
                }
                invalidate()
                handler.postDelayed(this, 100)
            }
        }
        handler.post(runnable)
    }

    private val scanRunnable: Runnable = object : Runnable {
        override fun run() {
            sweepAngle += sweepSpeed
            if (sweepAngle >= 360) {
                sweepAngle = 0
            }
            invalidate()
            if (isAnimating) {
                handler!!.postDelayed(this, 50) // Animasyonu tekrar başlatın (50 milisaniye)
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2
        centerY = h / 2
        maxRadius = Math.min(centerX, centerY)

        circleRadii = IntArray(circleCount)
        for (i in 0 until circleCount) {
            circleRadii!![i] = maxRadius / (circleCount - 1) * i
        }
    }

    private fun setAttr(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RadarView)
        sweepSpeed = typedArray.getInteger(R.styleable.RadarView_sweepSpeed, sweepSpeed)
        circleColor = typedArray.getColor(
            R.styleable.RadarView_circleColor,
            context.resources.getColor(R.color.green)
        )
        sweepColor = typedArray.getColor(
            R.styleable.RadarView_sweepColor,
            context.resources.getColor(R.color.red)
        )
        circleCount = typedArray.getInteger(R.styleable.RadarView_circleCount, circleCount) + 1
        typedArray.recycle()
    }

    fun setIconClickListener(listener: OnRadarIconClickListener?) {
        iconClickListener = listener
        setOnTouchListener(IconClickHandler(iconClickListener!!, iconPositions!!))
    }

}