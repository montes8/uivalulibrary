package com.gb.vale.uivalulibrary.button

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.gb.vale.uivalulibrary.R
import com.gb.vale.uivalulibrary.utils.UI_TAY_EMPTY
import com.gb.vale.uivalulibrary.utils.setOnClickUiTayDelay
import com.gb.vale.uivalulibrary.utils.uiTayBgBorder
import com.gb.vale.uivalulibrary.utils.uiTayBgBorderStroke
import com.gb.vale.uivalulibrary.utils.uiTayGone
import com.gb.vale.uivalulibrary.utils.uiTayHandler
import com.gb.vale.uivalulibrary.utils.uiTayInvisible
import com.gb.vale.uivalulibrary.utils.uiTayVisibility
import com.gb.vale.uivalulibrary.utils.uiTayVisible


class UITayButton @JvmOverloads constructor(
    context: Context, private val attrs: AttributeSet?, defaultStyle: Int = 0
) : ConstraintLayout(
    context, attrs, defaultStyle
) {

    private var button = TextView(this.context)
    private var iconStart = ImageView(this.context)
    private var iconEnd = ImageView(this.context)
    private var constraintSet = ConstraintSet()
    private var constraintSetH = ConstraintSet()
    private var tayBtnStyleCurrent = UiTayStyleButton.UI_TAY_PRIMARY
    private var horizontalContentLayout = ConstraintLayout(context)
    private var loading = ProgressBar(this.context)
    var colorDefaultIcon = true

    var colorBtnPrimaryEnable = R.color.tay_btn_bg_primary_enable
    var colorBtnPrimaryDisable = R.color.tay_btn_bg_primary_disable
    var colorBtnPrimaryActive = R.color.tay_btn_bg_primary_selected
    var colorTextPrimaryEnable = R.color.tay_btn_text_primary
    var colorTextPrimaryDisable = R.color.tay_btn_text_primary_disable
    var colorIconPrimaryEnable = R.color.tay_btn_icon_primary
    var colorIconPrimaryDisable = R.color.tay_btn_icon_primary_disable



    var colorBgSecondaryCornerEnable = R.color.tay_btn_bg_secondary_corner_enable
    var colorBgSecondaryEnableSolid = R.color.tay_btn_bg_secondary_solid_enable
    var colorBgSecondaryCornerDisable = R.color.tay_btn_bg_secondary_corner_disable
    var colorBgSecondaryCornerDisableSolid = R.color.tay_btn_bg_secondary_solid_disable
    var colorBgSecondaryCornerSelected = R.color.tay_btn_bg_secondary_corner_selected
    var colorBgSecondarySolidSelected = R.color.tay_btn_bg_secondary_solid_selected
    var colorTextSecondaryEnable = R.color.tay_btn_text_secondary
    var colorTextSecondaryDisable = R.color.tay_btn_text_secondary_disable
    var colorIconSecondaryEnable = R.color.tay_btn_icon_secondary
    var colorIconSecondaryDisable = R.color.tay_btn_icon_secondary_disable

    var tayBtnText: String = UI_TAY_EMPTY
        set(value) {
            field = value
            button.text = value

        }

    var tayBtnEnable: Boolean = true
        set(value) {
            field = value
            button.isEnabled = value
            this.isEnabled = value
            configStyleInit(value)
        }

    var tayBtnIconColorDefault: Boolean = true
        set(value) {
            field = value
            colorDefaultIcon = value
        }

    var tayBtnIconStart: Drawable? = null
        set(value) {
            field = value
            value?.let {
                iconStart.uiTayVisibility(true)
                iconStart.setImageDrawable(it)
            } ?: iconStart.uiTayVisibility(false)

        }

    var tayBtnIconEnd: Drawable? = null
        set(value) {
            field = value
            value?.let {
                iconEnd.uiTayVisibility(true)
                iconEnd.setImageDrawable(it)
            } ?: iconEnd.uiTayVisibility(false)
        }


    var tayBtnStyle: UiTayStyleButton = UiTayStyleButton.UI_TAY_PRIMARY
        set(value) {
            field = value
            tayBtnStyleCurrent = value
        }

    var tayBtnLoading: Boolean = false
        set(value) {
            field = value
            setLoading(value)
        }

    private fun setLoading(value: Boolean) {
        if (value) {
            loading.uiTayVisible()
            button.uiTayInvisible()
        } else {
            loading.uiTayInvisible()
            button.uiTayVisible()
        }
    }


    init {
        loadAttributes()
        configView()
        constraintSet.clone(horizontalContentLayout)
        constraintSetH.clone(this)
        positionBtn()
    }


    private fun loadAttributes() {
        val attributeSet = context.obtainStyledAttributes(attrs, R.styleable.UITayButton)
        attributeSet.let {
            tayBtnIconColorDefault = it.getBoolean(R.styleable.UITayButton_uiTayBtnIconColorDefault, true)
            tayBtnText = it.getString(R.styleable.UITayButton_uiTayBtnText)
                ?: this.context.resources.getString(R.string.tay_ui_btn_continue)
            tayBtnIconStart = it.getDrawable(R.styleable.UITayButton_uiTayBtnIconStart)
            tayBtnIconEnd = it.getDrawable(R.styleable.UITayButton_uiTayBtnIconEnd)
            tayBtnStyle =
                UiTayStyleButton.values()[it.getInt(R.styleable.UITayButton_uiTayBtnStyle, 0)]
            tayBtnEnable = it.getBoolean(R.styleable.UITayButton_uiTayBtnEnable, true)
        }
        attributeSet.recycle()
    }

    fun setOnClickTayBtnListener(time: Long = 300, listener: UiTayBtnClickListener) {
        this.setOnClickUiTayDelay(time) {
            configStyleSelected()
            uiTayHandler(time) {
                configStyleInit(true)
                listener.onClick(this)
            }
        }
        button.setOnClickUiTayDelay(time) {
            configStyleSelected()
            uiTayHandler {
                configStyleInit(true)
                listener.onClick(this)
            }
        }
    }

    private fun configView() {
        configParent()
        configButton()
        configIconStar()
        configIconEnd()
        configProgress()
    }

    private fun configParent() {
        horizontalContentLayout.id = View.generateViewId()
        val horizontalLayoutParams = LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT)
        val layout = LayoutParams(LayoutParams.WRAP_CONTENT, sizeHeight())
        horizontalContentLayout.setPadding(this.context.resources.getDimensionPixelOffset(R.dimen.dim_tay_16), 0,
            this.context.resources.getDimensionPixelOffset(R.dimen.dim_tay_16), 0)
        this.layoutParams = layout
        horizontalContentLayout.layoutParams = horizontalLayoutParams
        this.addView(horizontalContentLayout)

    }

    private fun configButton() {
        button.id = View.generateViewId()
        val typeface = ResourcesCompat.getFont(context,R.font.ui_tay_montserrat_bold)
        val layoutParamBtn = LayoutParams(LayoutParams.WRAP_CONTENT
            ,sizeHeight())
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizeText().toFloat())
        button.setBackgroundColor(ContextCompat.getColor(this.context, R.color.ui_tay_transparent))
        button.isAllCaps = false
        button.gravity = Gravity.CENTER
        button.typeface = typeface
        button.layoutParams = layoutParamBtn
        loading.uiTayGone()
        horizontalContentLayout.addView(button)
    }

    private fun configIconStar() {
        iconStart.id = View.generateViewId()
        val layoutParamStart = LayoutParams(sizeMax(), sizeMax())
        layoutParamStart.setMargins(0,0,this.context.resources.getDimensionPixelOffset(R.dimen.dim_tay_8),0)
        iconStart.layoutParams = layoutParamStart
        horizontalContentLayout.addView(iconStart)

    }

    private fun configIconEnd() {
        iconEnd.id = View.generateViewId()
        val layoutParamEnd = LayoutParams(sizeMax(), sizeMax())
        layoutParamEnd.setMargins(this.context.resources.getDimensionPixelOffset(R.dimen.dim_tay_8),0,0,0)
        iconEnd.layoutParams = layoutParamEnd
        horizontalContentLayout.addView(iconEnd)
    }

    private fun configProgress() {
        loading.id = View.generateViewId()
        val layoutLoading = LayoutParams(
            this.context.resources.getDimensionPixelOffset(R.dimen.dim_tay_btn_loading),
            this.context.resources.getDimensionPixelOffset(R.dimen.dim_tay_btn_loading)
        )
        loading.isIndeterminate = true
        loading.indeterminateTintList =
            ColorStateList.valueOf(this.context.resources.getColor(if (tayBtnStyleCurrent ==
                UiTayStyleButton.UI_TAY_PRIMARY)R.color.tay_btn_loading_primary
            else R.color.tay_btn_loading_secondary, null))
        loading.layoutParams = layoutLoading
        horizontalContentLayout.addView(loading)
    }


    fun configStyleInit(value: Boolean) {
        when (tayBtnStyleCurrent) {
            UiTayStyleButton.UI_TAY_PRIMARY -> {
                setEnablePrimary(value)
            }
            else -> {
                setEnableSecondary(value)
            }

        }
    }

    private fun configStyleSelected() {
        when (tayBtnStyleCurrent) {
            UiTayStyleButton.UI_TAY_PRIMARY -> {
                setSelectedPrimary()
            }
            else -> {
                setSelectedSecondary()
            }

        }
    }

    private fun setEnablePrimary(value: Boolean) {
        if (value) this.uiTayBgBorder(colorBtnPrimaryEnable,R.dimen.dim_tay_btn_bg_radius
        ) else this.uiTayBgBorder(colorBtnPrimaryDisable,R.dimen.dim_tay_btn_bg_radius)
        setColorTextAndIcon(text = if (value) colorTextPrimaryEnable else
            colorTextPrimaryDisable, icon = if (value) colorIconPrimaryEnable
        else colorIconPrimaryDisable)
    }
    private fun setSelectedPrimary() {
        this.uiTayBgBorder(colorBtnPrimaryActive,R.dimen.dim_tay_btn_bg_radius)
        setColorTextBtn(colorTextPrimaryEnable)
    }

    private fun setEnableSecondary(value: Boolean) {
        if (value) this.uiTayBgBorderStroke(colorBgSecondaryCornerEnable,
            colorBgSecondaryEnableSolid,R.dimen.dim_tay_btn_bg_radius)
        else this.uiTayBgBorderStroke(colorBgSecondaryCornerDisable,
            colorBgSecondaryCornerDisableSolid,R.dimen.dim_tay_btn_bg_radius)
        setColorTextAndIcon(text =  if (value) colorTextSecondaryEnable else
            colorTextSecondaryDisable, icon = if (value)
            colorIconSecondaryEnable else colorIconSecondaryDisable)
    }

    private fun setSelectedSecondary() {
        this.uiTayBgBorderStroke(colorBgSecondaryCornerSelected,
            colorBgSecondarySolidSelected,R.dimen.dim_tay_btn_bg_radius)
        setColorTextBtn(colorBgSecondaryCornerSelected)
    }


    private fun setColorTextAndIcon(
        text: Int = R.color.tay_color_general,
        icon: Int = R.color.tay_color_general,
    ) {
        if (colorDefaultIcon) iconStart.setColorFilter(
            ContextCompat.getColor(context, icon), android.graphics.PorterDuff.Mode.SRC_IN
        )
        if (colorDefaultIcon)iconEnd.setColorFilter(
            ContextCompat.getColor(context, icon), android.graphics.PorterDuff.Mode.SRC_IN
        )
        setColorTextBtn(text)
    }

    private fun setColorTextBtn( color: Int = R.color.tay_color_general){
        button.setTextColor(this.context.resources.getColor(color, null))
    }


    private fun sizeHeight() =
        this.context.resources.getDimensionPixelOffset(R.dimen.dim_tay_btn_height)

    private fun sizeText() =
        this.context.resources.getDimensionPixelSize(R.dimen.dim_tay_btn_sp_text)

    private fun sizeMax() =
        this.context.resources.getDimensionPixelSize(R.dimen.dim_tay_btn_icon)

    private fun positionBtn() {
        constraintSet.connect(button.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        constraintSet.connect(button.id, ConstraintSet.START, iconStart.id, ConstraintSet.END)
        constraintSet.connect(button.id, ConstraintSet.END, iconEnd.id, ConstraintSet.START)
        constraintSet.connect(button.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        constraintSet.applyTo(horizontalContentLayout)
        positionLoading()
    }

    private fun positionIconStar() {
        constraintSet.connect(iconStart.id, ConstraintSet.TOP, button.id, ConstraintSet.TOP)
        constraintSet.connect(iconStart.id, ConstraintSet.END, button.id, ConstraintSet.START)
        constraintSet.connect(iconStart.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        constraintSet.connect(iconStart.id, ConstraintSet.BOTTOM, button.id, ConstraintSet.BOTTOM)
        constraintSet.applyTo(horizontalContentLayout)
        positionIconEnd()
    }
    private fun positionIconEnd() {
        constraintSet.connect(iconEnd.id, ConstraintSet.TOP, button.id, ConstraintSet.TOP)
        constraintSet.connect(iconEnd.id, ConstraintSet.START, button.id, ConstraintSet.END)
        constraintSet.connect(iconEnd.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        constraintSet.connect(iconEnd.id, ConstraintSet.BOTTOM, button.id, ConstraintSet.BOTTOM)
        constraintSet.applyTo(horizontalContentLayout)
        positionHorizontal()
    }

    private fun positionHorizontal() {
        constraintSetH.connect(horizontalContentLayout.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        constraintSetH.connect(horizontalContentLayout.id, ConstraintSet.END,ConstraintSet.PARENT_ID, ConstraintSet.END)
        constraintSetH.connect(horizontalContentLayout.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        constraintSetH.connect(horizontalContentLayout.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        constraintSetH.applyTo(this)
    }

    private fun positionLoading() {
        constraintSet.connect(loading.id, ConstraintSet.TOP, button.id, ConstraintSet.TOP)
        constraintSet.connect(loading.id, ConstraintSet.END,  button.id, ConstraintSet.END)
        constraintSet.connect(loading.id, ConstraintSet.START,  button.id, ConstraintSet.START)
        constraintSet.connect(loading.id, ConstraintSet.BOTTOM, button.id, ConstraintSet.BOTTOM)
        constraintSet.applyTo(this)
        positionIconStar()

    }
}

fun interface UiTayBtnClickListener {
    fun onClick(view: View)
}

enum class UiTayStyleButton(var code: Int) {
    UI_TAY_PRIMARY(0),
    UI_TAY_SECONDARY(1)
}