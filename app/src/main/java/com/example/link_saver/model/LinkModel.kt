package com.example.link_saver.model

import android.os.Parcel
import android.os.Parcelable

data class LinkModel(
    var id: Long = 0,
    var subBoardId: Long,
    var uri: String?): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeLong(subBoardId)
        parcel.writeString(uri)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LinkModel> {
        override fun createFromParcel(parcel: Parcel): LinkModel {
            return LinkModel(parcel)
        }

        override fun newArray(size: Int): Array<LinkModel?> {
            return arrayOfNulls(size)
        }
    }
}