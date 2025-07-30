package com.gb.vale.uivalulibrary.extra

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.gb.vale.uivalulibrary.R

class UiTayRatingBar @JvmOverloads constructor(
    context: Context,
    private val attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var uiTayNumIcon: Int = 3
    private var uiTayIconSelected: Drawable? = ContextCompat.getDrawable(context, R.drawable.ui_tay_start_selected)
    private var uiTayIconUnselected: Drawable? =
        ContextCompat.getDrawable(context, R.drawable.ui_tay_start_unselected)
    var onRatingBarSelectedListener: (Int) -> Unit = {}
    private var startSize: Int = context.resources.getDimensionPixelSize(R.dimen.dim_tay_rb_size)
    private val images = arrayListOf<ImageView>()
    private var uiTayRbEnableSelected = false

    private var rateSelected: Int = -1
        set(value) {
            field = value
            configSelected()
            onRatingBarSelectedListener(value.plus(1))
        }


    private var uiTayRbNumStarts: Int = uiTayNumIcon
        set(value) {
            field = value
            uiTayNumIcon = value
            createImages()
        }

    private var uiTayRbNumRangeSelected: Int = -1
        set(value) {
            field = value
            if (!uiTayRbEnableSelected){
                images.forEachIndexed { index, appCompatImageView ->
                    appCompatImageView.setImageDrawable(
                        if (index < value)uiTayIconSelected else uiTayIconUnselected
                    )
                }
            }
        }
    init {
        loadAttributes()
    }

    private fun loadAttributes() {
        context.obtainStyledAttributes(attrs, R.styleable.UiTayRatingBar).apply {
            uiTayRbEnableSelected = getBoolean(R.styleable.UiTayRatingBar_uiTayRbEnableSelected,false)
            startSize = getDimensionPixelSize(R.styleable.UiTayRatingBar_uiTayRbDimenSize, startSize)
            uiTayRbNumStarts = getInt(R.styleable.UiTayRatingBar_uiTayRbNumStarts, uiTayNumIcon)
            uiTayIconSelected = getDrawable(R.styleable.UiTayRatingBar_uiTayRbIconSelected) ?: uiTayIconSelected
            uiTayIconUnselected =
                getDrawable(R.styleable.UiTayRatingBar_uiTayRbIconUnselected) ?: uiTayIconUnselected
            uiTayRbNumRangeSelected = getInt(R.styleable.UiTayRatingBar_uiTayRbNumRangeSelected, -1)

        }.recycle()
    }

    private fun createImages() {
        this.removeAllViews()
        for (number in 0..uiTayNumIcon.minus(1)) {
            val image = AppCompatImageView(context).apply {
                id = View.generateViewId()
                setImageDrawable(uiTayIconUnselected)
                layoutParams = LayoutParams(startSize, startSize)
                tag = number
                this.setOnClickListener {
                    if (uiTayRbEnableSelected) rateSelected = number
                }
            }
            images.add(image)
            addView(image)
            setViews(images)
        }
    }

    private fun setViews(list: List<View>) {
        val set = ConstraintSet()
        set.clone(this)
        list.forEachIndexed { index, view ->
            set.apply {
                connect(view.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
                connect(
                    view.id,
                    ConstraintSet.START,
                    if (index == 0) ConstraintSet.PARENT_ID else list[index.minus(1)].id,
                    if (index == 0) ConstraintSet.START else ConstraintSet.END
                )
                connect(
                    view.id,
                    ConstraintSet.END,
                    if (index == list.size.minus(1)) ConstraintSet.PARENT_ID else list[index.plus(
                        1
                    )].id,
                    if (index == list.size.minus(1)) ConstraintSet.END else ConstraintSet.START
                )
                connect(
                    view.id,
                    ConstraintSet.BOTTOM,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.BOTTOM
                )
            }
        }
        set.applyTo(this)
    }

    private fun configSelected() {
        for (position in 0..childCount.minus(1)) {
            val image = getChildAt(position) as AppCompatImageView
            val drawable = if (image.tag.toString().toInt() <= rateSelected)
                uiTayIconSelected else uiTayIconUnselected
            image.setImageDrawable(drawable)
        }
    }
}