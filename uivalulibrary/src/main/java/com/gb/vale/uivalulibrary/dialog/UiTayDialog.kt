package com.gb.vale.uivalulibrary.dialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Parcelable
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import com.gb.vale.uivalulibrary.R
import com.gb.vale.uivalulibrary.utils.UI_TAY_DIALOG_ACCEPT
import com.gb.vale.uivalulibrary.utils.UI_TAY_DIALOG_CANCEL
import com.gb.vale.uivalulibrary.utils.UI_TAY_DIALOG_SUB_TITLE
import com.gb.vale.uivalulibrary.utils.UI_TAY_DIALOG_TITLE
import com.gb.vale.uivalulibrary.utils.uiTayBgBorderStroke
import com.gb.vale.uivalulibrary.utils.uiTayInVisibility
import com.gb.vale.uivalulibrary.utils.uiTayParcelable
import com.gb.vale.uivalulibrary.utils.uiTayTryCatch
import com.gb.vale.uivalulibrary.utils.uiTayVisibility
import kotlinx.parcelize.Parcelize


typealias OnCLickUiTayDialog = ((Boolean)) -> Unit

class UiTayDialog : DialogFragment() {

   var func: OnCLickUiTayDialog = {}
    private var uiTayModelDialog: UiTayDialogModel = UiTayDialogModel()

    private lateinit var uiTayContentDialog : ConstraintLayout
    private lateinit var uiTayLinearVerticalDialog : LinearLayout
    private lateinit var uiTayIconClose : ImageView
    private lateinit var uiTayImageLogo : ImageView
    private lateinit var uiTayTextTitle : TextView
    private lateinit var uiTayTextSubTitle : TextView
    private lateinit var uiTayBtnAccept : TextView
    private lateinit var uiTayBtnCancel : TextView
    private var constraintSetCtn = ConstraintSet()


