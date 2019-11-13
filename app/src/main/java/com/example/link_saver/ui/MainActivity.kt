package com.example.link_saver.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.link_saver.R
import com.example.link_saver.model.BoardModel
import com.example.link_saver.adapters.OnBoardItemClickListener

class MainActivity : AppCompatActivity(), OnBoardItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        starFragment()
    }

    override fun onBoardItemClicked(boardModel: BoardModel) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        val subBoardFragment = SubBoardFragment.newInstance(boardModel)
        transaction.replace(R.id.container, subBoardFragment)
        transaction.commit()
    }

    private fun starFragment(){
        val transaction = supportFragmentManager.beginTransaction()
        val startFragment = MainFragment.newInstance(this)
        transaction.replace(R.id.container, startFragment)
        transaction.commit()
    }
}
