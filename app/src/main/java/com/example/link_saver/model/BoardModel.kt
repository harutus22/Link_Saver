package com.example.link_saver.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BoardModel(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var title: String?,
    var imageUri: String? = "",
    var color: Int
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(title)
        dest.writeString(imageUri)
        dest.writeInt(color)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BoardModel> {
        override fun createFromParcel(parcel: Parcel): BoardModel{
            return BoardModel(parcel)
        }

        override fun newArray(size: Int): Array<BoardModel?>{
            return arrayOfNulls(size)
        }
    }
}

