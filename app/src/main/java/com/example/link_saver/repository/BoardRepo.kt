package com.example.link_saver.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.link_saver.database.LinkSaverDatabase
import com.example.link_saver.model.BoardModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BoardRepo(context: Context) {
    private val boardDao by lazy { LinkSaverDatabase.invoke(context).boardDao() }

    fun getAllBoards(): LiveData<List<BoardModel>>{
        return boardDao.getAllBoards()
    }

    fun saveBoard(boardModel: BoardModel){
        GlobalScope.launch {
            boardDao.add(boardModel)
        }
    }

    fun updateBoard(boardModel: BoardModel){
        GlobalScope.launch {
            boardDao.update(boardModel)
        }
    }

    fun deleteBoard(boardId: Long){
        GlobalScope.launch {
            boardDao.delete(boardId)
        }
    }
}