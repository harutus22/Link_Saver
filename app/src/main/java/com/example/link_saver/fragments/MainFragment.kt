package com.example.link_saver.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
import com.example.link_saver.utils.DELETE_DATA
import com.example.link_saver.utils.EDIT_DATA
import com.example.link_saver.utils.SELECT_PICTURE_DATA
import com.example.link_saver.viewmodel.BoardViewModel

class MainFragment: Fragment(), OnBoardItemMenuClickListener {
    private lateinit var boardListAdapter: GridViewAdapter
    private lateinit var onBoardItemClickListener: OnBoardItemClickListener
    private lateinit var mainSearchView: SearchView
    private lateinit var searchCardView: CardView
    private lateinit var addNewBoardView: CardView
    private lateinit var addBoardDone: Button
    private lateinit var editTextBoardTitle: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayout: LinearLayout
    private lateinit var lonelyAddButton: CardView
    private lateinit var progressBar: ProgressBar

    private val viewModel: BoardViewModel by lazy {
        ViewModelProviders.of(this).get(BoardViewModel::class.java)
    }

    companion object{
        @JvmStatic
        fun newInstance(onBoardItemClickListener: OnBoardItemClickListener) = MainFragment().apply {
            this.onBoardItemClickListener = onBoardItemClickListener
        }

        private var color_count: Int = 0
        private fun addCount(){
            color_count += 1
            if (color_count > 3) color_count = 0
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        boardListAdapter = GridViewAdapter(onBoardItemClickListener, this)
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
        progressBar = view.findViewById(R.id.progressBar)
        recyclerView = view.findViewById(R.id.recyclerView)
        lonelyAddButton = view.findViewById(R.id.lonelyAddButton)
        val layoutManager = GridLayoutManager(this.context, 2)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = boardListAdapter
    }

    override fun onStart() {
        super.onStart()
        if (boardListAdapter.isAdapterEmpty()){
            lonelyAddButton.setOnClickListener {
                lonelyAddButton.visibility = View.GONE

                addButtonClicked()
            }
        } else {
            lonelyAddButton.visibility = View.GONE
            linearLayout.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }

    private fun searchItem(view: View){
        searchCardView = view.findViewById(R.id.mainSearchView)
        mainSearchView = view.findViewById(R.id.searchView)
        addNewBoardView = view.findViewById(R.id.addNewBoardView)
        addBoardDone = view.findViewById(R.id.addBoardDone)
        editTextBoardTitle = view.findViewById(R.id.editTextBoardTitle)
        linearLayout = view.findViewById(R.id.linearLayout)

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
            progressBar.visibility = View.VISIBLE
            boardListAdapter.submitList(it)
            if (it.isNotEmpty()) {
                color_count = it.last().color
                linearLayout.visibility = View.VISIBLE
            } else {
                lonelyAddButton.visibility = View.VISIBLE
            }
            progressBar.visibility = View.GONE
        })
    }

    override fun onBoardItemMenuClicked(boardModel: BoardModel, command: String) {
        Toast.makeText(this.context, "On Board Item Menu Clicked ${boardModel.imageUri}", Toast.LENGTH_LONG).show()
        when(command){
            //TODO spinner item dont work properly
//            EDIT_DATA ->{}
//            DELETE_DATA -> viewModel.deleteBoard(boardModel.id)
//            SELECT_PICTURE_DATA ->{}
        }
    }

    override fun onBoardAddClicked() {
        addButtonClicked()
    }

    private fun addButtonClicked(){
        Toast.makeText(this.context, "On Board add Clicked", Toast.LENGTH_LONG).show()
        addNewBoardView.visibility = View.VISIBLE
        linearLayout.visibility = View.GONE

        addBoardDone.setOnClickListener {
            if (editTextBoardTitle.text.isEmpty()){
                Toast.makeText(this.context, "Must enter the title", Toast.LENGTH_LONG).show()
            } else {
                addNewBoardView.visibility = View.GONE
                val text = editTextBoardTitle.text.toString()
                editTextBoardTitle.text.clear()
                mainSearchView.setQuery("", false)
                addCount()
                val board = BoardModel(title = text, imageUri = "",color = color_count)
                viewModel.addBoard(board)
                linearLayout.visibility = View.VISIBLE
            }
        }
    }
}