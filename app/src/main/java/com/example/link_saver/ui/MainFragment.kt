package com.example.link_saver.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.link_saver.R
import com.example.link_saver.model.BoardModel
import com.example.link_saver.adapters.GridViewAdapter
import com.example.link_saver.adapters.OnBoardItemClickListener
import com.example.link_saver.adapters.OnBoardItemMenuClickListener
import com.example.link_saver.utils.*
import com.example.link_saver.viewmodel.BoardViewModel

class MainFragment : Fragment(), OnBoardItemMenuClickListener {
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
    private var board: BoardModel? = null

    private val viewModel: BoardViewModel by lazy {
        ViewModelProviders.of(this).get(BoardViewModel::class.java)
    }

    companion object {
        @JvmStatic
        fun newInstance(onBoardItemClickListener: OnBoardItemClickListener) = MainFragment().apply {
            this.onBoardItemClickListener = onBoardItemClickListener
        }

        private var color_count: Int = 0
        private fun addCount() {
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
        setViews(view)
        return view
    }

    private fun setViews(view: View) {
        searchCardView = view.findViewById(R.id.mainSearchView)
        mainSearchView = view.findViewById(R.id.searchView)
        addNewBoardView = view.findViewById(R.id.addNewBoardView)
        addBoardDone = view.findViewById(R.id.addBoardDone)
        editTextBoardTitle = view.findViewById(R.id.editTextBoardTitle)
        linearLayout = view.findViewById(R.id.linearLayout)
        progressBar = view.findViewById(R.id.progressBar)
        recyclerView = view.findViewById(R.id.recyclerView)
        lonelyAddButton = view.findViewById(R.id.lonelyAddButton)
        val layoutManager = GridLayoutManager(this.context, 2)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = boardListAdapter
    }

    override fun onStart() {
        super.onStart()
        simpleOrRecyclerView()
        searchItem()
    }

    private fun searchItem() {
        mainSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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

    private fun observeViewModel() {
        viewModel.getAllBoards().observe(this, Observer {
            progressBar.visibility = View.VISIBLE
            boardListAdapter.submitList(it)
            if (it.isNotEmpty()) {
                color_count = it.last().color
                linearLayout.visibility = View.VISIBLE
            } else {
                lonelyAddButton.visibility = View.VISIBLE
                linearLayout.visibility = View.GONE
            }
            progressBar.visibility = View.GONE
        })
    }

    override fun onBoardItemMenuClicked(boardModel: BoardModel, command: String) {
        when (command) {
            EDIT_DATA -> editButtonClicked(boardModel)
            DELETE_DATA -> viewModel.deleteBoard(boardModel.id)
            SELECT_PICTURE_DATA -> setPictureToBoard(boardModel)
        }
    }

    override fun onBoardAddClicked() {
        addButtonClicked()
    }

    private fun addButtonClicked() {
        showMainViews()
        addBoardDoneClick()
    }

    private fun editButtonClicked(boardModel: BoardModel) {
        editTextBoardTitle.setText(boardModel.title)
        editTextBoardTitle.setCursor(boardModel.title!!.length)
        showMainViews()
        addBoardDoneClick(boardModel, EDIT_DATA)
    }

    private fun addBoardDoneClick(boardModel: BoardModel? = null, command: String = ADD_DATA) {
        editTextBoardTitle.setCursor()
        editTextBoardTitle.addTextChangedListener {
            if (editTextBoardTitle.text.isEmpty()) {
                addBoardDone.visibility = View.GONE
            } else {
                addBoardDone.visibility = View.VISIBLE
            }
            addBoardDone.setOnClickListener {
                val text = editTextBoardTitle.text.toString()
                editTextBoardTitle.text.clear()
                mainSearchView.setQuery("", false)
                if (command == ADD_DATA) {
                    addCount()
                    val board = BoardModel(title = text, imageUri = "", color = color_count)
                    viewModel.addBoard(board)
                } else {
                    val board = BoardModel(
                        id = boardModel!!.id,
                        title = text,
                        imageUri = boardModel.imageUri,
                        color = boardModel.color
                    )
                    viewModel.updateBoard(board)
                }
                hideMainViews()
            }
        }
    }

    private fun setPictureToBoard(boardModel: BoardModel) {
        board = boardModel
        val getPictureIntent = Intent(
            Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(getPictureIntent, GET_PICTURE_RESULT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GET_PICTURE_RESULT && resultCode == RESULT_OK && data != null) {
            val uri = data.data
            board?.imageUri = uri.toString()
            viewModel.updateBoard(board!!)
            board = null
        }
    }

    private fun simpleOrRecyclerView() {
        lonelyAddButton.setOnClickListener {
            lonelyAddButton.visibility = View.GONE

            addButtonClicked()
        }
        if (!boardListAdapter.isAdapterEmpty()) {
            lonelyAddButton.visibility = View.GONE
            linearLayout.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }

    private fun showMainViews() {
        addNewBoardView.visibility = View.VISIBLE
        linearLayout.visibility = View.GONE
    }

    private fun hideMainViews() {
        addNewBoardView.visibility = View.GONE
        linearLayout.visibility = View.VISIBLE
    }
}