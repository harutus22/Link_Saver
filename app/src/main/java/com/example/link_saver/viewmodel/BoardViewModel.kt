package com.example.link_saver.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.link_saver.model.BoardModel
import com.example.link_saver.model.LinkModel
import com.example.link_saver.model.SubBoard
import com.example.link_saver.repository.BoardRepo
import com.example.link_saver.repository.LinkRepo
import com.example.link_saver.repository.SubboardRepo

class BoardViewModel(app: Application): AndroidViewModel(app) {
    private val boardRepo: BoardRepo by lazy { BoardRepo(app) }
    private val subBoardRepo: SubboardRepo by lazy { SubboardRepo(app) }
    private val linkRepo: LinkRepo by lazy { LinkRepo(app) }

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

    fun getAllSubboards(foreignKey: Long): LiveData<List<SubBoard>>{
        return subBoardRepo.getAllSubboards(foreignKey)
    }

    fun addSuboard(subBoardModel: SubBoard){
        return subBoardRepo.addSubboard(subBoardModel)
    }

    fun updatesubBoard(subBoardModel: SubBoard){
        return subBoardRepo.updateSubboard(subBoardModel)
    }

    fun deleteSubBoard(subBoardId: Long){
        return subBoardRepo.deleteSubBoard(subBoardId)
    }

    fun getAllLinks(foreignKey: Long): List<LinkModel>{
        return linkRepo.getAllLinks(foreignKey)
    }

    fun addLink(linkModel: LinkModel){
        return linkRepo.addLink(linkModel)
    }

    fun updateLink(linkModel: LinkModel){
        return linkRepo.updateLink(linkModel)
    }

    fun deleteLink(linkId: Long){
        return linkRepo.deleteLink(linkId)
    }
}