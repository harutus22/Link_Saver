package com.example.link_saver.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.link_saver.model.BoardModel

class SubBoardFragment: Fragment() {
    private lateinit var boardModel: BoardModel

    companion object{
        @JvmStatic
        fun newInstance(boardModel: BoardModel) = SubBoardFragment().apply {
            this.boardModel = boardModel
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}