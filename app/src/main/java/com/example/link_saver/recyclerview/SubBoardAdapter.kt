package com.example.link_saver.recyclerview

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.link_saver.model.SubBoard

class SubBoardAdapter: RecyclerView.Adapter<SubBoardAdapter.SubBoardViewHolder>() {
    private var subBoardList: ArrayList<SubBoard> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubBoardViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        return subBoardList.size
    }

    override fun onBindViewHolder(holder: SubBoardViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    inner class SubBoardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }
}