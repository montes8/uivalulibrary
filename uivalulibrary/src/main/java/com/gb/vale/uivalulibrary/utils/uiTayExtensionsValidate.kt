package com.gb.vale.uivalulibrary.utils

import android.annotation.SuppressLint
import android.content.Context
import android.telephony.TelephonyManager
import android.text.InputFilter
import android.widget.EditText
import android.widget.TextView
import java.text.NumberFormat
import java.util.regex.Pattern


fun String.uiTayValidateEmail() : Boolean{
    val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"
    val pattern = Pattern.compile(emailRegex)
    val matcher = pattern.matcher(this)
    return matcher.find()
}

fun String.uiTayValidatePhoneFormat() : Boolean{
    return if (this.isNotEmpty()){
        val numberStart = this[0]
        if (numberStart == '9') {
            if (this.contains('*')) {
                false
            } else {
                val phoneRegex = "(?=.[0-9]).{9}"
                val pattern = Pattern.compile(phoneRegex)
                pattern.matcher(this).matches()
            }
        } else
            false
    }else{
        false
    }
}

fun String.uiTayFormatDecimal(decimal : Int = 2): String {
    return try {
        val formatParse = NumberFormat.getInstance()
        formatParse.maximumFractionDigits = decimal
        formatParse.minimumFractionDigits = decimal
        formatParse.format(this.toDouble())

    } catch (e: Exception) {
        "0.00"
    }
}


fun Context.getCountryNetwork():String{
    var codeCountry = "PE"
    uiTayTryCatch {
        val tm = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        codeCountry =  tm.networkCountryIso
    }
    return codeCountry.uppercase()
}

@SuppressLint("SetTextI18n")
fun TextView.uiTaySplitTextTwo(text: String) {
    try {
        val parts = text.split(" ").toTypedArray()
        val part1 = parts[0]
        val part2 = parts[1]
        this.text = "${part1.substring(0,1)}${part2.substring(0,1)}"
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun String.uiTayValidateCap():Boolean{
    val capitalLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    var count = 0
    capitalLetters.forEach {
        this.forEach {pass->
            if (pass == it) count += 1
        }
    }
    return count >= 1
}

fun String.uiTayValidateLow():Boolean{
    val lowerCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".lowercase()
    var count = 0
    lowerCase.forEach {
        this.forEach {pass->
            if (pass == it) count += 1
        }
    }
    return count >= 1
}

fun String.uiTayValidateNumber():Boolean{
    val numbers = "0123456789"
    var count = 0
    numbers.forEach {
        this.forEach {pass->
            if (pass == it) count += 1
        }
    }
    return count >= 1
}


fun EditText.uiTayValidateCharacterLettersNumbers(){
    val letterFilter = InputFilter { source, start, end, _, _, _ ->
        var filtered = ""
        for (i in start until end) {
            val character = source[i]
            if (!Character.isWhitespace(character) && Character.isLetter(character) || Character.isDigit(character)) {
                filtered += character
            }
        }

        filtered
    }

    this.filters = arrayOf(letterFilter)
}


fun String.uiTayRemoveSpaces(): String = replace("\\s".toRegex(), "")






