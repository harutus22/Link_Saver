package com.example.link_saver.database

import androidx.room.*
import com.example.link_saver.model.LinkModel

@Dao
interface LinkDao{
    @Query("SELECT * FROM LinkModel WHERE subBoardId = :foreignId")
    fun getAllLinks(foreignId: Long): List<LinkModel>

    @Insert
    fun add(linkModel: LinkModel)

    @Update
    fun update(linkModel: LinkModel)

    @Query("DELETE FROM LinkModel WHERE id = :linkId")
    fun delete(linkId: Long)
}