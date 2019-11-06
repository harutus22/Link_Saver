package com.example.link_saver.recyclerview

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.link_saver.model.LinkModel

class LinkAdapter: RecyclerView.Adapter<LinkAdapter.LinkViewHolder>() {
    private var linkList: ArrayList<LinkModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        return linkList.size
    }

    override fun onBindViewHolder(holder: LinkViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    inner class LinkViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }
}