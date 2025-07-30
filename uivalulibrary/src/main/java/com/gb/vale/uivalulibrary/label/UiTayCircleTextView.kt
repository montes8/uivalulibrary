package com.gb.vale.uivalulibrary.label

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class UiTayCircleTextView(context: Context, attrs: AttributeSet?) : AppCompatTextView(context, attrs) {
    private var strokeWidth = 0f
    private var strokeColor = 0
    private var solidColo = 0

    override fun draw(canvas: Canvas) {
        val circlePaint = Paint()
        circlePaint.color = solidColo
        circlePaint.flags = Paint.ANTI_ALIAS_FLAG

        val strokePaint = Paint()
        strokePaint.color = strokeColor
        strokePaint.flags = Paint.ANTI_ALIAS_FLAG

        val h = this.height
        val w = this.width

        val diameter = if (h > w) h else w
        val radius = diameter / 2

        this.height = diameter
        this.width = diameter

        canvas.drawCircle(diameter / 2f, diameter / 2f, radius.toFloat(), strokePaint)

        canvas.drawCircle(diameter / 2f, diameter / 2f, radius - strokeWidth, circlePaint)

        super.draw(canvas)
    }
}