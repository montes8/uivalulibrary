package com.gb.vale.uivalulibrary.label.imageurl

import android.text.Selection
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.view.MotionEvent
import android.widget.TextView
import com.gb.vale.uivalulibrary.utils.uiTayTryCatch

typealias UIClickLinkUrl = (String) -> Unit
class UiTayUrlLinkClick : LinkMovementMethod() {

    private fun getX(widget: TextView,event: MotionEvent):Int{
        var x = event.x.toInt()
        x -= widget.totalPaddingLeft
        x += widget.scrollX
        return  x
    }

    private fun getY(widget: TextView,event: MotionEvent):Int{
        var y = event.y.toInt()
        y -= widget.totalPaddingTop
        y += widget.scrollY
        return  y
    }

     var uiClickLinkUrl: UIClickLinkUrl = {}
    override fun onTouchEvent(
        widget: TextView,
        buffer: Spannable, event: MotionEvent
    ): Boolean {
        if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_DOWN) {
            val off: Int = widget.layout.getOffsetForHorizontal(widget.layout.getLineForVertical(getY(widget,event)), getX(widget,event).toFloat())
            val link  = buffer.getSpans(off, off, URLSpan::class.java)
            if (link.isNotEmpty()) {
                if (event.action == MotionEvent.ACTION_UP) { configActionUrl(link) } else {
                    Selection.setSelection(buffer, buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0])
                    )
                }
                return true
            }
        }
        return super.onTouchEvent(widget, buffer, event)
    }

    private fun configActionUrl(link:Array<URLSpan>){
        var url = link[0].url.trim { it <= ' ' }
        if (url.startsWith("www")) { url = "http://$url" }
        if (url.startsWith("https://") || url.startsWith("http://") || url.startsWith("tel:") || url.startsWith(
                "mailto:")) {
            uiTayTryCatch { uiClickLinkUrl.invoke(url) }
        }
    }

    companion object {
        private var sInstance: UiTayUrlLinkClick? = null
        val instance: UiTayUrlLinkClick?
            get() {
                if (sInstance == null) sInstance = UiTayUrlLinkClick()
                return sInstance
            }
    }
}