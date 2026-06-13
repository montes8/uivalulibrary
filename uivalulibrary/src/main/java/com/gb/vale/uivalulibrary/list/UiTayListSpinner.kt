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
import com.gb.vale.uivalulibrary.list.adapter.UiTayListAdapter
import com.gb.vale.uivalulibrary.list.adapter.UiTayListCustomAdapter
import com.gb.vale.uivalulibrary.list.model.UiTayModelCustom
import com.gb.vale.uivalulibrary.utils.uiTayBgBorderStroke
import com.gb.vale.uivalulibrary.utils.uiTayVisibility


/**  positions first position selected (Int) , second position Bottom (Boolean)
 * **/

fun ConstraintLayout.uiTayListSpinner(
    viewTop: View,
    list: List<String> = ArrayList(),
    listCustom: List<UiTayModelCustom> = ArrayList(),
    positions: Pair<Int,Boolean> = Pair(-1,true),
    itemCustom: Boolean = false,
    onClickContent: () -> Unit,
    onClickSelected: (Int) -> Unit
): LinearLayout {
    val linear = LinearLayout(this.context)
    if (list.isNotEmpty() || listCustom.isNotEmpty()) {
        val adapter = UiTayListAdapter()
        val adapterCustom = UiTayListCustomAdapter()
        val rvList = RecyclerView(ContextThemeWrapper(context, R.style.UITayStyleList))
        configRvTSInit(rvList, linear, positions.second, itemCustom, list.size < 4, listCustom.size < 4)
        val constraintSet = ConstraintSet()
        constraintSet.clone(this)
        rvList.adapter = if (itemCustom) adapterCustom else adapter
        if (!itemCustom) adapter.selectedPosition(positions.first)
        if (itemCustom) adapterCustom.list = listCustom else adapter.list = list
        configRvTSAction(adapterCustom, adapter, rvList, linear) { onClickSelected.invoke(it) }
        this.setOnClickListener {
            onClickContent.invoke()
            configCtnTSRemove(rvList, linear)
        }
        configRvTSPosition(positions.second, linear, constraintSet, viewTop)
        linear.elevation = 30f
        linear.bringToFront()
    }
    return linear
}

private fun ConstraintLayout.configRvTSAction(adapterCustom:UiTayListCustomAdapter ,adapter:
    UiTayListAdapter,
                                              rvList:RecyclerView,linear:LinearLayout,onClickSelected: (Int) -> Unit){
        adapterCustom.onClickOption = {
            onClickSelected.invoke(it)
            configCtnTSRemove(rvList,linear)
        }
        adapter.onClickOption = {
            onClickSelected.invoke(it)
            configCtnTSRemove(rvList,linear)
        }
}


private fun ConstraintLayout.configCtnTSRemove(rvList:RecyclerView,linear:LinearLayout){
    rvList.uiTayVisibility(false)
    this.removeView(linear)
}

private fun ConstraintLayout.configRvTSInit(rvList : RecyclerView,linear:LinearLayout,positionBottom:Boolean,itemCustom:Boolean
                                          ,scrollTayBay : Boolean,scrollCustomTaybay : Boolean){
    rvList.layoutManager =
        LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
    rvList.id = View.generateViewId()
    linear.id = View.generateViewId()
    linear.elevation = 12f
    if (scrollTayBay) rvList.isVerticalScrollBarEnabled = true
    if (scrollCustomTaybay) rvList.isVerticalScrollBarEnabled = true
    configRvTSStyle(rvList,linear,positionBottom,itemCustom)
    this.addView(linear)
    linear.addView(rvList)
}

private fun ConstraintLayout.configRvTSStyle(rvList : RecyclerView,linear:LinearLayout,positionBottom:Boolean,
                                             itemCustom:Boolean){
    val paramLinear = LayoutParams(
        this.context.resources.getDimensionPixelOffset(R.dimen.dim_tay_0),
        LayoutParams.WRAP_CONTENT
    )
    linear.uiTayBgBorderStroke(
        R.color.tay_edit_list_bg_content_stroke,
        R.color.tay_edit_list_bg_content_solid,
        R.dimen.dim_tay_bg_ui_tay_list_radius
    )
    paramLinear.setMargins(
        0, if (positionBottom) this.resources.getDimensionPixelSize(R.dimen.dim_ui_tay_list_margin_top_bottom) else 0,
        0,
        if (!positionBottom) this.resources.getDimensionPixelSize(R.dimen.dim_ui_tay_list_margin_top_bottom) else 0,
    )
    linear.setPadding(
        2,
        this.resources.getDimensionPixelSize(R.dimen.dim_ui_tay_list_padding_top_bottom),
        2,
        this.resources.getDimensionPixelSize(R.dimen.dim_ui_tay_list_padding_top_bottom)
    )
    val paramRv = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    paramLinear.matchConstraintMaxHeight =
        this.context.resources.getDimensionPixelOffset(if (itemCustom)R.dimen.dim_ui_tay_list_max_height_custom
        else
            R.dimen.dim_ui_tay_list_max_height)
    rvList.layoutParams = paramRv
    linear.layoutParams = paramLinear
    rvList.setPadding(0, 0, 0, 0)
}

private fun ConstraintLayout.configRvTSPosition(positionBottom : Boolean,linear: LinearLayout,constraintSet :ConstraintSet ,
                                                viewTop: View){
    if (positionBottom) {
        constraintSet.connect(linear.id, ConstraintSet.TOP, viewTop.id, ConstraintSet.BOTTOM)
    } else {
        constraintSet.connect(linear.id, ConstraintSet.BOTTOM, viewTop.id, ConstraintSet.TOP)
    }
    constraintSet.connect(linear.id, ConstraintSet.START, viewTop.id, ConstraintSet.START)
    constraintSet.connect(linear.id, ConstraintSet.END, viewTop.id, ConstraintSet.END)
    constraintSet.applyTo(this)
}

