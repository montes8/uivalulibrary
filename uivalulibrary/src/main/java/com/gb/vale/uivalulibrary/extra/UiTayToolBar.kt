package com.gb.vale.uivalulibrary.extra

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.gb.vale.uivalulibrary.R
import com.gb.vale.uivalulibrary.utils.UI_TAY_EMPTY
import com.gb.vale.uivalulibrary.utils.setOnClickUiTayDelay
import com.gb.vale.uivalulibrary.utils.uiTayBgBorder
import com.gb.vale.uivalulibrary.utils.uiTayInVisibility
import com.gb.vale.uivalulibrary.utils.uiTayVisibility

class UiTayToolBar @JvmOverloads constructor(
    context: Context, private val attrs: AttributeSet, defaultStyle: Int = 0
) : ConstraintLayout(
    context, attrs, defaultStyle
) {

    private var imgTbStart = ImageView(this.context)
    private var imgTbEnd = ImageView(this.context)
    private var textToolBar = TextView(this.context)
    private var progressStep = UiTayLineProgress(context, attrs)
    private var constraintSet = ConstraintSet()
    private var maxProgress = 0
    private var currentProgress = 0
    private var uiTayIconColorDefault = true
    private var uiTayGravityTb = UITayStyleGravityTb.TB_CENTER

    private var imgCtn = ImageView(this.context)

    var uiTayTextTb: String = UI_TAY_EMPTY
        set(value) {
            field = value
            textToolBar.text = value

        }


    var uiTayStyleTbIcon: UITayStyleTbIcon = UITayStyleTbIcon.UI_TAY_ICON_START
        set(value) {
            field = value
            configStyleIcon(value)
        }


    var uiTayStyleGravityTb : UITayStyleGravityTb = UITayStyleGravityTb.TB_CENTER
        set(value) {
            field = value
            uiTayGravityTb = value
        }

    var uiTayVisibilityText : Boolean = true
        set(value) {
            field = value
            textToolBar.uiTayInVisibility(value)
        }

    private fun configStyleIcon(style: UITayStyleTbIcon) {
        when (style) {
            UITayStyleTbIcon.UI_TAY_ICON_START -> {
                visibilityIcons(start = true, end = false)
            }

            UITayStyleTbIcon.UI_TAY_ICON_END -> {
                visibilityIcons(start = false, end = true)
            }

            UITayStyleTbIcon.UI_TAY_ICON_TWO -> {
                visibilityIcons(start = true, end = true)
            }

            else -> {
                visibilityIcons()
            }
        }
    }

    private fun visibilityIcons(start: Boolean = false, end: Boolean = false) {
        imgTbStart.uiTayInVisibility(start)
        imgTbEnd.uiTayInVisibility(end)
    }


    var uiTayIconStartTb: Drawable? = null
        set(value) {
            field = value
            value?.let {
                imgTbStart.setImageDrawable(it)
            }
        }

    var uiTayIconEndTb: Drawable? = null
        set(value) {
            field = value
            value?.let {
                imgTbEnd.setImageDrawable(it)
            }
        }
    var uiTayTbIndicator: Boolean = false
        set(value) {
            field = value
            progressStep.uiTayVisibility(value)
        }

    var uiTayTbIconColorDefault: Boolean = true
        set(value) {
            field = value
            uiTayIconColorDefault = value
        }


    private var uiTayTbProgress: Int = 0
        set(value) {
            field = value
            currentProgress = value
        }

    private var uiTayMaxTbProgress: Int = 0
        set(value) {
            field = value
            maxProgress = value
        }

    init {
        loadAttributes()
        configView()
        constraintSet.clone(this)
        positionText()
    }


    private fun loadAttributes() {
        val attributeSet = context.obtainStyledAttributes(attrs, R.styleable.UiTayToolBar)
        attributeSet.let {
            uiTayStyleGravityTb = UITayStyleGravityTb.values()[it.getInt(
                R.styleable.UiTayToolBar_uiTayStyleGravityTb,
                0
            )]
            uiTayTbIconColorDefault =
                it.getBoolean(R.styleable.UiTayToolBar_uiTayTbIconColorDefault, true)
            uiTayTextTb = it.getString(R.styleable.UiTayToolBar_uiTayTextTb)
                ?: this.context.resources.getString(R.string.tay_ui_text_title_toolbar)
            uiTayIconStartTb =
                it.getDrawable(R.styleable.UiTayToolBar_uiTayIconStartTb)
                    ?: ContextCompat.getDrawable(
                        this.context, R.drawable.ui_tay_ic_back
                    )
            uiTayIconEndTb =
                it.getDrawable(R.styleable.UiTayToolBar_uiTayIconEndTb)
                    ?: ContextCompat.getDrawable(
                        this.context, R.drawable.ui_tay_ic_menu
                    )
            uiTayStyleTbIcon =
                UITayStyleTbIcon.values()[it.getInt(R.styleable.UiTayToolBar_uiTayStyleTbIcon, 1)]
            uiTayTbIndicator = it.getBoolean(R.styleable.UiTayToolBar_uiTayTbIndicator, false)
            uiTayMaxTbProgress = it.getInt(R.styleable.UiTayToolBar_uiTayTbMaxProgress, 0)
            uiTayTbProgress = it.getInt(R.styleable.UiTayToolBar_uiTayTbProgress, 0)
        }
        attributeSet.recycle()
    }

    private fun configStyleInit() {
        this.uiTayBgBorder(
            R.color.tay_toolbar_solid, R.dimen.dim_tay_0
        )
        if (uiTayIconColorDefault) imgTbStart.setColorFilter(
            ContextCompat.getColor(
                context,
                R.color.tay_toolbar_icon_start
            ),
            PorterDuff.Mode.SRC_IN
        )
        if (uiTayIconColorDefault) imgTbEnd.setColorFilter(
            ContextCompat.getColor(
                context,
                R.color.tay_toolbar_icon_end
            ),
            PorterDuff.Mode.SRC_IN
        )
        textToolBar.setTextColor(
            this.context.resources.getColor(
                R.color.tay_toolbar_text, null
            )
        )
    }

    fun setOnClickTayBackListener(time: Long = 700, listener: UiTayBackListener) {
        imgTbStart.setOnClickUiTayDelay(time) {
            listener.onClick(this)
        }
    }

    fun setOnClickTayMenuListener(time: Long = 700, listener: UiTayMenuListener) {
        imgTbEnd.setOnClickUiTayDelay(time) {
            listener.onClick(this)
        }
    }


    private fun configView() {
        imgCtn.id = View.generateViewId()
        val layoutParamI = LayoutParams(
            LayoutParams.MATCH_PARENT,
            this.context.resources.getDimensionPixelOffset(R.dimen.dim_tay_56)
        )
        imgCtn.scaleType = ImageView.ScaleType.FIT_XY
        imgCtn.layoutParams = layoutParamI
        this.addView(imgCtn)
        textToolBar.id = View.generateViewId()
        val layoutParam = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
        val typeface = ResourcesCompat.getFont(context, R.font.ui_tay_montserrat_semi_bold)
        val layoutText = LayoutParams(
            this.context.resources.getDimensionPixelOffset(R.dimen.dim_tay_0),
            LayoutParams.WRAP_CONTENT
        )
        textToolBar.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            this.context.resources.getDimensionPixelSize(R.dimen.dim_tay_toolbar_sp_text).toFloat()
        )
        textToolBar.gravity = configGravity()
        textToolBar.typeface = typeface
        textToolBar.setPadding(
            this.context.resources.getDimensionPixelOffset(R.dimen.dim_tay_toolbar_padding_start_end),
            this.context.resources.getDimensionPixelOffset(R.dimen.dim_tay_toolbar_padding_top_bottom),
            this.context.resources.getDimensionPixelOffset(R.dimen.dim_tay_toolbar_padding_start_end),
            this.context.resources.getDimensionPixelOffset(R.dimen.dim_tay_toolbar_padding_top_bottom)
        )
        textToolBar.layoutParams = layoutText
        this.addView(textToolBar)
        configIconStart()
        configIconEnd()
        configProgress()
        this.layoutParams = layoutParam
        configStyleInit()
    }
    private fun configGravity()=when(uiTayGravityTb){
        UITayStyleGravityTb.TB_CENTER -> Gravity.CENTER
        UITayStyleGravityTb.TB_STAR -> Gravity.START
        else -> Gravity.END
    }

    private fun configIconStart() {
        imgTbStart.id = View.generateViewId()
        val layoutIcon = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        imgTbStart.setPadding(
            this.context.resources.getDimensionPixelOffset(R.dimen.dim_tay_16),
            0, 0, 0
        )
        imgTbStart.layoutParams = layoutIcon
        this.addView(imgTbStart)
    }

    private fun configIconEnd() {
        imgTbEnd.id = View.generateViewId()
        val layoutIconEnd = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        imgTbEnd.setPadding(
            0,
            0, this.context.resources.getDimensionPixelOffset(R.dimen.dim_tay_16), 0
        )
        imgTbEnd.layoutParams = layoutIconEnd
        this.addView(imgTbEnd)

    }

    fun updateStyle(primary : Int = 0,secondary: Int = 0){
        if (primary !=0){
            textToolBar.setTextColor(
                this.context.resources.getColor(
                    primary, null
                )
            )
        }

        if (secondary !=0){
            this.setBackgroundColor(this.context.resources.getColor(
                secondary, null
            ))
        }

    }

    fun getImageBgToolBar() = imgCtn

    fun updateStyleTotal(primary : Int = 0,secondary: Int = 0){
        if (primary !=0){
            textToolBar.setTextColor(
                this.context.resources.getColor(
                    primary, null
                )
            )
            imgTbStart.setColorFilter(
                ContextCompat.getColor(
                    context,
                    primary
                ),
                PorterDuff.Mode.SRC_IN
            )

            imgTbEnd.setColorFilter(
                ContextCompat.getColor(
                    context,
                    primary
                ),
                PorterDuff.Mode.SRC_IN
            )
        }

        if (secondary !=0){
            this.setBackgroundColor(this.context.resources.getColor(
                secondary, null
            ))
        }

    }

    private fun configProgress() {
        progressStep.id = View.generateViewId()
        val layoutP = LayoutParams(
            LayoutParams.MATCH_PARENT,
            this.context.resources.getDimensionPixelOffset(R.dimen.dim_tay_toolbar_size_indication)
        )
        progressStep.layoutParams = layoutP
        updateUiTayProgress()
        this.addView(progressStep)
    }

    fun setUiTayProgress(position: Int, range: Int) {
        currentProgress = position
        maxProgress = range
        updateUiTayProgress()
    }

    private fun updateUiTayProgress() {
        if (currentProgress != 0 && maxProgress != 0 && maxProgress >= currentProgress) {
            progressStep.initLine(maxProgress, currentProgress)
        }
    }

    private fun positionText() {
        constraintSet.connect(
            textToolBar.id,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP
        )
        constraintSet.connect(
            textToolBar.id,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START
        )
        constraintSet.connect(
            textToolBar.id,
            ConstraintSet.END,
            ConstraintSet.PARENT_ID,
            ConstraintSet.END
        )
        constraintSet.connect(
            textToolBar.id,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM
        )
        constraintSet.applyTo(this)
        positionIcon()
    }

    private fun positionIcon() {
        constraintSet.connect(
            imgTbStart.id,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP
        )
        constraintSet.connect(
            imgTbStart.id,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START
        )
        constraintSet.connect(
            imgTbStart.id,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM
        )
        constraintSet.applyTo(this)
        positionIconEnd()
    }

    private fun positionIconEnd() {
        constraintSet.connect(
            imgTbEnd.id,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP
        )
        constraintSet.connect(
            imgTbEnd.id,
            ConstraintSet.END,
            ConstraintSet.PARENT_ID,
            ConstraintSet.END
        )
        constraintSet.connect(
            imgTbEnd.id,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM
        )
        constraintSet.applyTo(this)
        positionProgress()
    }

    private fun positionProgress() {
        constraintSet.connect(
            progressStep.id,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM
        )
        constraintSet.connect(
            progressStep.id,
            ConstraintSet.END,
            ConstraintSet.PARENT_ID,
            ConstraintSet.END
        )
        constraintSet.connect(
            progressStep.id,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START
        )
        constraintSet.applyTo(this)
    }


    fun setUpdateStyle(bg: Drawable, iconStart: Int, iconEnd: Int, textColor: Int) {
        this.background = bg
        imgTbStart.setColorFilter(
            ContextCompat.getColor(
                context,
                iconStart
            ),
            PorterDuff.Mode.SRC_IN
        )
        imgTbEnd.setColorFilter(
            ContextCompat.getColor(
                context,
                iconEnd
            ),
            PorterDuff.Mode.SRC_IN
        )
        textToolBar.setTextColor(
            this.context.resources.getColor(
                textColor, null
            )
        )
    }
}

fun interface UiTayBackListener {
    fun onClick(view: View)
}

fun interface UiTayMenuListener {
    fun onClick(view: View)
}

enum class UITayStyleTbIcon(var code: Int) {
    UI_TAY_NONE(0),
    UI_TAY_ICON_START(1),
    UI_TAY_ICON_END(2),
    UI_TAY_ICON_TWO(3)
}

enum class UITayStyleGravityTb(var code: Int) {
    TB_CENTER(0),
    TB_STAR(1),
    TB_END(2)
}