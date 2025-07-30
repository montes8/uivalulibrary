package com.gb.vale.uivalulibrary.date

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Parcelable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.gb.vale.uivalulibrary.R
import com.gb.vale.uivalulibrary.date.UiTayDatePickerSpinner.Companion.FORMAT_DATE_UI_TAY_DEFAULT
import com.gb.vale.uivalulibrary.date.UiTayDatePickerSpinner.Companion.UI_TAY_TYPE_DP_FULL
import com.gb.vale.uivalulibrary.utils.uiTayBgBorderStroke
import com.gb.vale.uivalulibrary.utils.uiTayDateToString
import com.gb.vale.uivalulibrary.utils.uiTayGetTimeDate
import com.gb.vale.uivalulibrary.utils.uiTayParcelable
import com.gb.vale.uivalulibrary.utils.uiTaySetListTimeDate
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


typealias UITayClickDatePicker = (Pair<Date, String>) -> Unit

@Suppress("DEPRECATION")
class UiTayDatePickerSpinner : DialogFragment() {

    private var uiModelDP: UiTayModelDatePicker = UiTayModelDatePicker()
    private lateinit var selectedDate: Date
    var uiTayClickDatePicker: UITayClickDatePicker = {}

    private lateinit var uiTayContentGeneralDatePicker : ConstraintLayout
    private lateinit var uiTayLinearVertical : LinearLayout
    private lateinit var uiTayLinearHorizontal : LinearLayout
    private lateinit var uiTayDatePicker : DatePicker
    private lateinit var uiTayBtnAccept : Button
    private lateinit var uiTayBtnCancel : Button
    private var constraintSetCtn = ConstraintSet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uiModelDP = arguments?.uiTayParcelable(UiTayDatePickerSpinner::class.java.name)
            ?: UiTayModelDatePicker()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        uiTayConfigView()
        return uiTayContentGeneralDatePicker
    }

    @SuppressLint("DiscouragedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectedDate = uiModelDP.dateSelected.time
        uiModelDP.dateMin?.let {
            uiTayDatePicker.minDate = it.timeInMillis
        }
        uiModelDP.dateMax?.let {
            uiTayDatePicker.maxDate = it.timeInMillis
        }

       when (uiModelDP.type) {
            UI_TAY_TYPE_DP_MONTH_YEAR -> {
                uiTayDatePicker.findViewById<View>(
                    Resources.getSystem().getIdentifier("day", "id", "android")
                ).visibility = View.GONE
            }
            UI_TAY_TYPE_DP_YEAR -> {
                uiTayDatePicker.findViewById<View>(
                    Resources.getSystem().getIdentifier("day", "id", "android")
                ).visibility = View.GONE
                uiTayDatePicker.findViewById<View>(
                    Resources.getSystem().getIdentifier("month", "id", "android")
                ).visibility = View.GONE
            }

            UI_TAY_TYPE_DP_MONTH -> {
                uiTayDatePicker.findViewById<View>(
                    Resources.getSystem().getIdentifier("day", "id", "android")
                ).visibility = View.GONE
                uiTayDatePicker.findViewById<View>(
                    Resources.getSystem().getIdentifier("year", "id", "android")
                ).visibility = View.GONE
            }

        }


        uiTayDatePicker.init(
            uiTayGetTimeDate(Calendar.YEAR,uiModelDP.dateSelected),
            uiTayGetTimeDate(Calendar.MONTH,uiModelDP.dateSelected),
            uiTayGetTimeDate(Calendar.DAY_OF_MONTH,uiModelDP.dateSelected)
        ) { _, year, month, dayOfMonth ->
            selectedDate = uiTaySetListTimeDate(arrayListOf(year,month,dayOfMonth))
        }

        uiTayBtnAccept.setOnClickListener {
            uiTayClickDatePicker.invoke(
                Pair(
                    selectedDate,
                    selectedDate.uiTayDateToString(uiModelDP.format)
                )
            )
            dismiss()
        }
        uiTayBtnCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun uiTayConfigContent(){
        uiTayContentGeneralDatePicker = ConstraintLayout(requireContext())
        uiTayLinearVertical = LinearLayout(requireContext())
        uiTayLinearHorizontal = LinearLayout(requireContext())
        uiTayDatePicker = DatePicker(requireContext(),null,R.style.UITayDatePicker)
        uiTayBtnAccept = Button(requireContext())
        uiTayBtnCancel = Button(requireContext())
        uiTayContentGeneralDatePicker.id = View.generateViewId()
        uiTayLinearVertical.id = View.generateViewId()
        uiTayLinearHorizontal.id = View.generateViewId()
        uiTayDatePicker.id = View.generateViewId()
        uiTayBtnAccept.id = View.generateViewId()
        uiTayBtnCancel.id = View.generateViewId()
        val ctnGeneralLayout =
            ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT)
        uiTayContentGeneralDatePicker.layoutParams = ctnGeneralLayout
        uiTayContentGeneralDatePicker.setBackgroundColor(requireContext().getColor(R.color.ui_tay_dp_transparent))


        val ctnLayout =
            ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        uiTayLinearVertical.layoutParams = ctnLayout
        uiTayLinearVertical.uiTayBgBorderStroke(R.color.tay_bg_dp_stroke,
            R.color.tay_bg_dp_stroke
            ,R.dimen.dim_tay_bg_radius_dp)

        val ctnHLayout =
            ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        uiTayLinearHorizontal.layoutParams = ctnHLayout

        uiTayLinearVertical.orientation = LinearLayout.VERTICAL
        uiTayLinearHorizontal.orientation = LinearLayout.HORIZONTAL

        uiTayLinearVertical.setPadding(requireContext().resources.getDimensionPixelOffset(R.dimen.dim_tay_16),
            requireContext().resources.getDimensionPixelOffset(R.dimen.dim_tay_16),
            requireContext().resources.getDimensionPixelOffset(R.dimen.dim_tay_16),
            requireContext().resources.getDimensionPixelOffset(R.dimen.dim_tay_16))
        uiTayContentGeneralDatePicker.addView(uiTayLinearVertical)
    }


    private fun uiTayConfigView(){
        uiTayConfigContent()
        uiTayConfigViewDatePicker()
        constraintSetCtn.clone(uiTayContentGeneralDatePicker)
        uiTayPositionPrincipal()

    }
    private fun uiTayPositionPrincipal() {
        constraintSetCtn.connect(uiTayLinearVertical.id, ConstraintSet.TOP,
            ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        constraintSetCtn.connect(uiTayLinearVertical.id, ConstraintSet.START,
            ConstraintSet.PARENT_ID, ConstraintSet.START)
        constraintSetCtn.connect(uiTayLinearVertical.id, ConstraintSet.END,
            ConstraintSet.PARENT_ID, ConstraintSet.END)
        constraintSetCtn.connect(uiTayLinearVertical.id, ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        constraintSetCtn.applyTo(uiTayContentGeneralDatePicker)
    }

    private fun uiTayConfigViewDatePicker(){
        val dpLayout =
            ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT)
        uiTayDatePicker.spinnersShown = true
        uiTayDatePicker.calendarViewShown = false
        uiTayDatePicker.layoutParams = dpLayout
        val dpBtnAccept = LinearLayout.LayoutParams( requireContext().resources.getDimensionPixelOffset(R.dimen.dim_tay_0),
                requireContext().resources.getDimensionPixelOffset(R.dimen.dim_tay_dp_height))
        dpBtnAccept.setMargins(requireContext().resources.getDimensionPixelOffset(R.dimen.dim_tay_8),0,
            requireContext().resources.getDimensionPixelOffset(R.dimen.dim_tay_4)
            ,requireContext().resources.getDimensionPixelOffset(R.dimen.dim_tay_8))

        val dpBtnCancel = LinearLayout.LayoutParams(
            requireContext().resources.getDimensionPixelOffset(R.dimen.dim_tay_0),
            requireContext().resources.getDimensionPixelOffset(R.dimen.dim_tay_dp_height)
        )

        dpBtnCancel.setMargins( requireContext().resources.getDimensionPixelOffset(R.dimen.dim_tay_4) ,0,
            requireContext().resources.getDimensionPixelOffset(R.dimen.dim_tay_8),requireContext().resources.getDimensionPixelOffset(R.dimen.dim_tay_8))

        dpBtnAccept.weight =1f
        dpBtnCancel.weight =1f
        uiTayBtnAccept.text = requireContext().getText(R.string.tay_ui_dp_accept)
        uiTayBtnCancel.text = requireContext().getText(R.string.tay_ui_dp_calcel)
        uiTayBtnAccept.setTextColor(requireContext().getColor(R.color.tay_dp_text_accept))
        uiTayBtnCancel.setTextColor(requireContext().getColor(R.color.tay_dp_text_cancel))
        uiTayBtnAccept.setTextSize( TypedValue.COMPLEX_UNIT_PX,
            requireContext().resources.getDimensionPixelSize(R.dimen.dim_tay_dp_sp_text).toFloat())
        uiTayBtnCancel.setTextSize( TypedValue.COMPLEX_UNIT_PX,
            requireContext().resources.getDimensionPixelSize(R.dimen.dim_tay_dp_sp_text).toFloat())
        uiTayBtnAccept.uiTayBgBorderStroke(R.color.tay_bg_btn_dp_accept_stroke,
            R.color.tay_bg_btn_dp_accept_solid,R.dimen.dim_tay_bg_btn_accept_radius_dp)
        uiTayBtnCancel.uiTayBgBorderStroke(R.color.tay_bg_btn_dp_cancel_stroke,
            R.color.tay_bg_btn_dp_cancel_solid,R.dimen.dim_tay_bg_btn_cancel_radius_dp)
        uiTayBtnAccept.layoutParams = dpBtnAccept
        uiTayBtnCancel.layoutParams = dpBtnCancel
        uiTayLinearVertical.addView(uiTayDatePicker)
        uiTayLinearVertical.addView(uiTayLinearHorizontal)
        uiTayLinearHorizontal.addView(uiTayBtnCancel)
        uiTayLinearHorizontal.addView(uiTayBtnAccept)

    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val backgroundColor =
                ColorDrawable(ContextCompat.getColor(it.context,R.color.ui_tay_dp_transparent))
            it.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            it.window?.setBackgroundDrawable(backgroundColor)
        }
    }

    companion object {

        const val UI_TAY_TYPE_DP_FULL = 0
        const val UI_TAY_TYPE_DP_MONTH = 1
        const val UI_TAY_TYPE_DP_YEAR = 2
        const val UI_TAY_TYPE_DP_MONTH_YEAR = 1
        const val FORMAT_DATE_UI_TAY_DEFAULT = "dd/MM/yyyy"

        fun newInstance(uiTayData: UiTayModelDatePicker = UiTayModelDatePicker()): UiTayDatePickerSpinner =
            UiTayDatePickerSpinner().apply {
                arguments = Bundle().apply {
                    putParcelable(UiTayModelDatePicker::class.java.name, uiTayData)
                }
            }
         }


}