    companion object {
        fun newInstance(modelGeneric: UiTayDialogModel = UiTayDialogModel()): UiTayDialog =
            UiTayDialog().apply {
                arguments = Bundle().apply {
                    putParcelable(UiTayDialog::class.java.name, modelGeneric)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uiTayModelDialog = arguments?.uiTayParcelable(UiTayDialog::class.java.name) ?: UiTayDialogModel()
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val backgroundColor =
                ColorDrawable(ContextCompat.getColor(it.context, R.color.ui_tay_dialog_transparent))
            it.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            it.window?.setBackgroundDrawable(backgroundColor)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        uiTayConfigViewGeneral()
        constraintSetCtn.clone(uiTayContentDialog)
        uiTayPositionPrincipal()
        return uiTayContentDialog
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configActionDialog()
    }

    private fun configActionDialog() {
        uiTayBtnAccept.setOnClickListener {
            dismissCustom(true)
        }

        uiTayBtnCancel.setOnClickListener { dismissCustom(false) }
        uiTayIconClose.setOnClickListener { dismissCustom(false) }
        uiTayContentDialog.setOnClickListener {
            if (uiTayModelDialog.isCancel) {
                dismissCustom(false)
            }
        }
    }

    private fun dismissCustom(value: Boolean) {
        dialog?.dismiss()
        func.invoke(value)
    }

    private fun uiTayPositionPrincipal() {
        constraintSetCtn.connect(uiTayLinearVerticalDialog.id, ConstraintSet.TOP,
            ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        constraintSetCtn.connect(uiTayLinearVerticalDialog.id, ConstraintSet.START,
            ConstraintSet.PARENT_ID, ConstraintSet.START)
        constraintSetCtn.connect(uiTayLinearVerticalDialog.id, ConstraintSet.END,
            ConstraintSet.PARENT_ID, ConstraintSet.END)
        constraintSetCtn.connect(uiTayLinearVerticalDialog.id, ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        constraintSetCtn.applyTo(uiTayContentDialog)
    }

    private fun uiTayConfigViewGeneral(){
        uiTayContentDialog = ConstraintLayout(requireContext())
        uiTayLinearVerticalDialog = LinearLayout(requireContext())
        uiTayIconClose = ImageView(requireContext())
        uiTayImageLogo = ImageView(requireContext())
        uiTayTextTitle = TextView(requireContext())
        uiTayTextSubTitle = TextView(requireContext())
        uiTayBtnAccept = TextView(requireContext())
        uiTayBtnCancel = TextView(requireContext())
        uiTayContentDialog.id = View.generateViewId()
        uiTayLinearVerticalDialog.id = View.generateViewId()
        uiTayIconClose.id = View.generateViewId()
        uiTayImageLogo.id = View.generateViewId()
        uiTayTextTitle.id = View.generateViewId()
        uiTayTextSubTitle.id = View.generateViewId()
        uiTayBtnAccept.id = View.generateViewId()
        uiTayBtnCancel.id = View.generateViewId()
        uiTayConfigContentView()
    }
    private fun uiTayConfigContentView(){
        val ctnGeneralLayout =
            ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT)
        uiTayContentDialog.layoutParams  = ctnGeneralLayout

        val ctnLinearLayout =
            ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT)
        uiTayLinearVerticalDialog.layoutParams  = ctnLinearLayout
        uiTayLinearVerticalDialog.uiTayBgBorderStroke(uiTayModelDialog.styleCustom.contentColorStroke,
            uiTayModelDialog.styleCustom.contentColorSolid,uiTayModelDialog.styleCustom.contentRadius)
        uiTayLinearVerticalDialog.setPadding(
            requireContext().resources.getDimensionPixelOffset(uiTayModelDialog.styleCustom.paddingContentDataHorizontal),
            requireContext().resources.getDimensionPixelOffset(uiTayModelDialog.styleCustom.paddingContentDataVertical),
            requireContext().resources.getDimensionPixelOffset(uiTayModelDialog.styleCustom.paddingContentDataHorizontal),
            requireContext().resources.getDimensionPixelOffset(uiTayModelDialog.styleCustom.paddingContentDataVertical)
        )
        uiTayContentDialog.setPadding(requireContext().resources.getDimensionPixelOffset
            (uiTayModelDialog.styleCustom.uiTayPaddingContentHorizontal),0,
            requireContext().resources.getDimensionPixelOffset
                (uiTayModelDialog.styleCustom.uiTayPaddingContentHorizontal),0)
        uiTayContentDialog.addView(uiTayLinearVerticalDialog)
        uiTayLinearVerticalDialog.orientation = LinearLayout.VERTICAL
        uiTayConfigViewIcon()
    }
    private fun uiTayConfigViewIcon(){
        val iconCloseParam =
            LinearLayout.LayoutParams(
                requireContext().resources.getDimensionPixelOffset(uiTayModelDialog.styleCustom.sizeIcon),
                requireContext().resources.getDimensionPixelOffset(uiTayModelDialog.styleCustom.sizeIcon)
            )
        iconCloseParam.gravity = Gravity.END

        val iconLogoParam =
            LinearLayout.LayoutParams(
                requireContext().resources.getDimensionPixelOffset(uiTayModelDialog.styleCustom.sizeLogo),
                requireContext().resources.getDimensionPixelOffset(uiTayModelDialog.styleCustom.sizeLogo))
        iconLogoParam.gravity = Gravity.CENTER

        uiTayIconClose.layoutParams = iconCloseParam
        uiTayImageLogo.layoutParams = iconLogoParam
        uiTayIconClose.uiTayInVisibility(uiTayModelDialog.isCancel)
        uiTayIconClose.setImageResource(uiTayModelDialog.styleCustom.imageClose)
        uiTayImageLogo.setImageResource(uiTayModelDialog.image)
        uiTayLinearVerticalDialog.addView(uiTayIconClose)
        uiTayLinearVerticalDialog.addView(uiTayImageLogo)
        uiTayConfigViewText()
    }
    private fun uiTayConfigViewText(){
        uiTayTryCatch {
            val typefaceTitle = ResourcesCompat.getFont(requireContext(), uiTayModelDialog.styleCustom.titleFont)
            val typefaceSubTitle = ResourcesCompat.getFont(requireContext(), uiTayModelDialog.styleCustom.subTitleFont)
            uiTayTextTitle.typeface = typefaceTitle
            uiTayTextSubTitle.typeface = typefaceSubTitle
        }
        val textTitleParam = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)

        textTitleParam.gravity = Gravity.CENTER
        uiTayTextTitle.gravity =  Gravity.CENTER

        val subTextTitleParam = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)

        uiTayTextSubTitle.gravity = Gravity.CENTER
        uiTayTextTitle.gravity =  Gravity.CENTER

        uiTayTextTitle.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            requireContext().resources.getDimensionPixelSize(uiTayModelDialog.styleCustom.sizeTextTitle).toFloat()
        )

