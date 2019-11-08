package com.example.link_saver.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.link_saver.R
import com.example.link_saver.model.BoardModel

class SubBoardFragment: Fragment() {
    private lateinit var boardModel: BoardModel
    private lateinit var searchView: SearchView
    private lateinit var subBoardImage: ImageView
    private lateinit var subBoardTitle: TextView
    private lateinit var subBoardMenu: ImageView
    private lateinit var addNewSubBoard: CardView
    private lateinit var addNewSubBoardTitle: EditText
    private lateinit var addNewSubBoardHint: TextView
    private lateinit var addNewSubBoardDone: Button
    private lateinit var subBoardRecyclerView: RecyclerView

    companion object{
        @JvmStatic
        fun newInstance(boardModel: BoardModel) = SubBoardFragment().apply {
            this.boardModel = boardModel
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        subBoardTitle.text = boardModel.title

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_subbord, container, false)
        setViews(view)
        return view
    }

    private fun setViews(view: View){
        searchView = view.findViewById(R.id.searchView)
        subBoardImage = view.findViewById(R.id.subBoardBoardImage)
        subBoardTitle = view.findViewById(R.id.subBoardBoardTitle)
        subBoardMenu = view.findViewById(R.id.subBoardBoardMenuDots)
        addNewSubBoard = view.findViewById(R.id.addSubBoard)
        addNewSubBoardTitle = view.findViewById(R.id.editTextBoardTitle)
        addNewSubBoardHint = view.findViewById(R.id.hintAboutSubBoard)
        addNewSubBoardDone = view.findViewById(R.id.addSubcategoryDoneButton)
        subBoardRecyclerView = view.findViewById(R.id.subBoardRecyclerView)
    }
}