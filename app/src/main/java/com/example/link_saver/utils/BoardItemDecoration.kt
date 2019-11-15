package com.example.link_saver.utils

import android.content.Context
import android.graphics.Rect
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.RecyclerView

class BoardItemDecoration(private val space: Int): RecyclerView.ItemDecoration(){
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val wm = view.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val metrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(metrics)
        val heightPixel = metrics.heightPixels
        val widthPixel = metrics.widthPixels
        val dpi = metrics.densityDpi


        Log.d("TAG", "$heightPixel $widthPixel $dpi ${metrics.xdpi} ${metrics.ydpi}")

        outRect.apply {
            if (dpi >= 420) {
                when {
                    parent.getChildAdapterPosition(view) == 0 -> {
                        right = space
                    }
                    parent.getChildAdapterPosition(view) == 1 -> {
                        left = space * 2
                        right = space
                    }
                    parent.getChildAdapterPosition(view) % 2 == 0 -> {
                        right = space
                    }
                    else -> {
                        left = space * 2
                        right = space
                    }
                }
            } else {
                right = space
            }
        }
    }
}