        uiTayTextSubTitle.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            requireContext().resources.getDimensionPixelSize(uiTayModelDialog.styleCustom.sizeTextSubTitle).toFloat()
        )

        uiTayTextTitle.setTextColor(ContextCompat.getColor(requireContext(),uiTayModelDialog.styleCustom.titleColor))
        uiTayTextSubTitle.setTextColor(ContextCompat.getColor(requireContext(),uiTayModelDialog.styleCustom.subTitleColor))
        uiTayTextTitle.setPadding(0,
            requireContext().resources.getDimensionPixelOffset(uiTayModelDialog.styleCustom.paddingTopTitle),0,0)
        uiTayTextSubTitle.setPadding(0,
            requireContext().resources.getDimensionPixelOffset(uiTayModelDialog.styleCustom.paddingTopSubTitle),0,0)
        uiTayTextTitle.layoutParams = textTitleParam
        uiTayTextSubTitle.layoutParams = subTextTitleParam
        uiTayTextTitle.text = uiTayModelDialog.title
        uiTayTextSubTitle.text = uiTayModelDialog.subTitle
        uiTayLinearVerticalDialog.addView(uiTayTextTitle)
        uiTayLinearVerticalDialog.addView(uiTayTextSubTitle)
        uiTayConfigViewBtn()
    }

    private fun uiTayConfigViewBtn(){
        uiTayTryCatch {
            val typefaceBtnAccept = ResourcesCompat.getFont(requireContext(), uiTayModelDialog.styleCustom.buttonAcceptFond)
            val typefaceBtnCancel = ResourcesCompat.getFont(requireContext(), uiTayModelDialog.styleCustom.buttonCancelFond)
            uiTayBtnAccept.typeface = typefaceBtnAccept
            uiTayBtnCancel.typeface = typefaceBtnCancel
        }

        val btnAcceptParam = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            requireContext().resources.getDimensionPixelOffset(uiTayModelDialog.styleCustom.sizeHeightButton))

        btnAcceptParam.setMargins(0,
            requireContext().resources.getDimensionPixelOffset(uiTayModelDialog.styleCustom.marginTopBtnAccept),0,0)

        btnAcceptParam.gravity = Gravity.CENTER
        uiTayBtnAccept.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            requireContext().resources.getDimensionPixelSize(uiTayModelDialog.styleCustom.sizeTextBtnAccept).toFloat()
        )
        uiTayBtnAccept.text = uiTayModelDialog.buttonText
        uiTayBtnAccept.gravity = Gravity.CENTER
        uiTayBtnAccept.setTextColor(ContextCompat.getColor(requireContext(),uiTayModelDialog.styleCustom.buttonTextAcceptColor))
        uiTayBtnAccept.uiTayBgBorderStroke(uiTayModelDialog.styleCustom.btnAcceptStrokeColor,
            uiTayModelDialog.styleCustom.btnAcceptStrokeColor,uiTayModelDialog.styleCustom.btnRadius )

        val btnCancelParam = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            requireContext().resources.getDimensionPixelOffset(R.dimen.dim_tay_48))
        btnCancelParam.gravity = Gravity.CENTER
        uiTayBtnCancel.gravity = Gravity.CENTER
        btnCancelParam.setMargins(0,
            requireContext().resources.getDimensionPixelOffset(uiTayModelDialog.styleCustom.marginTopBtnCancel),0,0)
        uiTayBtnCancel.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            requireContext().resources.getDimensionPixelSize(uiTayModelDialog.styleCustom.sizeTextBtnAccept).toFloat()
        )

        uiTayBtnCancel.text = uiTayModelDialog.buttonCancelText
        uiTayBtnCancel.setTextColor(ContextCompat.getColor(requireContext(),uiTayModelDialog.styleCustom.buttonCancelTextColor))
        uiTayBtnCancel.uiTayBgBorderStroke(uiTayModelDialog.styleCustom.btnCancelStrokeColor,
            uiTayModelDialog.styleCustom.btnCancelSolidColor,uiTayModelDialog.styleCustom.btnRadius )
        uiTayBtnCancel.uiTayVisibility(uiTayModelDialog.btnCancel)
        uiTayBtnAccept.layoutParams = btnAcceptParam
        uiTayBtnCancel.layoutParams = btnAcceptParam
        uiTayLinearVerticalDialog.addView(uiTayBtnAccept)
        uiTayLinearVerticalDialog.addView(uiTayBtnCancel)

    }


}

