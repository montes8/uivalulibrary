package com.gb.vale.uivalulibrary.utils

import android.app.NotificationManager
import android.content.Context
import android.net.wifi.WifiManager
import android.text.InputFilter
import java.math.BigInteger
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.UnknownHostException
import java.nio.ByteOrder
import java.util.Collections
import java.util.regex.Pattern

fun uiTayGetMobilIPAddress(): String {
    try {
        val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
        for (interfaceItem in interfaces) {
            val address = Collections.list(interfaceItem.inetAddresses)
            for (addressItem in address) {
                if (!addressItem.isLoopbackAddress) {
                    return addressItem.hostAddress ?: UI_TAY_EMPTY
                }
            }
        }
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
    return UI_TAY_EMPTY
}

@Suppress("DEPRECATION")
fun Context.uiTayWifiIpAddress(): String? {
    val wifiManager =
        this.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    var ipAddress = wifiManager.connectionInfo.ipAddress

    if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
        ipAddress = Integer.reverseBytes(ipAddress)
    }
    val ipByteArray = BigInteger.valueOf(ipAddress.toLong()).toByteArray()

    var ipAddressString: String?
    try {
        ipAddressString = InetAddress.getByAddress(ipByteArray).hostAddress
    } catch (ex: UnknownHostException) {
        ipAddressString = null
    }

    return ipAddressString
}

fun String.uiTayValidateCardNumber(): Boolean {
    val ints = IntArray(this.length)
    for (i in indices) {
        ints[i] = Integer.parseInt(this.substring(i, i + 1))
    }
    run {
        var i = ints.size - 2
        while (i >= 0) {
            var j = ints[i]
            j *= 2
            if (j > 9) {
                j = j % 10 + 1
            }
            ints[i] = j
            i -= 2
        }
    }
    var sum = 0
    for (i in ints.indices) {
        sum += ints[i]
    }

    return sum % 10 == 0
}


fun Context.uiTayClearNotifications() {
    val notificationManager =
        this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.cancelAll()
}



val pattern: Pattern =
    Pattern.compile("[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz]")

val patternNumberAndLetter: Pattern =
    Pattern.compile("[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789]")


val patternNumber: Pattern =
    Pattern.compile("[0123456789]")

val uiTayFilterSpaces = InputFilter { source, _, _, _, _, _ ->
    source.toString().filterNot { it.isWhitespace() }
}

val uiTayFilterOnlyLetter = InputFilter { source, _, _, _, _, _ ->
    source.toString().filter { pattern.matcher(it.toString()).matches() }
}

val uiTayFilterOnlyLetterAndNumber = InputFilter { source, _, _, _, _, _ ->
    source.toString().filter { patternNumberAndLetter.matcher(it.toString()).matches() }
}

val uiTayFilterOnlyNumbers = InputFilter { source, _, _, _, _, _ ->
    source.toString().filter { patternNumber.matcher(it.toString()).matches() }
}