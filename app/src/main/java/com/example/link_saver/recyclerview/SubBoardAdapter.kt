package com.example.link_saver.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.link_saver.R
import com.example.link_saver.model.SubBoard
import com.example.link_saver.utils.checkUrl

class SubBoardAdapter(private val onSubBoardItemClickListener: OnSubBoardItemClickListener,
                      private val onLinkButtonClickListener: OnLinkButtonClickListener):
    RecyclerView.Adapter<SubBoardAdapter.SubBoardViewHolder>() {
    private var subBoardList: ArrayList<SubBoard> = ArrayList()
    private var viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubBoardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.subboard_item, parent, false)
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
        holder.menuButton.setOnClickListener { onSubBoardItemClickListener.onMenuClick()
        holder.doneButton.setOnClickListener {
            val url = holder.addUrlEditText.text.toString()
            if (url.checkUrl()) {
                onSubBoardItemClickListener.onDoneClick(url, subBoard.id)
            } else {
                Toast.makeText(holder.itemView.context, "Wrong address", Toast.LENGTH_LONG).show()
            }
        }}
    }

    class SubBoardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.subBoardTitle)
        val menuButton: ImageButton = itemView.findViewById(R.id.subBoardItemMenu)
        val recyclerView: RecyclerView = itemView.findViewById(R.id.subBoardRecyclerView)
        val addUrlEditText: EditText = itemView.findViewById(R.id.addLinkToSubBoard)
        val doneButton: Button = itemView.findViewById(R.id.linkAddDoneButton)
    }
}

interface OnSubBoardItemClickListener{
    fun onMenuClick()
    fun onDoneClick(uri: String, subBoardId: Long)
}