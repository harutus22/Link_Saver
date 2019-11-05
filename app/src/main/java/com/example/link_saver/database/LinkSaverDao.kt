package com.example.link_saver.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.link_saver.model.LinkModel

@Dao
interface LinkDao{
    @Query("SELECT * FROM linkmodel WHERE subBoardId = :foreignId")
    fun getAllLinks(foreignId: Long): LiveData<List<LinkModel>>

    @Insert
    fun add(linkModel: LinkModel)

    @Update
    fun update(linkModel: LinkModel)

    @Delete
    fun delete(linkModel: LinkModel)
}