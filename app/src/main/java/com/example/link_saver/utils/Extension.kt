package com.example.link_saver.utils

import android.os.Build
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.link_saver.R
import java.text.FieldPosition

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

fun EditText.setCursor(position: Int = 0){
    this.requestFocus()
    this.setSelection(position)
}