@Parcelize
data class UiTayDialogModel(
    val image: Int = R.drawable.ui_tay_ic_info,
    val title: String = UI_TAY_DIALOG_TITLE,
    val subTitle: String = UI_TAY_DIALOG_SUB_TITLE,
    val buttonText: String = UI_TAY_DIALOG_ACCEPT,
    val buttonCancelText: String = UI_TAY_DIALOG_CANCEL,
    val btnCancel: Boolean = false,
    val isCancel: Boolean = true,
    val styleCustom :UiTayDialogModelCustom=UiTayDialogModelCustom()
) : Parcelable

@Parcelize
data class UiTayDialogModelCustom(
    val imageClose: Int = R.drawable.ui_tay_ic_close,
    val contentRadius: Int = R.dimen.dim_tay_20,
    val uiTayPaddingContentHorizontal: Int = R.dimen.dim_tay_48,
    @ColorRes val contentColorStroke: Int = R.color.ui_tay_white,
    @ColorRes val contentColorSolid: Int = R.color.ui_tay_white,
    @ColorRes val titleColor: Int = R.color.ui_tay_black,
    @ColorRes val subTitleColor: Int = R.color.ui_tay_black,
    @ColorRes val buttonTextAcceptColor: Int = R.color.ui_tay_white,
    @ColorRes val buttonCancelTextColor: Int = R.color.tay_color_general,
    @ColorRes val btnAcceptStrokeColor: Int = R.color.tay_color_general,
    @ColorRes val btnAcceptSolidColor: Int = R.color.tay_color_general,
    @ColorRes val btnCancelStrokeColor: Int = R.color.tay_color_general,
    @ColorRes val btnCancelSolidColor: Int = R.color.ui_tay_white,
    val btnRadius: Int = R.dimen.dim_tay_28,
    val titleFont: Int = R.font.ui_tay_montserrat_semi_bold,
    val subTitleFont: Int = R.font.ui_tay_montserrat_medium,
    val buttonAcceptFond: Int = R.font.ui_tay_montserrat_semi_bold,
    val buttonCancelFond: Int = R.font.ui_tay_montserrat_semi_bold,
    val sizeIcon: Int = R.dimen.dim_tay_24,
    val sizeLogo: Int = R.dimen.dim_tay_48,
    val sizeTextTitle: Int = R.dimen.dim_tay_sp_text_18,
    val sizeTextSubTitle: Int = R.dimen.dim_tay_sp_text_14,
    val sizeTextBtnAccept: Int = R.dimen.dim_tay_sp_text_14,
    val sizeTextBtnCancel: Int = R.dimen.dim_tay_sp_text_14,
    val sizeHeightButton: Int = R.dimen.dim_tay_40,
    val paddingTopTitle: Int = R.dimen.dim_tay_8,
    val paddingTopSubTitle: Int = R.dimen.dim_tay_8,
    val marginTopBtnAccept: Int = R.dimen.dim_tay_20,
    val marginTopBtnCancel: Int = R.dimen.dim_tay_16,
    val paddingContentDataHorizontal: Int = R.dimen.dim_tay_16,
    val paddingContentDataVertical: Int = R.dimen.dim_tay_20
) : Parcelable