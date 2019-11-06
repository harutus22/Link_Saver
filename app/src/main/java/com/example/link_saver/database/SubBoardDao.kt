package com.example.link_saver.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.link_saver.model.SubBoard

@Dao
interface SubBoardDao {
    @Query("SELECT * FROM subboard WHERE boardId = :foreignId")
    fun getAllSubBoards(foreignId: Long): LiveData<List<SubBoard>>

    @Insert
    fun add(subBoard: SubBoard)

    @Update
    fun update(subBoard: SubBoard)

    @Query("DELETE FROM subboard WHERE id = :subBoardId")
    fun delete(subBoardId: Long)
}