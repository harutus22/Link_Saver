package com.example.link_saver.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.link_saver.R
import com.example.link_saver.model.LinkModel
import com.example.link_saver.model.SubBoard
import com.example.link_saver.utils.WRONG_LINK_TYPE
import com.example.link_saver.utils.checkUrl
import com.example.link_saver.utils.setCursor
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
        val button: Button = holder.itemView.findViewById(R.id.deleteLinksDone)
        holder.title.text = subBoard.title
        holder.recyclerView.apply {
            layoutManager = LinearLayoutManager(holder.recyclerView.context)
            adapter = LinkAdapter(onLinkButtonClickListener, object :OnEditClickListener{
                override fun onEditDone(linkModel: LinkModel, linkPosition: Int) {
                    val editedSubBoard = subBoardList[position]
                    editedSubBoard.linkModelList[linkPosition] = linkModel
                    onSubBoardItemClickListener.onEditClick(editedSubBoard)
                }
            })
            (adapter as LinkAdapter).updateList(subBoard.linkModelList)
            setRecycledViewPool(viewPool)
        }
        val linkAdapter = holder.recyclerView.adapter as LinkAdapter
        button.setOnClickListener { view: View ->
            if (linkAdapter.isCheckboxVisible()) {
                val list = linkAdapter.getLinksToDelete()
                holder.linksAllSelectButton.visibility = View.GONE
                linkAdapter.deleteDone()
                linkAdapter.hideSelectAllLinks()
                subBoard.linkModelList.removeAll(list)
                view.visibility = View.GONE
                onSubBoardItemClickListener.onLinkDeleted(subBoard)
                notifyDataSetChanged()
            }
        }

        holder.linksAllSelectButton.setOnClickListener {
            linkAdapter.selectAllLinks()
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
                        editSubBoard(subBoard, holder)
                        true
                    }
                    R.id.menuDeleteLinksButton -> {
                        (holder.recyclerView.adapter as LinkAdapter).prepareToDelete()
                        holder.linksAllSelectButton.visibility = View.VISIBLE
                        button.visibility = View.VISIBLE
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
        subBoardViewHolder.apply {
            hintAboutSubBoard.text = subBoard.title
            addLinkLayout.visibility = View.VISIBLE
            addUrlEditText.setCursor()
            addUrlEditText.addTextChangedListener {
                if (it!!.isNotEmpty()) {
                    doneButton.visibility = View.VISIBLE
                    doneButton.setOnClickListener {
                        val url = addUrlEditText.text.toString()
                        if (url.checkUrl()) {
                            onSubBoardItemClickListener.onDoneClick(url, subBoard)
                            addUrlEditText.text.clear()
                            subBoardViewHolder.addLinkLayout.visibility = View.GONE
                        } else {
                            Toast.makeText(
                                itemView.context,
                                WRONG_LINK_TYPE,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } else {
                    doneButton.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun editSubBoard(subBoard: SubBoard, holder: SubBoardViewHolder) {
        holder.apply {
            subBoardTitleDescription.visibility = View.GONE
            editSubBoardTitleView.visibility = View.VISIBLE
            editSubBoardTitleEditText.setText(subBoard.title)
            editSubBoardTitleEditText.addTextChangedListener {
                if (editSubBoardTitleEditText.text.isEmpty()) {
                    editSubBoardTitleDone.visibility = View.GONE
                } else {
                    editSubBoardTextDone(holder, subBoard)
                }
            }
            if (editSubBoardTitleEditText.text.isNotEmpty()){
                editSubBoardTextDone(this, subBoard)
            }
        }
    }

    private fun editSubBoardTextDone(holder: SubBoardViewHolder, subBoard: SubBoard){
        holder.apply {
        editSubBoardTitleDone.visibility = View.VISIBLE
        editSubBoardTitleDone.setOnClickListener {
            editSubBoardTitleView.visibility = View.GONE
            subBoardTitleDescription.visibility = View.VISIBLE
            subBoard.title = editSubBoardTitleEditText.text.toString()
            onSubBoardItemClickListener.onEditClick(subBoard)
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
        val editSubBoardTitleView: CardView = itemView.findViewById(R.id.editSubBoardTitle)
        val editSubBoardTitleEditText: EditText = itemView.findViewById(R.id.editBoardTitle)
        val editSubBoardTitleDone: Button = itemView.findViewById(R.id.editSubBoardTitleDoneButton)
        val subBoardTitleDescription: LinearLayout = itemView.findViewById(R.id.subBoardTitleDescription)
        val linksAllSelectButton: ImageButton = itemView.findViewById(R.id.selectAllButton)
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
    fun onLinkDeleted(subBoard: SubBoard)
}