package com.gb.vale.uivalulibrary.swipe

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.util.LinkedList

@SuppressLint("ClickableViewAccessibility")
abstract class UiTayCardSwipeHelper(
    context: Context,
    private val recyclerView: RecyclerView,
    private var buttonWidth: Int,
    private var marginStart: Int = 0
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private var buttonList: ArrayList<UiTayCardSwipeButton> = arrayListOf()
    private lateinit var gestureDetector: GestureDetector
    private var swipePosition = -1
    private var swipeThreshold = 0.5f
    private var buttonBuffer: MutableMap<Int, ArrayList<UiTayCardSwipeButton>> = hashMapOf()
    private var removeQueue: LinkedList<Int> = LinkedList<Int>()

    abstract fun instanceCardSwipe(
        viewHolder: RecyclerView.ViewHolder,
        buffer: ArrayList<UiTayCardSwipeButton>
    )

    private val gestureListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            for (button in buttonList)
                if (button.onClick(x = e.x, y = e.y))
                    break
            return true
        }
    }

    private val onTouchListener = View.OnTouchListener { _, motionEvent ->
        if (swipePosition < 0) return@OnTouchListener false
        val point = Point(motionEvent.rawX.toInt(), motionEvent.rawY.toInt())
        val swipeViewHolder = recyclerView.findViewHolderForAdapterPosition(swipePosition)
        val swipeItem = swipeViewHolder?.itemView
        val rect = Rect()
        swipeItem?.getGlobalVisibleRect(rect)
        if (motionEvent.action == MotionEvent.ACTION_DOWN ||
            motionEvent.action == MotionEvent.ACTION_MOVE ||
            motionEvent.action == MotionEvent.ACTION_UP
        ) {
            if (rect.top < point.y && rect.bottom > point.y)
                gestureDetector.onTouchEvent(motionEvent)
            else {
                removeQueue.add(swipePosition)
                swipePosition = -1
                recoverSwipeItem()
            }
        }
        false
    }

    @Synchronized
    private fun recoverSwipeItem() {
        while (removeQueue.isNotEmpty()) {
            val pos = removeQueue.poll()?.toInt()?:0
            if (pos > -1)
                recyclerView.adapter?.notifyItemChanged(pos)
        }
    }

    init {
        gestureDetector = GestureDetector(context, gestureListener)
        recyclerView.setOnTouchListener(null)
        recyclerView.setOnTouchListener(onTouchListener)
        attachSwipe()
    }

    private fun attachSwipe() {
        val itemTouchHelper = ItemTouchHelper(this)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val pos = viewHolder.adapterPosition
        if (swipePosition != pos)
            removeQueue.add(swipePosition)
        swipePosition = pos
        if (buttonBuffer.containsKey(swipePosition))
            buttonList = buttonBuffer[swipePosition]?: arrayListOf()
        else buttonList.clear()
        buttonBuffer.clear()
        swipeThreshold = 0.5f * buttonList.size.toFloat() * buttonWidth.toFloat()
        recoverSwipeItem()
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return swipeThreshold
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return 0.1f * defaultValue
    }

    override fun getSwipeVelocityThreshold(defaultValue: Float): Float {
        return 0.5f * defaultValue
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val pos = viewHolder.adapterPosition
        var translationX = dX
        val itemView = viewHolder.itemView
        if (pos < 0) {
            swipePosition = pos
            return
        }
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && dX < 0) {
                var buffer: ArrayList<UiTayCardSwipeButton>? = arrayListOf()
                if (!buttonBuffer.containsKey(pos)) {
                    instanceCardSwipe(viewHolder, buffer?:arrayListOf())
                    buttonBuffer[pos] = buffer?:arrayListOf()
                } else {
                    buffer = buttonBuffer[pos]
                }
                if (buffer != null) {
                    translationX = dX * buffer.size.toFloat() * buttonWidth.toFloat() / itemView.width
                }
                drawButton(c, itemView, buffer?:arrayListOf(), pos, translationX+marginStart)
        }
        super.onChildDraw(
            c,
            recyclerView,
            viewHolder,
            translationX,
            dY,
            actionState,
            isCurrentlyActive
        )
    }

    private fun drawButton(
        c: Canvas,
        itemView: View,
        buffer: List<UiTayCardSwipeButton>,
        pos: Int,
        translationX: Float
    ) {
        var right = itemView.right.toFloat()
        val dButtonWidth = -1 * translationX / buffer.size
        for (button in buffer) {
            val left = right - dButtonWidth
            button.onDraw(
                c,
                RectF(
                    left,
                    itemView.top.toFloat(),
                    right,
                    itemView.bottom.toFloat()
                ),
                pos
            )
            right = left
        }
    }

}