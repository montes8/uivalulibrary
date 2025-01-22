package com.gb.vale.uitaylibrary.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.StrictMode
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import androidx.exifinterface.media.ExifInterface
import com.gb.vale.uitaylibrary.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.Calendar
import kotlin.math.min


fun uiTayReduceBitmapSize(imageFilePath: File): Bitmap {
    val bmOptions = BitmapFactory.Options()
    bmOptions.inJustDecodeBounds = true
    BitmapFactory.decodeFile(imageFilePath.absolutePath, bmOptions)
    bmOptions.inSampleSize = uiTayCalculateInSampleSize(bmOptions)
    bmOptions.inJustDecodeBounds = false
    return BitmapFactory.decodeFile(imageFilePath.absolutePath, bmOptions)
}


fun uiTayCalculateInSampleSize(bmOptions: BitmapFactory.Options): Int {
    val photoWidth = bmOptions.outWidth
    val photoHeight = bmOptions.outHeight
    var scaleFactor = 1
    if (photoWidth > 1000 || photoHeight > 1000) {
        val halfPhotoWidth = photoWidth / 2
        val halfPhotoHeight = photoHeight / 2
        while (halfPhotoWidth / scaleFactor >= 500 && halfPhotoHeight / scaleFactor >= 500) {
            scaleFactor *= 2
        }
    }
    return scaleFactor
}


fun uiTayImageBitmapPath(path: String): Bitmap? {
    val pathUri = File(path)
    return BitmapFactory.decodeFile(pathUri.absolutePath)
}

fun Context.uiTayCreatePictureFile(nameFile: String = "imgSave"): File {
    val storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val newPath = File("$storageDir$nameFile")
    if (!newPath.exists()) {
        newPath.mkdirs()
    }
    return newPath
}

fun Context.uiTaySaveImg(
    nameFile: File, img: Bitmap, nameImage: String,
    toast: Boolean = false, message: String = UI_TAY_EMPTY
): String {
    val myPath = File(nameFile, "$nameImage.jpg")

    val fos: FileOutputStream?
    try {
        fos = FileOutputStream(myPath)
        img.compress(Bitmap.CompressFormat.JPEG, 10, fos)
        fos.flush()
        if (toast) this.uiTayShowToast(message)
    } catch (ex: FileNotFoundException) {
        ex.printStackTrace()
        if (toast) this.uiTayShowToast(this.getString(R.string.tay_ui_error_pdf_link))
    } catch (ex: IOException) {
        ex.printStackTrace()
        if (toast) this.uiTayShowToast(this.getString(R.string.tay_ui_error_pdf_link))
    }
    return myPath.absolutePath
}

fun Context.uiTayRotateIfNeeded(bitmap: Bitmap, uri: Uri): Bitmap {
    val exitInt = this.contentResolver.openInputStream(uri)?.let { ExifInterface(it) }
    return when (exitInt?.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_NORMAL
    )) {
        ExifInterface.ORIENTATION_ROTATE_90 -> {
            uiTayRotateImages(bitmap, 90)
        }

        ExifInterface.ORIENTATION_ROTATE_180 -> {
            uiTayRotateImages(bitmap, 180)
        }

        ExifInterface.ORIENTATION_ROTATE_270 -> {
            uiTayRotateImages(bitmap, 270)
        }

        else -> {
            bitmap
        }
    }
}


fun uiTayRotateImages(imageToOrient: Bitmap, degreesToRotate: Int): Bitmap {
    var result: Bitmap = imageToOrient
    try {
        if (degreesToRotate != 0) {
            val matrix = Matrix()
            matrix.setRotate(degreesToRotate.toFloat())
            result = Bitmap.createBitmap(
                imageToOrient,
                0,
                0,
                imageToOrient.width,
                imageToOrient.height,
                matrix,
                true /*filter*/
            )
        }
    } catch (e: java.lang.Exception) {
        Log.d("TAGError", "Exception when trying to orient image", e)

    }
    return result
}

fun String.uiTayBase64toBitmap(): Bitmap? {
    val decodedBytes = Base64.decode(this, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
}

fun Bitmap.bitmapToBase64(): String? {
    val image = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, image)
    val b = image.toByteArray()
    return Base64.encodeToString(b, Base64.DEFAULT)
}

fun ImageView.uiTayLoadUrl(
    url: String = UI_TAY_EMPTY, circle: Boolean = false,
    placeHolder: Drawable = this.context.uiTayDrawableRound(R.color.ui_tay_gray, R.dimen.dim_tay_0)
) {
    val handler = Handler(Looper.getMainLooper())
    handler.post {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val bitmap: Bitmap?
        val inputStream: InputStream
        try {
            inputStream = URL(url).openStream()
            bitmap = BitmapFactory.decodeStream(inputStream)
            if (circle) {
                val imgCircle = bitmap.converterCircle()
                imgCircle?.let {
                    this.setImageBitmap(it)
                } ?: this.setImageDrawable(placeHolder)
            } else {
                bitmap?.let {
                    this.setImageBitmap(it)
                } ?: this.setImageDrawable(placeHolder)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            this.setImageDrawable(placeHolder)
        }
    }
}


fun Bitmap.converterCircle(): Bitmap? {
    val size: Int = min(this.width, this.height)
    val bitmap = ThumbnailUtils.extractThumbnail(this, size, size)
    val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(output)
    val color = -0x10000
    val paint = Paint()
    val rect = Rect(0, 0, bitmap.width, bitmap.height)
    val rectF = RectF(rect)
    paint.isAntiAlias = true
    paint.isDither = true
    paint.isFilterBitmap = true
    canvas.drawARGB(0, 0, 0, 0)
    paint.color = color
    canvas.drawOval(rectF, paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(bitmap, rect, rect, paint)
    return output
}