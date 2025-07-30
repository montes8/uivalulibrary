package com.gb.vale.uivalulibrary.extra

import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import com.gb.vale.uivalulibrary.R

data class LineSelect(var position: Int, var startX: Float, var endX: Float)

class UiTayLineProgress(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    private val colorList: ArrayList<LineSelect> = ArrayList()
    private val height: Float =  4f.toDp(context.resources.displayMetrics)
    private val linePaint = Paint()
    private val linePaintSelect = Paint()
    private val pointsPaint = Paint()
    private var selectPosition = 0
    private var positionRange = 0
    private val colorEnd: Int = context.resources.getColor(R.color.tay_indication_line_end,null)
    private val colorStart: Int = context.resources.getColor(R.color.tay_indication_line_start,null)
    private val colorUnselected: Int = context.resources.getColor(R.color.tay_indication_line_unselected,null)
    private val colorPositionDots: Int = context.resources.getColor(R.color.tay_indication_line_start,null)

    init {
        linePaint.color = colorUnselected
        linePaint.isAntiAlias = true
        linePaintSelect.isAntiAlias = true
        pointsPaint.color = colorPositionDots
        pointsPaint.isAntiAlias = true
    }

    fun initLine( positionRange: Int,positionSelected : Int){
        this.positionRange = positionRange
        this.selectPosition = positionSelected
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        this.colorList.clear()
        this.drawLines(canvas)
    }

    private fun drawLines(canvas: Canvas?) {
        val sizeLines = (width).div(positionRange)
        val shader = LinearGradient(width/2.toFloat(), 0f, 0f, 0f, colorEnd, colorStart,
            Shader.TileMode.CLAMP)
        linePaintSelect.shader = shader
        linePaintSelect.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        var startX = if (positionRange == 6)7f else 8f
        canvas?.drawRoundRect(
            RectF(startX, 0f, width.plus(if (positionRange == 6)7f else 8f), 0f),
            0f,
            0f,
            linePaint
        )
        for (position in 0..positionRange) {
            val range = calculateRange(startX)
            colorList.add(LineSelect(position, range.first, range.second))
            if (position < selectPosition) {
                canvas?.drawRoundRect(
                    RectF(startX.minus(20f), height, startX.plus(sizeLines), 0f),
                    0f,
                    0f,
                    linePaintSelect
                )
            }
            startX += sizeLines.plus(if (positionRange == 6)7f else 8f)
        }
    }

    private fun calculateRange(startX: Float): Pair<Float, Float> {
        return Pair(startX.minus(50f), startX.plus(50f))
    }

    private fun Float.toDp(metrics: DisplayMetrics): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, metrics)
    }
}