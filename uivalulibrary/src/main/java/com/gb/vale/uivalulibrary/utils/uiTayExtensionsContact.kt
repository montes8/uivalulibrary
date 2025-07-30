package com.gb.vale.uivalulibrary.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.telecom.Call
import android.telephony.TelephonyManager
import android.util.Log
import com.gb.vale.uivalulibrary.model.UiTayContactPhone

fun Context.uiTayDialedNumber(
    number: String = UI_TAY_EMPTY, key: String = "tel:",
    uiTayAction: String = Intent.ACTION_DIAL
) {
    try {
        val intent = Intent(uiTayAction)
        intent.setData(Uri.parse("$key$number"))
        this.startActivity(intent)
    } catch (e: SecurityException) {
        Log.e(UI_TAY_TAG_ERROR, e.message.toString())
    }
}

fun Context.uiTayViewCall() {
    try {
        val intent = Intent()
        intent.setClass(this, Call::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        this.startActivity(intent)
    } catch (e: SecurityException) {
        Log.e(UI_TAY_TAG_ERROR, e.message.toString())
    }
}

fun Context.uiTayViewCallButton() {
    val intent = Intent(Intent.ACTION_CALL_BUTTON)
    this.startActivity(intent)
}

fun Application.uiTayLoadContact(): List<UiTayContactPhone> {
    val contacts: ArrayList<UiTayContactPhone> = ArrayList()
    val projection = arrayOf(
        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER,
    )

    val cursor = this.contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        projection,
        null,
        null,
        "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"
    )

    if (cursor?.moveToFirst() == true)
        do {
            val name =
                cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    .trim()
            val phone =
                cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    .trim()

            if (phone.isNotEmpty()) {
                contacts.add(
                    UiTayContactPhone(
                        name = name, phoneNumber = phone,
                        initialLetter = name.substring(0, 1)
                    )
                )
            }
        } while (cursor.moveToNext())

    cursor?.close()
    return contacts
}

fun uiTayValidNumberBlocking(list: List<UiTayContactPhone>, numberCall: String): Boolean {
    val incomingCall = numberCall.filter { it.isDigit() }.trim()
    val incomingLengthCall = incomingCall.length
    list.forEach { contact ->
        val currentNumber = contact.phoneNumber.filter { it.isDigit() }.trim()
        val currentLengthNumber = currentNumber.length
        if (currentLengthNumber >= incomingLengthCall) {
            val numberBlocking = currentNumber.substring(
                currentLengthNumber - incomingLengthCall,
                currentLengthNumber
            ) == incomingCall.substring(0, incomingLengthCall)
            if (numberBlocking) return false
        }
    }
    return true
}

@SuppressLint("SuspiciousIndentation")
fun Application.uiTayDeleteSMS(all: Boolean = false, utNumber: String = UI_TAY_EMPTY) {
    this.contentResolver.query(
        Uri.parse("content://sms/"),
        arrayOf("_id", "thread_id", "address", "person", "date", "body"),
        null,
        null,
        null
    )?.let { c ->
        uiTayTryCatch {
            while (c.moveToNext()) {
                val id = c.getInt(0)
                val address = c.getString(2)
                  if (all) {
                     this.contentResolver.delete(
                        Uri.parse("content://sms/$id"), null, null
                      )
                   } else {
                     if (address == utNumber) {
                        this.contentResolver.delete(
                            Uri.parse("content://sms/$id"), null, null
                        )
                    } } }
            c.close()
        }
    }


}

@SuppressLint("MissingPermission", "HardwareIds")
fun  Context.getNumberPhone() :String{
    var numberPhone = UI_TAY_EMPTY
    uiTayTryCatch {
        val mPhoneNumber = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        numberPhone = mPhoneNumber.line1Number
    }
    return  numberPhone
}