package com.example.link_saver.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.link_saver.database.LinkSaverDatabase
import com.example.link_saver.database.SubBoardDao
import com.example.link_saver.model.SubBoard
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SubboardRepo(context: Context) {
    private val subBoardDao: SubBoardDao by lazy { LinkSaverDatabase.invoke(context).subBoardDao() }

    fun getAllSubboards(foreignKey: Long): LiveData<List<SubBoard>>{
        return subBoardDao.getAllSubBoards(foreignKey)
    }

    fun addSubboard(subBoard: SubBoard){
        GlobalScope.launch {
            subBoardDao.add(subBoard)
        }
    }

    fun updateSubboard(subBoard: SubBoard){
        GlobalScope.launch {
            subBoardDao.update(subBoard)
        }
    }

    fun deleteSubBoard(subBoardId: Long){
        GlobalScope.launch {
            subBoardDao.delete(subBoardId)
        }
    }
}