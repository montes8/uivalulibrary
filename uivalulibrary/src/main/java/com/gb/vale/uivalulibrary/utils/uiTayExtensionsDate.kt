package com.gb.vale.uivalulibrary.utils

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.text.format.DateFormat
import androidx.appcompat.app.AppCompatActivity
import com.gb.vale.uivalulibrary.R
import com.gb.vale.uivalulibrary.date.UiTayDatePickerSpinner
import com.gb.vale.uivalulibrary.date.UiTayModelDatePicker
import com.gb.vale.uivalulibrary.date.uiTayDatePickerBasic
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

/** convert a long to a text in stable date format*/
fun Long.uiTayFormatLongToString(format : String = UI_TAY_FORMAT_DATE):String{
    return DateFormat.format(format, Date(this)).toString()
}
/** convert a long to a date in stable date format*/
fun Long.uiTayFormatLongToDate():Date{
    return Date(this)
}

/** convert a date to a text in stable date format*/
@SuppressLint("SimpleDateFormat")
fun String.uiTayFormatDateStrint(formatInit:String = UI_TAY_FORMAT_DATE, formatEnd:String = UI_TAY_FORMAT_DATE,locale : Locale? = null):String{
    val formatCurrent = SimpleDateFormat(formatInit,locale?: Locale.getDefault())
    val dateCurrent = formatCurrent.parse(this)
    val formatUpdate = SimpleDateFormat(formatEnd,locale?: Locale.getDefault())
    return dateCurrent?.let { formatUpdate.format(it) }?:""
}


/** convert a string to a calendar*/
fun String.uiTayStringToCalendar(format : String = UI_TAY_FORMAT_DATE,locale : Locale? = null):Calendar{
    val cal = Calendar.getInstance()
    val sdf = SimpleDateFormat(format, locale?: Locale.getDefault())
    return try {
        sdf.parse(this)?.let { cal.time = it }
        cal
    } catch (e: ParseException) {
        e.printStackTrace()
        Calendar.getInstance()
    }

}

fun String.uiTayStringToDate(format : String = UI_TAY_FORMAT_DATE,locale : Locale? = null):Date{
    val sdf = SimpleDateFormat(format,locale?: Locale.getDefault())
    return try {
         sdf.parse(this)?:Date()
    } catch (e: ParseException) {
        e.printStackTrace()
        Date()
    }
}

fun Date.uiTayDateToCalendar():Calendar{
    val cal = Calendar.getInstance()
    return try {
        cal.time = this
        cal
    } catch (e: ParseException) {
        e.printStackTrace()
        Calendar.getInstance()
    }

}

fun Date.uiTayDateToString(format : String = UI_TAY_FORMAT_DATE,locale: Locale? = null):String{
    return try {
        val sdf = SimpleDateFormat(format, locale?:Locale.getDefault())
        sdf.format(this)
    } catch (e: ParseException) {
        e.printStackTrace()
        UI_TAY_EMPTY
    }
}


fun Calendar.uiTayCalendarToString(format : String = UI_TAY_FORMAT_DATE,locale: Locale? = null):String{
    val sdf = SimpleDateFormat(format, locale?:Locale.getDefault())
    return try {
        sdf.format(this.time)
    } catch (e: ParseException) {
        e.printStackTrace()
        UI_TAY_EMPTY
    }
}

fun Calendar.uiTayCalendarToDate():Date{
    return try {
        this.time
    } catch (e: ParseException) {
       Date()
    }
}

/** is obtained on the first and last day of the current month*/
fun uiTayGetInitEndMonths(calendar : Calendar = Calendar.getInstance(),formatCurrent: String = UI_TAY_FORMAT_DATE,locale: Locale? = null): Pair<String,String> {
    val format = SimpleDateFormat(formatCurrent, locale?:Locale.getDefault())
    val dateStar = format.format(uiTaySetTimeDate(Calendar.DATE, 1,calendar))
    val dateEnd = format.format(uiTaySetTimeDate(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH)))
    return Pair(dateStar,dateEnd)
}

