package com.gb.vale.uivalulibrary.label

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import com.gb.vale.uivalulibrary.R
import com.gb.vale.uivalulibrary.utils.UI_TAY_DESCRIPTION_DEFAULT_EDIT
import com.gb.vale.uivalulibrary.utils.UI_TAY_EMPTY
import com.gb.vale.uivalulibrary.utils.uiTayBgBorderStroke
import com.gb.vale.uivalulibrary.utils.uiTayFilterOnlyLetterAndNumber
import com.gb.vale.uivalulibrary.utils.uiTayFilterOnlyNumbers
import com.gb.vale.uivalulibrary.utils.uiTayFilterSpaces
import com.gb.vale.uivalulibrary.utils.uiTayHandler
import com.gb.vale.uivalulibrary.utils.uiTayHideKeyboard
import com.google.android.material.textfield.TextInputEditText


typealias UiTayOtpChangeListener = (Pair<Boolean,String>)  -> Unit
typealias UITayOtpKeyListener = (Int) -> Unit

class UiTayOtpCode @JvmOverloads constructor(
    context: Context,
    private val attrs: AttributeSet? = null,
    defaultStyle: Int = 0
) : ConstraintLayout(context, attrs, defaultStyle) {

    var uiTayOtpChangeListener: UiTayOtpChangeListener = {}
    var uiTayOtpKeyListener: UITayOtpKeyListener = {}

    private fun pixelsToSp(px: Float) = px / context.resources.displayMetrics.scaledDensity

    private var sizeText = 6
    private val allEditViewList = arrayListOf<TextInputEditText>()
    private var itemWidth = context.resources.getDimensionPixelSize(R.dimen.dim_tay_otp_width)
    private var itemHeight = context.resources.getDimensionPixelSize(R.dimen.dim_tay_otp_height)
    private var flagTextEmpty = true
    private var uiTayOtpTextDefault = UI_TAY_EMPTY
    private var uiTayHint = UI_TAY_EMPTY
    private var uiTayInput = InputType.TYPE_CLASS_NUMBER
    private var uiTayContentDescription = UI_TAY_DESCRIPTION_DEFAULT_EDIT
    private var uiTayError = false
    private var uiTayEnable = true
    private var uiTayCursor = true
    private var uiTayFocus = true
    var uiTayOtpText: String = UI_TAY_EMPTY
        set(value) {
            field = value
            uiTayOtpTextDefault = value
            uiTaySetText()
        }
        get() = getUIText()

    var uiTayOtpHint: String = UI_TAY_EMPTY
        set(value) {
            field = value
            uiTayHint = value
            uiTaySetHint()
        }

    var uiTayOtpError: Boolean = false
        set(value) {
            field = value
            allEditViewList.forEach {
                if (value) uiTaySetBackgroundError(it)else uiTaySetBackgroundDefault(it)
            }
        }

    var uiTayOtpEnable: Boolean = true
        set(value) {
            field = value
            allEditViewList.forEach {
                it.isEnabled = value
                if (value) uiTaySetBackgroundDefault(it)else uiTaySetBackgroundDisable(it)
            }
        }

    var uiTayOtpFocusable: Boolean = true
        set(value) {
            field = value
            setFocusEnable(value)
        }

    var uiTayOtpCursorVisible: Boolean = false
        set(value) {
            field = value
            setCursorEnable(value)
        }

    var uiTayOtpSize: Int = 6
        set(value) {
            field = value
            sizeText = value
        }

    var uiTayOtpInputType: Int = InputType.TYPE_CLASS_TEXT
        set(value) {
            field = value
            uiTayInput = value
            allEditViewList.forEach{
                it.inputType = value
            }
        }


    var uiTayOtpContentDescription: String = UI_TAY_EMPTY
        set(value) {
            field = value
            if (value.isNotEmpty())
                allEditViewList.forEachIndexed { index, it ->
                    it.contentDescription = "$value$index"
                }
        }

    init {
        loadAttributes()
        initEditView()
        setConfigViewTwo()
    }

    private fun setConfigViewTwo(){
            uiTayOtpEnable = uiTayEnable
            uiTayOtpFocusable = uiTayFocus
            uiTayOtpCursorVisible = uiTayCursor
            uiTayOtpContentDescription = uiTayContentDescription
            uiTayOtpText = uiTayOtpTextDefault
            uiTayOtpHint = uiTayHint
    }

    private fun loadAttributes() {
        val attributeSet = context.obtainStyledAttributes(attrs, R.styleable.UiTayOtpCode)
        attributeSet.apply {
            uiTayOtpInputType = getInt(R.styleable.UiTayOtpCode_android_inputType, InputType.TYPE_CLASS_TEXT)
            uiTayHint = getString(R.styleable.UiTayOtpCode_uiTayOtpHint) ?: context.getString(R.string.ui_tay_otp_default_hint)
            uiTayOtpTextDefault = getString(R.styleable.UiTayOtpCode_uiTayOtpText) ?: UI_TAY_EMPTY
            uiTayOtpSize = getInteger(R.styleable.UiTayOtpCode_uiTayOtpSize, 6)
            uiTayEnable = getBoolean(R.styleable.UiTayOtpCode_uiTayOtpEnable, true)
            uiTayError =  getBoolean(R.styleable.UiTayOtpCode_uiTayOtpError, false)
            uiTayCursor = getBoolean(R.styleable.UiTayOtpCode_uiTayOtpCursorVisible, false)
            uiTayFocus = getBoolean(R.styleable.UiTayOtpCode_uiTayOtpFocusable, true)
            uiTayContentDescription = getString(R.styleable.UiTayOtpCode_uiTayOtpContentDescription) ?: UI_TAY_DESCRIPTION_DEFAULT_EDIT
        }
        attributeSet.recycle()
    }


    private fun initEditView() {
        allEditViewList.clear()
        val uiTayTypeface = ResourcesCompat.getFont(context, R.font.ui_tay_montserrat_semi_bold)
        for (value in 1..sizeText) {
            val mText = TextInputEditText(context).apply {
                id = View.generateViewId()
                gravity = Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL
                layoutParams = LayoutParams(itemWidth, itemHeight)
                isSingleLine = true
                isCursorVisible = true
                typeface = uiTayTypeface
                textSize = pixelsToSp(context.resources.getDimensionPixelSize(R.dimen.dim_tay_otp_sp_text).toFloat())
                setHintTextColor(ContextCompat.getColor(context, R.color.ui_tay_otp_text_hin))
                filters = if (uiTayInput == InputType.TYPE_CLASS_NUMBER) arrayOf(
                   InputFilter.LengthFilter(1),
                    InputFilter.AllCaps(),
                    uiTayFilterSpaces,
                    uiTayFilterOnlyNumbers
                ) else arrayOf(InputFilter.LengthFilter(1), InputFilter.AllCaps(),
                    uiTayFilterSpaces, uiTayFilterOnlyLetterAndNumber)
                uiTaySetBackgroundDefault(this)
                configureEditTextFocusClick(this)
                configNextItem(this,value)
                configKeyCode(this,value)
            }
            allEditViewList.add(mText)
            this.addView(mText)
        }
        setViews(allEditViewList)
    }

    private fun configNextItem(edit :TextInputEditText,value :Int){
        edit.addTextChangedListener {
            uiTaySetBackgroundDefault(edit)
            if (flagTextEmpty && it.toString().isNotEmpty()) {
                if (value < sizeText) {
                    if (this.focusSearch(View.FOCUS_RIGHT) != null) {
                        this.focusSearch(View.FOCUS_RIGHT).requestFocus()
                    } else {
                        edit.setText(UI_TAY_EMPTY)
                    }
                }
            } else {
                flagTextEmpty = true
            }
            if (flagTextEmpty)uiTayOtpChangeListener(Pair(value == sizeText,getUITextInside(edit,value)))
        }
    }

    private fun configKeyCode(edit :TextInputEditText,value :Int){
        this.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_DEL && edit.text.isNullOrEmpty() && value > 1) {
                val view = this.focusSearch(View.FOCUS_LEFT) as TextInputEditText
                view.requestFocus()
                uiTaySetBackgroundDefault(edit)
            }
            uiTayOtpKeyListener(keyCode)
            false
        }
    }

    private fun configureEditTextFocusClick(editText: TextInputEditText){
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                uiTaySetBackgroundSelected(editText)
            }else{
                uiTaySetBackgroundDefault(editText)
            }
            uiTayHandler(10) {
                editText.setSelection(editText.text?.length ?: 0)
            }
        }
    }

    private fun setViews(list: List<View>) {
        val set = ConstraintSet()
        set.clone(this)
        list.forEachIndexed { index, view ->
            set.apply {
                connect(view.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
                connect(view.id,
                    ConstraintSet.START,
                    if (index == 0) ConstraintSet.PARENT_ID else list[index.minus(1)].id,
                    if (index == 0) ConstraintSet.START else ConstraintSet.END
                )
                connect(view.id, ConstraintSet.END,
                    if (index == list.size.minus(1)) ConstraintSet.PARENT_ID else list[index.plus(
                        1
                    )].id,
                    if (index == list.size.minus(1)) ConstraintSet.END else ConstraintSet.START
                )
                connect(view.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            }
        }
        set.applyTo(this)
    }

    private fun uiTaySetBackgroundDefault(editText: TextInputEditText) {
        editText.setTextColor(ContextCompat.getColor(context, R.color.ui_tay_otp_text_enable))
        editText.uiTayBgBorderStroke(R.color.ui_tay_otp_bg_stroke_enable,R.color.ui_tay_otp_bg_solid_enable,
            R.dimen.dim_tay_otp_bg_radius)

    }

    private fun getUIText(): String {
        var text = UI_TAY_EMPTY
        allEditViewList.forEach {
            text += it.text.toString()
        }
        return text.trim()
    }

    private fun uiTaySetText() {
        if (uiTayOtpTextDefault.length == allEditViewList.size){
            allEditViewList.forEachIndexed { index, editText ->
                editText.setText(uiTayOtpTextDefault[index].toString())
            }
        }
    }

    private fun uiTaySetHint() {
        if (uiTayHint.length >=  allEditViewList.size){
            allEditViewList.forEachIndexed { index, editText ->
                editText.hint = uiTayHint[index].toString()
            }
        }

    }

    private fun getUITextInside(editText: TextInputEditText,index : Int): String {
        val inputDataText = getUIText()
        if(index >= sizeText && editText.text.toString().isNotEmpty()){
            clearFocusInputs()
            editText.uiTayHideKeyboard()
        }

        return inputDataText
    }

    private fun clearFocusInputs(){
        allEditViewList.forEach {
            it.clearFocus()
        }
    }

    fun cleanText() {
        flagTextEmpty = false
        allEditViewList.forEach { it.setText(UI_TAY_EMPTY) }
        allEditViewList[0].requestFocus()
        flagTextEmpty = true
    }


    private fun uiTaySetBackgroundSelected(
        editText: TextInputEditText
    ) {
        editText.setTextColor(ContextCompat.getColor(context, R.color.ui_tay_otp_text_selected))
        editText.uiTayBgBorderStroke(R.color.ui_tay_bg_otp_stroke_selected,R.color.ui_tay_bg_otp_solid_selected,
            R.dimen.dim_tay_otp_bg_radius)

    }


    private fun uiTaySetBackgroundDisable(
        editText: TextInputEditText
    ) {
        editText.setTextColor(ContextCompat.getColor(context, R.color.ui_tay_otp_text_disable))
        editText.uiTayBgBorderStroke(R.color.ui_tay_bg_otp_stroke_disable,R.color.ui_tay_bg_otp_solid_disable,
            R.dimen.dim_tay_otp_bg_radius)

    }

    private fun uiTaySetBackgroundError(
        editText: EditText
    ) {

        editText.setTextColor(ContextCompat.getColor(context, R.color.ui_tay_otp_text_error))
        editText.uiTayBgBorderStroke(R.color.ui_tay_bg_otp_stroke_error,R.color.ui_tay_bg_otp_solid_error,
            R.dimen.dim_tay_otp_bg_radius)
    }
    fun setCursorEnable(value: Boolean){
        allEditViewList.forEach {
            it.isCursorVisible = value
        }
    }
    fun setFocusEnable(value: Boolean){
        allEditViewList.forEach {
            it.isFocusable = value
        }
    }

    fun setFilters(inputFilterList: Array<InputFilter>) {
        allEditViewList.forEach {
            it.filters = inputFilterList
        }

    }
}
