package com.gb.vale.uivalulibrary.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.KeyguardManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gb.vale.uivalulibrary.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.NotFoundException
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeWriter
import kotlin.math.pow
import kotlin.math.sqrt


@SuppressLint("QueryPermissionsNeeded")
fun Context.uiTayOpenPdfUrl(url: String?,messageError:String = this.getString(R.string.tay_ui_error_pdf_link)) {
    url?.let {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            this.uiTayShowToast(messageError)
            return
        }
        val newUrl = "https://docs.google.com/viewer?url=$url"
        val webPage = Uri.parse(newUrl)
        val intent = Intent(Intent.ACTION_VIEW, webPage)
        if (intent.resolveActivity(this.packageManager) != null) {
            this.startActivity(intent)
        }
    }
}

fun Context.uiTaycopy(text : String, message:String = getString(R.string.tay_ui_text_message_copy)){
    val clipboard = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("label", text)
    clipboard.setPrimaryClip(clip)
    this.uiTayShowToast(message)
}

fun Context.uiTayOpenUrl(url : String){
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    this.startActivity(intent)
}

fun uiTayFullScreen(window : Window){
    window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
}

@Suppress("DEPRECATION")
fun Activity.uiTayCheckIsTablet(): Boolean {

    val metrics = DisplayMetrics()

    if (SDK_INT >= android.os.Build.VERSION_CODES.R) {
        val display = this.display
        display?.getRealMetrics(metrics)
    } else {
        val display = this.windowManager.defaultDisplay
        display.getMetrics(metrics)
    }

    var isTablet = false
    val widthInches: Float = metrics.widthPixels / metrics.xdpi
    val heightInches: Float = metrics.heightPixels / metrics.ydpi
    val diagonalInches =
        sqrt(
            widthInches.toDouble().pow(2.0) + heightInches.toDouble().pow(2.0)
        )
    if (diagonalInches >= 7.0) {
        isTablet = true
    }

    return isTablet
}

fun Activity.uiTayHideKeyboard() {
    val inputMethodManager: InputMethodManager =
        this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
}

fun EditText.uiTayHideKeyboard(){
    val im: InputMethodManager = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    im.hideSoftInputFromWindow(this.windowToken,0)
}

fun Context.uiTayShowToast(value : String){
    Toast.makeText(this, value, Toast.LENGTH_SHORT).show()
}
fun Context.uiTayShowToast(value : Int){
    Toast.makeText(this, value, Toast.LENGTH_SHORT).show()
}

fun uiTayPixelsToSp(context: Context,px: Float) = px / context.resources.displayMetrics.scaledDensity

@SuppressLint("MissingPermission")
fun Context?.uiTayIsConnected(): Boolean {
    return this?.let {
        val cm = it.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.getNetworkCapabilities(cm.activeNetwork)
            ?.hasCapability((NetworkCapabilities.NET_CAPABILITY_INTERNET)) ?: false
    } ?: false
}

fun Context?.uiTayIsAirplaneModeActive(): Boolean {
    return this?.let {
        return Settings.Global.getInt(it.contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) != 0
    } ?: false

}

@SuppressLint("MissingPermission")
fun callPhoneIntent(context: Context,number : String) {
    val intent = Intent(Intent.ACTION_CALL)
    intent.data = Uri.parse("tel:$number")
    context.startActivity(intent)
}


inline fun <reified T>uiTayJsonToObjet(json: String): T {
    val jsonData = Gson()
    return jsonData.fromJson(json,object : TypeToken<T>(){}.type)
}

fun <T> T.uiTayObjetToJson(): String {
    val jsonData = Gson()
    return jsonData.toJson(this)
}

infix fun Int.uiTayPercentOf(value: Int): Int {
    return if (this == 0) 0
    else ((this.toDouble() / 100) * value).toInt()
}

@Suppress("deprecation")
fun Window.getUiSizeContent():Pair<Int,Int>{
    val metrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(metrics)
    return Pair(metrics.widthPixels,metrics.heightPixels)
}


fun AppCompatActivity.uiTayIsSystemKeyboardVisible(): Boolean {
    return try {
        val manager = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val windowHeightMethod =
            InputMethodManager::class.java.getMethod("getInputMethodWindowVisibleHeight")
        val height = windowHeightMethod.invoke(manager) as Int
        height > 0
    } catch (e: Exception) {
        false
    }
}

fun uiTayIsDeviceLocked(context: Context): Boolean {
    val manager =  context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
    return manager.isDeviceSecure
}

fun Bitmap.uiTayWriteCodeQrImage():String{
    val width = this.width
    val height = this.height
    val pixels = IntArray(width * height)
    this.getPixels(pixels, 0, width, 0, 0, width, height)
    this.recycle()
    val source = RGBLuminanceSource(width, height, pixels)
    val bBitmap = BinaryBitmap(HybridBinarizer(source))
    val reader = MultiFormatReader()
    return try {
        val result = reader.decode(bBitmap)
        result.text

    } catch (e: NotFoundException) {
        Log.e("TAG", "decode exception", e)
        UI_TAY_EMPTY
    }
}

fun String.uiTayGenerateQrImage(size : Int = 512):Bitmap?{
    return if (this.isNotEmpty()){
        val hints = hashMapOf<EncodeHintType, Int>().also { it[EncodeHintType.MARGIN] = 1 }
        val bits = QRCodeWriter().encode(this, BarcodeFormat.QR_CODE, size, size, hints)
        Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565).also {
            for (x in 0 until size) {
                for (y in 0 until size) {
                    it.setPixel(x, y, if (bits[x, y]) Color.BLACK else Color.WHITE)
                }
            }
        }
    }else{
        null
    }
}

inline fun <reified T : Parcelable> Intent.uiTayParcelable(key: String): T? = when {
    SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

@Suppress("DEPRECATION")
inline fun <reified T : Parcelable> Bundle.uiTayParcelable(key: String): T? = when {
    SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> getParcelable(key) as? T
}


fun uiTayTwoDigitNumber(number: Long): String {
    if (number == 0L) {
        return "00"
    } else if (number / 10 == 0L) {
        return "0$number"
    }
    return number.toString()
}

fun uiTayDpToPx(dp:Int) = (dp * Resources.getSystem().displayMetrics.density).toInt()
