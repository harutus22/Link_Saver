package com.example.link_saver.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.link_saver.model.BoardModel
import com.example.link_saver.model.SubBoard
import com.example.link_saver.repository.BoardRepo
import com.example.link_saver.repository.SubboardRepo

class BoardViewModel(app: Application): AndroidViewModel(app) {
    private val boardRepo: BoardRepo by lazy { BoardRepo(app) }
    private val subBoardRepo: SubboardRepo by lazy { SubboardRepo(app) }

    fun getAllBoards(): LiveData<List<BoardModel>>{
        return boardRepo.getAllBoards()
    }

    fun addBoard(boardModel: BoardModel){
        return boardRepo.saveBoard(boardModel)
    }

    fun updateBoard(boardModel: BoardModel){
        return boardRepo.updateBoard(boardModel)
    }

    fun deleteBoard(boardId: Long){
        return boardRepo.deleteBoard(boardId)
    }

    fun getAllSubBoards(foreignKey: Long): LiveData<List<SubBoard>>{
        return subBoardRepo.getAllSubboards(foreignKey)
    }

    fun addSubBoard(subBoardModel: SubBoard){
        return subBoardRepo.addSubboard(subBoardModel)
    }

    fun updateSubBoard(subBoardModel: SubBoard){
        return subBoardRepo.updateSubboard(subBoardModel)
    }

    fun deleteSubBoard(subBoardId: Long){
        return subBoardRepo.deleteSubBoard(subBoardId)
    }
}