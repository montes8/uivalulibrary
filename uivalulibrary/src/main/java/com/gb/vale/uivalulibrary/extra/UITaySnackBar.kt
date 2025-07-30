package com.gb.vale.uivalulibrary.extra

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.res.ResourcesCompat
import com.gb.vale.uivalulibrary.R
import com.gb.vale.uivalulibrary.utils.UI_TAY_EMPTY
import com.gb.vale.uivalulibrary.utils.uiTayDrawableStroke
import com.gb.vale.uivalulibrary.utils.uiTayGone
import com.gb.vale.uivalulibrary.utils.uiTayHandler
import com.gb.vale.uivalulibrary.utils.uiTayVisible

class UITaySnackBar @JvmOverloads constructor(
    context: Context, private val attrs: AttributeSet?, defaultStyle: Int = 0
) : ConstraintLayout(
    context, attrs, defaultStyle
) {

    private var textSBar = TextView(this.context)
    private var constraintSet = ConstraintSet()
    private var uiTayDuration = 2000L
    private var uiTayText = UI_TAY_EMPTY

    var uiTaySBText: String = UI_TAY_EMPTY
        set(value) {
            field = value
            uiTayText = value
            textSBar.text = value
        }

    var uiTaySBDuration: Int = 2000
        set(value) {
            field = value
            uiTayDuration = (value * 1000).toLong()
        }

    var uiTaySbBackground: Drawable =  this.context.uiTayDrawableStroke(R.color.tay_snack_bar_bg_stroke,
        R.color.tay_snack_bar_bg_stroke,R.dimen.dim_tay_snack_bar_radius)
        set(value) {
            field = value
            this.background = value
        }

    var uiTaySbTextColor: Int = Color.WHITE
        set(value) {
            field = value
            textSBar.setTextColor(value)
        }

    var uiTayIconSB: Drawable? = null
        set(value) {
            field = value
            value?.let {
                uiTayDrawableIcon(it)
            }
        }

    init {
        loadAttributes()
        configView()
        constraintSet.clone(this)
        positionView()
        this.uiTayGone()
    }

    private fun configView() {
        textSBar.id = View.generateViewId()
        val typeface = ResourcesCompat.getFont(context, R.font.ui_tay_montserrat_medium)
        val layoutText = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
        textSBar.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            this.context.resources.getDimensionPixelSize(R.dimen.dim_tay_snack_sp_text).toFloat()
        )
        textSBar.typeface = typeface
        this.setPadding(getUiTayPadding(),getUiTayPadding(),getUiTayPadding(),getUiTayPadding())
        textSBar.layoutParams = layoutText
        this.addView(textSBar)
    }

    fun uiTayTypeFace(typeface : Typeface? = ResourcesCompat.getFont(context, R.font.ui_tay_montserrat_medium)){
        textSBar.typeface = typeface
    }

    private fun getUiTayPadding() =  this.context.resources.getDimensionPixelOffset(R.dimen.dim_tay_16)

    private fun loadAttributes() {
        val attributeSet = context.obtainStyledAttributes(attrs, R.styleable.UITaySnackBar)
        attributeSet.let {
            uiTayIconSB =
                it.getDrawable(R.styleable.UITaySnackBar_uiTayIconSB)
            uiTaySBText =
                it.getString(R.styleable.UITaySnackBar_uiTaySBText)
                    ?: context.getString(R.string.tay_ui_script)

            uiTaySBDuration = it.getInt(R.styleable.UITaySnackBar_uiTaySBDuration, 2)

            uiTaySbBackground = it.getDrawable(R.styleable.UITaySnackBar_android_background)?:
                    this.context.uiTayDrawableStroke(R.color.tay_snack_bar_bg_stroke,
                        R.color.tay_snack_bar_bg_stroke,R.dimen.dim_tay_snack_bar_radius)
            uiTaySbTextColor = it.getColor(R.styleable.UITaySnackBar_android_textColor,Color.WHITE)

        }
        attributeSet.recycle()
    }

    private fun uiTayDrawableIcon(tayIconSB: Drawable){
        textSBar.setCompoundDrawablesWithIntrinsicBounds(
            tayIconSB,
            null,
            null,
            null
        )
        textSBar.compoundDrawablePadding = context.resources.getDimensionPixelOffset(R.dimen.dim_tay_8)
    }

    private fun positionView() {
        constraintSet.connect(
            textSBar.id,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP
        )
        constraintSet.connect(
            textSBar.id,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START
        )
        constraintSet.connect(
            textSBar.id,
            ConstraintSet.END,
            ConstraintSet.PARENT_ID,
            ConstraintSet.END
        )
        constraintSet.connect(
            textSBar.id,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM
        )
        constraintSet.applyTo(this)
    }

    fun showUiTaySB(text : String = uiTayText) {
        this.uiTayVisible()
        textSBar.text = text
        uiTayHandler(uiTayDuration) { this.uiTayGone() }
    }

}