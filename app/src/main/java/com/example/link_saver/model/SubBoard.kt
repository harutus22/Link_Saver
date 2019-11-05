package com.example.link_saver.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*

@Entity(
    foreignKeys = [ForeignKey(
        entity = BoardModel::class,
        parentColumns = ["id"],
        childColumns = ["boardId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("boardId")]
)
data class SubBoard(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var boardId: Long,
    var title: String?,
    var linkModelList: ArrayList<LinkModel> = ArrayList()
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString(),
        arrayListOf<LinkModel>().apply {
            parcel.readList(this as List<*>, LinkModel::class.java.classLoader)
        }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int)
    {
        parcel.writeLong(id)
        parcel.writeLong(boardId)
        parcel.writeString(title)
        parcel.writeList(linkModelList as List<*>)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SubBoard> {
        override fun createFromParcel(parcel: Parcel): SubBoard {
            return SubBoard(parcel)
        }

        override fun newArray(size: Int): Array<SubBoard?> {
            return arrayOfNulls(size)
        }
    }

}