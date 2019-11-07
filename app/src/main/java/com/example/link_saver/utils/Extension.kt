package com.example.link_saver.utils

import android.os.Build
import android.view.View
import androidx.core.content.ContextCompat
import com.example.link_saver.R

fun View.setColor(colorId: Int) = run {
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
        ContextCompat.getColor(this.context, getTextColor(colorId))
    }
    else {
        this.context.resources.getColor(getTextColor(colorId))
    }
}

private fun getTextColor(inputColor: Int): Int{
    return when(inputColor){
        0 -> R.color.text_color_one
        1 -> R.color.text_color_two
        2 -> R.color.text_color_three
        else -> R.color.text_color_four
    }
}

fun String.checkUrl() = startsWith("https://")