/**calculate the days between two dates*/
@SuppressLint("SimpleDateFormat")
fun uiTayGetRangeDaysDate(start : String,end : String,format: String = UI_TAY_FORMAT_DATE,locale: Locale? = null):Long {
    try {
        val dateFormat = SimpleDateFormat(format,locale?:Locale.getDefault())
        var dateInit: Date? = null
        var dateEnd: Date? = null
        dateFormat.parse(start)?.let { dateInit = it}
        dateFormat.parse(end)?.let { dateEnd = it }
        dateInit?.let {init->
            dateEnd?.let { end->
                return ((end.time - init.time) / 86400000)
            }
        }
        return  0
    }catch (e:Exception){
        return  0
    }
}

/**returns an array of dates going backwards according to a range*/
fun uiTayGetListMonthsReverse(range:Int = 3,formatCurrent : String = UI_TAY_FORMAT_MONTH,locale: Locale? = null): List<String> {
    val calendar = Calendar.getInstance()
    val format = SimpleDateFormat(formatCurrent,locale?: Locale.getDefault())
    val monthsName = mutableListOf<String>()
    for (i in 0 until range) {
        val monthName = format.format(calendar.time)
        monthsName.add(monthName)
        calendar.add(Calendar.MONTH, -1)
    }
    return monthsName
}

/** returns an array of dates going forward according to a range*/
fun uiTayGetListMonths(range:Int = 3,formatCurrent : String = UI_TAY_FORMAT_MONTH,locale: Locale? = null): List<String> {
    val calendar = Calendar.getInstance()
    val format = SimpleDateFormat(formatCurrent, locale?:Locale.getDefault())
    val monthsName = mutableListOf<String>()
    for (i in 0 until range) {
        val monthName = format.format(calendar.time)
        monthsName.add(monthName)
        calendar.add(Calendar.MONTH, 1)
    }
    return monthsName
}

@SuppressLint("SimpleDateFormat")
fun String.uiTayFormatTwelveHour():String{
    return try {
        val formatHour = SimpleDateFormat("hh:mm:ss")
        val hourCurrent: Date = formatHour.parse(this) as Date
        val converter = SimpleDateFormat("hh:mm a")
        converter.format(hourCurrent)
    } catch (e: ParseException) {
        UI_TAY_EMPTY
    }
}


fun AppCompatActivity.uiTayShowDatePickerSpinner(uiModelDP:UiTayModelDatePicker=UiTayModelDatePicker(),
                                             onSuccessDate: ((Pair<Date,String>) -> Unit)? = null){
    val pd = UiTayDatePickerSpinner.newInstance(uiModelDP)
    pd.show(supportFragmentManager, UiTayDatePickerSpinner::class.java.name)
    pd.uiTayClickDatePicker={
        onSuccessDate?.invoke(it)
    }
}


fun AppCompatActivity.uiTayShowUiTayDatePicker(uiModelDP:UiTayModelDatePicker=UiTayModelDatePicker(),
                                      onSuccessDate: ((Pair<Date,String>) -> Unit)? = null){
    uiTayDatePickerBasic(uiModelDP) {onSuccessDate?.invoke(it)}
}


fun AppCompatActivity.uiTayShowDateMultiple(title:String = "Selecione fecha",format:String = UI_TAY_FORMAT_DATE,
                                        onSuccessDate: ((Pair<String,String>) -> Unit)? = null){
    val dateRangePicker =
        MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText(title)
            .setSelection(
                androidx.core.util.Pair(
                    MaterialDatePicker.thisMonthInUtcMilliseconds(),
                    MaterialDatePicker.todayInUtcMilliseconds()
                )
            )
            .build()

    dateRangePicker.addOnPositiveButtonClickListener {
        onSuccessDate?.invoke(Pair(it.first.uiTayFormatLongToString(format),
            it.second.uiTayFormatLongToString(format)))
    }

    dateRangePicker.show(supportFragmentManager,"uiTayDateMultiple")
}


