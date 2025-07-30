package com.gb.vale.uivalulibrary.list.adapter

import android.annotation.SuppressLint
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.gb.vale.uivalulibrary.R
import com.gb.vale.uivalulibrary.list.model.UiTayMenu
import com.gb.vale.uivalulibrary.utils.setOnClickUiTayDelay
import com.gb.vale.uivalulibrary.utils.uiTayBgBorder

class UiTayListMenuAdapter(var onClickOption: ((Int) -> Unit)? = null) :
    RecyclerView.Adapter<UiTayListMenuAdapter.UiTayListViewHolder>() {

    private var positionSelected = -1
    var list: List<UiTayMenu> = ArrayList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun selectedPosition(value: Int) {
        positionSelected = value
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UiTayListViewHolder {
        return UiTayListViewHolder(TextView(parent.context))
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: UiTayListViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class UiTayListViewHolder(private val dataBinding: TextView) :
        RecyclerView.ViewHolder(dataBinding) {


        @SuppressLint("NotifyDataSetChanged")
        fun bind(option: UiTayMenu) {
            dataBinding.text = option.name
            configSelected()
            if (option.image != 0){
                dataBinding.setCompoundDrawablesWithIntrinsicBounds( ContextCompat.getDrawable(
                    dataBinding.context,option.image
                ),
                    null, null, null)
                dataBinding.compoundDrawablePadding = dataBinding.context.resources.
                getDimensionPixelSize(R.dimen.dim_ui_tay_list_menu_drawable_padding_menu)

            }
            dataBinding.gravity = Gravity.CENTER_VERTICAL
            dataBinding.layoutParams = configView()
            dataBinding.setOnClickUiTayDelay {
                positionSelected = adapterPosition
                onClickOption?.invoke(adapterPosition)
                notifyDataSetChanged()
            }
        }

        private fun configView() : ConstraintLayout.LayoutParams {
            val typeface = ResourcesCompat.getFont(dataBinding.context,R.font.ui_tay_montserrat_medium)
            dataBinding.typeface = typeface
            dataBinding.setTextColor(ContextCompat.getColor(dataBinding.context,colorText()))
            dataBinding.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                dataBinding.context.resources.getDimensionPixelSize( R.dimen.dim_ui_tay_list_item_text_size_menu).toFloat())
            val layoutCtn = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
            dataBinding.setPadding(dataBinding.context.resources.getDimensionPixelOffset(R.dimen.dim_ui_tay_list_item_padding_star_end_menu),
                dataBinding.context.resources.getDimensionPixelOffset(R.dimen.dim_ui_tay_list_item_padding_top_bottom_menu),
                dataBinding.context.resources.getDimensionPixelOffset(R.dimen.dim_ui_tay_list_item_padding_star_end_menu),
                dataBinding.context.resources.getDimensionPixelOffset(R.dimen.dim_ui_tay_list_item_padding_top_bottom_menu))
            return layoutCtn
        }
        private fun configSelected() {
            dataBinding.uiTayBgBorder(
                color =
                if (positionSelected == adapterPosition) R.color.tay_edit_list_bg_selected_menu
                else R.color.tay_edit_list_bg_unselected_menu,
                radius = R.dimen.dim_tay_0)
        }


        private fun colorText() = if (positionSelected == adapterPosition) R.color.tay_edit_list_text_selected_menu
        else R.color.tay_edit_list_text_unselected_menu

    }
}