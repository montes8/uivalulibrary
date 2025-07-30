package com.gb.vale.uivalulibrary.label

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.InputFilter
import android.text.InputType
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import com.gb.vale.uivalulibrary.R
import com.gb.vale.uivalulibrary.list.model.UiTayModelCustom
import com.gb.vale.uivalulibrary.list.uiTayListSpinner
import com.gb.vale.uivalulibrary.utils.UI_TAY_EMPTY
import com.gb.vale.uivalulibrary.utils.setOnClickUiTayDelay
import com.gb.vale.uivalulibrary.utils.uiTayPixelsToSp
import com.gb.vale.uivalulibrary.utils.uiTayTryCatch
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class UiTayEditLayout @JvmOverloads constructor(
    context: Context,
    private val attrs: AttributeSet, defaultStyle: Int = 0
) : TextInputLayout(context, attrs, defaultStyle) {

    private var ediText = TextInputEditText(context, null, R.style.EditStyleGray)


    private var uiTayLDrawableStart:Drawable? = null
    private var uiTayLDrawableEnd:Drawable? = null
    private var listOptionLayout: List<String> = ArrayList()
    private var listOptionCustomLayout: List<UiTayModelCustom> = ArrayList()
    private var positionSelectedLayout = -1
    private var ctnLayoutList: LinearLayout? = null
    private var typeBottomLayout = true
    private var uiTayEnableCopyPaste = false

    var uiTayLCopyPaste : Boolean = true
        set(value) {
            field = value
            uiTayEnableCopyPaste = value
        }


    var uiTayLHint: String = UI_TAY_EMPTY
        set(value) {
            field = value
            this.isHintEnabled = true
            this.hint = value
        }

    var uiTayLListBottom: Boolean = true
        set(value) {
            field = value
            typeBottomLayout = value
        }
    var uiTayLEditInputType: Int = InputType.TYPE_CLASS_TEXT
        set(value) {
            field = value
            ediText.inputType = value
        }

    var uiTayLEditMaxLength = 200
        set(value) {
            field = value
            ediText.filters = arrayOf(InputFilter.LengthFilter(value))
        }


    var uiTayLIconPassword: Drawable? =  ContextCompat.getDrawable(context, R.drawable.ui_tay_selector_password)
        set(value) {
            field = value
            setUIIconDrawable(
                value ?: ContextCompat.getDrawable(
                    context,
                    R.drawable.ui_tay_selector_password
                )
            )
        }


    var uiTayLErrorMessage: String = UI_TAY_EMPTY
        set(value) {
            field = value
            uiTayLErrorEnable = value.isNotEmpty()
            this.setErrorIconDrawable(if (value.isNotEmpty())R.drawable.ui_tay_ic_error_edit
            else 0)
            this.error = value.ifEmpty { null }
        }

    var uiTayLErrorEnable: Boolean = false
        set(value) {
            field = value
            this.isErrorEnabled = value
        }

    var uiTayLEnable: Boolean = true
        set(value) {
            field = value
            this.isEnabled = value
            ediText.isEnabled = value
            if (value)setEditDefault() else setEditDisable()
        }

    var uiTayLFocusable: Boolean = true
        set(value) {
            field = value
            ediText.isFocusableInTouchMode = value
            ediText.isCursorVisible = value
            ediText.isFocusable = value
        }

    var uiTayLText: String = UI_TAY_EMPTY
        set(value) {
            field = value
            ediText.setText(value)
            ediText.text?.toString()?.length?.let {num -> ediText.setSelection(num)}

        }
        get() = ediText.text?.toString() ?: UI_TAY_EMPTY


    var uiTayLImeiOptions: Int = EditorInfo.IME_ACTION_DONE
        set(value) {
            field = value
            ediText.imeOptions = value
        }

    var uiTayLPasswordEnabled: Boolean = false
        set(value) {
            field = value
           this.endIconMode =
                if (value) END_ICON_PASSWORD_TOGGLE
                else END_ICON_NONE
        }

    var uiTayLIconDrawableEnd: Drawable? = null
        set(value) {
            field = value
            value?.let {
                uiTayLDrawableEnd= value
            }

        }

    var uiTayLIconDrawableStart: Drawable? = null
        set(value) {
            field = value
            value?.let {
                uiTayLDrawableStart= value
            }
        }

    private fun setImageEndIcon(){
        uiTayTryCatch {
            uiTayLDrawableStart?.setBounds(context.resources.getDimensionPixelOffset(R.dimen.dim_tay_20),
                context.resources.getDimensionPixelOffset(R.dimen.dim_tay_20)
                ,context.resources.getDimensionPixelOffset(R.dimen.dim_tay_20),
                context.resources.getDimensionPixelOffset(R.dimen.dim_tay_20))
            uiTayLDrawableEnd?.setBounds(context.resources.getDimensionPixelOffset(R.dimen.dim_tay_20),
                context.resources.getDimensionPixelOffset(R.dimen.dim_tay_20)
                ,context.resources.getDimensionPixelOffset(R.dimen.dim_tay_20),
                context.resources.getDimensionPixelOffset(R.dimen.dim_tay_20))
            ediText.setCompoundDrawablesWithIntrinsicBounds(
                uiTayLDrawableStart,
                null,
                uiTayLDrawableEnd,
                null
            )

            ediText.compoundDrawablePadding = context.resources.getDimensionPixelOffset(R.dimen.dim_tay_20)
        }

        ediText.setOnFocusChangeListener { _, isFocused ->
            if (isFocused)setEditActive() else setEditDefault()
            this.defaultHintTextColor = ColorStateList.valueOf(ContextCompat.getColor(context,
                if (ediText.text.toString().isNotEmpty()  || isFocused )R.color.tay_edit_two_hint_l_active else R.color.tay_edit_two_hint_enable))
        }
        this.defaultHintTextColor = ColorStateList.valueOf(ContextCompat.getColor(context,
            if (ediText.text.toString().isNotEmpty())R.color.tay_edit_two_hint_l_active else R.color.tay_edit_two_hint_enable))
    }



    private fun setUIIconDrawable(icon: Drawable?) {
        icon?.let {
            this.endIconDrawable = it
       }
    }


    init {
        initV1()
        loadAttributes()
        setImageEndIcon()
        setUpView()
    }

    private fun setUpView(){
        if (!uiTayEnableCopyPaste){
            ediText.customSelectionActionModeCallback = object : android.view.ActionMode.Callback {
                override fun onCreateActionMode(mode: android.view.ActionMode?, menu: Menu?): Boolean {
                    return false
                }

                override fun onPrepareActionMode(mode: android.view.ActionMode?, menu: Menu?): Boolean {
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

    private fun initV1() {
        setWillNotDraw(false)
        this.isErrorEnabled = false
        this.errorIconDrawable = null
        createEditLayout(ediText)
    }

    private fun loadAttributes() {
        val attributeSet = context.obtainStyledAttributes(attrs, R.styleable.UiTayEditLayout)
        attributeSet.let { uiTayLEditInputType = it.getInt(R.styleable.UiTayEditLayout_android_inputType,
            InputType.TYPE_CLASS_TEXT
        )
            uiTayLImeiOptions = it.getInt(
                R.styleable.UiTayEditLayout_android_imeOptions,
                EditorInfo.IME_ACTION_DONE
            )
            uiTayLPasswordEnabled =
                it.getBoolean(R.styleable.UiTayEditLayout_uiTayLPasswordEnabled, false)
            uiTayLErrorEnable = it.getBoolean(R.styleable.UiTayEditLayout_uiTayLErrorEnabled, false)
            uiTayLEditMaxLength = it.getInteger(R.styleable.UiTayEditLayout_uiTayLEditMaxLength, 40)
            uiTayLIconPassword = it.getDrawable(R.styleable.UiTayEditLayout_uiTayLIconPassword)?:
                    ContextCompat.getDrawable(context, R.drawable.ui_tay_selector_password)
            uiTayLIconDrawableEnd = it.getDrawable(R.styleable.UiTayEditLayout_uiTayLIconDrawableEnd)
            uiTayLIconDrawableStart = it.getDrawable(R.styleable.UiTayEditLayout_uiTayLIconDrawableStart)
            uiTayLFocusable = it.getBoolean(R.styleable.UiTayEditLayout_uiTayLFocusable, true)
            uiTayLText = it.getString(R.styleable.UiTayEditLayout_uiTayLText) ?: UI_TAY_EMPTY
            uiTayLHint = it.getString(R.styleable.UiTayEditLayout_uiTayLHint) ?: context.getString(R.string.tay_ui_text_hint_edit_two)
            uiTayLEnable = it.getBoolean(R.styleable.UiTayEditLayout_uiTayLEnabled, true)
            uiTayLListBottom = it.getBoolean(R.styleable.UiTayEditLayout_uiTayLListBottom, true)
            uiTayLCopyPaste =  it.getBoolean(R.styleable.UiTayEditLayout_uiTayLCopyPaste, true)

        }
        attributeSet.recycle()
    }


    fun setEditActive(){
        editText?.typeface = ResourcesCompat.getFont(context, R.font.ui_tay_montserrat_medium)
        ediText.setTextColor(ContextCompat.getColor(context, R.color.tay_edit_two_text_active))
        ediText.setHintTextColor(ContextCompat.getColor(context, R.color.tay_edit_two_hint_text_active))
        this.setEndIconTintList(ColorStateList.valueOf(ContextCompat.getColor(context,R.color.tay_edit_two_eyes_active)))

    }

    fun setEditDefault(){
        editText?.typeface = ResourcesCompat.getFont(context, R.font.ui_tay_montserrat_medium)
        ediText.setTextColor(ContextCompat.getColor(context, R.color.tay_edit_two_text_enable))
        ediText.setHintTextColor(ContextCompat.getColor(context, R.color.tay_edit_two_hint_enable))
        this.setEndIconTintList(ColorStateList.valueOf(ContextCompat.getColor(context,R.color.tay_edit_two_eyes_enable)))


    }

    fun setEditDisable(){
        editText?.typeface = ResourcesCompat.getFont(context, R.font.ui_tay_montserrat_medium)
        ediText.setTextColor(ContextCompat.getColor(context, R.color.tay_edit_two_text_disable))
        ediText.setHintTextColor(ContextCompat.getColor(context, R.color.tay_edit_two_hint_text_disable))
        this.defaultHintTextColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.tay_edit_two_hint_enable))
        this.setEndIconTintList(ColorStateList.valueOf(ContextCompat.getColor(context,R.color.tay_edit_two_eyes_disable)))


    }
    private fun createEditLayout(editText: TextInputEditText) {
        val padding16 = context.resources.getDimensionPixelSize(R.dimen.dim_tay_16)
        if (!editText.isInEditMode)
            editText.typeface = ResourcesCompat.getFont(context, R.font.ui_tay_montserrat_medium)
        editText.ellipsize = TextUtils.TruncateAt.END
        with(editText) {
            setPadding(padding16, this.context.resources.getDimensionPixelOffset(
                R.dimen.dim_tay_edit_two_padding_top_button
            ), this.context.resources.getDimensionPixelOffset(
                R.dimen.dim_tay_edit_two_padding_top_button
            ), this.context.resources.getDimensionPixelOffset(
                R.dimen.dim_tay_edit_two_padding_top_button
            ))
            gravity = Gravity.CENTER_VERTICAL
            setTextColor(ContextCompat.getColor(context, R.color.tay_edit_two_text_enable))
            textSize = uiTayPixelsToSp(context,
                context.resources.getDimensionPixelSize(R.dimen.dim_tay_edit_two_sp_text).toFloat()
            )
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
            maxLines = 1
            setLines(1)
            isSingleLine = true
            ellipsize = TextUtils.TruncateAt.END

        }
        addView(editText)
    }

    fun setOnClickTayEditListener(listener: TayEditLayoutCLickListener) {
        ediText.setOnClickUiTayDelay { listener.onClick(it) }
        this.setOnClickUiTayDelay { listener.onClick(it) }
    }

    fun setOnChangeTayEditLayoutListener(listener: TayEditLayoutChangeListener) {
        ediText.addTextChangedListener{
            listener.onChange(it.toString())
            uiTayLErrorMessage = UI_TAY_EMPTY
        }
    }

    fun setSelectionTay(value: Int) {
        ediText.setSelection(value)
    }

    fun setListOptionDropDawn(list: List<String>) {
        listOptionLayout = list
    }

    fun setListOptionCustomDropDawn(list: List<UiTayModelCustom>) {
        listOptionCustomLayout = list
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setOnIconClickTayEditListener(listener: TayEditLayoutIconCLickListener) {
        ediText.setOnTouchListener { _, event ->
            if (ediText.compoundDrawables[2] !=null && event.rawX >=
                (right - ediText.compoundDrawables[2].bounds.width())){
                listener.onIconClick()

            }
            false
        }
    }

    fun setOnTaySearchNeoEditListener(viewCtn: ConstraintLayout,
                                      viewTop: View,list : List<String>,listener: TayEditListCLickListener) {
        if (list.isNotEmpty()) {
            if (ctnLayoutList != null){ removeListSearch(viewCtn) }
            ctnLayoutList = viewCtn.uiTayListSpinner(
                viewTop = viewTop,
                list = list, positions = Pair(positionSelectedLayout,typeBottomLayout),
                onClickContent = {
                    removeListSearch(viewCtn)
                }
            ) {
                listener.onItemClick(it)
                removeListSearch(viewCtn)
            }

        }else{
            if (ctnLayoutList != null){ removeListSearch(viewCtn) }
        }
    }
    private fun removeListSearch(viewCtn: ConstraintLayout){
        setEditDefault()
        viewCtn.removeView(ctnLayoutList)
        ctnLayoutList = null
    }



    fun setOnListClickTayEditListener(
        viewCtn: ConstraintLayout,
        viewTop: View,
        listener: TayEditLayoutListCLickListener
    ) {
        ediText.setOnClickUiTayDelay {
            if (listOptionLayout.isNotEmpty()) {
                if (ctnLayoutList == null) {
                    setEditActive()
                    ctnLayoutList = viewCtn.uiTayListSpinner(
                        viewTop = viewTop,
                        list = listOptionLayout,
                        listCustom = listOptionCustomLayout,
                        positions = Pair(positionSelectedLayout,typeBottomLayout),
                        itemCustom = listOptionCustomLayout.isNotEmpty(),
                         onClickContent = {
                            ctnLayoutList = null
                            setEditDefault()
                        }
                    ) {
                        setEditDefault()
                        listener.onItemClick(it)
                        ctnLayoutList = null
                    }

                } else {
                    setEditDefault()
                    viewCtn.removeView(ctnLayoutList)
                    ctnLayoutList = null
                }
            }
        }
    }

    fun setTypeFaceTay(typeface : Typeface? = ResourcesCompat.getFont(context, R.font.ui_tay_montserrat_medium)) {
        ediText.typeface = typeface
    }

    fun setFilterTay(inputFilterList : Array<InputFilter>){
        ediText.filters = inputFilterList
    }

    fun setOnFocusTayEditListener(listener: TayEditLayoutFocusListener) {
        ediText.setOnFocusChangeListener { _, isFocused ->
            listener.onClick(isFocused)

        }
    }

    fun setOnKeyCodeTayEditLayout(listener: TayEditLayoutKeyCode) {
        ediText.setOnKeyListener { _, keyCode, _ ->
            listener.onCodeClick(keyCode)
            false
        }
    }

    fun updatePositionSelectedItem(value: Int) {
        positionSelectedLayout = value
    }
}

fun interface TayEditLayoutCLickListener {
    fun onClick(view: View)
}

fun interface TayEditLayoutChangeListener {
    fun onChange(value: String)
}

fun interface TayEditLayoutFocusListener {
    fun onClick(value: Boolean)
}

fun interface TayEditLayoutListCLickListener {
    fun onItemClick(item: Int)
}

fun interface TayEditLayoutIconCLickListener {
    fun onIconClick()
}

fun interface TayEditLayoutKeyCode {
    fun onCodeClick(code : Int)
}