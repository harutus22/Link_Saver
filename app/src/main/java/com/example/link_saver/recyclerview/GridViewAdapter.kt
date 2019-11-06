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
import java.util.*
import kotlin.collections.ArrayList

private const val ADD_BUTTON_VIEW = -42

class GridViewAdapter(private val onBoardItemClickListener: OnBoardItemClickListener):
    RecyclerView.Adapter<GridViewAdapter.GridViewHolder>() {
    private var itemsCopy: MutableList<BoardModel> = mutableListOf()
    private val boardItems: ArrayList<BoardModel> = ArrayList()
    init {
        itemsCopy.addAll(boardItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val view: View = if (viewType == ADD_BUTTON_VIEW)
            LayoutInflater.from(parent.context).inflate(R.layout.grid_view_add_item, parent, false)
        else
            LayoutInflater.from(parent.context).inflate(R.layout.grid_view_item, parent, false)
        return GridViewHolder(view)
    }

    override fun getItemCount(): Int {
        return boardItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) ADD_BUTTON_VIEW else super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val boardModel = boardItems[position]
        if (position == 0) {
            holder.itemView.setOnClickListener {
                onBoardItemClickListener.onBoardAddClicked()
            }
        } else if (position != RecyclerView.NO_POSITION) {
            holder.boardTitle.text = boardModel.title
            holder.boardTitle.setTextColor(holder.itemView.context.resources.getColor(getTextColor(boardModel.color)))
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
        val locale = Locale.getDefault()
        val first = boardItems[0]
        boardItems.clear()
        if (text.isEmpty()) {
            boardItems.addAll(itemsCopy)
        } else {
            val newText = text.toLowerCase(locale)
            for (item in itemsCopy) {
                if (item.title!!.toLowerCase(locale).contains(newText)) {
                    boardItems.add(item)
                }
            }
        }

        boardItems.add(0, first) //find another way to keep add button
        notifyDataSetChanged()
    }

    fun submitList(list: MutableList<BoardModel>) {
        boardItems.clear()
        itemsCopy.clear()
        boardItems.add(0, BoardModel(title = "addButton", color = 0))
        itemsCopy.addAll(list)
        boardItems.addAll(list)
        notifyDataSetChanged()
    }

    //TODO take this to the place where BoardModel will be created
    companion object {
        private var color_count: Int = 0
        private fun addCount(){
            color_count += 1
            if (color_count > 3) color_count = 0
        }
    }

    private fun getTextColor(inputColor: Int): Int{
        return when(inputColor){
            0 -> R.color.text_color_one
            1 -> R.color.text_color_two
            2 -> R.color.text_color_three
            else -> R.color.text_color_four
        }
    }
}

interface OnBoardItemClickListener{
    fun onBoardItemClicked(boardModel: BoardModel)
    fun onBoardAddClicked()
    fun onBoardItemMenuClicked(boardModel: BoardModel)
}