package com.gb.vale.uivalulibrary.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.gb.vale.uivalulibrary.R

class UiTayDialogLayout(
    private val layout: Int,
    private var block: UiTayDialogLayoutBlock = null) :
    DialogFragment() {
    lateinit var uiTayView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.UiTayDialogLayoutStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(layout, container, false)
        this.uiTayView = view
        block?.invoke(this)
        return view
    }
}

typealias UiTayDialogLayoutBlock = ((UiTayDialogLayout) -> Unit)?
