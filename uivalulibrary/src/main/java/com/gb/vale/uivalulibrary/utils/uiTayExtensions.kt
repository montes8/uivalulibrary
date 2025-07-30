package com.gb.vale.uivalulibrary.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.gb.vale.uivalulibrary.R


fun View.uiTayVisible(){
    this.visibility = View.VISIBLE
}

fun View.uiTayGone(){
    this.visibility = View.GONE
}

fun View.uiTayInvisible(){
    this.visibility = View.INVISIBLE
}
fun View.uiTayVisibility(value:Boolean){
   if (value)this.uiTayVisible() else this.uiTayGone()
}

fun View.uiTayVisibilityDuo(value:Boolean,view :View){
    this.uiTayInVisibility(value)
    view.uiTayInVisibility(!value)
}

fun View.uiTayInVisibility(value:Boolean){
    if (value)this.uiTayVisible() else this.uiTayInvisible()
}

fun uiTayHandler(
    time: Long = 200,
    func: (() -> Unit)? = null
) {
    Handler(Looper.getMainLooper()).postDelayed({
        func?.invoke()
    }, time)
}

fun uiTayTryCatch(catch: ((String) -> Unit)? = null, func: (() -> Unit)? = null){
    try {  func?.invoke() }catch (e: Exception){
        e.printStackTrace()
        catch?.invoke(e.message?: UI_TAY_ERROR)
    }
}
fun View.setOnClickUiTayDelay(time: Long = 700, onClick: (View) -> Unit) {
    this.setOnClickListener {
        it.isEnabled = false
        uiTayHandler(time) { it.isEnabled = true }
        onClick(it)
    }
}

fun Int.toPxUiTay(context: Context): Int {
    val metrics = context.resources.displayMetrics
    val densityFactor = metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT
    return (this * densityFactor).toInt()
}

fun View.uiTayBgBorder(color : Int =
                            R.color.tay_color_general, radius : Int){
    this.background = this.context.uiTayDrawableRound(color,radius)
}

fun Context.uiTayDrawableRound(color : Int =
                              R.color.tay_color_general, radius : Int):Drawable{
    val shape = GradientDrawable()
    shape.setColor(ContextCompat.getColor(this,color))
    shape.cornerRadius = this.resources.getDimension(radius)
    return  shape
}

fun View.uiTayBgBorderCircle(color : Int =
                            R.color.tay_color_general, colorStroke : Int =
                            0,withStroke : Int = 2){
    this.background = this.context.uiTayDrawableCircle(color,colorStroke,withStroke)
}

fun View.uiTayBgCircleGradient(colorTop : Int = R.color.tay_color_general,
                          colorBottom : Int = R.color.tay_color_general){
    this.background = this.context.uiTayDrawableCircleGradient(colorTop,colorBottom)
}

fun Context.uiTayDrawableCircle(color : Int = R.color.tay_color_general,colorStroke : Int =
    0,withStroke : Int = 2):Drawable{
    val shape = GradientDrawable()
    shape.shape = GradientDrawable.OVAL
    shape.cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
    shape.setColor(this.getColor(color))
    if (colorStroke != 0) shape.setStroke(withStroke, this.getColor(colorStroke))
    return shape
}

fun Context.uiTayDrawableCircleGradient(colorTop : Int = R.color.tay_color_general,
                                   colorBottom : Int = R.color.tay_color_general):Drawable{
    val shape = GradientDrawable()
    shape.shape = GradientDrawable.OVAL
    shape.colors = intArrayOf(
        ContextCompat.getColor(this,colorTop),
        ContextCompat.getColor(this,colorBottom),
    )
    shape.cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
    return shape
}
fun Context.uiTayDrawableStrokeTop(colorStroke : Int = R.color.tay_color_general,colorSolid : Int = R.color.ui_tay_white,
                                   withStroke : Int = 2):Drawable{
    val shape = GradientDrawable()
    shape.setColor(ContextCompat.getColor(this,colorSolid))
    shape.setStroke(withStroke,ContextCompat.getColor(this,colorStroke))
    shape.cornerRadii = floatArrayOf(48f, 48f, 48f, 48f, 0f, 0f, 0f, 0f)
    return shape
}

