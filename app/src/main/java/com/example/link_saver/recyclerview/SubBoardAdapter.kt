package com.example.link_saver.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.link_saver.R
import com.example.link_saver.model.BoardModel
import com.example.link_saver.model.SubBoard
import com.example.link_saver.utils.checkUrl
import com.example.link_saver.viewmodel.BoardViewModel
import java.util.*
import kotlin.collections.ArrayList

class SubBoardAdapter(
    private val onSubBoardItemClickListener: OnSubBoardItemClickListener,
    private val onLinkButtonClickListener: OnLinkButtonClickListener
) :
    RecyclerView.Adapter<SubBoardAdapter.SubBoardViewHolder>() {
    private var subBoardList: ArrayList<SubBoard> = ArrayList()
    private var itemsCopy: ArrayList<SubBoard> = ArrayList()
    private var viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubBoardViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.subboard_item, parent, false)
        return SubBoardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return subBoardList.size
    }

    override fun onBindViewHolder(holder: SubBoardViewHolder, position: Int) {
        val subBoard = subBoardList[position]
        holder.title.text = subBoard.title
        holder.recyclerView.apply {
            layoutManager = LinearLayoutManager(holder.recyclerView.context)
            adapter = LinkAdapter(onLinkButtonClickListener)
            (adapter as LinkAdapter).updateList(subBoard.linkModelList)
            setRecycledViewPool(viewPool)
        }
        holder.menuButton.setOnClickListener {
            val popupMenu = PopupMenu(it.context, it)
            popupMenu.menuInflater.inflate(R.menu.menu_link, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menuAddButton -> {
                        addNewLink(position, holder)
                        true
                    }
                    R.id.menuEditButton -> {
                        //TODO make possible to edit subBoard title
                        true
                    }
                    else -> {
                        onSubBoardItemClickListener.onDeleteClick(subBoard.id)
                        true
                    }
                }
            }
            popupMenu.show()
        }
    }

    private fun addNewLink(position: Int, viewHolder: RecyclerView.ViewHolder) {
        val subBoard = subBoardList[position]
        val subBoardViewHolder = (viewHolder as SubBoardViewHolder)
        subBoardViewHolder.hintAboutSubBoard.text = subBoard.title
        subBoardViewHolder.addLinkLayout.visibility = View.VISIBLE
        subBoardViewHolder.addUrlEditText.addTextChangedListener {
            if (it!!.isNotEmpty()) {
                viewHolder.doneButton.visibility = View.VISIBLE
                viewHolder.doneButton.setOnClickListener {
                    val url = viewHolder.addUrlEditText.text.toString()
                    if (url.checkUrl()) {
                        onSubBoardItemClickListener.onDoneClick(url, subBoard)
                        viewHolder.addUrlEditText.text.clear()
                        subBoardViewHolder.addLinkLayout.visibility = View.GONE
                    } else {
                        Toast.makeText(
                            viewHolder.itemView.context,
                            "Wrong address",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    class SubBoardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.subBoardTitle)
        val menuButton: ImageButton = itemView.findViewById(R.id.subBoardItemMenu)
        val recyclerView: RecyclerView = itemView.findViewById(R.id.linkRecyclerView)
        val addUrlEditText: EditText = itemView.findViewById(R.id.addLinkToSubBoard)
        val doneButton: Button = itemView.findViewById(R.id.linkAddDoneButton)
        val addLinkLayout: LinearLayout = itemView.findViewById(R.id.addLinkLayout)
        val hintAboutSubBoard: TextView = itemView.findViewById(R.id.hintAboutSubBoard)
    }

    fun filter(text: String) {
        val locale = Locale.getDefault()
        subBoardList.clear()
        if (text.isEmpty()) {
            subBoardList.addAll(itemsCopy)
        } else {
            val newText = text.toLowerCase(locale)
            for (item in itemsCopy) {
                if (item.title!!.toLowerCase(locale).contains(newText)) {
                    subBoardList.add(item)
                }
            }
        }
        notifyDataSetChanged()
    }

    fun submitList(list: List<SubBoard>) {
        subBoardList.clear()
        itemsCopy.clear()
        itemsCopy.addAll(list)
        subBoardList.addAll(list)
        notifyDataSetChanged()
    }
}

interface OnSubBoardItemClickListener {
    fun onDeleteClick(subBoardId: Long)
    fun onEditClick(subBoard: SubBoard)
    fun onDoneClick(uri: String, subBoard: SubBoard)
}