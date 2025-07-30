package com.gb.vale.uivalulibrary.swipe

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.gb.vale.uivalulibrary.R

class UiTayCardSwipeButton(
    private val context: Context,
    private val color: Int = context.getColor(R.color.ui_tay_color_swipe),
    private val imageResId: Int = 0,
    private val listener: CardSwipeButtonListener
) {

    private var pos: Int = 0
    private var clickRegion: RectF? = null

    fun onClick(x: Float, y: Float): Boolean {
        if (clickRegion != null && clickRegion?.contains(x, y) == true) {
            listener.onClick(pos)
            return true
        }
        return false
    }

    fun onDraw(c: Canvas, rectF: RectF, pos: Int) {
        val p = Paint()
        p.color = color
        p.textAlign= Paint.Align.CENTER
        c.drawRoundRect(rectF,16f,16f, p)
        if (imageResId != 0) {
            drawIcon(c, rectF, p)
        }
        clickRegion = rectF
        this.pos = pos
    }

    private fun drawIcon(c: Canvas, rectF: RectF, p: Paint) {
        val deleteIcon = ContextCompat.getDrawable(context, imageResId)
        val bitmap = drawableToBitmap(deleteIcon)
        c.drawBitmap(bitmap, (rectF.left + rectF.right-bitmap.width) / 2, (rectF.top + rectF.bottom -bitmap.height) / 2, p)
    }

    private fun drawableToBitmap(d: Drawable?): Bitmap {
        if (d is BitmapDrawable) return d.bitmap
        val bitmap =
            Bitmap.createBitmap(
                d?.intrinsicWidth ?: 0,
                d?.intrinsicHeight ?: 0,
                Bitmap.Config.ARGB_8888
            )
        val canvas = Canvas(bitmap)
        d?.setBounds(0, 0, canvas.width, canvas.height)
        d?.draw(canvas)
        return bitmap
    }

}

fun interface CardSwipeButtonListener {
    fun onClick(pos: Int)
}