package com.example.link_saver.utils

import androidx.room.TypeConverter
import com.example.link_saver.model.LinkModel
import com.example.link_saver.model.SubBoard
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable

class DataConverter : Serializable {
    @TypeConverter
    fun fromSubBoardList(subBoard: ArrayList<SubBoard>?): String? {
        if (subBoard == null) return null
        val gson = Gson()
        val type = object : TypeToken<SubBoard>() {}.type
        return gson.toJson(subBoard, type)
    }

    @TypeConverter
    fun toSubBoard(value: String?): ArrayList<SubBoard>? {
        if (value == null) return value
        val gson = Gson()
        val type = object : TypeToken<SubBoard>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromLinkModelArrayList(board: ArrayList<LinkModel>?): String?{
        if (board == null) return null
        val gson = Gson()
        val type = object : TypeToken<LinkModel>(){}.type
        return gson.toJson(board, type)
    }

    @TypeConverter
    fun toLinkModelArrayList(value: String?): ArrayList<LinkModel>?{
        if (value == null) return value
        val gson = Gson()
        val type = object : TypeToken<LinkModel>(){}.type
        return gson.fromJson(value, type)
    }
}