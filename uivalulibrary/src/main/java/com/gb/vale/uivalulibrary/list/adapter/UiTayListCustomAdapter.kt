package com.gb.vale.uivalulibrary.list.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gb.vale.uivalulibrary.databinding.UiTayListCustomBinding
import com.gb.vale.uivalulibrary.list.model.UiTayModelCustom
import com.gb.vale.uivalulibrary.utils.setOnClickUiTayDelay
import com.gb.vale.uivalulibrary.utils.uiTayLoadUrl
import com.gb.vale.uivalulibrary.utils.uiTayVisibility

class UiTayListCustomAdapter(var onClickOption: ((Int) -> Unit)? = null) :
    RecyclerView.Adapter<UiTayListCustomAdapter.UiTayListViewHolder>() {
    var list: List<UiTayModelCustom> = ArrayList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UiTayListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            UiTayListCustomBinding.inflate(layoutInflater, parent, false)
        return UiTayListViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: UiTayListViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class UiTayListViewHolder(private val binding: UiTayListCustomBinding) :
        RecyclerView.ViewHolder(binding.root) {


        @SuppressLint("NotifyDataSetChanged")
        fun bind(option: UiTayModelCustom) {
            binding.uiTayRowImgListCustom.uiTayVisibility(option.imageVisibility)
            if (option.image != 0){
                binding.uiTayRowImgListCustom.setImageResource(option.image)
            }
            if (option.imageUrl.isNotEmpty()){
                binding.uiTayRowImgListCustom.uiTayLoadUrl(option.imageUrl,option.imageCircle)
            }
            binding.uiTayRowSubTitleListCustom.uiTayVisibility(option.subTitle.isNotEmpty())
            binding.uiTayRowMessageListCustom.uiTayVisibility(option.message.isNotEmpty())
            if (list.size-1 == adapterPosition)binding.lineCustom.visibility = View.GONE
            binding.root.setOnClickUiTayDelay { onClickOption?.invoke(adapterPosition) }
            binding.uiTayRowTitleListCustom.text = option.title
            binding.uiTayRowSubTitleListCustom.text = option.subTitle
            binding.uiTayRowMessageListCustom.text = option.message

        }

    }
}