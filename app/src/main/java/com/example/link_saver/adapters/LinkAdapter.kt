package com.example.link_saver.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.link_saver.R
import com.example.link_saver.model.LinkModel
import com.example.link_saver.utils.checkUrl
import com.example.link_saver.utils.setCursor

class LinkAdapter(private val onClickListener: OnLinkButtonClickListener,
                  private val onEditClickListener: OnEditClickListener):
    RecyclerView.Adapter<LinkAdapter.LinkViewHolder>() {
    private var linkList: ArrayList<LinkModel> = ArrayList()
    private val listToDelete: ArrayList<LinkModel> = ArrayList()
    private var isCheckboxVisible = false
    private var selectAll = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.link_item, parent, false)
        return LinkViewHolder(view)
    }

    override fun getItemCount(): Int {
        return linkList.size
    }

    override fun onBindViewHolder(holder: LinkViewHolder, position: Int) {
        val uri = linkList[position].uri
        holder.uriView.text = uri
        holder.goToLinkButton.setOnClickListener {
            onClickListener.onButtonClicked(uri!!)
        }
        holder.uriView.setOnClickListener {
            onClickListener.onButtonClicked(uri!!)
        }
        holder.uriView.setOnLongClickListener {
            editLink(holder, position)
            true
        }
        if (isCheckboxVisible) {
            holder.checkBox.isChecked = selectAll
            holder.checkBox.visibility = View.VISIBLE
            holder.goToLinkButton.visibility = View.GONE
            holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) listToDelete.add(linkList[position])
                else listToDelete.remove(linkList[position])
            }
        } else {
            holder.checkBox.visibility = View.GONE
            holder.goToLinkButton.visibility = View.VISIBLE
        }
    }

    private fun editLink(holder: LinkViewHolder, position: Int){
        holder.apply {
            linkTextViewLayout.visibility = View.GONE
            linkEditTextLayout.visibility = View.VISIBLE
            linkEditText.setText(uriView.text)
            linkEditText.setCursor(linkEditText.text.length)
            linkEditDoneButton.setOnClickListener {
                val url = holder.linkEditText.text.toString()
                if (url.checkUrl()) {
                    linkTextViewLayout.visibility = View.VISIBLE
                    linkEditTextLayout.visibility = View.GONE
                    onEditClickListener.onEditDone(
                        LinkModel(
                            subBoardId = linkList[0].subBoardId,
                            uri = url
                        ), position
                    )
                }
            }
        }
    }

    fun prepareToDelete(){
        isCheckboxVisible = true
        notifyDataSetChanged()
    }

    fun deleteDone(){
        isCheckboxVisible = false
        notifyDataSetChanged()
    }

    fun getLinksToDelete():ArrayList<LinkModel>{
        val list = ArrayList<LinkModel>()
        list.addAll(listToDelete)
        listToDelete.clear()
        return list
    }

    fun isCheckboxVisible() = isCheckboxVisible

    fun updateList(newList: ArrayList<LinkModel>){
        linkList.clear()
        linkList.addAll(newList)
        notifyDataSetChanged()
    }

    fun selectAllLinks(){
        if (!selectAll) selectAll = true
        else if(selectAll) selectAll = false
        notifyDataSetChanged()
    }

    fun hideSelectAllLinks(){
        selectAll = false
        notifyDataSetChanged()
    }

    class LinkViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val uriView: TextView = itemView.findViewById(R.id.linkDescription)
        val goToLinkButton: Button = itemView.findViewById(R.id.goToLink)
        val checkBox: CheckBox = itemView.findViewById(R.id.linkItemCheckBox)
        val linkTextViewLayout: LinearLayout = itemView.findViewById(R.id.linkViewLayout)
        val linkEditTextLayout: LinearLayout = itemView.findViewById(R.id.linkEditLayout)
        val linkEditText: EditText = itemView.findViewById(R.id.linkEditEditText)
        val linkEditDoneButton: Button = itemView.findViewById(R.id.linkEditButtonDone)
    }
}

interface OnLinkButtonClickListener{
    fun onButtonClicked(uri: String)
}

interface OnEditClickListener{
    fun onEditDone(linkModel: LinkModel, position: Int)
}