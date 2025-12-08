package com.example.workandstudyapp.ui.todolist.task.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemDayDecoration(private val space:Int): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount ?: 0

        outRect.top = 0
        outRect.bottom = 0

        // spacing trái phải nhẹ
        outRect.left = space / 2
        outRect.right = space / 2

        if(position==itemCount-2){
            outRect.left = space / 3
            outRect.right = space / 2
        }
        if (position == itemCount - 1) {
            outRect.left = space / 6
        }
    }
}