fun Context.uiTayDrawableStroke(colorStroke : Int = R.color.tay_color_general,colorSolid : Int = R.color.ui_tay_white,
                           radius : Int = R.dimen.dim_tay_0, withStroke : Int = 2):Drawable{
    val shape = GradientDrawable()
    shape.setColor(ContextCompat.getColor(this,colorSolid))
    shape.setStroke(withStroke,ContextCompat.getColor(this,colorStroke))
    shape.cornerRadius = this.resources.getDimension(radius)
    return shape
}


fun View.uiTayBgBorderStroke(colorStroke : Int = R.color.tay_color_general,colorSolid : Int = R.color.ui_tay_white,
                        radius : Int = R.dimen.dim_tay_0, withStroke : Int = 2){
    this.background = this.context.uiTayDrawableStroke(colorStroke,colorSolid,radius,withStroke)
}



fun View.uiTayBgGradient(colorList : ArrayList<Int> = arrayListOf(R.color.tay_color_gradient_default,
    R.color.tay_color_gradient_secondary_default), radius : Int = R.dimen.dim_tay_0,
                          orientation : GradientDrawable.Orientation = GradientDrawable.Orientation.TOP_BOTTOM){
    this.background = this.context.uiTayDrawableGradient(colorList,radius,orientation)
}

fun Context.uiTayDrawableGradient(colorList : ArrayList<Int> = arrayListOf(R.color.tay_color_gradient_default,
    R.color.tay_color_gradient_secondary_default), radius : Int = R.dimen.dim_tay_0,
                                  orientation : GradientDrawable.Orientation = GradientDrawable.Orientation.TOP_BOTTOM): Drawable {
    val shape = GradientDrawable()
    shape.colors = this.uiTaySetColor(colorList).toIntArray()
    shape.orientation = orientation
    shape.cornerRadii = floatArrayOf(this.resources.getDimension(radius),
        this.resources.getDimension(radius), this.resources.getDimension(radius),
        this.resources.getDimension(radius), this.resources.getDimension(radius),
        this.resources.getDimension(radius), this.resources.getDimension(radius),
        this.resources.getDimension(radius))
    return shape
}

fun Context.uiTaySetColor(colorList : ArrayList<Int>) : List<Int>  {
    val colorGradient : ArrayList<Int>  = ArrayList()
    colorList.forEach {
        colorGradient.add(ContextCompat.getColor(this,it))
    }
     return colorGradient
}

fun Context.converterDimen(value : Int) = this.resources.getDimension(value)


fun AppCompatActivity.uiTaySetTopBarColor(color: Any) {
    var bgColor = 0
    when (color) {
        is Int -> {
            bgColor = ContextCompat.getColor(this, color)
        }
        is String -> {
            bgColor = Color.parseColor(color)
        }
    }
    this.window.statusBarColor = bgColor
    this.uiTaySetTopBarTextColor(bgColor)
}

@Suppress("DEPRECATION")
fun AppCompatActivity.uiTaySetTopBarTextColor(color : Int){
    val window = window
    val decorView = window.decorView
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        decorView.windowInsetsController?.let {
            val wic: WindowInsetsController = it
            if (ColorUtils.calculateLuminance(color)> 0.5){
                wic.setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS)
            }
        }

    } else  this.window.decorView.systemUiVisibility =
        if (ColorUtils.calculateLuminance(color)> 0.5) View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else 0
}

fun ViewPager2.uiTayRemoveOverScroll() {
    apply {
        (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
    }
}


fun View.uiTaySetWidthAndHeight(width: Int? = null, height: Int? = null) {
    val layoutParams = layoutParams
    layoutParams.height = resources.getDimensionPixelSize(height?: resources.getDimensionPixelSize(R.dimen.dim_tay_0))
    layoutParams.width = resources.getDimensionPixelSize(width?: resources.getDimensionPixelSize(R.dimen.dim_tay_0))
}

fun View.uiTayWidthAndHeightInt(width: Int? = null, height: Int? = null) {
    val layoutParams = layoutParams
    layoutParams.height = height?: 0
    layoutParams.width = width?: 0
}

fun View.uiTayMargins(
    left: Int? = null,
    top: Int? = null,
    right: Int? = null,
    bottom: Int? = null
) {
    val lp = layoutParams as? ViewGroup.MarginLayoutParams ?: return
    lp.setMargins(
        left ?: lp.leftMargin,
        top ?: lp.topMargin,
        right ?: lp.rightMargin,
        bottom ?: lp.bottomMargin
    )

    layoutParams = lp
}





