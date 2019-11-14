package com.example.link_saver.ui

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.link_saver.R
import com.example.link_saver.model.BoardModel
import com.example.link_saver.model.LinkModel
import com.example.link_saver.model.SubBoard
import com.example.link_saver.adapters.OnLinkButtonClickListener
import com.example.link_saver.adapters.OnSubBoardItemClickListener
import com.example.link_saver.adapters.SubBoardAdapter
import com.example.link_saver.utils.*
import com.example.link_saver.viewmodel.BoardViewModel

class SubBoardFragment : Fragment(), OnSubBoardItemClickListener, OnLinkButtonClickListener {

    override fun onLinkDeleted(subBoard: SubBoard) {
        viewModel.updateSubBoard(subBoard)
    }

    override fun onDeleteClick(subBoardId: Long) {
        viewModel.deleteSubBoard(subBoardId)
    }

    override fun onEditClick(subBoard: SubBoard) {
        viewModel.updateSubBoard(subBoard)
    }

    override fun onDoneClick(uri: String, subBoard: SubBoard) {
        subBoard.linkModelList.add(0, LinkModel(subBoardId = subBoard.id, uri = uri))
        viewModel.updateSubBoard(subBoard)
    }

    override fun onButtonClicked(uri: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setPackage(CHROME_PACKAGE)
        try {
            startActivity(intent)
        } catch (ex: ActivityNotFoundException){
            intent.setPackage(null)
            startActivity(Intent.createChooser(intent, SELECT_BROWSER))
        }
    }

    private lateinit var boardModel: BoardModel
    private lateinit var searchView: SearchView
    private lateinit var subBoardImage: ImageView
    private lateinit var subBoardTitle: TextView
    private lateinit var subBoardMenu: ImageButton
    private lateinit var addNewSubBoard: CardView
    private lateinit var addNewSubBoardTitle: EditText
    private lateinit var addNewSubBoardHint: TextView
    private lateinit var addNewSubBoardDone: Button
    private lateinit var subBoardRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var boardTitleEditText: EditText
    private lateinit var boardTitleEditDone: Button

    private val subBoardAdapter: SubBoardAdapter = SubBoardAdapter(this, this)
    private val viewModel: BoardViewModel by lazy {
        ViewModelProviders.of(this).get(BoardViewModel::class.java)
    }

    companion object {
        @JvmStatic
        fun newInstance(boardModel: BoardModel) = SubBoardFragment().apply {
            this.boardModel = boardModel
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getAllSubBoards(boardModel.id).observe(this, Observer {
            subBoardAdapter.submitList(it)
            progressBar.visibility = View.GONE
            if (it.isEmpty()) {
                addNewSubBoard.visibility = View.VISIBLE
            }
        })
    }

    override fun onStart() {
        super.onStart()
        subBoardTitle.text = boardModel.title
        selectPicture(boardModel.imageUri!!)
        searchItem()
        setRecyclerView()
        addSubCategory()
        addClickListener()
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

    private fun setRecyclerView() {
        subBoardRecyclerView.apply {
            layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
            adapter = subBoardAdapter
        }
    }

    private fun addSubCategory() {
        addNewSubBoardHint.text = boardModel.title
        addNewSubBoardTitle.setCursor()
        addNewSubBoardTitle.addTextChangedListener {
            if (addNewSubBoardTitle.text.isNotEmpty()) {
                addNewSubBoardDone.visibility = View.VISIBLE
                val title = addNewSubBoardTitle.text.toString()
                addNewSubBoardDone.setOnClickListener {
                    viewModel.addSubBoard(SubBoard(boardId = boardModel.id, title = title))

                    addNewSubBoard.visibility = View.GONE
                    addNewSubBoardDone.visibility = View.GONE
                }
            } else {
                addNewSubBoardDone.visibility = View.GONE
            }
        }
    }

    private fun searchItem() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                subBoardAdapter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                subBoardAdapter.filter(newText)
                return true
            }
        })
    }

    private fun addClickListener(){
        subBoardMenu.setOnClickListener {
            val popupMenu = PopupMenu(it.context, it)
            popupMenu.menuInflater.inflate(R.menu.menu_sub_board, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menuAddButton -> {
                        addNewSubBoardTitle.text.clear()
                        addNewSubBoard.visibility = View.VISIBLE
                        true
                    }
                    R.id.menuDeleteButton -> {
                        deleteBoard()
                        true
                    }
                    R.id.menuEditButton -> {
                        editBoardTitleText()
                        true
                    }
                    else -> {
                        getGalleryPicture()
                        true
                    }
                }
            }
            popupMenu.show()
        }
        subBoardImage.setOnClickListener {
            getGalleryPicture()
        }
    }

    private fun deleteBoard(){
        viewModel.deleteBoard(boardModel.id)
        fragmentManager?.popBackStack()
    }

    private fun selectPicture(uri: String){
        Glide.with(this).load(uri).circleCrop()
            .into(subBoardImage)
    }

    private fun getGalleryPicture(){
        val getPictureIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(getPictureIntent, GET_PICTURE_RESULT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GET_PICTURE_RESULT && resultCode == Activity.RESULT_OK && data != null) {
            val uri = data.data.toString()
            boardModel.imageUri = uri
            viewModel.updateBoard(boardModel)
            selectPicture(uri)
        }
    }

    private fun editBoardTitleText(){
        subBoardTitle.visibility = View.GONE
        boardTitleEditText.visibility = View.VISIBLE
        boardTitleEditDone.visibility = View.VISIBLE
        boardTitleEditText.setText(boardModel.title)
        boardTitleEditText.setCursor(boardModel.title!!.length)
        boardTitleEditText.addTextChangedListener {
            if (boardTitleEditText.text.isEmpty()){
                boardTitleEditDone.visibility = View.GONE
            } else{
                editTitleDone()
            }
        }
        editTitleDone()
    }

    private fun editTitleDone(){
        boardTitleEditDone.visibility = View.VISIBLE
        boardTitleEditDone.setOnClickListener {
            boardModel.title = boardTitleEditText.text.toString()
            viewModel.updateBoard(boardModel)
            subBoardTitle.text = boardModel.title
            subBoardTitle.visibility = View.VISIBLE
            boardTitleEditText.visibility = View.GONE
            boardTitleEditDone.visibility = View.GONE
        }
    }

    private fun setViews(view: View) {
        searchView = view.findViewById(R.id.searchView)
        subBoardImage = view.findViewById(R.id.subBoardBoardImage)
        subBoardTitle = view.findViewById(R.id.subBoardBoardTitle)
        subBoardMenu = view.findViewById(R.id.subBoardBoardMenuDots)
        addNewSubBoard = view.findViewById(R.id.addSubBoard)
        addNewSubBoardTitle = view.findViewById(R.id.editTextBoardTitle)
        addNewSubBoardHint = view.findViewById(R.id.hintAboutSubBoard)
        addNewSubBoardDone = view.findViewById(R.id.addSubcategoryDoneButton)
        subBoardRecyclerView = view.findViewById(R.id.subBoardRecyclerView)
        progressBar = view.findViewById(R.id.progressBar)
        boardTitleEditText = view.findViewById(R.id.subBoardBoardTitleEditText)
        boardTitleEditDone = view.findViewById(R.id.subBoardBoardTitleEditDone)
    }
}