package com.example.link_saver.repository

import android.content.Context
import com.example.link_saver.database.LinkDao
import com.example.link_saver.database.LinkSaverDatabase
import com.example.link_saver.model.LinkModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LinkRepo (context: Context){
    private val linkDao: LinkDao by lazy { LinkSaverDatabase.invoke(context).linkDao() }

    fun getAllLinks(foreignId: Long): List<LinkModel>{
        return linkDao.getAllLinks(foreignId)
    }

    fun addLink(linkModel: LinkModel){
        GlobalScope.launch {
            linkDao.add(linkModel)
        }
    }

    fun updateLink(linkModel: LinkModel){
        GlobalScope.launch {
            linkDao.update(linkModel)
        }
    }

    fun deleteLink(linkId: Long){
        GlobalScope.launch {
            linkDao.delete(linkId)
        }
    }
}