@Parcelize
data class UiTayModelDatePicker(
    val dateSelected: Calendar = Calendar.getInstance(),
    val dateMin: Calendar? = null,
    val dateMax: Calendar? = null,
    val type: Int = UI_TAY_TYPE_DP_FULL,
    val format: String = FORMAT_DATE_UI_TAY_DEFAULT
) : Parcelable


@SuppressLint("SimpleDateFormat")
fun Context.uiTayDatePickerBasic(uiModelDP:UiTayModelDatePicker=UiTayModelDatePicker(),
                                 actionDate: ((date: Pair<Date,String>) -> Unit)? = null){
    val datePickerListener =
        DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
            try {
                val sdf = SimpleDateFormat(uiModelDP.format)
                val selectedDate = uiTaySetListTimeDate(arrayListOf(selectedYear,selectedMonth,selectedDay))
                val date: String = sdf.format(selectedDate)
                actionDate?.invoke(Pair(selectedDate,date))
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    val date = DatePickerDialog(
        this,R.style.UITayDatePickerBasic,
        datePickerListener,
        uiTayGetTimeDate(Calendar.YEAR),
        uiTayGetTimeDate(Calendar.MONTH),
        uiTayGetTimeDate(Calendar.DAY_OF_MONTH)
    )
    uiModelDP.dateMin?.let { date.datePicker.minDate = it.timeInMillis }
    uiModelDP.dateMax?.let { date.datePicker.maxDate = it.timeInMillis}
    date.show()
}