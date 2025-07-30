package com.gb.vale.uivalulibrary.label

import android.content.Context
import android.text.Html
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.res.ResourcesCompat
import com.gb.vale.uivalulibrary.R
import com.gb.vale.uivalulibrary.label.imageurl.UiTayURLImage
import com.gb.vale.uivalulibrary.label.imageurl.UiTayUrlLinkClick
import com.gb.vale.uivalulibrary.utils.UI_TAY_EMPTY

@Suppress("DEPRECATION")
class UiTayTextHtml@JvmOverloads constructor(
    context: Context, private val attrs: AttributeSet?, defaultStyle: Int = 0
) : ConstraintLayout(
    context, attrs, defaultStyle
) {

    private var textHtml = TextView(this.context)
    private var constraintSet = ConstraintSet()
    private var sizeImageWidth = 0
    private var sizeImageHeight = 0

    var uiTayTextNormal: String = UI_TAY_EMPTY
        set(value) {
            field = value
            if (value.isNotEmpty())textHtml.text = value
        }
        get() = textHtml.text?.toString() ?: UI_TAY_EMPTY

    var uiTayTextHtml: String = UI_TAY_EMPTY
        set(value) {
            field = value
            if (value.isNotEmpty()) configTextHtml(value)
        }
        get() = textHtml.text?.toString() ?: UI_TAY_EMPTY

     var uiTayTextImageSizeWidth: Int = 0
        set(value) {
            field = value
            sizeImageWidth = value
        }

     var uiTayTextImageSizeHeight : Int = 0
        set(value) {
            field = value
            sizeImageHeight = value
        }

     var uiTayTextHtmlSize : Int = this.context.resources.getDimensionPixelSize(R.dimen.dim_tay_text_html_sp_text)
        set(value) {
            field = value
            textHtml.setTextSize(
                TypedValue.COMPLEX_UNIT_PX, value
                    .toFloat()
            )
        }

     var uiTayTextHtmlColor : Int = this.context.resources.getDimensionPixelSize(R.dimen.dim_tay_edit_title_basic_sp_text)
        set(value) {
            field = value
            textHtml.setTextColor(value)
        }

    private fun configTextHtml(text : String){
        val converterTextImg = UiTayURLImage(textHtml, this.context,sizeImageWidth,sizeImageHeight)
        val htmlSpan = Html.fromHtml(text, converterTextImg, null)
        textHtml.text = htmlSpan
    }


    init {
        loadAttributes()
        configView()
        constraintSet.clone(this)
        positionText()
    }

    private fun loadAttributes() {
        val attributeSet = context.obtainStyledAttributes(attrs, R.styleable.UiTayTextHtml)
        attributeSet.let {
            uiTayTextHtml = it.getString(R.styleable.UiTayTextHtml_uiTayTextHtml)
                ?: UI_TAY_EMPTY
            uiTayTextNormal = it.getString(R.styleable.UiTayTextHtml_uiTayTextNormal)
                ?: UI_TAY_EMPTY

            uiTayTextImageSizeWidth = it.getInt(R.styleable.UiTayTextHtml_uiTayTextImageSizeWidth,0)
            uiTayTextImageSizeHeight = it.getInt(R.styleable.UiTayTextHtml_uiTayTextImageSizeHeight,0)
            uiTayTextHtmlSize = it.getDimensionPixelSize(R.styleable.UiTayTextHtml_android_textSize,
                this.context.resources.getDimensionPixelSize(R.dimen.dim_tay_text_html_sp_text))
            uiTayTextHtmlColor = it.getColor(R.styleable.UiTayTextHtml_android_textColor,0)

        }
        attributeSet.recycle()
    }


    fun setOnTayTextHlmClickLink (listener: TayTextHlmClickLink) {
        val clickLink = UiTayUrlLinkClick.instance
        textHtml.movementMethod = clickLink
        clickLink?.uiClickLinkUrl = {
            if (it.isNotEmpty())listener.onClickLickUlr(it)
        }
    }

    fun setOnTayTextlickLink(listener: TayTextHlmClick) {
        textHtml.setOnClickListener {
            listener.onClick(it)
        }

    }

    private fun configView() {
        val typeface = ResourcesCompat.getFont(context, R.font.ui_tay_montserrat_medium)
        val layoutText = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        textHtml.id = View.generateViewId()
        textHtml.typeface = typeface
        textHtml.layoutParams = layoutText
        this.addView(textHtml)
    }

    private fun positionText() {
        constraintSet.connect(textHtml.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        constraintSet.connect(
            textHtml.id, ConstraintSet.START,ConstraintSet.PARENT_ID, ConstraintSet.START
        )
        constraintSet.connect(
            textHtml.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM
        )
        constraintSet.connect(
            textHtml.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END
        )
        constraintSet.applyTo(this)
    }

    fun setTypeFaceTay(font : Int = R.font.ui_tay_montserrat_medium) {
        val typeface = ResourcesCompat.getFont(context, font)
        textHtml.typeface = typeface
    }
}


fun interface TayTextHlmClickLink {
    fun onClickLickUlr(value: String)
}

fun interface TayTextHlmClick {
    fun onClick(view: View)
}
