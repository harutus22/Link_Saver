package com.example.link_saver.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.link_saver.R
import com.example.link_saver.model.LinkModel

class LinkAdapter(private val onClickListener: OnLinkButtonClickListener): RecyclerView.Adapter<LinkAdapter.LinkViewHolder>() {
    private var linkList: ArrayList<LinkModel> = ArrayList()

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
    }

    fun updateList(newList: ArrayList<LinkModel>){
        linkList.clear()
        linkList.addAll(newList)
        notifyDataSetChanged()
    }

    class LinkViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val uriView: TextView = itemView.findViewById(R.id.linkDescription)
        val goToLinkButton: Button = itemView.findViewById(R.id.goToLink)
    }
}

interface OnLinkButtonClickListener{
    fun onButtonClicked(uri: String)
}