package com.gb.vale.uivalulibrary.label.imageurl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.util.Base64
import android.view.View
import java.io.InputStream
import java.net.URL
import java.util.concurrent.Executors

class UiTayURLImage(private var container: View, var context: Context, private var uiTayWidth:Int =0, private var uiTayHeight: Int =0) :
    Html.ImageGetter {

    private var executor = Executors.newSingleThreadExecutor()
    private var handler: Handler = Handler(Looper.getMainLooper())

    private fun fetchDrawable(urlString: String?): Drawable? {
        return try {
            val urlParse = URL(urlString).content as InputStream
            val drawable = Drawable.createFromStream(urlParse, "src")
            drawable?.setBounds(
                0,
                0,
                0 + if (uiTayWidth !=0)uiTayWidth else drawable.intrinsicWidth,
                0 + if (uiTayHeight !=0)uiTayWidth else drawable.intrinsicHeight
            )
            drawable
        } catch (e: Exception) {
            null
        }
    }
    override fun getDrawable(source: String): Drawable {
        return if (source.matches("data:image.*base64.*".toRegex())) {
            val base64 = source.replace("data:image.*base64".toRegex(), "")
            val data: ByteArray = Base64.decode(base64, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
            val image: Drawable = BitmapDrawable(context.resources,bitmap)
            image.setBounds(0, 0, 0 + if (uiTayWidth !=0)uiTayWidth else image.intrinsicWidth,
                0 + if (uiTayHeight !=0)uiTayWidth else image.intrinsicHeight)
            image
        } else {
            val urlDrawable = URLDrawable(context)
            executor.execute {
                val image = fetchDrawable(source)
                handler.post {
                    urlDrawable.setBounds(
                        0,
                        0,
                        0.plus(if (uiTayWidth !=0)uiTayWidth else image?.intrinsicWidth?:0),
                        0.plus(if (uiTayHeight !=0)uiTayWidth else image?.intrinsicHeight?:0)
                    )
                    urlDrawable.drawable =
                        image
                    container.invalidate()
                }
            }
            urlDrawable
        }
    }

}

class URLDrawable(context: Context) : BitmapDrawable( context.resources,
    Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)) {

    var drawable: Drawable? = null
    override fun draw(canvas: Canvas) {
        if (drawable != null) {
            drawable?.draw(canvas)
        }
    }
}