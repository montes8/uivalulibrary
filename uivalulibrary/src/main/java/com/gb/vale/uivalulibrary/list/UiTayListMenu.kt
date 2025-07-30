package com.gb.vale.uivalulibrary.list

import android.view.ContextThemeWrapper
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gb.vale.uivalulibrary.R
import com.gb.vale.uivalulibrary.list.adapter.UiTayListMenuAdapter
import com.gb.vale.uivalulibrary.list.model.UiTayMenu
import com.gb.vale.uivalulibrary.utils.uiTayBgBorderStroke
import com.gb.vale.uivalulibrary.utils.uiTayVisibility

fun ConstraintLayout.uiTayListMenu(
    viewTop: View,
    listMenu: List<UiTayMenu> = ArrayList(), position: Int = -1,
     onClickSelected: (Int) -> Unit
): LinearLayout {
    val linear = LinearLayout(this.context)
    if (listMenu.isNotEmpty()) {
        val adapter = UiTayListMenuAdapter()
        val rvList = RecyclerView(ContextThemeWrapper(context, R.style.UITayStyleList))
        rvList.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        rvList.id = View.generateViewId()
        linear.id = View.generateViewId()
        linear.elevation = 12f
        if (listMenu.size < 4) rvList.isVerticalScrollBarEnabled = true
        val paramLinear = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        linear.uiTayBgBorderStroke(
            R.color.tay_edit_list_bg_content_stroke,
            R.color.tay_edit_list_bg_content_solid,
            R.dimen.dim_tay_bg_ui_tay_list_radius_menu
        )
        paramLinear.setMargins(
            0, this.resources.getDimensionPixelSize(R.dimen.dim_ui_tay_list_margin_top_bottom_menu),
            0,
            0,
        )
        linear.setPadding(
            2,
            this.resources.getDimensionPixelSize(R.dimen.dim_ui_tay_list_padding_top_bottom_menu),
            2,
            this.resources.getDimensionPixelSize(R.dimen.dim_ui_tay_list_padding_top_bottom_menu)
        )
        val paramRv = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        paramLinear.matchConstraintMinHeight =
            this.context.resources.getDimensionPixelOffset(
                R.dimen.dim_ui_tay_list_menu_min_height
            )

        paramLinear.matchConstraintMinWidth =
            this.context.resources.getDimensionPixelOffset(
                R.dimen.dim_ui_tay_list_menu_min_width
            )
        rvList.layoutParams = paramRv
        linear.layoutParams = paramLinear
        rvList.setPadding(0, 0, 0, 0)
        this.addView(linear)
        linear.addView(rvList)
        val constraintSet = ConstraintSet()
        constraintSet.clone(this)

        rvList.adapter = adapter
        adapter.selectedPosition(position)
        adapter.list = listMenu
        adapter.onClickOption = {
            onClickSelected.invoke(it)
            rvList.uiTayVisibility(false)
            this.removeView(linear)
        }

        this.setOnClickListener {
            rvList.uiTayVisibility(false)
            this.removeView(linear)

        }

        constraintSet.connect(linear.id, ConstraintSet.TOP, viewTop.id, ConstraintSet.BOTTOM)
        constraintSet.connect(linear.id, ConstraintSet.END, viewTop.id, ConstraintSet.END)
        constraintSet.applyTo(this)
    }

    return linear
}