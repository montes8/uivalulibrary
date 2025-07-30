package com.gb.vale.uivalulibrary.extra

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.gb.vale.uivalulibrary.R
import com.gb.vale.uivalulibrary.utils.UI_TAY_EMPTY
import com.gb.vale.uivalulibrary.utils.uiTayBgBorderStroke
import com.gb.vale.uivalulibrary.utils.uiTayDrawableStroke

class UiTayInfo @JvmOverloads constructor(
    context: Context, private val attrs: AttributeSet?, defaultStyle: Int = 0
) : ConstraintLayout(
    context, attrs, defaultStyle
) {

    private var iconInfo = ImageView(this.context)
    private var textInfo = TextView(this.context)
    private var constraintSet = ConstraintSet()
    private var uiTayInfoStyleCurrent = UITayStyleInfo.UI_TAY_INFO
    private var uiTayStyleEnable = true

    var uiTayTextInformation: String = UI_TAY_EMPTY
        set(value) {
            field = value
            if (value.isNotEmpty()) textInfo.text = value
        }

    var uiTayTextInfoSpp: SpannableString = SpannableString(UI_TAY_EMPTY)
        set(value) {
            field = value
            if (value.isNotEmpty()) textInfo.text = value
        }


    var uiTayIconInformation: Drawable? =
        ContextCompat.getDrawable(this.context, R.drawable.ui_tay_ic_info)
        set(value) {
            field = value
            value?.let {
                iconInfo.setImageDrawable(it)
            }
        }

    var uiTayStyleInfo: UITayStyleInfo = UITayStyleInfo.UI_TAY_INFO
        set(value) {
            field = value
            if (uiTayStyleEnable){
                uiTayInfoStyleCurrent = value
                configStyle()
            }
        }

    var uiTayStyleInfoEnable: Boolean = true
        set(value) {
            field = value
            uiTayStyleEnable = value
        }

    var uiTayBackgroundInfo: Drawable = this.context.uiTayDrawableStroke(
        R.color.tay_info_bg_stroke,R.color.tay_info_bar_bg_solid,R.dimen.dim_tay_info_radius)
        set(value) {
            field = value
            if (!uiTayStyleEnable) this.background = value
        }

    var uiTayTextColorInfo: Int = this.context.resources.getColor(R.color.tay_info_text, null)
        set(value) {
            field = value
            if (!uiTayStyleEnable) textInfo.setTextColor(value)
        }

    private fun configStyle() {
        this.uiTayBgBorderStroke(if (validDefault())R.color.tay_info_bg_stroke else R.color.tay_info_bg_stroke_error,
            if (validDefault())R.color.tay_info_bar_bg_solid else R.color.tay_info_bar_bg_solid_error,
            R.dimen.dim_tay_info_radius)
        iconInfo.setColorFilter(
            ContextCompat.getColor(context,if (validDefault())
                R.color.tay_info_bar_icon else R.color.tay_info_bar_icon_error), android.graphics.PorterDuff.Mode.SRC_IN
        )
        textInfo.setTextColor(this.context.resources.getColor(
            if (validDefault())R.color.tay_info_text else R.color.tay_info_text_error, null))
    }

    private fun validDefault() = uiTayInfoStyleCurrent == UITayStyleInfo.UI_TAY_INFO


    init {
        loadAttributes()
        configView()
        constraintSet.clone(this)
        positionView()
    }

    private fun positionView() {
        constraintSet.connect(
            iconInfo.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP
        )
        constraintSet.connect(
            iconInfo.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START
        )

        constraintSet.connect(
            textInfo.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP
        )
        constraintSet.connect(
            textInfo.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM
        )
        constraintSet.connect(
            textInfo.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END
        )
        constraintSet.connect(
            textInfo.id, ConstraintSet.START, iconInfo.id, ConstraintSet.END
        )

        constraintSet.applyTo(this)
    }

    private fun loadAttributes() {
        val attributeSet = context.obtainStyledAttributes(attrs, R.styleable.UiTayInfo)
        attributeSet.let {
            uiTayTextInformation =
                it.getString(R.styleable.UiTayInfo_uiTayTextInformation)
                    ?: context.getString(R.string.tay_ui_script)
            uiTayIconInformation =
                it.getDrawable(R.styleable.UiTayInfo_uiTayIconInformation)
                    ?: ContextCompat.getDrawable(this.context, R.drawable.ui_tay_ic_info)
            uiTayStyleInfoEnable =
                it.getBoolean(R.styleable.UiTayInfo_uiTayStyleInfoEnable, true)

            uiTayBackgroundInfo = it.getDrawable(R.styleable.UiTayInfo_android_background)?:
                    this.context.uiTayDrawableStroke(
                        R.color.tay_info_bg_stroke,R.color.tay_info_bar_bg_solid,R.dimen.dim_tay_info_radius)
            uiTayTextColorInfo = it.getColor(R.styleable.UiTayInfo_android_textColor,
                this.context.resources.getColor(R.color.tay_info_text, null))

            uiTayStyleInfo =
                UITayStyleInfo.values()[it.getInt(R.styleable.UiTayInfo_uiTayStyleInfo, 0)]
        }
        attributeSet.recycle()
    }

    private fun configView() {
        iconInfo.id = View.generateViewId()
        textInfo.id = View.generateViewId()
        val layout = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        val layoutIcon = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        textInfo.setPadding(
            this.context.resources.getDimensionPixelOffset(
                R.dimen.dim_tay_8
            ), 0, 0, 0
        )
        val layoutText = LayoutParams(
            this.context.resources.getDimensionPixelOffset(R.dimen.dim_tay_0),
            LayoutParams.WRAP_CONTENT
        )
        this.setPadding(
            this.context.resources.getDimensionPixelOffset(
                R.dimen.dim_tay_16
            ), this.context.resources.getDimensionPixelOffset(
                R.dimen.dim_tay_16
            ), this.context.resources.getDimensionPixelOffset(
                R.dimen.dim_tay_16
            ), this.context.resources.getDimensionPixelOffset(
                R.dimen.dim_tay_16
            )
        )

        val typeface = ResourcesCompat.getFont(context, R.font.ui_tay_montserrat_medium)
        textInfo.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            this.context.resources.getDimensionPixelSize(R.dimen.dim_tay_info_sp_text).toFloat()
        )
        textInfo.typeface = typeface
        this.addView(iconInfo)
        this.addView(textInfo)
        textInfo.layoutParams = layoutText
        iconInfo.layoutParams = layoutIcon
        this.layoutParams = layout
    }


    fun updateStyleInfo(primary:Int,secondary:Int){
        textInfo.setTextColor(this.resources.getColor(primary,null))
        this.context.uiTayDrawableStroke(
            secondary,secondary,R.dimen.dim_tay_info_radius)
        iconInfo.setColorFilter(
            ContextCompat.getColor(context, primary),
            PorterDuff.Mode.SRC_IN
        )
    }
}

enum class UITayStyleInfo(var code: Int) {
    UI_TAY_INFO(0), UI_TAY_ERROR(1)
}