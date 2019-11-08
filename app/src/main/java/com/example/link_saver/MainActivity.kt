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
import com.example.link_saver.fragments.MainFragment
import com.example.link_saver.fragments.SubBoardFragment
import com.example.link_saver.model.BoardModel
import com.example.link_saver.recyclerview.GridViewAdapter
import com.example.link_saver.recyclerview.OnBoardItemClickListener
import com.example.link_saver.viewmodel.BoardViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnBoardItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        starFragment()
    }

    override fun onBoardItemClicked(boardModel: BoardModel) {
        Toast.makeText(this, "On Board Item Clicked ${boardModel.imageUri}", Toast.LENGTH_LONG).show()
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
