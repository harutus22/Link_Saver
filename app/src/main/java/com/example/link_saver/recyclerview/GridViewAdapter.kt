package com.example.link_saver.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.link_saver.R
import com.example.link_saver.model.BoardModel

class GridViewAdapter(private val boardItems: ArrayList<BoardModel>,
                      private val onBoardItemClickListener: OnBoardItemClickListener):
    RecyclerView.Adapter<GridViewAdapter.GridViewHolder>() {
    private var itemsCopy: MutableList<BoardModel> = mutableListOf()
    init {
        itemsCopy.addAll(boardItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val view: View = if (viewType == -42) LayoutInflater.from(parent.context).inflate(R.layout.grid_view_add_item, parent, false)
        else LayoutInflater.from(parent.context).inflate(R.layout.grid_view_item, parent, false)
        return GridViewHolder(view)
    }

    override fun getItemCount(): Int {
        return boardItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) -42 else super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val boardModel = boardItems[position]
        if (position == 0) {
            holder.itemView.setOnClickListener {
                onBoardItemClickListener.onBoardAddClicked()
            }
        } else if (position != RecyclerView.NO_POSITION) {
            holder.boardTitle.text = boardModel.title
            Glide.with(holder.itemView).load(boardModel.imageUri).circleCrop().into(holder.boardImage)
            holder.itemView.setOnClickListener {
                onBoardItemClickListener.onBoardItemClicked(boardModel)
            }
            holder.boardMenuButton?.setOnClickListener {
                onBoardItemClickListener.onBoardItemMenuClicked(boardModel)
            }
        }
    }

    inner class GridViewHolder(itemView: View):
        RecyclerView.ViewHolder(itemView){
        val boardImage: ImageView = itemView.findViewById(R.id.boardImage)
        val boardTitle: TextView = itemView.findViewById(R.id.boardTitle)
        val boardMenuButton: ImageButton? = itemView.findViewById(R.id.boardMenu)
    }

    fun filter(text: String) {
        val first = boardItems[0]
        boardItems.clear()
        if (text.isEmpty()) {
            boardItems.addAll(itemsCopy)
        } else {
            val newText = text.toLowerCase()
            for (item in itemsCopy) {
                if (item.title!!.toLowerCase().contains(newText)) {
                    boardItems.add(item)
                }
            }
        }

        if (boardItems.isEmpty()) boardItems.add(first) //find another way to keep add button
        notifyDataSetChanged()
    }

    fun updateList(list: MutableList<BoardModel>) {
        boardItems.clear()
        itemsCopy.clear()
        itemsCopy.addAll(list)
        boardItems.addAll(list)
        notifyDataSetChanged()
    }
}

interface OnBoardItemClickListener{
    fun onBoardItemClicked(boardModel: BoardModel)
    fun onBoardAddClicked()
    fun onBoardItemMenuClicked(boardModel: BoardModel)
}