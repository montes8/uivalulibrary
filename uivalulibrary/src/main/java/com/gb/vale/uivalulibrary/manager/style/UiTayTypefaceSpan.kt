package com.gb.vale.uivalulibrary.manager.style

import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.MetricAffectingSpan

class CustomTypefaceSpan(typeface: Typeface) : MetricAffectingSpan() {

    private var typeface: Typeface? = null

    init {
        this.typeface = typeface
    }

    override fun updateDrawState(ds: TextPaint) {
        typeface?.let { applyCustomTypeFace(ds, it) }
    }

    override fun updateMeasureState(paint: TextPaint) {
        typeface?.let { applyCustomTypeFace(paint, it) }
    }

    private fun applyCustomTypeFace(paint: Paint, tf: Typeface) {
        paint.typeface = tf
    }
}