package com.example.edvora.classes_views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private var mDrawPath: CustomPath? = null
    private var mCanvasBitmap: Bitmap? = null
    private var mDrawPaint: Paint? = null
    private var mCanvasPaint: Paint? = null
    private var mBrashSize: Float = 0.toFloat()
    private var color = Color.BLACK
    private var canvas: Canvas? = null
    private val mPaths = ArrayList<CustomPath>()
    private var num: Int = 0
    var mStartX: Float = 0.0f
    var mStartY: Float = 0.0f
    var touchX: Float = 0.0f
    var touchY: Float = 0.0f

    init {
        setUpDrawing()
    }

    /**
     * This method initializes the attributes of the
     * ViewForDrawing class.
     */
    private fun setUpDrawing() {
        mDrawPaint = Paint()
        mDrawPath = CustomPath(color, mBrashSize)
        mDrawPaint!!.color = color
        mDrawPaint!!.style = Paint.Style.STROKE
        mDrawPaint!!.strokeJoin = Paint.Join.ROUND
        mDrawPaint!!.strokeCap = Paint.Cap.ROUND
        mCanvasPaint = Paint(Paint.DITHER_FLAG)
        mBrashSize = 20.toFloat()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(mCanvasBitmap!!)
    }

    //change color
    fun setColor(newColor: Int) {
        color = newColor
        mDrawPaint!!.color = color
    }


    /**
     * This method is called when a stroke is drawn on the canvas
     * as a part of the painting.
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(mCanvasBitmap!!, 0f, 0f, mCanvasPaint)
        //the draw stays on screen(draw the path that saved in ArrayList)
        for (path in mPaths) {
            mDrawPaint!!.strokeWidth = path.brushThickness
            mDrawPaint!!.color = path.color
            canvas.drawPath(path, mDrawPaint!!)
        }
        if (!mDrawPath!!.isEmpty) {
            mDrawPaint!!.strokeWidth = mDrawPath!!.brushThickness
            mDrawPaint!!.color = mDrawPath!!.color
            canvas.drawPath(mDrawPath!!, mDrawPaint!!)
        }
    }


    /**
     * This method acts as an event listener when a touch
     * event is detected on the device.
     */
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        touchX = event!!.x
        touchY = event?.y

        if (getnum() == 1) {
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    invalidate()
                    mDrawPath!!.color = color
                    mDrawPath!!.brushThickness = mBrashSize
                    mDrawPath!!.reset()

                    if (touchX != null && touchY != null)
                        mDrawPath!!.moveTo(touchX, touchY)

                }
                MotionEvent.ACTION_MOVE -> {
                    invalidate()
                    if (touchX != null && touchY != null) {
                        mDrawPath!!.lineTo(touchX, touchY)

                    }
                }
                MotionEvent.ACTION_UP -> {
                    invalidate()
                    //save current path
                    mPaths.add(mDrawPath!!)
                    mDrawPath = CustomPath(color, mBrashSize)

                }
                else -> {
                    return false
                }
            }
        } else if (getnum() == 2) {
            onTouchEventArrow(event, touchX, touchY)
        } else if (getnum() == 3) {
            onTouchEventRectangle(event, touchX, touchY)
        } else if (getnum() == 4) {
            onTouchEventCircle(event, touchX, touchY)
        }
        invalidate()
        return true
    }


    fun num(x: Int) {
        num = x
    }

    fun getnum(): Int {
        return num
    }

    private fun onTouchEventRectangle(event: MotionEvent, touchX: Float, touchY: Float) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mDrawPath!!.color = color
                mDrawPath!!.brushThickness = mBrashSize
                mDrawPath!!.reset()
                //isDrawing = true
                mStartX = touchX
                mStartY = touchX
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> invalidate()
            MotionEvent.ACTION_UP -> {
                // isDrawing = false
                mPaths.add(mDrawPath!!)
                mDrawPath = CustomPath(color, mBrashSize)
                drawRectangle(canvas!!, mDrawPaint!!, touchX, touchY)
                invalidate()
            }
        }
    }


    private fun drawRectangle(canvas: Canvas, paint: Paint, touchX: Float, touchY: Float) {
        val right: Float = if (mStartX > touchX) mStartX else touchX
        val left: Float = if (mStartX > touchX) touchX else mStartX
        val bottom: Float = if (mStartY > touchY) mStartY else touchY
        val top: Float = if (mStartY > touchY) touchY else mStartY
        for (path in mPaths) {
            mDrawPaint!!.strokeWidth = path.brushThickness
            mDrawPaint!!.color = path.color
            canvas.drawPath(path, mDrawPaint!!)
            canvas.drawRect(left, top, right, bottom, paint)

        }
        if (!mDrawPath!!.isEmpty) {
            mDrawPaint!!.strokeWidth = mDrawPath!!.brushThickness
            mDrawPaint!!.color = mDrawPath!!.color
            canvas.drawPath(mDrawPath!!, mDrawPaint!!)
            canvas.drawRect(left, top, right, bottom, paint)
        }
    }