fun Context.showUiTayHour(actionTime: ((String) -> Unit)? = null){
    val timePickerDialog = TimePickerDialog(
        this, R.style.UITayTimePickerBasic,
        { _, hourOfDay, minute ->
            actionTime?.invoke("$hourOfDay:$minute:00")
        },
        uiTayGetTimeDate(Calendar.HOUR_OF_DAY),
        uiTayGetTimeDate(Calendar.MINUTE),
        false
    )

    timePickerDialog.show()
}


fun millisecondToDate(t: Long,locale: Locale? = null): String {
    var i = t
    i /= 1000 /*from   ww w .  j  a v  a  2  s .co  m*/
    var minute = i / 60
    val hour = minute / 60
    val second = i % 60
    minute %= 60
    return if (hour <= 0) String.format(
        locale?:Locale.getDefault(), "%02d:%02d", minute,
        second
    ) else String.format(
        locale?:Locale.getDefault(), "%02d:%02d:%02d",
        hour, minute, second
    )
}

fun Context.showUiTayHourSpinner(actionTime: ((String) -> Unit)? = null){
    val timePickerDialog = TimePickerDialog(
        this, R.style.UITayTimePickerSpinner,
        { _, hourOfDay, minute ->
            actionTime?.invoke("$hourOfDay:$minute:00")
        },
        uiTayGetTimeDate(Calendar.HOUR_OF_DAY),
        uiTayGetTimeDate(Calendar.MINUTE),
        false
    )
    timePickerDialog.show()
}

fun uiTayTimePickerCount(time: Long =30000, formatTime: Int = 3,actionUpdate: ((String) -> Unit)? = null,
                          actionFinish: (() -> Unit)? = null) {
    Handler(Looper.getMainLooper()).post {
        val timer = object: CountDownTimer(time, 1000) {
            @SuppressLint("StringFormatMatches", "DefaultLocale")
            override fun onTick(millisUntilFinished: Long) {
                val hour = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
                val minute = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished))
                val second = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
                val timeString :String = when(formatTime){
                    0->{
                        "${uiTayTwoDigitNumber(minute)}:${uiTayTwoDigitNumber(second)}"}
                    1->{ uiTayTwoDigitNumber(second) }
                    else->{
                        "${uiTayTwoDigitNumber(hour)}:${uiTayTwoDigitNumber(minute)}:${uiTayTwoDigitNumber(second)}"}
                }

                actionUpdate?.invoke(timeString)
            }

            override fun onFinish() {
                actionFinish?.invoke()
            }
        }
        timer.start()
    }
}

/**
 *+ one Calendar.MONDAY
 * * one Calendar.TUESDAY
 * * one Calendar.WEDNESDAY
 * * one Calendar.THURSDAY
 * * one Calendar.THURSDAY
 * * one Calendar.SATURDAY
 * * one Calendar.SUNDAY
 * **/

@SuppressLint("SimpleDateFormat")
fun uiTayGetDayName(day :Int = Calendar.MONDAY,format:String = UI_TAY_FORMAT_NAME_DAY,locale : Locale? = null):String{
    return SimpleDateFormat(format,locale?:Locale.getDefault()).format(uiTaySetTimeDate(Calendar.DAY_OF_WEEK, day))
}

fun uiTayGetTimeDate(value : Int = Calendar.HOUR_OF_DAY,calendar : Calendar = Calendar.getInstance()) =
    calendar.get(value)

fun uiTaySetTimeDate(key : Int = Calendar.DAY_OF_WEEK,value :Int = Calendar.MONDAY,calendar : Calendar = Calendar.getInstance()):Date{
    calendar.set(key, value)
    return calendar.time
}

fun uiTaySetListTimeDate(value : List<Int> = arrayListOf(), key :List<Int> = arrayListOf(Calendar.YEAR,Calendar.MONTH,
    Calendar.DAY_OF_MONTH), calendar : Calendar = Calendar.getInstance()):Date{
   try {
       key.forEachIndexed { index, i ->
           calendar.set(i, value[index])
       }
       return calendar.time
   }catch (e:Exception){
       return  Date()
   }
}
