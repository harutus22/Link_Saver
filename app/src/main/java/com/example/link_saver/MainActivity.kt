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
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnBoardItemClickListener {
    private lateinit var boardListAdapter: GridViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val boardList = ArrayList<BoardModel>()
        boardList.add((BoardModel(title = "Weeding", imageUri = "dasdasdasda")))
        boardList.add(BoardModel(title = "Sport", imageUri = "sparta"))
        boardList.add(BoardModel(title = "French", imageUri = "this is it"))
        boardList.add(BoardModel(title = "German", imageUri = "Zig Hi!!!!!"))
        boardList.add(BoardModel(title = "Italian", imageUri = "Mescuzi un pizza"))
        boardList.add(BoardModel(title = "Russian", imageUri = "Ra ra rasputin"))
        boardList.add(BoardModel(title = "Chinese", imageUri = "Cheese"))
        boardList.add(BoardModel(title = "Japan", imageUri = "Arigato arimasen"))
        boardList.add(BoardModel(title = "Irish", imageUri = "What do we do with the drunken sailor"))
        searchItem()


        recyclerView.layoutManager = GridLayoutManager(this, 2)
        boardListAdapter = GridViewAdapter(boardList, this)
        recyclerView.adapter = boardListAdapter
    }

    private fun searchItem(){
        val searchView = findViewById<SearchView>(R.id.searchView)
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
