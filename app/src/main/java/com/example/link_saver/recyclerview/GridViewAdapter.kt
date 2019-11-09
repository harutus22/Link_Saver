package com.example.link_saver.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Delete
import com.bumptech.glide.Glide
import com.example.link_saver.R
import com.example.link_saver.model.BoardModel
import com.example.link_saver.utils.setColor
import java.util.*
import kotlin.collections.ArrayList

private const val ADD_BUTTON_VIEW = -42

class GridViewAdapter(private val onBoardItemClickListener: OnBoardItemClickListener,
                      private val onBoardItemMenuClickListener: OnBoardItemMenuClickListener):
    RecyclerView.Adapter<GridViewAdapter.GridViewHolder>(), AdapterView.OnItemSelectedListener {
    private var board:BoardModel? = null

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val command = parent?.getItemAtPosition(position).toString()
        onBoardItemMenuClickListener.onBoardItemMenuClicked(board!!, command)
    }

    private var itemsCopy: ArrayList<BoardModel> = ArrayList()
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
                onBoardItemMenuClickListener.onBoardAddClicked()
            }
        } else if (position != RecyclerView.NO_POSITION) {
            holder.boardTitle.text = boardModel.title
            holder.boardTitle.setTextColor(holder.itemView.setColor(boardModel.color))
            Glide.with(holder.itemView).load(boardModel.imageUri).circleCrop().into(holder.boardImage)
            holder.itemView.setOnClickListener {
                onBoardItemClickListener.onBoardItemClicked(boardModel)
            }
            board = boardModel
            holder.boardMenuButton?.onItemSelectedListener = this
        }
    }

    class GridViewHolder(itemView: View):
        RecyclerView.ViewHolder(itemView){
        val boardImage: ImageView = itemView.findViewById(R.id.boardImage)
        val boardTitle: TextView = itemView.findViewById(R.id.boardTitle)
        val boardMenuButton: Spinner? = itemView.findViewById(R.id.boardMenu)
        init {
            val adapter = ArrayAdapter.createFromResource(itemView.context, R.array.board_menu, R.layout.custom_spinner)
            boardMenuButton?.adapter = adapter
        }
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

    fun isAdapterEmpty(): Boolean{
        return boardItems.size == 1 || boardItems.size == 0
    }

    fun submitList(list: List<BoardModel>) {
        boardItems.clear()
        itemsCopy.clear()
        boardItems.add(0, BoardModel(title = "addButton", color = 0))
        itemsCopy.addAll(list)
        boardItems.addAll(list)
        notifyDataSetChanged()
    }
}

interface OnBoardItemClickListener{
    fun onBoardItemClicked(boardModel: BoardModel)
}

interface OnBoardItemMenuClickListener{
    fun onBoardItemMenuClicked(boardModel: BoardModel, command: String)
    fun onBoardAddClicked()
}