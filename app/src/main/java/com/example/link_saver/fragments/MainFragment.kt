package com.example.link_saver.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.link_saver.R
import com.example.link_saver.model.BoardModel
import com.example.link_saver.recyclerview.GridViewAdapter
import com.example.link_saver.recyclerview.OnBoardItemClickListener
import com.example.link_saver.recyclerview.OnBoardItemMenuClickListener
import com.example.link_saver.viewmodel.BoardViewModel

class MainFragment: Fragment(), OnBoardItemMenuClickListener {
    private lateinit var boardListAdapter: GridViewAdapter
    private lateinit var onBoardItemClickListener: OnBoardItemClickListener
    private lateinit var mainSearchView: SearchView
    private lateinit var searchCardView: CardView

    private val viewModel: BoardViewModel by lazy {
        ViewModelProviders.of(this).get(BoardViewModel::class.java)
    }

    companion object{
        @JvmStatic
        fun newInstance(onBoardItemClickListener: OnBoardItemClickListener) = MainFragment().apply {
            this.onBoardItemClickListener = onBoardItemClickListener
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        searchItem(view)
        setRecyclerView(view)
        return view
    }

    private fun setRecyclerView(view: View){
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        val boardList = ArrayList<BoardModel>()
//        boardList.add((BoardModel(title = "Weeding", imageUri = "dasdasdasda", color = 0)))
//        boardList.add(BoardModel(title = "Sport", imageUri = "sparta", color = 1))
//        boardList.add(BoardModel(title = "French", imageUri = "this is it", color = 2))
//        boardList.add(BoardModel(title = "German", imageUri = "Zig Hi!!!!!", color = 3))
//        boardList.add(BoardModel(title = "Italian", imageUri = "Mescuzi un pizza", color = 0))
//        boardList.add(BoardModel(title = "Russian", imageUri = "Ra ra rasputin", color = 1))
//        boardList.add(BoardModel(title = "Chinese", imageUri = "Cheese", color = 2))
//        boardList.add(BoardModel(title = "Japan", imageUri = "Arigato arimasen", color = 3))
//        boardList.add(BoardModel(title = "Irish", imageUri = "What do we do with the drunken sailor", color = 0))

        //TODO change in future to check with database and simplify the code
        val lonelyAddButton: CardView = view.findViewById(R.id.lonelyAddButton)
        if (boardList.isEmpty()){
            recyclerView.visibility = View.INVISIBLE
            searchCardView.visibility = View.INVISIBLE
            lonelyAddButton.setOnClickListener {
                addButtonClicked()
                //TODO add method to add first Board Item to change the view type
                recyclerView.visibility = View.VISIBLE
                searchCardView.visibility = View.VISIBLE
                lonelyAddButton.visibility = View.GONE
            }
        } else {
            recyclerView.visibility = View.VISIBLE
            searchCardView.visibility = View.VISIBLE
            lonelyAddButton.visibility = View.GONE
        }


        val layoutManager = GridLayoutManager(this.context, 2)
        recyclerView.layoutManager = layoutManager
        boardListAdapter = GridViewAdapter(onBoardItemClickListener, this)
        boardListAdapter.submitList(boardList)
        recyclerView.adapter = boardListAdapter
    }

    private fun searchItem(view: View){
        searchCardView = view.findViewById(R.id.mainSearchView)
        mainSearchView = view.findViewById(R.id.searchView)
        mainSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                boardListAdapter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                boardListAdapter.filter(newText)
                return true
            }
        })
    }

    private fun observeViewModel(){
        viewModel.getAllBoards().observe(this, Observer {
            boardListAdapter.submitList(it)
        })
    }

    override fun onBoardItemMenuClicked(boardModel: BoardModel) {
        Toast.makeText(this.context, "On Board Item Menu Clicked ${boardModel.imageUri}", Toast.LENGTH_LONG).show()
    }

    override fun onBoardAddClicked() {
        addButtonClicked()
    }

    private fun addButtonClicked(){
        Toast.makeText(this.context, "On Board add Clicked", Toast.LENGTH_LONG).show()
    }
}