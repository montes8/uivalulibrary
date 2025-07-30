package com.gb.vale.uivalulibrary.label

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.InputFilter
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import com.gb.vale.uivalulibrary.R
import com.gb.vale.uivalulibrary.list.model.UiTayModelCustom
import com.gb.vale.uivalulibrary.list.uiTayListSpinner
import com.gb.vale.uivalulibrary.utils.UI_TAY_EMPTY
import com.gb.vale.uivalulibrary.utils.setOnClickUiTayDelay
import com.gb.vale.uivalulibrary.utils.uiTayBgBorderStroke
import com.gb.vale.uivalulibrary.utils.uiTaySetColouredSpan
import com.gb.vale.uivalulibrary.utils.uiTayVisibility

class UiTayEditBasic @JvmOverloads constructor(
    context: Context, private val attrs: AttributeSet?, defaultStyle: Int = 0
) : ConstraintLayout(
    context, attrs, defaultStyle
) {

    private var editLabel = EditText(this.context)
    private var iconLabel = ImageView(this.context)
    private var iconLabelStar = ImageView(this.context)
    private var textLabel = TextView(this.context)
    private var textLabelMessage = TextView(this.context)
    private var constraintSet = ConstraintSet()
    private var visibilityIconStart = false
    private var visibilityIconEnd = false
    private var visibilityInfo = false
    private var visibilityLabelInfo = false
    private var typeBottom = true
    private var colorDefaultIconEnd = true
    private var listOption: List<String> = ArrayList()
    private var listOptionCustom: List<UiTayModelCustom> = ArrayList()
    private var positionSelected = -1
    private var ctnList: LinearLayout? = null
    private var uiTayBasicPass = false
    private var uiTayChecked = false
    private var uiTayEnableCopyPaste = false
    private var uiTayTextNewColor = "*"
    private var uiTayIconPassActive: Drawable? =
        ContextCompat.getDrawable(context, R.drawable.ui_tay_ic_eyes_active)
    private var uiTayIconPassInactive: Drawable? =
        ContextCompat.getDrawable(context, R.drawable.ui_tay_ic_eyes_inactive)


    var uiTayLabelTitle: String = UI_TAY_EMPTY
        set(value) {
            field = value
            textLabel.text = value

        }

    var uiTayLabelEdit: String = UI_TAY_EMPTY
        set(value) {
            field = value
            editLabel.setText(value)
        }
        get() = editLabel.text?.toString() ?: UI_TAY_EMPTY

    var uiTayLabelHint: String = UI_TAY_EMPTY
        set(value) {
            field = value
            editLabel.hint = value
        }

    var uiTayLabelContentDescription: String = UI_TAY_EMPTY
        set(value) {
            field = value
            if (value.isNotEmpty()) editLabel.contentDescription = value
        }

    var uiTayLabelImeiOptions: Int = EditorInfo.IME_ACTION_DONE
        set(value) {
            field = value
            editLabel.imeOptions = value
        }

    var uiTayLabelInputType: Int = InputType.TYPE_CLASS_TEXT
        set(value) {
            field = value
            editLabel.inputType = value
        }

    var uiTayLabelEditMaxLength = 30
        set(value) {
            field = value
            editLabel.filters = arrayOf(InputFilter.LengthFilter(value))
        }

    var uiTayLabelEnable: Boolean = true
        set(value) {
            field = value
            editLabel.isEnabled = value
            iconLabel.isEnabled = value
            setPaddingIcon(visibilityIconStart, visibilityIconEnd)
            if (value) styleDefault() else styleDisable()
        }

    var uiTayLabelIconEditEnd: Drawable? = null
        set(value) {
            field = value
            visibilityIconEnd = value != null
            value?.let {
                iconLabel.uiTayVisibility(true)
                setUIIconDrawable(
                    it, iconLabel
                )
            } ?: iconLabel.uiTayVisibility(false)

        }

    var uiTayLabelIconEditStart: Drawable? = null
        set(value) {
            field = value
            visibilityIconStart = value != null
            value?.let {
                iconLabelStar.uiTayVisibility(true)
                setUIIconDrawable(
                    it, iconLabelStar
                )
            } ?: iconLabelStar.uiTayVisibility(false)
        }


    var uiTayLabelFocusable: Boolean = true
        set(value) {
            field = value
            editLabel.isFocusableInTouchMode = value
            editLabel.isCursorVisible = value
            editLabel.isFocusable = value
        }

    var uiTayVisibilityInfo: Boolean = false
        set(value) {
            field = value
            visibilityLabelInfo = value
        }

    var uiTayListBottom: Boolean = true
        set(value) {
            field = value
            typeBottom = value
        }

    var uiTayTitleVisibility: Boolean = true
        set(value) {
            field = value
            textLabel.uiTayVisibility(value)
            if (value) textLabel.setPadding(
                0, 0, 0, this.context.resources.getDimensionPixelOffset(R.dimen.dim_tay_8)
            )
        }

    var uiTayColorIconEndDefault: Boolean = true
        set(value) {
            field = value
            colorDefaultIconEnd = value
        }

    var uiTayErrorMessage: String = UI_TAY_EMPTY
        set(value) {
            field = value
            visibleInfo(value.isNotEmpty())
            if (value.isNotEmpty()) styleRed()
            if (value.isNotEmpty()) textLabelMessage.text = value
            if (value.isNotEmpty()) visibilityInfo = true
        }
        get() = textLabelMessage.text?.toString() ?: UI_TAY_EMPTY

    var uiTayPasswordEnabled: Boolean = false
        set(value) {
            field = value
            uiTayBasicPass = value
        }


    var uiTayIconPasswordActive: Drawable? =
        ContextCompat.getDrawable(context, R.drawable.ui_tay_ic_eyes_active)
        set(value) {
            field = value
            if (uiTayBasicPass) {
                visibilityIconEnd = value != null
                value?.let {
                    uiTayIconPassActive = it
                    iconLabel.uiTayVisibility(true)
                    setUIIconDrawable(
                        it, iconLabel
                    )

                } ?: iconLabel.uiTayVisibility(false)
            }
        }

    var uiTayIconPasswordInactive: Drawable? =
        ContextCompat.getDrawable(context, R.drawable.ui_tay_ic_eyes_inactive)
        set(value) {
            field = value
            if (uiTayBasicPass) {
                value?.let {
                    uiTayIconPassInactive = it
                }
            }
        }

    var uiTayColorPassEnable: Boolean = false
        set(value) {
            field = value
            if (uiTayBasicPass) {
                iconLabel.setColorFilter(
                    ContextCompat.getColor(context, R.color.tay_edit_eyes_pass),
                    PorterDuff.Mode.SRC_IN
                )
            }
        }

    var uiTayTextColor: String = uiTayTextNewColor
        set(value) {
            field = value
            uiTayTextNewColor = value
        }

    var uiTayCopyPaste: Boolean = true
        set(value) {
            field = value
            uiTayEnableCopyPaste = value
        }


    var uiTayAsterisk: Boolean = false
        set(value) {
            field = value
            if (value) {
                val textNew = "${textLabel.text}$uiTayTextNewColor"
                textLabel.text = textNew
                textLabel.uiTaySetColouredSpan(uiTayTextNewColor, R.color.tay_title_color_spannable)
            }
        }

    private fun setUIIconDrawable(icon: Drawable, view: ImageView) {
        view.setImageDrawable(icon)
    }


    fun setUIIconResourceDrawable(icon: Int, view: ImageView) {
        view.setImageResource(icon)
    }

    init {
        loadAttributes()
        configView()
        constraintSet.clone(this)
        positionTitle()
        setUp()
    }

    private fun setUp() {
        configInitFocus()
        configClickIcon()
        configCopyPaste()
    }

    private fun loadAttributes() {
        val attributeSet = context.obtainStyledAttributes(attrs, R.styleable.UiTayEditBasic)
        attributeSet.let {
            uiTayTitleVisibility =
                it.getBoolean(R.styleable.UiTayEditBasic_uiTayTitleVisibility, true)
            uiTayColorIconEndDefault =
                it.getBoolean(R.styleable.UiTayEditBasic_uiTayColorIconEndDefault, true)
            uiTayLabelInputType = it.getInt(
                R.styleable.UiTayEditBasic_android_inputType, InputType.TYPE_CLASS_TEXT
            )
            uiTayLabelImeiOptions = it.getInt(
                R.styleable.UiTayEditBasic_android_imeOptions, EditorInfo.IME_ACTION_DONE
            )
            uiTayLabelIconEditEnd = it.getDrawable(R.styleable.UiTayEditBasic_uiTayLabelIconEditEnd)
            uiTayLabelIconEditStart =
                it.getDrawable(R.styleable.UiTayEditBasic_uiTayLabelIconEditStart)
            uiTayLabelContentDescription =
                it.getString(R.styleable.UiTayEditBasic_uiTayLabelContentDescription)
                    ?: UI_TAY_EMPTY
            uiTayLabelEditMaxLength =
                it.getInteger(R.styleable.UiTayEditBasic_uiTayLabelEditMaxLength, 40)
            uiTayLabelTitle = it.getString(R.styleable.UiTayEditBasic_uiTayLabelTitle)
                ?: this.context.resources.getString(R.string.tay_ui_text_title_edit)
            uiTayLabelEdit = it.getString(R.styleable.UiTayEditBasic_uiTayLabelEdit) ?: UI_TAY_EMPTY
            uiTayLabelHint = it.getString(R.styleable.UiTayEditBasic_uiTayLabelHint)
                ?: this.context.resources.getString(R.string.tay_ui_text_hint_edit)
            uiTayLabelFocusable =
                it.getBoolean(R.styleable.UiTayEditBasic_uiTayLabelFocusable, true)
            uiTayLabelEnable = it.getBoolean(R.styleable.UiTayEditBasic_uiTayLabelEnable, true)
            uiTayErrorMessage =
                it.getString(R.styleable.UiTayEditBasic_uiTayErrorMessage) ?: UI_TAY_EMPTY
            uiTayVisibilityInfo =
                it.getBoolean(R.styleable.UiTayEditBasic_uiTayVisibilityInfo, false)
            uiTayListBottom = it.getBoolean(R.styleable.UiTayEditBasic_uiTayListBottom, true)
            uiTayPasswordEnabled =
                it.getBoolean(R.styleable.UiTayEditBasic_uiTayPasswordEnabled, false)
            uiTayIconPasswordActive =
                it.getDrawable(R.styleable.UiTayEditBasic_uiTayIconPasswordActive)
                    ?: uiTayIconPassActive
            uiTayIconPasswordInactive =
                it.getDrawable(R.styleable.UiTayEditBasic_uiTayIconPasswordInactive)
                    ?: uiTayIconPassInactive
            uiTayColorPassEnable =
                it.getBoolean(R.styleable.UiTayEditBasic_uiTayColorPassEnable, false)
            uiTayTextColor =
                it.getString(R.styleable.UiTayEditBasic_uiTayTextColor) ?: uiTayTextNewColor
            uiTayAsterisk = it.getBoolean(R.styleable.UiTayEditBasic_uiTayAsterisk, false)
            uiTayCopyPaste = it.getBoolean(R.styleable.UiTayEditBasic_uiTayCopyPaste, true)
        }
        attributeSet.recycle()
    }

    private fun configInitFocus(){
        textLabelMessage.uiTayVisibility(visibilityInfo || visibilityLabelInfo)
        editLabel.setOnFocusChangeListener { _, isFocused ->
            if (isFocused) {
                if (!visibilityLabelInfo) styleActive()
            } else {
                if (!visibilityLabelInfo) styleDefault()
            }
        }
    }

    private fun configClickIcon(){
        iconLabel.setOnClickUiTayDelay {
            if (uiTayBasicPass) {
                if (uiTayChecked) {
                    uiTayIconPassInactive?.let { icon -> setUIIconDrawable(icon, iconLabel) }
                    editLabel.transformationMethod = HideReturnsTransformationMethod.getInstance()

                } else {
                    uiTayIconPassActive?.let { icon -> setUIIconDrawable(icon, iconLabel) }
                    editLabel.transformationMethod = PasswordTransformationMethod.getInstance()

                }
                if (editLabel.text.toString()
                        .isNotEmpty()
                ) setSelectionTay(editLabel.text.toString().length)
                uiTayChecked = !uiTayChecked
            }
        }
    }

    private fun configCopyPaste(){
        if (!uiTayEnableCopyPaste) {
            editLabel.customSelectionActionModeCallback =
                object : android.view.ActionMode.Callback {
                    override fun onCreateActionMode(
                        mode: android.view.ActionMode?,
                        menu: Menu?
                    ): Boolean {
                        return false
                    }

                    override fun onPrepareActionMode(
                        mode: android.view.ActionMode?,
                        menu: Menu?
                    ): Boolean {
                        return false
                    }

                    override fun onActionItemClicked(
                        mode: android.view.ActionMode?,
                        item: MenuItem?
                    ): Boolean {
                        return false
                    }

                    override fun onDestroyActionMode(mode: android.view.ActionMode?) {
                        //not implementation
                    }

                }
        }
    }

    fun setOnTaySearchNeoEditListener(viewCtn: ConstraintLayout,
                                   viewTop: View,list : List<String>,listener: TayEditListCLickListener) {
        if (list.isNotEmpty()) {
            if (ctnList != null){ removeListSearch(viewCtn) }
            ctnList = viewCtn.uiTayListSpinner(
                viewTop = viewTop,
                list = list,
                positions = Pair(positionSelected,typeBottom),
                onClickContent = {
                    removeListSearch(viewCtn)
                }
            ) {
                listener.onItemClick(it)
                removeListSearch(viewCtn)
            }

        }else{
            if (ctnList != null){ removeListSearch(viewCtn) }
        }
    }


    fun setOnChangeTayEditListener(listener: TayEditBasicChangeListener) {
        editLabel.addTextChangedListener {
            listener.onChange(it.toString())
            if (!visibilityLabelInfo) visibleInfo(false)
        }
    }

    fun setOnIconClickTayEditListener(listener: TayEditBasicIconCLickListener) {
        iconLabel.setOnClickUiTayDelay {
            listener.onIconClick(it)
        }
    }

    fun setOnIconStartClickTayEditListener(listener: TayEditBasicIconCLickListener) {
        iconLabelStar.setOnClickUiTayDelay {
            listener.onIconClick(it)
        }
    }

    private fun removeListSearch(viewCtn: ConstraintLayout){
        styleDefault()
        viewCtn.removeView(ctnList)
        ctnList = null
    }

    fun setOnListClickTayEditListener(
        viewCtn: ConstraintLayout,
        viewTop: View,
        listener: TayEditListCLickListener
    ) {
        editLabel.setOnClickUiTayDelay {
            if (listOption.isNotEmpty() || listOptionCustom.isNotEmpty()) {
                if (ctnList == null) {
                    styleActive()
                    iconLabel.animate().rotation(180f).start()
                    ctnList = viewCtn.uiTayListSpinner(
                        viewTop = viewTop,
                        list = listOption,
                        listCustom = listOptionCustom,
                        positions = Pair(positionSelected,typeBottom),
                        itemCustom = listOptionCustom.isNotEmpty(),
                        onClickContent = {
                            ctnList = null
                            styleDefault()
                        }
                    ) {
                        styleDefault()
                        listener.onItemClick(it)
                        ctnList = null
                    }

                } else {
                    styleDefault()
                    viewCtn.removeView(ctnList)
                    ctnList = null
                }
            }
        }
    }

    fun setOnClickTayEditListener(listener: TayEditCLickListener) {
        editLabel.setOnClickUiTayDelay { listener.onClick(it) }
    }

    fun setOnFocusTayEditListener(listener: TayEditFocusListener) {
        editLabel.setOnFocusChangeListener { _, isFocused ->
            listener.onClick(isFocused)

        }
    }

    fun updatePositionSelectedItem(value: Int) {
        positionSelected = value
    }

    private fun configView() {
        editLabel.id = View.generateViewId()
        val typeface = ResourcesCompat.getFont(context, R.font.ui_tay_montserrat_medium)
        val layout = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        val layoutParamsEdit = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
        editLabel.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            this.context.resources.getDimensionPixelSize(R.dimen.dim_tay_edit_basic_sp_text)
                .toFloat()
        )
        editLabel.gravity = Gravity.CENTER_VERTICAL
        editLabel.typeface = typeface
        editLabel.layoutParams = layoutParamsEdit
        this.addView(editLabel)
        configText()
        this.layoutParams = layout
    }

    private fun sizeIcon() =
        this.context.resources.getDimensionPixelOffset(R.dimen.dim_tay_edit_basic_icon_size_end)


    private fun sizeIconStart() =
        this.context.resources.getDimensionPixelOffset(R.dimen.dim_tay_edit_basic_icon_size_start)

    private fun configText() {
        val layoutText = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        val layoutTextM = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        val typeface = ResourcesCompat.getFont(context, R.font.ui_tay_montserrat_semi_bold)
        val typefaceMessage = ResourcesCompat.getFont(context, R.font.ui_tay_montserrat_semi_bold)
        textLabel.id = View.generateViewId()
        iconLabel.id = View.generateViewId()
        iconLabelStar.id = View.generateViewId()
        textLabelMessage.id = View.generateViewId()
        textLabel.typeface = typeface
        textLabelMessage.typeface = typefaceMessage
        textLabel.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            this.context.resources.getDimensionPixelSize(R.dimen.dim_tay_edit_title_basic_sp_text)
                .toFloat()
        )
        textLabelMessage.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            this.context.resources.getDimensionPixelSize(R.dimen.dim_tay_edit_error_basic_sp_text)
                .toFloat()
        )
        val layoutIcon = LayoutParams(sizeIconStart(), sizeIconStart())
        layoutIcon.setMargins(
            0,
            0,
            this.context.resources.getDimensionPixelOffset(R.dimen.dim_tay_edit_basic_margin_icon_end),
            0
        )
        val layoutIconStart = LayoutParams(sizeIcon(), sizeIcon())
        layoutIconStart.setMargins(
            this.context.resources.getDimensionPixelOffset(R.dimen.dim_tay_edit_basic_margin_icon_start),
            0,
            0,
            0
        )

        iconLabel.setPadding(
            this.context.resources.getDimensionPixelOffset(R.dimen.dim_tay_edit_basic_padding_icon_horizontal_end),
            this.context.resources.getDimensionPixelOffset(R.dimen.dim_tay_edit_basic_padding_icon_vertical_end),
            this.context.resources.getDimensionPixelOffset(R.dimen.dim_tay_edit_basic_padding_icon_horizontal_end),
            this.context.resources.getDimensionPixelOffset(R.dimen.dim_tay_edit_basic_padding_icon_vertical_end)
        )

        iconLabelStar.setPadding(
            this.context.resources.getDimensionPixelOffset(R.dimen.dim_tay_edit_basic_padding_icon_horizontal_start),
            this.context.resources.getDimensionPixelOffset(R.dimen.dim_tay_edit_basic_padding_icon_vertical_start),
            this.context.resources.getDimensionPixelOffset(R.dimen.dim_tay_edit_basic_padding_icon_horizontal_start),
            this.context.resources.getDimensionPixelOffset(R.dimen.dim_tay_edit_basic_padding_icon_vertical_start)
        )

        iconLabel.layoutParams = layoutIcon
        iconLabelStar.layoutParams = layoutIconStart
        textLabel.layoutParams = layoutText
        textLabelMessage.layoutParams = layoutTextM
        this.addView(textLabel)
        this.addView(iconLabel)
        this.addView(iconLabelStar)
        this.addView(textLabelMessage)
    }

    private fun setPaddingIcon(start: Boolean, end: Boolean) {
        editLabel.setPadding(
            this.context.resources.getDimensionPixelOffset(
                if (start) R.dimen.dim_tay_edit_basic_padding_horizontal_icon else R.dimen.dim_tay_edit_basic_padding_horizontal
            ), this.context.resources.getDimensionPixelOffset(
                R.dimen.dim_tay_edit_basic_padding_top_button
            ), this.context.resources.getDimensionPixelOffset(
                if (end) R.dimen.dim_tay_edit_basic_padding_horizontal_icon else R.dimen.dim_tay_edit_basic_padding_horizontal
            ), this.context.resources.getDimensionPixelOffset(
                R.dimen.dim_tay_edit_basic_padding_top_button
            )
        )
    }

    private fun styleDefault() {
        editLabel.uiTayBgBorderStroke(
            R.color.tay_edit_basic_bg_corner_enable,
            R.color.tay_edit_basic_bg_solid_enable, R.dimen.dim_tay_bg_edit_basic_radius
        )
        setColorTextAndIcon(
            text = R.color.tay_edit_basic_text_title_enable,
            icon = R.color.tay_edit_icon_enable,
            info = R.color.tay_edit_text_info_enable,
            edit = R.color.tay_edit_basic_text_enable,
            hint = R.color.tay_edit_hint_text_enable
        )
        iconLabel.animate().rotation(360f).start()
    }

    private fun styleActive() {
        editLabel.uiTayBgBorderStroke(
            R.color.tay_edit_basic_bg_corner_active,
            R.color.tay_edit_basic_bg_solid_active, R.dimen.dim_tay_bg_edit_basic_radius
        )
        setColorTextAndIcon(
            text = R.color.tay_edit_basic_text_title_active,
            icon = R.color.tay_edit_icon_active,
            info = R.color.tay_edit_text_info_active,
            edit = R.color.tay_edit_basic_text_active,
            hint = R.color.tay_edit_hint_text_active
        )
    }

    private fun styleDisable() {
        editLabel.uiTayBgBorderStroke(
            R.color.tay_edit_basic_bg_corner_disable,
            R.color.tay_edit_basic_bg_solid_disable, R.dimen.dim_tay_bg_edit_basic_radius
        )
        setColorTextAndIcon(
            text = R.color.tay_edit_basic_text_title_disable,
            icon = R.color.tay_edit_icon_disable,
            info = R.color.tay_edit_text_info_disable,
            edit = R.color.tay_edit_basic_text_disable,
            hint = R.color.tay_edit_hint_text_disable
        )
    }

    private fun styleRed() {
        editLabel.uiTayBgBorderStroke(
            R.color.tay_edit_error_info,
            R.color.tay_edit_error_info_solid, R.dimen.dim_tay_bg_edit_basic_radius
        )
        setColorTextAndIcon(
            text = R.color.tay_edit_error_info,
            icon = R.color.tay_edit_error_info,
            info = R.color.tay_edit_error_info,
            edit = R.color.tay_edit_error_info
        )
    }

    @SuppressLint("SuspiciousIndentation")
    private fun setColorTextAndIcon(
        text: Int = R.color.tay_color_general,
        icon: Int = R.color.tay_color_general,
        info: Int = R.color.tay_color_general,
        edit: Int = R.color.tay_color_general,
        hint: Int = R.color.tay_edit_hint_text_enable
    ) {
        if (colorDefaultIconEnd) iconLabel.setColorFilter(
            ContextCompat.getColor(context, icon), PorterDuff.Mode.SRC_IN
        )
        if (colorDefaultIconEnd) iconLabelStar.setColorFilter(
            ContextCompat.getColor(context, icon), PorterDuff.Mode.SRC_IN
        )
        textLabel.setTextColor(this.context.resources.getColor(text, null))
        editLabel.setTextColor(this.context.resources.getColor(edit, null))
        textLabelMessage.setTextColor(this.context.resources.getColor(info, null))
        editLabel.setHintTextColor(this.context.resources.getColor(hint, null))
    }

    private fun positionTitle() {
        constraintSet.connect(
            textLabel.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP
        )
        constraintSet.connect(
            textLabel.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START
        )
        constraintSet.connect(
            textLabel.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END
        )
        constraintSet.applyTo(this)
        positionEdit()
    }

    private fun positionEdit() {
        constraintSet.connect(editLabel.id, ConstraintSet.TOP, textLabel.id, ConstraintSet.BOTTOM)
        constraintSet.connect(
            editLabel.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START
        )
        constraintSet.connect(
            editLabel.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END
        )
        constraintSet.applyTo(this)
        positionIcon()
        positionIconStart()
        positionMessage()
    }

    private fun positionIcon() {
        constraintSet.connect(iconLabel.id, ConstraintSet.TOP, editLabel.id, ConstraintSet.TOP)
        constraintSet.connect(iconLabel.id, ConstraintSet.END, editLabel.id, ConstraintSet.END)
        constraintSet.connect(
            iconLabel.id, ConstraintSet.BOTTOM, editLabel.id, ConstraintSet.BOTTOM
        )
        constraintSet.applyTo(this)
    }

    private fun positionIconStart() {
        constraintSet.connect(iconLabelStar.id, ConstraintSet.TOP, editLabel.id, ConstraintSet.TOP)
        constraintSet.connect(
            iconLabelStar.id, ConstraintSet.START, editLabel.id, ConstraintSet.START
        )
        constraintSet.connect(
            iconLabelStar.id, ConstraintSet.BOTTOM, editLabel.id, ConstraintSet.BOTTOM
        )
        constraintSet.applyTo(this)
    }

    fun setTypeFaceTay(typeface : Typeface? = ResourcesCompat.getFont(context, R.font.ui_tay_montserrat_medium)) {
        editLabel.typeface = typeface
    }

    private fun positionMessage() {
        constraintSet.connect(
            textLabelMessage.id, ConstraintSet.TOP, editLabel.id, ConstraintSet.BOTTOM
        )
        constraintSet.connect(
            textLabelMessage.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START
        )
        constraintSet.connect(
            textLabelMessage.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END
        )
        constraintSet.applyTo(this)
    }

    private fun visibleInfo(value: Boolean) {
        textLabelMessage.uiTayVisibility(value)
        if (value) textLabelMessage.setPadding(
            0, this.context.resources.getDimensionPixelOffset(R.dimen.dim_tay_8), 0, 0
        )
    }

    fun setSelectionTay(value: Int) {
        editLabel.setSelection(value)
    }

    fun setFilterTay(inputFilterList : Array<InputFilter>){
        editLabel.filters = inputFilterList
    }

    fun setListOptionDropDawn(list: List<String>) {
        listOption = list
    }

    fun setListOptionCustomDropDawn(list: List<UiTayModelCustom>) {
        listOptionCustom = list
    }

    fun setOnKeyCodeTayEditBasic(listener: TayEditBasicKeyCode) {
        editLabel.setOnKeyListener { _, keyCode, _ ->
            listener.onCodeClick(keyCode)
            false
        }
    }


}


fun interface TayEditBasicKeyCode {
    fun onCodeClick(code: Int)
}

fun interface TayEditBasicChangeListener {
    fun onChange(value: String)
}

fun interface TayEditBasicIconCLickListener {
    fun onIconClick(view: View)
}

fun interface TayEditListCLickListener {
    fun onItemClick(item: Int)
}

fun interface TayEditCLickListener {
    fun onClick(view: View)
}

fun interface TayEditFocusListener {
    fun onClick(value: Boolean)
}
