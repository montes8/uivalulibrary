package com.gb.vale.uitaylibrary.utils

import android.annotation.SuppressLint
import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.gb.vale.uitaylibrary.R
import com.gb.vale.uitaylibrary.manager.style.CustomTypefaceSpan

fun TextView.uiTaySetColouredSpanClick(
    word: String,
    color: Int = ContextCompat.getColor(this.context,R.color.tay_color_general),
    isUnderLine: Boolean = true,
    isBold: Boolean = false,
    block: () -> Unit
) {
    movementMethod = LinkMovementMethod.getInstance()
    val fontBold = ResourcesCompat.getFont(this.context, R.font.ui_tay_montserrat_bold)
    val spannableString = SpannableString(text)
    val start = text.indexOf(word)
    val end = text.indexOf(word) + word.length
    try {
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                block()
            }

            @SuppressLint("ResourceAsColor")
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = color
                ds.isUnderlineText = isUnderLine
            }
        }
        spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        if (isBold)spannableString.setSpan(fontBold?.let { CustomTypefaceSpan(it) }, start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)

        text = spannableString
    } catch (e: IndexOutOfBoundsException) {
        println("'$word' was not not found in TextView text")
    }

}

fun TextView.uiTaySetColouredSpan(
    word: String,
    color: Int = R.color.tay_color_general
) {
    val spannableString = SpannableString(text)
    val start = text.indexOf(word)
    val end = text.indexOf(word) + word.length
    try {
        spannableString.setSpan(ForegroundColorSpan(ContextCompat.getColor(this.context,color)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        text = spannableString
    } catch (e: IndexOutOfBoundsException) {
        println("'$word' was not not found in TextView text")
    }

}

fun TextView.uiTaySetSpanCustom(
    text: String,
    word: String,typeFont : Int = R.font.ui_tay_montserrat_bold
) {
    this.text = this.context.uiTaySetSpanCustom(text,word,typeFont)
}

fun Context.uiTaySetSpanCustom(
    text: String,
    word: String, typeFont : Int = R.font.ui_tay_montserrat_bold
):SpannableString {
    val fontBold = ResourcesCompat.getFont(this, typeFont)
    val spannableString = SpannableString(text)
    val start = text.indexOf(word)
    val end = text.indexOf(word) + word.length
    return try {
        spannableString.setSpan(fontBold?.let { CustomTypefaceSpan(it) }, start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
        spannableString
    } catch (e: IndexOutOfBoundsException) {
        SpannableString(UI_TAY_EMPTY)
    }
}



fun TextView.uiTayLine(value :String){
    val myText = SpannableString(value)
    myText.setSpan(UnderlineSpan(), 0, myText.length, 0)
    this.text = myText
}

@SuppressLint("UseCompatLoadingForDrawables")
fun TextView.addIconText(text:String,icon : Int = R.drawable.ui_tay_ic_info,word: String, typeFont : Int = R.font.ui_tay_montserrat_bold) {
    val modifiedText = "$text %icon%"
    val span = SpannableString(modifiedText)
    val fontBold = ResourcesCompat.getFont(this.context, typeFont)
    val drawable = ResourcesCompat.getDrawable(context.resources,icon,null)
    drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    val image = drawable?.let { ImageSpan(it, ImageSpan.ALIGN_BOTTOM) }
    val startIndex = modifiedText.indexOf("%icon%")
    if(word.isNotEmpty()){
        val start = text.indexOf(word)
        val end = text.indexOf(word) + word.length
        span.setSpan(fontBold?.let { CustomTypefaceSpan(it) }, start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
    }
    span.setSpan(image, startIndex, startIndex + 6, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
    this.text = span
}



