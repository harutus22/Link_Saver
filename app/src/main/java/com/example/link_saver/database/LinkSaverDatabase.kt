package com.example.link_saver.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.link_saver.model.BoardModel
import com.example.link_saver.model.LinkModel
import com.example.link_saver.model.SubBoard
import com.example.link_saver.utils.DataConverter

@Database(entities = [LinkModel::class, SubBoard::class, BoardModel::class], version = 1)
@TypeConverters(DataConverter::class)
abstract class LinkSaverDatabase: RoomDatabase() {
    abstract fun linkDao(): LinkDao
    abstract fun subBoardDao(): SubBoardDao
    abstract fun boardDao(): BoardDao

    companion object{
        @Volatile private var instance: LinkSaverDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also{ instance = it}
        }

    private  fun buildDatabase(context: Context) = Room.databaseBuilder(context,
        LinkSaverDatabase::class.java, "link_saver_database").fallbackToDestructiveMigration()
        .build()
    }
}