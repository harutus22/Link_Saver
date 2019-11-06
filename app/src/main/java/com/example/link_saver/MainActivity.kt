package com.example.link_saver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.link_saver.model.BoardModel
import com.example.link_saver.recyclerview.GridViewAdapter
import com.example.link_saver.recyclerview.OnBoardItemClickListener
import com.example.link_saver.viewmodel.BoardViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnBoardItemClickListener {
    private lateinit var boardListAdapter: GridViewAdapter
    private lateinit var searchView: SearchView
    private lateinit var viewModel: BoardViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val boardList = ArrayList<BoardModel>()
        boardList.add((BoardModel(title = "Weeding", imageUri = "dasdasdasda", color = 0)))
        boardList.add(BoardModel(title = "Sport", imageUri = "sparta", color = 1))
        boardList.add(BoardModel(title = "French", imageUri = "this is it", color = 2))
        boardList.add(BoardModel(title = "German", imageUri = "Zig Hi!!!!!", color = 3))
        boardList.add(BoardModel(title = "Italian", imageUri = "Mescuzi un pizza", color = 0))
        boardList.add(BoardModel(title = "Russian", imageUri = "Ra ra rasputin", color = 1))
        boardList.add(BoardModel(title = "Chinese", imageUri = "Cheese", color = 2))
        boardList.add(BoardModel(title = "Japan", imageUri = "Arigato arimasen", color = 3))
        boardList.add(BoardModel(title = "Irish", imageUri = "What do we do with the drunken sailor", color = 0))
        searchItem()

        //TODO change in future to check with database and simplify the code
        if (boardList.isEmpty()){
            recyclerView.visibility = View.INVISIBLE
            mainSearchView.visibility = View.INVISIBLE
            lonelyAddButton.setOnClickListener {
                //TODO add method to add first Board Item to change the view type
                recyclerView.visibility = View.VISIBLE
                mainSearchView.visibility = View.VISIBLE
                lonelyAddButton.visibility = View.GONE
            }
        } else {
            recyclerView.visibility = View.VISIBLE
            mainSearchView.visibility = View.VISIBLE
            lonelyAddButton.visibility = View.GONE
        }


        val layoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = layoutManager
        boardListAdapter = GridViewAdapter(this)
        boardListAdapter.submitList(boardList)
        recyclerView.adapter = boardListAdapter
    }

    private fun searchItem(){
        searchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
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

    override fun onBoardItemClicked(boardModel: BoardModel) {
        Toast.makeText(this, "On Board Item Clicked ${boardModel.imageUri}", Toast.LENGTH_LONG).show()
    }

    override fun onBoardAddClicked() {
        Toast.makeText(this, "On Board add Clicked", Toast.LENGTH_LONG).show()
    }

    override fun onBoardItemMenuClicked(boardModel: BoardModel) {
        Toast.makeText(this, "On Board Item Menu Clicked ${boardModel.imageUri}", Toast.LENGTH_LONG).show()
    }
}
