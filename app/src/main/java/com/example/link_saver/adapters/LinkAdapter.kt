package com.example.link_saver.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.link_saver.R
import com.example.link_saver.model.LinkModel

class LinkAdapter(private val onClickListener: OnLinkButtonClickListener):
    RecyclerView.Adapter<LinkAdapter.LinkViewHolder>() {
    private var linkList: ArrayList<LinkModel> = ArrayList()
    private val listToDelete: ArrayList<LinkModel> = ArrayList()
    private var isCheckboxVisible = false

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
        if (isCheckboxVisible) {
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

    class LinkViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val uriView: TextView = itemView.findViewById(R.id.linkDescription)
        val goToLinkButton: Button = itemView.findViewById(R.id.goToLink)
        val checkBox: CheckBox = itemView.findViewById(R.id.linkItemCheckBox)
    }
}

interface OnLinkButtonClickListener{
    fun onButtonClicked(uri: String)
}

interface OnLinkDeleteItems{
    fun onDelete(listToDelete: ArrayList<LinkModel>)
}