//rectangle

    //circle


    private fun onTouchEventCircle(event: MotionEvent, touchX: Float, touchY: Float) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mDrawPath!!.color = color
                mDrawPath!!.brushThickness = mBrashSize
                mDrawPath!!.reset()
                mStartX = this.touchX
                mStartY = this.touchY
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> invalidate()
            MotionEvent.ACTION_UP -> {
                mPaths.add(mDrawPath!!)
                mDrawPath = CustomPath(color, mBrashSize)
                for (path in mPaths) {
                    mDrawPaint!!.strokeWidth = path.brushThickness
                    mDrawPaint!!.color = path.color
                    canvas!!.drawPath(path, mDrawPaint!!)
                    canvas!!.drawCircle(
                        mStartX, mStartY,
                        calculateRadius(mStartX, mStartY, this.touchX, this.touchY), mDrawPaint!!
                    )
                }
                if (!mDrawPath!!.isEmpty) {
                    mDrawPaint!!.strokeWidth = mDrawPath!!.brushThickness
                    mDrawPaint!!.color = mDrawPath!!.color
                    canvas!!.drawPath(mDrawPath!!, mDrawPaint!!)
                    canvas!!.drawCircle(
                        mStartX, mStartY,
                        calculateRadius(mStartX, mStartY, this.touchX, this.touchY), mDrawPaint!!
                    )
                }

                invalidate()
            }
        }
    }

    protected fun calculateRadius(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return Math.sqrt(
            Math.pow((x1 - x2).toDouble(), 2.0) +
                    Math.pow((y1 - y2).toDouble(), 2.0)
        ).toFloat()
    }

    //line


    private fun onTouchEventArrow(event: MotionEvent, touchX: Float, touchY: Float) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mDrawPath!!.color = color
                mDrawPath!!.brushThickness = mBrashSize
                mDrawPath!!.reset()
                mStartX = this.touchX
                mStartY = this.touchY
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> invalidate()
            MotionEvent.ACTION_UP -> {
                mPaths.add(mDrawPath!!)
                mDrawPath = CustomPath(color, mBrashSize)
                for (path in mPaths) {
                    mDrawPaint!!.strokeWidth = path.brushThickness
                    mDrawPaint!!.color = path.color
                    canvas!!.drawPath(path, mDrawPaint!!)
                    canvas!!.drawLine(mStartX, mStartY, this.touchX, this.touchY, mDrawPaint!!)

                }
                if (!mDrawPath!!.isEmpty) {
                    mDrawPaint!!.strokeWidth = mDrawPath!!.brushThickness
                    mDrawPaint!!.color = mDrawPath!!.color
                    canvas!!.drawPath(mDrawPath!!, mDrawPaint!!)
                    canvas!!.drawLine(mStartX, mStartY, this.touchX, this.touchY, mDrawPaint!!)

                }
                invalidate()
            }
        }
    }

    // An inner class for custom path with two params as color and stroke size.
    internal inner class CustomPath(var color: Int, var brushThickness: Float) : Path() {

    }


}