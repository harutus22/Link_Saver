package com.example.link_saver.ui

import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class BoardItemDecoration(private val margin: Int): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val viewWidth = if(view.width == 0) fixLayoutSize(view, parent) else view
        val space = parent.width - viewWidth.width * 2
        Log.d("TAG", "${parent.width} ${viewWidth.width} ${space}")
        if(parent.getChildAdapterPosition(view) == 0){
            outRect.left = margin
            outRect.right = space / 2 - margin
        } else if (parent.getChildAdapterPosition(view) % 2 != 0){
            outRect.left = space / 2 - margin
            outRect.right = margin
        } else if (parent.getChildAdapterPosition(view) % 2 == 0){
            outRect.left = margin
            outRect.right = space / 2 - margin
        }
    }

    private fun fixLayoutSize(view: View, parent: ViewGroup): View {
        if (view.layoutParams == null) {
            view.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)
        val childWidth = ViewGroup.getChildMeasureSpec(
            widthSpec, parent.paddingLeft + parent.paddingRight, view.layoutParams.width
        )
        val childHeight = ViewGroup.getChildMeasureSpec(
            heightSpec, parent.paddingTop + parent.paddingBottom, view.layoutParams.height
        )
        view.measure(childWidth, childHeight)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        return view
    }


}