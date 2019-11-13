package com.example.link_saver.adapters

import android.os.Build
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.link_saver.R
import com.example.link_saver.model.BoardModel
import com.example.link_saver.utils.setColor
import java.util.*
import kotlin.collections.ArrayList

private const val ADD_BUTTON_VIEW = -42

class GridViewAdapter(
    private val onBoardItemClickListener: OnBoardItemClickListener,
    private val onBoardItemMenuClickListener: OnBoardItemMenuClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var board: BoardModel? = null

    private var itemsCopy: ArrayList<BoardModel> = ArrayList()
    private val boardItems: ArrayList<BoardModel> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        if(viewType == ADD_BUTTON_VIEW) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.grid_view_add_item, parent, false)
            return AddViewHolder(view)
        }
        else
            view = LayoutInflater.from(parent.context).inflate(R.layout.grid_view_item, parent, false)
        return GridViewHolder(view)
    }

    override fun getItemCount(): Int {
        return boardItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) ADD_BUTTON_VIEW else super.getItemViewType(position)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val boardModel = boardItems[position]
        if (position == 0) {
            val addHolder = holder as AddViewHolder
            addHolder.itemView.setOnClickListener {
                onBoardItemMenuClickListener.onBoardAddClicked()
            }
        } else if (position != RecyclerView.NO_POSITION) {
            val gridViewHolder = holder as GridViewHolder
            gridViewHolder.boardTitle.text = boardModel.title
            gridViewHolder.boardTitle.setTextColor(gridViewHolder.itemView.setColor(boardModel.color))
            Glide.with(gridViewHolder.itemView).load(boardModel.imageUri).circleCrop()
                .into(gridViewHolder.boardImage)
            gridViewHolder.itemView.setOnClickListener {
                onBoardItemClickListener.onBoardItemClicked(boardModel)
            }

            //TODO change the size of menu item click as it is too big
            gridViewHolder.boardMenuButton?.setOnClickListener {
                val popupMenu = PopupMenu(it.context, it)
                popupMenu.menuInflater.inflate(R.menu.menu_board, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    board = boardItems[position]
                    when (menuItem.itemId) {
                        R.id.menuDeleteButton -> {
                            onBoardItemMenuClickListener.onBoardItemMenuClicked(board!!, menuItem.toString())
                            true
                        }
                        R.id.menuEditButton -> {
                            onBoardItemMenuClickListener.onBoardItemMenuClicked(board!!, menuItem.toString())
                            true
                        }
                        else -> {
                            onBoardItemMenuClickListener.onBoardItemMenuClicked(board!!, menuItem.toString())
                            true
                        }
                    }
                }
                popupMenu.show()
            }
        }
    }

    class GridViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val boardImage: ImageView = itemView.findViewById(R.id.boardImage)
        val boardTitle: TextView = itemView.findViewById(R.id.boardTitle)
        val boardMenuButton: ImageButton? = itemView.findViewById(R.id.boardMenu)
    }

    class AddViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)

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

    fun isAdapterEmpty(): Boolean {
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

interface OnBoardItemClickListener {
    fun onBoardItemClicked(boardModel: BoardModel)
}

interface OnBoardItemMenuClickListener {
    fun onBoardItemMenuClicked(boardModel: BoardModel, command: String)
    fun onBoardAddClicked()
}