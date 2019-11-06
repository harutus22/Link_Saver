package com.example.link_saver.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.link_saver.model.BoardModel

@Dao
interface BoardDao {
    @Query("SELECT * FROM Boardmodel ORDER BY id")
    fun getAllBoards(): LiveData<List<BoardModel>>

    @Insert
    fun add(boardModel: BoardModel)

    @Update
    fun update(boardModel: BoardModel)

    @Query("DELETE FROM BoardModel WHERE id = :boardId")
    fun delete(boardId: Long)
}